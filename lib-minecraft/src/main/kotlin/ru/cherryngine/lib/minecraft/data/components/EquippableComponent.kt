package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
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

    companion object {
        val DEFAULT_EQUIP_SOUND get() = Registries.soundEvent[SoundEvents.ITEM_ARMOR_EQUIP_GENERIC].value
        val DEFAULT_SHEARING_SOUND get() = Registries.soundEvent[SoundEvents.ITEM_SHEARS_SNIP].value

        val CODEC = StructCodec.of(
            "slot", Codec.enum<EquipmentSlot>(), EquippableComponent::equipmentSlot,
            "equip_sound", SoundEvent.CODEC.default(DEFAULT_EQUIP_SOUND), EquippableComponent::equipSound,
            "asset_id", Codec.STRING.optional(), EquippableComponent::assetId,
            "camera_overlay", Codec.STRING.optional(), EquippableComponent::cameraOverlay,
            "allowed_entities", Registries.entityType.keyCodec.list().optional(), EquippableComponent::allowedEntities,
            "dispensable", Codec.BOOLEAN.default(true), EquippableComponent::dispensable,
            "swappable", Codec.BOOLEAN.default(true), EquippableComponent::swappable,
            "damage_on_hurt", Codec.BOOLEAN.default(true), EquippableComponent::damageOnHurt,
            "equip_on_interact", Codec.BOOLEAN.default(false), EquippableComponent::equipOnInteract,
            "can_be_sheared", Codec.BOOLEAN.default(false), EquippableComponent::canBeSheared,
            "shearing_sound", SoundEvent.CODEC.default(DEFAULT_SHEARING_SOUND), EquippableComponent::shearingSound,
            ::EquippableComponent
        )

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