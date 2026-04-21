package ru.cherryngine.platform.minecraft.java.network.protocol.decoders

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import net.minestom.server.network.NetworkBuffer
import net.minestom.server.network.packet.PacketVanilla
import net.minestom.server.registry.Registries
import org.slf4j.LoggerFactory
import ru.cherryngine.platform.minecraft.java.network.Connection

class RawPacketDecoder(
    val connection: Connection,
    val registries: Registries?,
) : MessageToMessageDecoder<ByteBuf>() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val parser = PacketVanilla.CLIENT_PACKET_PARSER

    override fun decode(ctx: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        if (!ctx.channel().isActive) return

        val bytes = ByteArray(buffer.readableBytes())
        buffer.readBytes(bytes)
        val netBuf = NetworkBuffer.wrap(bytes, 0, bytes.size, registries)

        val packetId = netBuf.read(NetworkBuffer.VAR_INT)
        val state = connection.state

        try {
            val packet = parser.parse(state, packetId, netBuf)
            out.add(packet)
        } catch (ex: Exception) {
            logger.error("Failed to read packet. Packet id: 0x${packetId.toString(16)}, state: $state", ex)
        }
    }
}
