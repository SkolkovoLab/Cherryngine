package ru.cherryngine.lib.minecraft.protocol.types.predicate

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.block.Block
import java.util.function.Predicate

class BlockPredicate(
    val blocks: BlockTypeFilter?,
    val state: PropertiesPredicate?,
    val nbt: CompoundBinaryTag?
) : Predicate<Block> {
    override fun test(block: Block): Boolean {
        if (blocks != null && !blocks.test(block)) return false
        if (state != null && !state.test(block)) return false
        return nbt == null //TODO get NBT from block
    }

    companion object {
        val ALL = BlockPredicate(null, null, null)
        val NONE = BlockPredicate(null, PropertiesPredicate(mapOf("uwu_owo_nya" to ValuePredicate.Exact("purrr"))), null)

        val STREAM_CODEC = StreamCodec.of(
            BlockTypeFilter.STREAM_CODEC.optional(), BlockPredicate::blocks,
            PropertiesPredicate.STREAM_CODEC.optional(), BlockPredicate::state,
            StreamCodecNBT.COMPOUND_STREAM.optional(), BlockPredicate::nbt,
            ::BlockPredicate
        )
    }
}