package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.tide.stream.StreamCodec

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