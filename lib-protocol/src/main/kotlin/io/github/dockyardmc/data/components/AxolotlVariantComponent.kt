package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class AxolotlVariantComponent(
    val variant: Variant
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofEnum(variant))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Variant>(), AxolotlVariantComponent::variant,
            ::AxolotlVariantComponent
        )
    }

    enum class Variant {
        LUCY,
        WILD,
        GOLD,
        CYAN,
        BLUE // <-- rare shiny one :3
    }
}