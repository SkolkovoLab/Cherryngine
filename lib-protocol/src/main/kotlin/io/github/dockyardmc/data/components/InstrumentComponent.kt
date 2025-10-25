package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.registry.dummy.DummyInstrument
import io.github.dockyardmc.tide.stream.StreamCodec

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