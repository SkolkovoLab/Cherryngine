package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.StreamCodec

class EnchantmentGlintOverrideComponent(
    val enchantGlint: Boolean
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofBoolean(enchantGlint))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BOOLEAN, EnchantmentGlintOverrideComponent::enchantGlint,
            ::EnchantmentGlintOverrideComponent
        )
    }
}