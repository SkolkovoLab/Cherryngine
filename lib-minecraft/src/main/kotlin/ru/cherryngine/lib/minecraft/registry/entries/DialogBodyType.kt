package ru.cherryngine.lib.minecraft.registry.entries

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.dialog.body.DialogBody
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import kotlin.reflect.KClass

data class DialogBodyType(
    val identifier: String,
    val clazz: KClass<out DialogBody>,
) : RegistryEntry {
    override fun getNbt(): CompoundBinaryTag? {
        return null
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}