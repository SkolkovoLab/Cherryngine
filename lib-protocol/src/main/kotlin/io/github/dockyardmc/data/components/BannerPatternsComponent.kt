package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.HashList
import io.github.dockyardmc.protocol.DataComponentHashable
import io.github.dockyardmc.protocol.types.DyeColor
import io.github.dockyardmc.registry.registries.BannerPattern
import io.github.dockyardmc.registry.registries.BannerPatternRegistry
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

class BannerPatternsComponent(val layers: List<Layer>) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return HashList(layers.map { layer -> layer.hashStruct() })
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Layer.STREAM_CODEC.list(), BannerPatternsComponent::layers,
            ::BannerPatternsComponent
        )
    }

    data class Layer(
        val pattern: BannerPattern,
        val color: DyeColor
    ) : DataComponentHashable {
        override fun hashStruct(): HashHolder {
            return CRC32CHasher.of {
                static("pattern", CRC32CHasher.ofRegistryEntry(pattern))
                static("color", CRC32CHasher.ofEnum(color))
            }
        }

        companion object {
            val STREAM_CODEC = StreamCodec.of(
                RegistryStreamCodec(BannerPatternRegistry), Layer::pattern,
                EnumStreamCodec<DyeColor>(), Layer::color,
                ::Layer,
            )
        }
    }
}