package ru.cherryngine.lib.minecraft.registry.dummy

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.ComponentCodecs
import ru.cherryngine.lib.minecraft.protocol.types.SoundEvent
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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