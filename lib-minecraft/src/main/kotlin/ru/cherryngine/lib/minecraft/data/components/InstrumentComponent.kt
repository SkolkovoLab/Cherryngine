package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.dummy.DummyInstrument
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class InstrumentComponent(
    val instrument: DummyInstrument
) : DataComponent() {

    companion object {
        val CODEC = DummyInstrument.CODEC.transform(
            ::InstrumentComponent,
            InstrumentComponent::instrument
        )
        val STREAM_CODEC = StreamCodec.of(
            DummyInstrument.STREAM_CODEC, InstrumentComponent::instrument,
            ::InstrumentComponent
        )
    }
}