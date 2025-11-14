package ru.cherryngine.lib.minecraft.protocol.types

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.codec.ActionStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

sealed interface ArgumentParserType {
    companion object {
        val STREAM_CODEC = ActionStreamCodec<ArgumentParserType>(
            StreamCodec.VAR_INT,
            ActionStreamCodec.Entry(Bool::class, Bool.STREAM_CODEC),
            ActionStreamCodec.Entry(Float::class, Float.STREAM_CODEC),
            ActionStreamCodec.Entry(Double::class, Double.STREAM_CODEC),
            ActionStreamCodec.Entry(Integer::class, Integer.STREAM_CODEC),
            ActionStreamCodec.Entry(Long::class, Long.STREAM_CODEC),
            ActionStreamCodec.Entry(String::class, String.STREAM_CODEC),
            ActionStreamCodec.Skip(), // minecraft:entity
            ActionStreamCodec.Entry(GameProfile::class, GameProfile.STREAM_CODEC),
            ActionStreamCodec.Skip(), // minecraft:block_pos
            ActionStreamCodec.Skip(), // minecraft:column_pos
            ActionStreamCodec.Entry(Vec3::class, Vec3.STREAM_CODEC),
            ActionStreamCodec.Skip(), // minecraft:vec2
            ActionStreamCodec.Skip(), // minecraft:block_state
            ActionStreamCodec.Skip(), // minecraft:block_predicate
            ActionStreamCodec.Skip(), // minecraft:item_stack
            ActionStreamCodec.Skip(), // minecraft:item_predicate
            ActionStreamCodec.Skip(), // minecraft:color
            ActionStreamCodec.Skip(), // minecraft:hex_color
            ActionStreamCodec.Skip(), // minecraft:component
            ActionStreamCodec.Skip(), // minecraft:style
            ActionStreamCodec.Skip(), // minecraft:message
            ActionStreamCodec.Skip(), // minecraft:nbt_compound_tag
            ActionStreamCodec.Skip(), // minecraft:nbt_tag
            ActionStreamCodec.Skip(), // minecraft:nbt_path
            ActionStreamCodec.Skip(), // minecraft:objective
            ActionStreamCodec.Skip(), // minecraft:objective_criteria
            ActionStreamCodec.Skip(), // minecraft:operation
            ActionStreamCodec.Skip(), // minecraft:particle
            ActionStreamCodec.Skip(), // minecraft:angle
            ActionStreamCodec.Skip(), // minecraft:rotation
            ActionStreamCodec.Skip(), // minecraft:scoreboard_slot
            ActionStreamCodec.Skip(), // minecraft:score_holder
            ActionStreamCodec.Skip(), // minecraft:swizzle
            ActionStreamCodec.Skip(), // minecraft:team
            ActionStreamCodec.Skip(), // minecraft:item_slot
            ActionStreamCodec.Skip(), // minecraft:item_slots
            ActionStreamCodec.Entry(ResourceLocation::class, ResourceLocation.STREAM_CODEC),
            ActionStreamCodec.Skip(), // minecraft:function
            ActionStreamCodec.Skip(), // minecraft:entity_anchor
            ActionStreamCodec.Skip(), // minecraft:int_range
            ActionStreamCodec.Skip(), // minecraft:float_range
            ActionStreamCodec.Skip(), // minecraft:dimension
            ActionStreamCodec.Skip(), // minecraft:gamemode
            ActionStreamCodec.Skip(), // minecraft:time
            ActionStreamCodec.Skip(), // minecraft:resource_or_tag
            ActionStreamCodec.Skip(), // minecraft:resource_or_tag_key
            ActionStreamCodec.Skip(), // minecraft:resource
            ActionStreamCodec.Skip(), // minecraft:resource_key
            ActionStreamCodec.Skip(), // minecraft:resource_selector
            ActionStreamCodec.Skip(), // minecraft:template_mirror
            ActionStreamCodec.Skip(), // minecraft:template_rotation
            ActionStreamCodec.Skip(), // minecraft:heightmap
            ActionStreamCodec.Skip(), // minecraft:loot_table
            ActionStreamCodec.Skip(), // minecraft:loot_predicate
            ActionStreamCodec.Skip(), // minecraft:loot_modifier
            ActionStreamCodec.Skip(), // minecraft:dialog
            ActionStreamCodec.Entry(UUID::class, UUID.STREAM_CODEC),
        )
    }

