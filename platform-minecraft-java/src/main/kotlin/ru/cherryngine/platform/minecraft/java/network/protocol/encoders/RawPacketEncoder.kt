package ru.cherryngine.platform.minecraft.java.network.protocol.encoders

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import net.minestom.server.network.NetworkBuffer
import net.minestom.server.network.packet.PacketRegistry
import net.minestom.server.network.packet.PacketVanilla
import net.minestom.server.network.packet.server.ServerPacket
import net.minestom.server.registry.Registries
import org.slf4j.LoggerFactory
import ru.cherryngine.platform.minecraft.java.network.Connection

class RawPacketEncoder(
    val connection: Connection,
    val registries: Registries?,
) : MessageToByteEncoder<ServerPacket>() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val parser = PacketVanilla.SERVER_PACKET_PARSER

    override fun encode(ctx: ChannelHandlerContext, packet: ServerPacket, out: ByteBuf) {
        try {
            val registry = parser.stateRegistry(connection.state)
            @Suppress("UNCHECKED_CAST")
            val info = registry.packetInfo(packet) as PacketRegistry.PacketInfo<ServerPacket>
            val bodyBytes = NetworkBuffer.makeArray({ buf ->
                buf.write(NetworkBuffer.VAR_INT, info.id())
                buf.write(info.serializer(), packet)
            }, registries)
            out.writeBytes(bodyBytes)
        } catch (t: Throwable) {
            logger.error("There was an error while encoding packet ${packet::class.simpleName}", t)
            throw t
        }
    }
}
