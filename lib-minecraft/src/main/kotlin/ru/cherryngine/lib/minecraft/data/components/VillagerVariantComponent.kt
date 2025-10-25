package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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