package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSetEntityDataPacket(
    val entityId: Int,
    val metadata: Map<Int, Metadata.Entry<*>>,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = object : StreamCodec<ClientboundSetEntityDataPacket> {
            override fun write(buffer: ByteBuf, value: ClientboundSetEntityDataPacket) {
                StreamCodec.VAR_INT.write(buffer, value.entityId)
                value.metadata.entries.forEach {
                    StreamCodec.INT_BYTE.write(buffer, it.key)
                    Metadata.Entry.STREAM_CODEC.write(buffer, it.value)
                }
                buffer.writeByte(0xFF)
            }

            override fun read(buffer: ByteBuf): ClientboundSetEntityDataPacket {
                val entityId = StreamCodec.VAR_INT.read(buffer)
                val metadata: MutableMap<Int, Metadata.Entry<*>> = hashMapOf()
                while (true) {
                    val index = StreamCodec.INT_BYTE.read(buffer)
                    if (index == 0xFF) break
                    metadata[index] = Metadata.Entry.STREAM_CODEC.read(buffer)
                }
                return ClientboundSetEntityDataPacket(entityId, metadata)
            }
        }
    }
}