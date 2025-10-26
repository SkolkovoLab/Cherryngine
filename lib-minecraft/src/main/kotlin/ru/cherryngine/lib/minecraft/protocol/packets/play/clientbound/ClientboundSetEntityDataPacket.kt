package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.entity.MetadataEntry
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSetEntityDataPacket(
    val entityId: Int,
    val metadata: Map<Int, MetadataEntry<*>>,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = object : StreamCodec<ClientboundSetEntityDataPacket> {
            override fun write(buffer: ByteBuf, value: ClientboundSetEntityDataPacket) {
                StreamCodec.VAR_INT.write(buffer, value.entityId)
                value.metadata.entries.forEach {
                    StreamCodec.INT_BYTE.write(buffer, it.key)
                    MetadataEntry.STREAM_CODEC.write(buffer, it.value)
                }
                buffer.writeByte(0xFF)
            }

            override fun read(buffer: ByteBuf): ClientboundSetEntityDataPacket {
                val entityId = StreamCodec.VAR_INT.read(buffer)
                val metadata: MutableMap<Int, MetadataEntry<*>> = hashMapOf()
                while (true) {
                    val index = StreamCodec.INT_BYTE.read(buffer)
                    if (index == 0xFF) break
                    metadata[index] = MetadataEntry.STREAM_CODEC.read(buffer)
                }
                return ClientboundSetEntityDataPacket(entityId, metadata)
            }
        }
    }
}