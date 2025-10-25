package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object DamageTypeRegistry : DataDrivenRegistry<DamageType>() {
    override val identifier: String = "minecraft:damage_type"
}

@Serializable
data class DamageType(
    val identifier: String,
    val exhaustion: Float,
    val messageId: String,
    val scaling: String,
    val effects: String? = null,
    val deathMessageType: String? = null,
) : RegistryEntry {

    override fun getProtocolId(): Int {
        return DamageTypeRegistry.getProtocolIdByEntry(this)
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }


    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withFloat("exhaustion", exhaustion)
            withString("message_id", messageId)
            withString("scaling", scaling)
        }
    }
}