package ru.cherryngine.lib.minecraft.protocol.encoders

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.protocol.packets.CachedPacket
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.packets.registry.ClientboundPacketRegistry
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class RawPacketEncoder(
    val processor: Connection,
    val clientboundPacketRegistry: ClientboundPacketRegistry,
) : MessageToByteEncoder<ClientboundPacket>() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun encode(connection: ChannelHandlerContext, packet: ClientboundPacket, out: ByteBuf) {
        try {
            if (packet is CachedPacket<*>) {
                val packetId = clientboundPacketRegistry.getId(processor.state, packet.original::class)!!
                StreamCodec.VAR_INT.write(out, packetId)
                out.writeBytes(packet.byteArray)
            } else {
                val packetId = clientboundPacketRegistry.getId(processor.state, packet::class) ?: throw IllegalStateException("Unknown packet type: ${packet::class.simpleName} for state: ${processor.state}")

                @Suppress("UNCHECKED_CAST")
                val streamCodec = clientboundPacketRegistry.getStreamCodec(packet::class) as StreamCodec<ClientboundPacket>
                StreamCodec.VAR_INT.write(out, packetId)
                streamCodec.write(out, packet)
            }
        } catch (t: Throwable) {
            logger.error("There was an error while encoding packet", t)
            throw t
        }
    }
}