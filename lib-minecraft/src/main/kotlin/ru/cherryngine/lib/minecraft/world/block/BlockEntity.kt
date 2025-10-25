package ru.cherryngine.lib.minecraft.world.block

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.registry.registries.RegistryBlock
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

data class BlockEntity(
    val positionIndex: Int,
    val block: RegistryBlock,
    val data: CompoundBinaryTag,
) {
    val blockEntityTypeId get() = block.blockEntityId!!

    companion object {
        val STREAM_CODEC = object : StreamCodec<BlockEntity> {
            override fun write(buffer: ByteBuf, value: BlockEntity) {
                val id = value.blockEntityTypeId
                val point = ChunkUtils.chunkBlockIndexGetGlobal(value.positionIndex, 0, 0)

                StreamCodec.BYTE.write(buffer, ((point.x and 15) shl 4 or (point.z and 15)).toByte())
                StreamCodec.SHORT.write(buffer, point.y.toShort())
                StreamCodec.VAR_INT.write(buffer, id)
                StreamCodecNBT.STREAM.write(buffer, value.data)
            }

            override fun read(buffer: ByteBuf): BlockEntity {
                TODO("Not yet implemented")
            }
        }
    }
}