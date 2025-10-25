package ru.cherryngine.lib.minecraft.protocol.types.predicate

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.block.Block
import java.util.function.Predicate

class BlockPredicates(
    val predicates: List<BlockPredicate>
) : Predicate<Block> {
    override fun test(block: Block): Boolean {
        predicates.forEach { predicate ->
            if (predicate.test(block)) return true
        }
        return false
    }

    companion object {
        val NEVER = BlockPredicates(listOf())

        val STREAM_CODEC = StreamCodec.of(
            BlockPredicate.STREAM_CODEC.list(), BlockPredicates::predicates,
            ::BlockPredicates
        )
    }
}