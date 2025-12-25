package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class FireworksComponent(
    val flightDuration: Float,
    val explosions: List<FireworkExplosionComponent>
) : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(
            "flight_duration", Codec.FLOAT, FireworksComponent::flightDuration,
            "explosions", FireworkExplosionComponent.CODEC.list(), FireworksComponent::explosions,
            ::FireworksComponent
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, FireworksComponent::flightDuration,
            FireworkExplosionComponent.STREAM_CODEC.list(), FireworksComponent::explosions,
            ::FireworksComponent
        )
    }
}