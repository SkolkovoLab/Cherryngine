package io.github.dockyardmc.registry.dummy

import io.github.dockyardmc.codec.ComponentCodecs
import io.github.dockyardmc.protocol.types.SoundEvent
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.text.Component

data class DummyInstrument(
    val soundEvent: SoundEvent,
    val useDuration: Float,
    val range: Float,
    val description: Component
) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            SoundEvent.STREAM_CODEC, DummyInstrument::soundEvent,
            StreamCodec.FLOAT, DummyInstrument::useDuration,
            StreamCodec.FLOAT, DummyInstrument::range,
            ComponentCodecs.NBT, DummyInstrument::description,
            ::DummyInstrument
        )
    }
}