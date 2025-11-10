package ru.cherryngine.lib.minecraft.server

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.PacketHandler
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundKeepAlivePacket
import java.net.SocketAddress

class Connection(
    val packetHandler: PacketHandler,
) : SimpleChannelInboundHandler<ServerboundPacket>() {
    var state: ProtocolState = ProtocolState.HANDSHAKE
    lateinit var channel: Channel
    lateinit var address: SocketAddress
    val isActive: Boolean get() = channel.isActive

    private var currentKeepAlive = 0L

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        this.channel = ctx.channel()
        this.address = ctx.channel().remoteAddress()
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
        msg: ServerboundPacket,
    ) {
        packetHandler.onPacket(this, msg)
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
}