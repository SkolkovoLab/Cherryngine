package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.protocol.types.predicate.BlockPredicates
import io.github.dockyardmc.tide.stream.StreamCodec

class CanBreakComponent(
    val predicates: BlockPredicates
) : DataComponent() {
    //TODO(1.21.5): wait for minestom to do this so I can yoink it I don't understand block predicates
    override fun hashStruct(): HashHolder {
        return unsupported(this::class)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            BlockPredicates.STREAM_CODEC, CanBreakComponent::predicates,
            ::CanBreakComponent
        )
    }
}