    sealed interface Ranged<T : Number> : ArgumentParserType {
        val min: T?
        val max: T?
    }

    private class RangedStreamCodec<T : Ranged<N>, N : Number>(
        val rangeCodec: StreamCodec<N>,
        val constructor: (N?, N?) -> T,
    ) : StreamCodec<T> {
        override fun write(buffer: ByteBuf, value: T) {
            val hasMin = value.min != null
            val hasMax = value.max != null
            val flags = ((if (hasMin) 0x1 else 0) or (if (hasMax) 0x2 else 0)).toByte()
            StreamCodec.BYTE.write(buffer, flags)
            if (hasMin) rangeCodec.write(buffer, value.min!!)
            if (hasMax) rangeCodec.write(buffer, value.max!!)
        }

        override fun read(buffer: ByteBuf): T {
            val flags = StreamCodec.BYTE.read(buffer)
            val hasMin = (flags.toInt() and 0x1) != 0
            val hasMax = (flags.toInt() and 0x2) != 0
            val min = if (hasMin) rangeCodec.read(buffer) else null
            val max = if (hasMax) rangeCodec.read(buffer) else null
            return constructor(min, max)
        }
    }

    object Bool : ArgumentParserType {
        val STREAM_CODEC = StreamCodec.of { Bool }
    }

    data class Float(
        override val min: kotlin.Float?,
        override val max: kotlin.Float?,
    ) : Ranged<kotlin.Float> {
        companion object {
            val STREAM_CODEC: StreamCodec<Float> = RangedStreamCodec(StreamCodec.FLOAT, ::Float)
        }
    }

    data class Double(
        override val min: kotlin.Double?,
        override val max: kotlin.Double?,
    ) : Ranged<kotlin.Double> {
        companion object {
            val STREAM_CODEC: StreamCodec<Double> = RangedStreamCodec(StreamCodec.DOUBLE, ::Double)
        }
    }

    data class Integer(
        override val min: Int?,
        override val max: Int?,
    ) : Ranged<Int> {
        companion object {
            val STREAM_CODEC: StreamCodec<Integer> = RangedStreamCodec(StreamCodec.INT, ::Integer)
        }
    }

    data class Long(
        override val min: kotlin.Long?,
        override val max: kotlin.Long?,
    ) : Ranged<kotlin.Long> {
        companion object {
            val STREAM_CODEC: StreamCodec<Long> = RangedStreamCodec(StreamCodec.LONG, ::Long)
        }
    }

    data class String(
        val type: Type,
    ) : ArgumentParserType {
        enum class Type {
            SINGLE_WORD, QUOTABLE_PHRASE, GREEDY_PHRASE
        }

        companion object {
            val STREAM_CODEC = StreamCodec.of(
                EnumStreamCodec.Companion<Type>(), String::type,
                ::String
            )
        }
    }

    // Лайфхак: его збс юзать вместо String(SINGLE_WORD)
    // minecraft:game_profile принимает любые символы в отличие от brigadier:string (SINGLE_WORD)
    object GameProfile : ArgumentParserType {
        val STREAM_CODEC = StreamCodec.of { GameProfile }
    }

    // Поддерживает ~ и ^
    object Vec3 : ArgumentParserType {
        val STREAM_CODEC = StreamCodec.of { Vec3 }
    }

    object ResourceLocation : ArgumentParserType {
        val STREAM_CODEC = StreamCodec.of { ResourceLocation }
    }

    object UUID : ArgumentParserType {
        val STREAM_CODEC = StreamCodec.of { UUID }
    }
}