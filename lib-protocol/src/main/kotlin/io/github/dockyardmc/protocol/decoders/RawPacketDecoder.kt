package io.github.dockyardmc.protocol.decoders

import io.github.dockyardmc.server.Connection
import io.github.dockyardmc.protocol.packets.ProtocolState
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.packets.registry.ServerboundPacketRegistry
import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.MessageToMessageDecoder
import org.slf4j.LoggerFactory

class RawPacketDecoder(
    val processor: Connection,
    val serverboundPacketRegistry: ServerboundPacketRegistry,
) : MessageToMessageDecoder<ByteBuf>() {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun parse(id: Int, buffer: ByteBuf, protocolState: ProtocolState): ServerboundPacket? {
        try {
            val packetClass = serverboundPacketRegistry.getFromId(protocolState, id) ?: return null
            val streamCodec = serverboundPacketRegistry.getStreamCodec(packetClass) ?: return null
            return streamCodec.read(buffer) as ServerboundPacket
        } catch (ex: Exception) {
            logger.error("Failed to read packet. Packet id: $id, protocol state: $protocolState", ex)
            return null
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun decode(connection: ChannelHandlerContext, buffer: ByteBuf, out: MutableList<Any>) {
        if (!connection.channel().isActive) return // the connection was closed

        try {
            val packetId = StreamCodec.VAR_INT.read(buffer)
            val packetIdByteRep = "0x${packetId.toByte().toHexString()}"
            val state = processor.state

            val packet = parse(packetId, buffer, state)

            // no packet class was found to handle this packet, so we skip the bytes and log error
            if (packet == null) {
                logger.warn(
                    "Received unknown packet with id $packetId ($packetIdByteRep) during phase: ${state.name} [${
                        serverboundPacketRegistry.getSkippedFromId(state, packetId)
                    }]"
                )
                buffer.skipBytes(buffer.readableBytes())
                return
            }

            // if the buffer is still readable, there are leftover bytes we didn't read
            if (buffer.isReadable) throw DecoderException("Packet ${packet::class.simpleName} ($packetIdByteRep) was larger than expected, extra bytes: ${buffer.readableBytes()}")

            out.add(packet)

        } catch (ex: Exception) {
            logger.error("Error occurred while decoding packet", ex)
        }
    }
}