package io.github.dockyardmc.protocol.types

import io.github.dockyardmc.cherry.math.Vec3I
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.NetworkVec3I
import io.github.dockyardmc.protocol.DataComponentHashable
import io.github.dockyardmc.tide.codec.Codec
import io.github.dockyardmc.tide.codec.StructCodec
import io.github.dockyardmc.tide.stream.StreamCodec

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