package ru.cherryngine.lib.minecraft.registry.dummy

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.r2.SoundEvent

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
            ComponentStreamCodecs.NBT, DummyInstrument::description,
            ::DummyInstrument
        )
    }
}