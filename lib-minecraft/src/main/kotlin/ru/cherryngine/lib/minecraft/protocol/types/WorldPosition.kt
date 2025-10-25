package ru.cherryngine.lib.minecraft.protocol.types

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.NetworkVec3I
import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.StructCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class WorldPosition(
    val dimension: String,
    val blockPosition: Vec3I
) : DataComponentHashable {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            static("dimension", CRC32CHasher.ofString(dimension))
            static("pos", CRC32CHasher.ofIntArray(listOf(blockPosition.x, blockPosition.y, blockPosition.z).toIntArray()))
        }
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, WorldPosition::dimension,
            LocationCodecs.BLOCK_POSITION, WorldPosition::blockPosition,
            ::WorldPosition
        )

        val CODEC = StructCodec.of(
            "dimension", Codec.STRING, WorldPosition::dimension,
            "pos", NetworkVec3I.CODEC, WorldPosition::blockPosition,
            ::WorldPosition
        )
    }
}