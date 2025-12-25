package ru.cherryngine.lib.minecraft.network.protocol.types

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class WorldPosition(
    val dimension: String,
    val blockPosition: Vec3I,
) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, WorldPosition::dimension,
            LocationStreamCodecs.BLOCK_POSITION, WorldPosition::blockPosition,
            ::WorldPosition
        )

        val CODEC = StructCodec.of(
            "dimension", Codec.STRING, WorldPosition::dimension,
            "pos", LocationCodecs.VEC3I_ARRAY, WorldPosition::blockPosition,
            ::WorldPosition
        )
    }
}