package io.github.dockyardmc.dialog.button

import io.github.dockyardmc.nbt.nbt
import io.github.dockyardmc.protocol.NbtWritable
import io.github.dockyardmc.utils.toNBT
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component

sealed class AbstractDialogButton : NbtWritable {
    abstract val label: Component
    abstract val tooltip: Component?
    abstract val width: Int

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withCompound("label", label.toNBT())
            tooltip?.let {
                withCompound("tooltip", it.toNBT())
            }
            withInt("width", width)
        }
    }

    sealed class Builder(val label: Component) {
        var tooltip: Component? = null
        var width: Int = 150

        abstract fun build(): AbstractDialogButton
    }
}
