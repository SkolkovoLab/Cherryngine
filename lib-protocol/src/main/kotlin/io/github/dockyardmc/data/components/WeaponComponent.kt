package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.tide.stream.StreamCodec

class WeaponComponent(
    val itemDamagePerAttack: Int,
    val disableBlockingForSeconds: Float
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            default("item_damage_per_attack", 1, itemDamagePerAttack, CRC32CHasher::ofInt)
            default("disable_blocking_for_seconds", 0f, disableBlockingForSeconds, CRC32CHasher::ofFloat)
        }
    }

    companion object {
        val DEFAULT: WeaponComponent = WeaponComponent(1, 0.0f)

        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, WeaponComponent::itemDamagePerAttack,
            StreamCodec.FLOAT, WeaponComponent::disableBlockingForSeconds,
            ::WeaponComponent
        )
    }
}