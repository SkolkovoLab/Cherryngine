package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class FireworksComponent(
    val flightDuration: Float,
    val explosions: List<FireworkExplosionComponent>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, FireworksComponent::flightDuration,
            FireworkExplosionComponent.STREAM_CODEC.list(), FireworksComponent::explosions,
            ::FireworksComponent
        )
    }
}