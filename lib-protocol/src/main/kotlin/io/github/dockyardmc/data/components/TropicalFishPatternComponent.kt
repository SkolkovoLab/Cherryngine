package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class TropicalFishPatternComponent(
    val pattern: Pattern
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofEnum(pattern))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Pattern>(), TropicalFishPatternComponent::pattern,
            ::TropicalFishPatternComponent
        )
    }

    enum class Pattern {
        KOB,
        SUNSTREAK,
        SNOOPER,
        DASHER,
        BRINELY,
        SPOTTY,
        FLOPPER,
        STRIPEY,
        GLITTER,
        BLOCKFISH,
        BETY,
        CLAYFISH
    }
}