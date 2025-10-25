package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.HashList
import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable
import ru.cherryngine.lib.minecraft.protocol.types.DyeColor
import ru.cherryngine.lib.minecraft.registry.registries.BannerPattern
import ru.cherryngine.lib.minecraft.registry.registries.BannerPatternRegistry
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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