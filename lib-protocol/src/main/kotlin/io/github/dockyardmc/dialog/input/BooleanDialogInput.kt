package io.github.dockyardmc.dialog.input

import io.github.dockyardmc.extentions.modify
import io.github.dockyardmc.registry.DialogInputTypes
import io.github.dockyardmc.registry.registries.DialogInputType
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component

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