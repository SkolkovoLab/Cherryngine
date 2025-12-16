package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.dummy.DummyInstrument
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class InstrumentComponent(
    val instrument: DummyInstrument
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            DummyInstrument.STREAM_CODEC, InstrumentComponent::instrument,
            ::InstrumentComponent
        )
    }
}