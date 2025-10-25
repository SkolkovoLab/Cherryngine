package io.github.dockyardmc.server

import io.github.dockyardmc.PacketHandler
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.packets.ProtocolState
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.packets.common.ClientboundKeepAlivePacket
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.net.SocketAddress

class Connection(
    val packetHandler: PacketHandler,
) : SimpleChannelInboundHandler<ServerboundPacket>() {
    var state: ProtocolState = ProtocolState.HANDSHAKE
    lateinit var channel: Channel
    lateinit var address: SocketAddress

    private var currentKeepAlive = 0L

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        this.channel = ctx.channel()
        this.address = ctx.channel().remoteAddress()

        CoroutineScope(Dispatchers.IO).launch {
            while (channel.isActive) {
                if (state == ProtocolState.PLAY) {
                    sendPacket(ClientboundKeepAlivePacket(currentKeepAlive))
                    currentKeepAlive++
                }

                delay(10_000)
            }
        }
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