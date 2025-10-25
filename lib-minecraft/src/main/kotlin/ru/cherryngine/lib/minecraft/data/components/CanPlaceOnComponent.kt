package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.protocol.types.predicate.BlockPredicates
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class CanPlaceOnComponent(
    val predicates: BlockPredicates
) : DataComponent() {
    //TODO(1.21.5): wait for minestom to do this so I can yoink it I don't understand block predicates
    override fun hashStruct(): HashHolder {
        return unsupported(this::class)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            BlockPredicates.STREAM_CODEC, CanPlaceOnComponent::predicates,
            ::CanPlaceOnComponent
        )
    }
}