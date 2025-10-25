package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ProvidesBannerPatterns(
    val identifier: Key
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofString(identifier.asString()))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.KEY, ProvidesBannerPatterns::identifier,
            ::ProvidesBannerPatterns
        )
    }
}