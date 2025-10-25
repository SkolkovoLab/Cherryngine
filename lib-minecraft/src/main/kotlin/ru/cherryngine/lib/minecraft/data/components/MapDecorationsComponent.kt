package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable
import ru.cherryngine.lib.minecraft.tide.stream.MapStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class MapDecorationsComponent(
    val decorations: Map<String, Decoration>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofMap(decorations.mapKeys { map -> CRC32CHasher.ofString(map.key) }.mapValues { map -> map.value.hashStruct().getHashed() }))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            MapStreamCodec(StreamCodec.STRING, Decoration.STREAM_CODEC), MapDecorationsComponent::decorations,
            ::MapDecorationsComponent
        )
    }

    data class Decoration(
        val type: String,
        val x: Double,
        val z: Double,
        val rotation: Float
    ) : DataComponentHashable {
        override fun hashStruct(): HashHolder {
            return CRC32CHasher.of {
                static("type", CRC32CHasher.ofString(type))
                static("x", CRC32CHasher.ofDouble(x))
                static("z", CRC32CHasher.ofDouble(z))
                static("rotation", CRC32CHasher.ofFloat(rotation))
            }
        }

        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, Decoration::type,
                StreamCodec.DOUBLE, Decoration::x,
                StreamCodec.DOUBLE, Decoration::z,
                StreamCodec.FLOAT, Decoration::rotation,
                ::Decoration
            )
        }
    }
}