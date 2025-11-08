package ru.cherryngine.lib.minecraft.registry.registries

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.dialog.Dialog
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

class DialogRegistry : DynamicRegistry<DialogEntry>() {
    override val identifier: String = "minecraft:dialog"

    override fun updateCache() {
        cachedPacket = ClientboundRegistryDataPacket(this)
    }
}

data class DialogEntry(
    val identifier: String,
    val dialog: Dialog
) : RegistryEntry {
    override fun getNbt(): CompoundBinaryTag {
        return dialog.getNbt()
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}
