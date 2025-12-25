package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.protocol.types.predicate.BlockPredicates
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class CanBreakComponent(
    val predicates: BlockPredicates,
) : DataComponent() {

    companion object {
        val CODEC = object : Codec<CanBreakComponent> {
            override fun <D> encode(transcoder: Transcoder<D>, value: CanBreakComponent): D {
                TODO("Not yet implemented")
            }

            override fun <D> decode(transcoder: Transcoder<D>, value: D): CanBreakComponent {
                TODO("Not yet implemented")
            }
        }
        val STREAM_CODEC = StreamCodec.of(
            BlockPredicates.STREAM_CODEC, CanBreakComponent::predicates,
            ::CanBreakComponent
        )
    }
}