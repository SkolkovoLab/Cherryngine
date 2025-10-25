package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class VillagerVariantComponent(
    val type: Type
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<Type>(), VillagerVariantComponent::type,
            ::VillagerVariantComponent
        )
    }

    enum class Type(val identifier: String) {
        DESERT("minecraft:desert"),
        JUNGLE("minecraft:jungle"),
        PLAINS("minecraft:plains"),
        SAVANNA("minecraft:savanna"),
        SNOW("minecraft:snow"),
        SWAMP("minecraft:swamp"),
        TAIGA("minecraft:taiga")
    }
}