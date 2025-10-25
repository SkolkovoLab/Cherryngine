package io.github.dockyardmc.protocol.encoders

import io.github.dockyardmc.server.Connection
import io.github.dockyardmc.protocol.packets.CachedPacket
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.packets.registry.ClientboundPacketRegistry
import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.slf4j.LoggerFactory

class RawPacketEncoder(
    val processor: Connection,
    val clientboundPacketRegistry: ClientboundPacketRegistry,
) : MessageToByteEncoder<ClientboundPacket>() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun encode(connection: ChannelHandlerContext, packet: ClientboundPacket, out: ByteBuf) {
        try {
            if (packet is CachedPacket<*>) {
                val packetId = clientboundPacketRegistry.getId(processor.state, packet.original::class)!!
                StreamCodec.VAR_INT.write(out, packetId)
                out.writeBytes(packet.byteArray)
            } else {
                val packetId = clientboundPacketRegistry.getId(processor.state, packet::class)!!

                @Suppress("UNCHECKED_CAST")
                val streamCodec = clientboundPacketRegistry.getStreamCodec(packet::class) as StreamCodec<ClientboundPacket>
                StreamCodec.VAR_INT.write(out, packetId)
                streamCodec.write(out, packet)
            }
        } catch (exception: Exception) {
            logger.error("There was an error while encoding packet", exception)
        }
    }
}