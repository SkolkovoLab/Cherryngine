package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.protocol.DataComponentHashable
import io.github.dockyardmc.tide.stream.MapStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

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