package ru.cherryngine.lib.minecraft.dialog

import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.dialog.body.DialogBody
import ru.cherryngine.lib.minecraft.dialog.button.DialogButton
import ru.cherryngine.lib.minecraft.dialog.input.DialogInput
import ru.cherryngine.lib.minecraft.extentions.putList
import ru.cherryngine.lib.minecraft.registry.DialogTypes
import ru.cherryngine.lib.minecraft.registry.registries.DialogEntry
import ru.cherryngine.lib.minecraft.registry.registries.DialogRegistry
import ru.cherryngine.lib.minecraft.registry.registries.DialogType

class DialogListDialog(
    override val title: Component,
    override val externalTitle: Component?,
    override val canCloseWithEsc: Boolean,
    override val body: List<DialogBody>,
    override val afterAction: AfterAction,
    override val inputs: Collection<DialogInput>,
    val dialogs: Collection<DialogEntry>,
    val exitAction: DialogButton?,
    val columns: Int,
    val buttonWidth: Int,
) : Dialog() {
    override val type: DialogType = DialogTypes.DIALOG_LIST

    override fun getNbt(): CompoundBinaryTag {
        var nbt = super.getNbt()

        nbt = nbt.putList("dialogs", BinaryTagTypes.STRING, dialogs.map { StringBinaryTag.stringBinaryTag(it.getEntryIdentifier()) })

        exitAction?.let {
            nbt = nbt.put("exit_action", it.getNbt())
        }
        nbt = nbt.putInt("columns", columns)
        nbt = nbt.putInt("button_width", buttonWidth)
        return nbt
    }

    class Builder : Dialog.Builder() {
        val dialogs = mutableListOf<DialogEntry>()
        var exitAction: DialogButton? = null
        var columns: Int = 2
        var buttonWidth: Int = 150

        fun addDialog(dialog: DialogEntry) {
            dialogs.add(dialog)
        }

        override fun build(): DialogListDialog {
            return DialogListDialog(
                title,
                externalTitle,
                canCloseWithEsc,
                body.toList(),
                afterAction,
                inputs.toList(),
                dialogs.toList(),
                exitAction,
                columns,
                buttonWidth
            )
        }
    }
}

inline fun createDialogListDialog(id: String, block: DialogListDialog.Builder.() -> Unit): DialogEntry {
    val entry = DialogEntry(id, DialogListDialog.Builder().apply(block).build())
    DialogRegistry.addEntry(entry)
    return entry
}
