package ru.cherryngine.lib.minecraft.registry.entries

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.dialog.Dialog
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

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