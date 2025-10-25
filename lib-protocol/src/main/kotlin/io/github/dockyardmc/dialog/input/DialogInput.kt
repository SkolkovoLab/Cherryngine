package io.github.dockyardmc.dialog.input

import io.github.dockyardmc.nbt.nbt
import io.github.dockyardmc.protocol.NbtWritable
import io.github.dockyardmc.registry.registries.DialogInputType
import io.github.dockyardmc.utils.toNBT
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component

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