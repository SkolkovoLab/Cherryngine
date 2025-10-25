package ru.cherryngine.lib.minecraft.dialog.button

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.utils.toNBT

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
