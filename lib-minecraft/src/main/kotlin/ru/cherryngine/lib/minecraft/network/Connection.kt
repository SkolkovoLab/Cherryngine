package ru.cherryngine.lib.minecraft.network

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.minestom.server.network.ConnectionState
import net.minestom.server.network.packet.client.ClientPacket
import net.minestom.server.network.packet.client.configuration.ClientFinishConfigurationPacket
import net.minestom.server.network.packet.client.common.ClientPingRequestPacket
import net.minestom.server.network.packet.client.handshake.ClientHandshakePacket
import net.minestom.server.network.packet.client.login.ClientEncryptionResponsePacket
import net.minestom.server.network.packet.client.login.ClientLoginAcknowledgedPacket
import net.minestom.server.network.packet.client.login.ClientLoginStartPacket
import net.minestom.server.network.packet.server.ServerPacket
import net.minestom.server.network.packet.server.common.DisconnectPacket
import net.minestom.server.network.packet.server.common.KeepAlivePacket
import net.minestom.server.network.packet.server.common.PingResponsePacket
import net.minestom.server.network.packet.server.login.EncryptionRequestPacket
import net.minestom.server.network.packet.server.login.LoginDisconnectPacket
import net.minestom.server.network.packet.server.login.LoginSuccessPacket
import net.minestom.server.network.packet.server.login.SetCompressionPacket
import net.minestom.server.network.player.GameProfile
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.network.protocol.cryptography.EncryptionUtil
import ru.cherryngine.lib.minecraft.network.protocol.decoders.CompressionDecoder
import ru.cherryngine.lib.minecraft.network.protocol.decoders.PacketDecryptionHandler
import ru.cherryngine.lib.minecraft.network.protocol.encoders.CompressionEncoder
import ru.cherryngine.lib.minecraft.network.protocol.encoders.PacketEncryptionHandler
import ru.cherryngine.lib.minecraft.utils.MojangUtil
import java.math.BigInteger
import java.net.SocketAddress
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Connection(
    val connectionHandler: ConnectionHandler,
    val mojangAuth: Boolean,
    val compressionThreshold: Int,
) : SimpleChannelInboundHandler<ClientPacket>() {
    companion object {
        private val logger = LoggerFactory.getLogger(Connection::class.java)
    }

    private val crypto = EncryptionUtil.getNewPlayerCrypto()

    var state: ConnectionState = ConnectionState.HANDSHAKE
        private set
    private lateinit var context: ChannelHandlerContext
    val channel: Channel get() = context.channel()
    val address: SocketAddress get() = channel.remoteAddress()
    val isActive: Boolean get() = channel.isActive

    private var currentKeepAlive = 0L

    var protocolVersion: Int = -1
        private set
    lateinit var serverAddress: String
        private set
    var serverPort: Int = -1
        private set
    lateinit var intent: ClientHandshakePacket.Intent
        private set

    lateinit var helloGameProfile: GameProfile
        private set

    var onlineGameProfile: GameProfile? = null
        private set

    lateinit var gameProfile: GameProfile
        private set

    override fun channelActive(context: ChannelHandlerContext) {
        super.channelActive(context)
        this.context = context
        connectionHandler.onConnect(this)

        CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                if (state == ConnectionState.PLAY || state == ConnectionState.CONFIGURATION) {
                    sendPacket(KeepAlivePacket(currentKeepAlive))
                    currentKeepAlive++
                }

                delay(10_000)
            }
        }
    }

    override fun channelInactive(context: ChannelHandlerContext) {
        super.channelInactive(context)
        connectionHandler.onDisconnect(this)
    }

    override fun channelRead0(
        ctx: ChannelHandlerContext,
        packet: ClientPacket,
    ) {
        when (packet) {
            is ClientHandshakePacket -> handleIntention(packet)
            is ClientLoginStartPacket -> handleHello(packet)
            is ClientEncryptionResponsePacket -> handleEncryptionResponse(packet)
            is ClientLoginAcknowledgedPacket -> state = ConnectionState.CONFIGURATION
            is ClientFinishConfigurationPacket -> state = ConnectionState.PLAY
            is ClientPingRequestPacket -> sendPacket(PingResponsePacket(packet.number))
        }

        connectionHandler.onPacket(this, packet)
    }

    private fun handleIntention(packet: ClientHandshakePacket) {
        protocolVersion = packet.protocolVersion
        serverAddress = packet.serverAddress
        serverPort = packet.serverPort
        intent = packet.intent
        state = when (packet.intent) {
            ClientHandshakePacket.Intent.STATUS -> ConnectionState.STATUS
            ClientHandshakePacket.Intent.LOGIN -> ConnectionState.LOGIN
            ClientHandshakePacket.Intent.TRANSFER -> ConnectionState.LOGIN
        }
    }

    private fun handleHello(packet: ClientLoginStartPacket) {
        helloGameProfile = GameProfile(packet.profileId, packet.username, emptyList())
        if (mojangAuth) {
            sendPacket(EncryptionRequestPacket("", crypto.publicKey.encoded, crypto.verifyToken, true))
        } else {
            finishLogin()
        }
    }

    private fun handleEncryptionResponse(packet: ClientEncryptionResponsePacket) {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, crypto.privateKey)

        val verifyToken = cipher.doFinal(packet.encryptedVerifyToken)
        val sharedSecret = cipher.doFinal(packet.sharedSecret)

        if (!verifyToken.contentEquals(crypto.verifyToken)) {
            logger.error("Verify Token of player ${this@Connection.helloGameProfile.name()} does not match!")
            kick("Your encryption verify token does not match!")
            return
        }

        val sharedSecretKey = SecretKeySpec(sharedSecret, "AES")
        val digestedData = EncryptionUtil.digestData("", EncryptionUtil.keyPair.public, sharedSecretKey)

        val serverId = BigInteger(digestedData).toString(16)

        onlineGameProfile = try {
            val profileResponse = MojangUtil.authenticateSession(this@Connection.helloGameProfile.name(), serverId)
            val uuid = profileResponse.getUUID()
            val name = profileResponse.name
            val properties = profileResponse.properties.map { it.toMinestom() }

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

    private fun finishLogin() {
        if (compressionThreshold > -1) {
            sendPacket(SetCompressionPacket(compressionThreshold))
            channel.pipeline()
                .addAfter(
                    ChannelHandlers.PACKET_LENGTH_DECODER, ChannelHandlers.PACKET_COMPRESSION_DECODER,
                    CompressionDecoder()
                )
                .addAfter(
                    ChannelHandlers.PACKET_LENGTH_ENCODER, ChannelHandlers.PACKET_COMPRESSION_ENCODER,
                    CompressionEncoder(compressionThreshold)
                )
        }

        gameProfile = connectionHandler.setGameProfile(this, helloGameProfile, onlineGameProfile)
        sendPacket(LoginSuccessPacket(gameProfile))
    }

    override fun channelReadComplete(context: ChannelHandlerContext) {
        context.flush()
    }

    override fun exceptionCaught(context: ChannelHandlerContext, cause: Throwable) {
        logger.error("Connection closed", cause)
        context.flush()
        context.close()
    }

    fun sendPacket(packet: ServerPacket) {
        channel.writeAndFlush(packet)
    }

    /**
     * Отправляет пакет-обёртку Minestom (`CachedPacket`/`LazyPacket`/`FramedPacket`).
     * Распаковывает до `ServerPacket` (с учётом текущего состояния для CachedPacket)
     * и шлёт через `sendPacket(ServerPacket)`. `BufferedPacket` пока не поддерживаем
     * (он требует записи сырых байт напрямую в буфер).
     */
    fun sendPacket(packet: net.minestom.server.network.packet.server.SendablePacket) {
        val server = net.minestom.server.network.packet.server.SendablePacket.extractServerPacket(state, packet)
            ?: throw UnsupportedOperationException("BufferedPacket не поддерживается в Cherryngine pipeline")
        sendPacket(server)
    }

    fun kick(message: String) {
        val formattedMessage = Component.text("Disconnected").appendNewline().append(Component.text(message))
        val packet: ServerPacket = when (state) {
            ConnectionState.HANDSHAKE,
            ConnectionState.STATUS,
            ConnectionState.LOGIN,
                -> LoginDisconnectPacket(formattedMessage)

            ConnectionState.CONFIGURATION,
            ConnectionState.PLAY,
                -> DisconnectPacket(formattedMessage)
        }

        sendPacket(packet)
        channel.close()
    }
}
