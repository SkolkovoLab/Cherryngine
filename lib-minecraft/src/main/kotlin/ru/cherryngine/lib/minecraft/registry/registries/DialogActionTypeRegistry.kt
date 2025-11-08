package ru.cherryngine.lib.minecraft.registry.registries

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

class DialogActionTypeRegistry : DynamicRegistry<DialogActionType>() {
    override val identifier: String = "minecraft:dialog_action_type"

    init {
        // this is not okay
        listOf(
            "open_url",
            "run_command",
            "suggest_command",
            "change_page",
            "copy_to_clipboard",
            "show_dialog",
            "custom",

            "dynamic/run_command",
            "dynamic/custom"
        ).forEach {
            addEntry(DialogActionType(it))
        }
        updateCache()
    }

    override fun updateCache() {
        cachedPacket = ClientboundRegistryDataPacket(this)
    }
}

data class DialogActionType(
    val identifier: String,
) : RegistryEntry {
    override fun getNbt(): CompoundBinaryTag? {
        return null
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}
