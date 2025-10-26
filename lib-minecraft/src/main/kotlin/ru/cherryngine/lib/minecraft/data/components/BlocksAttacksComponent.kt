package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.RegistryCodec
import ru.cherryngine.lib.minecraft.codec.transcoder.CRC32CTranscoder
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable
import ru.cherryngine.lib.minecraft.protocol.types.SoundEvent
import ru.cherryngine.lib.minecraft.registry.registries.DamageType
import ru.cherryngine.lib.minecraft.registry.registries.DamageTypeRegistry
import ru.cherryngine.lib.minecraft.registry.registries.EntityType
import ru.cherryngine.lib.minecraft.registry.registries.EntityTypeRegistry
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.StructCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class BlocksAttacksComponent(
    val blocksDelaySeconds: Float,
    val disableCooldownScale: Float,
    val damageReductions: List<DamageReduction>,
    val itemDamageFunction: ItemDamageFunction,
    val bypassedBy: DamageType?,
    val blockSound: SoundEvent?,
    val disableSound: SoundEvent?
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CODEC.encode(CRC32CTranscoder, this))
    }

    companion object {
        val CODEC = StructCodec.of(
            "blocks_delay_seconds", Codec.FLOAT.default(0f), BlocksAttacksComponent::blocksDelaySeconds,
            "disable_cooldown_scale", Codec.FLOAT.default(1f), BlocksAttacksComponent::disableCooldownScale,
            "damage_reductions", DamageReduction.CODEC.list().default(listOf(DamageReduction.DEFAULT)), BlocksAttacksComponent::damageReductions,
            "item_damage", ItemDamageFunction.CODEC.default(ItemDamageFunction.DEFAULT), BlocksAttacksComponent::itemDamageFunction,
            "bypassed_by", RegistryCodec.codec(DamageTypeRegistry).optional(), BlocksAttacksComponent::bypassedBy,
            "block_sound", SoundEvent.CODEC.optional(), BlocksAttacksComponent::blockSound,
            "disabled_sound", SoundEvent.CODEC.optional(), BlocksAttacksComponent::disableSound,
            ::BlocksAttacksComponent
        )

        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, BlocksAttacksComponent::blocksDelaySeconds,
            StreamCodec.FLOAT, BlocksAttacksComponent::disableCooldownScale,
            DamageReduction.STREAM_CODEC.list(), BlocksAttacksComponent::damageReductions,
            ItemDamageFunction.STREAM_CODEC, BlocksAttacksComponent::itemDamageFunction,
            DamageTypeRegistry.STREAM_CODEC.optional(), BlocksAttacksComponent::bypassedBy,
            SoundEvent.STREAM_CODEC.optional(), BlocksAttacksComponent::blockSound,
            SoundEvent.STREAM_CODEC.optional(), BlocksAttacksComponent::disableSound,
            ::BlocksAttacksComponent
        )
    }

    data class ItemDamageFunction(
        val threshold: Float,
        val base: Float,
        val factor: Float
    ) : DataComponentHashable {
        override fun hashStruct(): HashHolder {
            return CRC32CHasher.of {
                static("threshold", CRC32CHasher.ofFloat(threshold))
                static("base", CRC32CHasher.ofFloat(base))
                static("factor", CRC32CHasher.ofFloat(factor))
            }
        }

        companion object {
            val DEFAULT = ItemDamageFunction(1f, 0f, 1f)

            val CODEC = StructCodec.of(
                "threshold", Codec.FLOAT, ItemDamageFunction::threshold,
                "base", Codec.FLOAT, ItemDamageFunction::base,
                "factor", Codec.FLOAT, ItemDamageFunction::factor,
                ::ItemDamageFunction
            )

            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.FLOAT, ItemDamageFunction::threshold,
                StreamCodec.FLOAT, ItemDamageFunction::base,
                StreamCodec.FLOAT, ItemDamageFunction::factor,
                ::ItemDamageFunction
            )
        }
    }

    data class DamageReduction(
        val horizontalBlockingAngle: Float,
        val type: EntityType?,
        val base: Float,
        val factor: Float
    ) : DataComponentHashable {
        override fun hashStruct(): HashHolder {
            return CRC32CHasher.of {
                default("horizontal_blocking_angle", DEFAULT.horizontalBlockingAngle, horizontalBlockingAngle, CRC32CHasher::ofFloat)
                optional("type", type?.identifier, CRC32CHasher::ofString)
                static("base", CRC32CHasher.ofFloat(base))
                static("factor", CRC32CHasher.ofFloat(factor))
            }
        }

        companion object {
            val DEFAULT = DamageReduction(90.0f, null, 0.0f, 1.0f)

            val CODEC = StructCodec.of(
                "horizontal_blocking_angle", Codec.FLOAT.default(DEFAULT.horizontalBlockingAngle), DamageReduction::horizontalBlockingAngle,
                "type", RegistryCodec.codec(EntityTypeRegistry).optional(), DamageReduction::type,
                "base", Codec.FLOAT, DamageReduction::base,
                "factor", Codec.FLOAT, DamageReduction::factor,
                ::DamageReduction
            )

            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.FLOAT, DamageReduction::horizontalBlockingAngle,
                EntityTypeRegistry.STREAM_CODEC.optional(), DamageReduction::type,
                StreamCodec.FLOAT, DamageReduction::base,
                StreamCodec.FLOAT, DamageReduction::factor,
                ::DamageReduction
            )
        }
    }
}