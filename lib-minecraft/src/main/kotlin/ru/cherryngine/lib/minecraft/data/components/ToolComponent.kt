package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.protocol.types.predicate.BlockTypeFilter
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class ToolComponent(
    val rules: List<Rule>,
    val defaultMiningSpeed: Float,
    val damagePerBlock: Int,
    val canDestroyBlocksInCreative: Boolean
) : DataComponent() {
    companion object {
        const val DEFAULT_MINING_SPEED = 1f
        const val DEFAULT_DAMAGE_PER_BLOCK = 1
        const val DEFAULT_CAN_DESTROY_BLOCKS_IN_CREATIVE = true

        val CODEC = StructCodec.of(
            "rules", Rule.CODEC.list(), ToolComponent::rules,
            "default_mining_speed", Codec.FLOAT.default(DEFAULT_MINING_SPEED), ToolComponent::defaultMiningSpeed,
            "damage_per_block", Codec.INT.default(DEFAULT_DAMAGE_PER_BLOCK), ToolComponent::damagePerBlock,
            "can_destroy_blocks_in_creative", Codec.BOOLEAN.default(DEFAULT_CAN_DESTROY_BLOCKS_IN_CREATIVE), ToolComponent::canDestroyBlocksInCreative,
            ::ToolComponent
        )

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
    ) {
        companion object {
            val CODEC = StructCodec.of(
                "blocks", BlockTypeFilter.CODEC, Rule::blocks,
                "speed", Codec.FLOAT.optional(), Rule::speed,
                "correct_for_drops", Codec.BOOLEAN.optional(), Rule::correctForDrops,
                ::Rule
            )
            val STREAM_CODEC = StreamCodec.of(
                BlockTypeFilter.STREAM_CODEC, Rule::blocks,
                StreamCodec.FLOAT.optional(), Rule::speed,
                StreamCodec.BOOLEAN.optional(), Rule::correctForDrops,
                ::Rule
            )
        }
    }
}