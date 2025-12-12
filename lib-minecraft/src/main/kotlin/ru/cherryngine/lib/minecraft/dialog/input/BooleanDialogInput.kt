package ru.cherryngine.lib.minecraft.dialog.input

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.registry.entries.DialogInputType
import ru.cherryngine.lib.minecraft.registry.keys.DialogInputTypes
import ru.cherryngine.lib.minecraft.utils.extentions.modify

class BooleanDialogInput(
    override val key: String,
    override val label: Component,
    val initial: Boolean,
    val onTrue: String,
    val onFalse: String,
) : DialogInput() {
    override val type: DialogInputType = DialogInputTypes.BOOLEAN

    override fun getNbt(): CompoundBinaryTag {
        return super.getNbt().modify {
            withBoolean("initial", initial)
            withString("on_true", onTrue)
            withString("on_false", onFalse)
        }
    }

    class Builder(key: String) : DialogInput.Builder(key) {
        var initial: Boolean = false
        var onTrue: String = "true"
        var onFalse: String = "false"

        override fun build(): BooleanDialogInput {
            return BooleanDialogInput(key, label, initial, onTrue, onFalse)
        }
    }
}