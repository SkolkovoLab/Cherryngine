package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.network.protocol.types.EquipmentSlot
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.keys.SoundEvents
import ru.cherryngine.lib.minecraft.registry.types.EntityType
import ru.cherryngine.lib.minecraft.registry.types.SoundEvent

class EquippableComponent(
    val equipmentSlot: EquipmentSlot,
    val equipSound: SoundEvent,
    val assetId: String?,
    val cameraOverlay: String?,
    val allowedEntities: List<EntityType>?,
    val dispensable: Boolean,
    val swappable: Boolean,
    val damageOnHurt: Boolean,
    val equipOnInteract: Boolean,
    val canBeSheared: Boolean,
    val shearingSound: SoundEvent,
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            static("slot", CRC32CHasher.ofEnum(equipmentSlot))
            defaultStruct("equip_sound", DEFAULT_EQUIP_SOUND, equipSound, SoundEvent::hashStruct)
            optional("asset_id", assetId, CRC32CHasher::ofString)
            optional("camera_overlay", cameraOverlay, CRC32CHasher::ofString)
            optionalList("allowed_entities", allowedEntities) { CRC32CHasher.ofRegistryEntry(Registries.entityType, it) }
            default("dispensable", true, dispensable, CRC32CHasher::ofBoolean)
            default("swappable", true, swappable, CRC32CHasher::ofBoolean)
            default("damage_on_hurt", true, damageOnHurt, CRC32CHasher::ofBoolean)
            default("equip_on_interact", false, equipOnInteract, CRC32CHasher::ofBoolean)
            default("can_be_sheared", false, canBeSheared, CRC32CHasher::ofBoolean)
            defaultStruct("shearing_sound", DEFAULT_SHEARING_SOUND, shearingSound, SoundEvent::hashStruct)
        }
    }

    companion object {
        val DEFAULT_EQUIP_SOUND get() = Registries.soundEvent[SoundEvents.ITEM_ARMOR_EQUIP_GENERIC].value
        val DEFAULT_SHEARING_SOUND get() = Registries.soundEvent[SoundEvents.ITEM_SHEARS_SNIP].value

        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<EquipmentSlot>(), EquippableComponent::equipmentSlot,
            SoundEvent.STREAM_CODEC, EquippableComponent::equipSound,
            StreamCodec.STRING.optional(), EquippableComponent::assetId,
            StreamCodec.STRING.optional(), EquippableComponent::cameraOverlay,
            Registries.entityType.streamCodec.list().optional(), EquippableComponent::allowedEntities,
            StreamCodec.BOOLEAN, EquippableComponent::dispensable,
            StreamCodec.BOOLEAN, EquippableComponent::swappable,
            StreamCodec.BOOLEAN, EquippableComponent::damageOnHurt,
            StreamCodec.BOOLEAN, EquippableComponent::equipOnInteract,
            StreamCodec.BOOLEAN, EquippableComponent::canBeSheared,
            SoundEvent.STREAM_CODEC, EquippableComponent::shearingSound,
            ::EquippableComponent
        )
    }
}