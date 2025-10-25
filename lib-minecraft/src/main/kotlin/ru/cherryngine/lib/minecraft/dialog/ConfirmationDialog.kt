package ru.cherryngine.lib.minecraft.dialog

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.dialog.body.DialogBody
import ru.cherryngine.lib.minecraft.dialog.button.DialogButton
import ru.cherryngine.lib.minecraft.dialog.input.DialogInput
import ru.cherryngine.lib.minecraft.registry.DialogTypes
import ru.cherryngine.lib.minecraft.registry.registries.DialogEntry
import ru.cherryngine.lib.minecraft.registry.registries.DialogRegistry
import ru.cherryngine.lib.minecraft.registry.registries.DialogType

class ConfirmationDialog(
    override val title: Component,
    override val externalTitle: Component?,
    override val canCloseWithEsc: Boolean,
    override val body: List<DialogBody>,
    override val afterAction: AfterAction,
    override val inputs: Collection<DialogInput>,
    val yes: DialogButton,
    val no: DialogButton,
) : Dialog() {
    override val type: DialogType = DialogTypes.CONFIRMATION

    override fun getNbt(): CompoundBinaryTag {
        var nbt = super.getNbt()
        nbt = nbt.put("yes", yes.getNbt())
        nbt = nbt.put("no", no.getNbt())

        return nbt
    }

    class Builder : Dialog.Builder() {
        lateinit var yes: DialogButton
        lateinit var no: DialogButton

        inline fun withYes(label: Component, block: DialogButton.Builder.() -> Unit = {}) {
            yes = DialogButton.Builder(label).apply(block).build()
        }

        inline fun withNo(label: Component, block: DialogButton.Builder.() -> Unit = {}) {
            no = DialogButton.Builder(label).apply(block).build()
        }

        override fun build(): ConfirmationDialog {
            return ConfirmationDialog(
                title,
                externalTitle,
                canCloseWithEsc,
                body.toList(),
                afterAction,
                inputs.toList(),
                yes,
                no
            )
        }
    }
}

inline fun createConfirmationDialog(id: String, block: ConfirmationDialog.Builder.() -> Unit): DialogEntry {
    val entry = DialogEntry(id, ConfirmationDialog.Builder().apply(block).build())
    DialogRegistry.addEntry(entry)
    return entry
}
