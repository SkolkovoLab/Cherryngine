package ru.cherryngine.lib.minecraft.data.dummy

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.types.SoundEvent

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