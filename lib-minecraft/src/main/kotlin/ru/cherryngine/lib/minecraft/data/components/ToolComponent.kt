package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable
import ru.cherryngine.lib.minecraft.protocol.types.predicate.BlockTypeFilter
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ToolComponent(
    val rules: List<Rule>,
    val defaultMiningSpeed: Float,
    val damagePerBlock: Int,
    val canDestroyBlocksInCreative: Boolean
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            structList("rules", rules, Rule::hashStruct)
            default("default_mining_speed", DEFAULT_MINING_SPEED, defaultMiningSpeed, CRC32CHasher::ofFloat)
            default("damage_per_block", DEFAULT_DAMAGE_PER_BLOCK, damagePerBlock, CRC32CHasher::ofInt)
            default("can_destroy_blocks_in_creative", DEFAULT_CAN_DESTROY_BLOCKS_IN_CREATIVE, canDestroyBlocksInCreative, CRC32CHasher::ofBoolean)
        }
    }

    companion object {
        const val DEFAULT_MINING_SPEED = 1f
        const val DEFAULT_DAMAGE_PER_BLOCK = 1
        const val DEFAULT_CAN_DESTROY_BLOCKS_IN_CREATIVE = true

        val STREAM_CODEC = StreamCodec.of(
            Rule.STREAM_CODEC.list(), ToolComponent::rules,
            StreamCodec.FLOAT, ToolComponent::defaultMiningSpeed,
            StreamCodec.VAR_INT, ToolComponent::damagePerBlock,
            StreamCodec.BOOLEAN, ToolComponent::canDestroyBlocksInCreative,
            ::ToolComponent
        )
    }

    data class Rule(
        val blocks: BlockTypeFilter,
        val speed: Float? = null,
        val correctForDrops: Boolean? = null
    ) : DataComponentHashable {
        override fun hashStruct(): HashHolder {
            return CRC32CHasher.of {
                static("blocks", blocks.hashStruct().getHashed())
                optional("speed", speed, CRC32CHasher::ofFloat)
                optional("correct_for_drops", correctForDrops, CRC32CHasher::ofBoolean)
            }
        }

        companion object {
            val STREAM_CODEC = StreamCodec.of(
                BlockTypeFilter.STREAM_CODEC, Rule::blocks,
                StreamCodec.FLOAT.optional(), Rule::speed,
                StreamCodec.BOOLEAN.optional(), Rule::correctForDrops,
                ::Rule
            )
        }
    }
}