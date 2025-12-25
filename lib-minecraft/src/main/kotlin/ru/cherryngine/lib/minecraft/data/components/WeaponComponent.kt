package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class WeaponComponent(
    val itemDamagePerAttack: Int,
    val disableBlockingForSeconds: Float
) : DataComponent() {

    companion object {
        val DEFAULT: WeaponComponent = WeaponComponent(1, 0.0f)

        val CODEC = StructCodec.of(
            "item_damage_per_attack", Codec.INT.default(1), WeaponComponent::itemDamagePerAttack,
            "disable_blocking_for_seconds", Codec.FLOAT.default(0f), WeaponComponent::disableBlockingForSeconds,
            ::WeaponComponent
        )

        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, WeaponComponent::itemDamagePerAttack,
            StreamCodec.FLOAT, WeaponComponent::disableBlockingForSeconds,
            ::WeaponComponent
        )
    }
}