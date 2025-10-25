package ru.cherryngine.lib.minecraft.dialog.input

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.registry.registries.DialogInputType
import ru.cherryngine.lib.minecraft.utils.toNBT

sealed class DialogInput : NbtWritable {
    abstract val key: String
    abstract val label: Component
    abstract val type: DialogInputType

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("key", key)
            withCompound("label", label.toNBT())
            withString("type", type.getEntryIdentifier())
        }
    }

    sealed class Builder(val key: String) {
        var label: Component = Component.empty()

        abstract fun build(): DialogInput
    }
}