package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class DamageType(
    val identifier: String,
    val exhaustion: Float,
    @SerialName("message_id")
    val messageId: String,
    val scaling: String,
    val effects: String? = null,
    @SerialName("death_message_type")
    val deathMessageType: String? = null,
) : RegistryEntry {
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