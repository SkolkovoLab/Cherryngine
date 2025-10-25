package ru.cherryngine.lib.minecraft.dialog

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.dialog.body.DialogBody
import ru.cherryngine.lib.minecraft.dialog.button.DialogButton
import ru.cherryngine.lib.minecraft.dialog.input.DialogInput
import ru.cherryngine.lib.minecraft.extentions.modify
import ru.cherryngine.lib.minecraft.registry.DialogTypes
import ru.cherryngine.lib.minecraft.registry.registries.DialogEntry
import ru.cherryngine.lib.minecraft.registry.registries.DialogRegistry
import ru.cherryngine.lib.minecraft.registry.registries.DialogType

class ServerLinksDialog(
    override val title: Component,
    override val externalTitle: Component?,
    override val canCloseWithEsc: Boolean,
    override val body: List<DialogBody>,
    override val afterAction: AfterAction,
    override val inputs: Collection<DialogInput>,
    val exitAction: DialogButton? = null,
    val columns: Int = 2,
    val buttonWidth: Int = 150,
) : Dialog() {
    override val type: DialogType = DialogTypes.SERVER_LINKS

    override fun getNbt(): CompoundBinaryTag {
        return super.getNbt().modify {
            exitAction?.let {
                withCompound("exit_action", it.getNbt())
            }
            withInt("columns", columns)
            withInt("button_width", buttonWidth)
        }
    }

    class Builder : Dialog.Builder() {
        var exitAction: DialogButton? = null
        var columns: Int = 2
        var buttonWidth: Int = 150

        override fun build(): ServerLinksDialog {
            return ServerLinksDialog(
                title,
                externalTitle,
                canCloseWithEsc,
                body.toList(),
                afterAction,
                inputs.toList(),
                exitAction,
                columns,
                buttonWidth
            )
        }
    }
}

inline fun createServerLinksDialog(id: String, block: ServerLinksDialog.Builder.() -> Unit): DialogEntry {
    val entry = DialogEntry(id, ServerLinksDialog.Builder().apply(block).build())
    DialogRegistry.addEntry(entry)
    return entry
}
