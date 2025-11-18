package ru.cherryngine.lib.minecraft.world.block

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.protocol.types.BlockEntityType
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.MapStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class BlockEntity(
    val blockEntityType: BlockEntityType,
    val data: CompoundBinaryTag,
) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<BlockEntityType>(), BlockEntity::blockEntityType,
            StreamCodecNBT.COMPOUND_STREAM, BlockEntity::data,
            ::BlockEntity
        )

        val STREAM_CODEC_POSITION_INDEX = object : StreamCodec<Vec3I> {
            override fun write(buffer: ByteBuf, value: Vec3I) {
                val packedXZ = (value.x and 15) shl 4 or (value.z and 15)
                StreamCodec.BYTE.write(buffer, packedXZ.toByte())
                StreamCodec.SHORT.write(buffer, value.y.toShort())
            }

            override fun read(buffer: ByteBuf): Vec3I {
                val packedXZ = StreamCodec.BYTE.read(buffer).toInt() and 0xFF
                val x = (packedXZ shr 4) and 0xF
                val z = packedXZ and 0xF
                val y = StreamCodec.SHORT.read(buffer).toInt()
                return Vec3I(x, y, z)
            }
        }

        val STREAM_CODEC_MAP = MapStreamCodec(STREAM_CODEC_POSITION_INDEX, STREAM_CODEC)
    }
}