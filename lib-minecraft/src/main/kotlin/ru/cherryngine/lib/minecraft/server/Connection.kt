package ru.cherryngine.lib.minecraft.server

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.PacketHandler
import ru.cherryngine.lib.minecraft.protocol.cryptography.EncryptionUtil
import ru.cherryngine.lib.minecraft.protocol.decoders.CompressionDecoder
import ru.cherryngine.lib.minecraft.protocol.decoders.PacketDecryptionHandler
import ru.cherryngine.lib.minecraft.protocol.encoders.CompressionEncoder
import ru.cherryngine.lib.minecraft.protocol.encoders.PacketEncryptionHandler
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundDisconnectPacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundKeepAlivePacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundPongResponsePacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ServerboundPingRequestPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ServerboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.handshake.ServerboundIntentionPacket
import ru.cherryngine.lib.minecraft.protocol.packets.handshake.ServerboundIntentionPacket.Intent
import ru.cherryngine.lib.minecraft.protocol.packets.login.*
import ru.cherryngine.lib.minecraft.protocol.types.GameProfile
import ru.cherryngine.lib.minecraft.utils.MojangUtil
import java.math.BigInteger
import java.net.SocketAddress
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Connection(
    val packetHandler: PacketHandler,
    val mojangAuth: Boolean,
    val compressionThreshold: Int,
) : SimpleChannelInboundHandler<ServerboundPacket>() {
    companion object {
        private val logger = LoggerFactory.getLogger(Connection::class.java)
    }

    private val crypto = EncryptionUtil.getNewPlayerCrypto()

    var state: ProtocolState = ProtocolState.HANDSHAKE
        private set
    lateinit var context: ChannelHandlerContext
    val channel: Channel get() = context.channel()
    val address: SocketAddress get() = channel.remoteAddress()
    val isActive: Boolean get() = channel.isActive

    private var currentKeepAlive = 0L

    var protocolVersion: Int = -1
        private set
    lateinit var serverAddress: String
        private set
    var serverPort: Short = -1
        private set
    lateinit var intent: Intent
        private set
    lateinit var gameProfile: GameProfile
        private set

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        this.context = ctx
        packetHandler.onConnect(this)

        CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                if (state == ProtocolState.PLAY || state == ProtocolState.CONFIGURATION) {
                    sendPacket(ClientboundKeepAlivePacket(currentKeepAlive))
                    currentKeepAlive++
                }

                delay(10_000)
            }
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        packetHandler.onDisconnect(this)
    }

    override fun channelRead0(
        ctx: ChannelHandlerContext,
        packet: ServerboundPacket,
    ) {
        when (packet) {
            is ServerboundIntentionPacket -> handleIntention(packet)
            is ServerboundHelloPacket -> handleHello(packet)
            is ServerboundEncryptionResponsePacket -> handleEncryptionResponse(packet)
            is ServerboundLoginAcknowledgedPacket -> state = ProtocolState.CONFIGURATION
            is ServerboundFinishConfigurationPacket -> state = ProtocolState.PLAY
            is ServerboundPingRequestPacket -> sendPacket(ClientboundPongResponsePacket(packet.time))
        }

        packetHandler.onPacket(this, packet)
    }

    private fun handleIntention(packet: ServerboundIntentionPacket) {
        protocolVersion = packet.protocolVersion
        serverAddress = packet.serverAddress
        serverPort = packet.serverPort
        intent = packet.intent
        state = when (packet.intent) {
            Intent.STATUS -> ProtocolState.STATUS
            Intent.LOGIN -> ProtocolState.LOGIN
            Intent.TRANSFER -> ProtocolState.LOGIN
        }
    }

    private fun handleHello(packet: ServerboundHelloPacket) {
        gameProfile = GameProfile(packet.uuid, packet.username, mutableListOf())
        if (mojangAuth) {
            sendPacket(ClientboundEncryptionRequestPacket("", crypto.publicKey.encoded, crypto.verifyToken, true))
        } else {
            finishLogin()
        }
    }

    private fun finishLogin() {
        if (compressionThreshold > -1) {
            sendPacket(ClientboundLoginCompressionPacket(compressionThreshold))
            channel.pipeline()
                .addBefore(
                    ChannelHandlers.RAW_PACKET_DECODER, ChannelHandlers.PACKET_COMPRESSION_DECODER,
                    CompressionDecoder()
                )
                .addBefore(
                    ChannelHandlers.RAW_PACKET_ENCODER, ChannelHandlers.PACKET_COMPRESSION_ENCODER,
                    CompressionEncoder(compressionThreshold)
                )
        }

        sendPacket(ClientboundLoginFinishedPacket(gameProfile))
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    override fun exceptionCaught(connection: ChannelHandlerContext, cause: Throwable) {
        LoggerFactory.getLogger(Connection::class.java).error("Connection closed", cause)
        connection.flush()
        connection.close()
    }

    fun sendPacket(packet: ClientboundPacket) {
        channel.writeAndFlush(packet)
    }

    fun kick(message: String) {
        val formattedMessage = Component.text("Disconnected").appendNewline().append(Component.text(message))
        val packet = when (state) {
            ProtocolState.HANDSHAKE,
            ProtocolState.STATUS,
            ProtocolState.LOGIN,
                -> ClientboundLoginDisconnectPacket(formattedMessage)

            ProtocolState.CONFIGURATION,
            ProtocolState.PLAY,
                -> ClientboundDisconnectPacket(formattedMessage)
        }

        sendPacket(packet)
        channel.close()
    }

    fun handleEncryptionResponse(packet: ServerboundEncryptionResponsePacket) {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, crypto.privateKey)

        val verifyToken = cipher.doFinal(packet.verifyToken)
        val sharedSecret = cipher.doFinal(packet.sharedSecret)

        if (!verifyToken.contentEquals(crypto.verifyToken)) {
            logger.error("Verify Token of player ${this@Connection.gameProfile.username} does not match!")
            kick("Your encryption verify token does not match!")
            return
        }

        val sharedSecretKey = SecretKeySpec(sharedSecret, "AES")
        val digestedData = EncryptionUtil.digestData("", EncryptionUtil.keyPair.public, sharedSecretKey)

        val serverId = BigInteger(digestedData).toString(16)

        gameProfile = try {
            val profileResponse = MojangUtil.authenticateSession(this@Connection.gameProfile.username, serverId)
            val uuid = profileResponse.getUUID()
            val name = profileResponse.name
            val properties = profileResponse.properties.toMutableList()

            GameProfile(uuid, name, properties)
        } catch (ex: Exception) {
            logger.error(ex.message, ex)
            kick("Failed to contact Mojang's Session Servers (Are they down?)")
            return
        }

        crypto.sharedSecret = sharedSecretKey
        crypto.isConnectionEncrypted = true

        channel.pipeline()
            .addBefore(
                ChannelHandlers.PACKET_LENGTH_DECODER,
                ChannelHandlers.PACKET_DECRYPTOR,
                PacketDecryptionHandler(crypto)
            )
            .addBefore(
                ChannelHandlers.PACKET_LENGTH_ENCODER,
                ChannelHandlers.PACKET_ENCRYPTOR,
                PacketEncryptionHandler(crypto)
            )

        finishLogin()
    }
}