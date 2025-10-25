package io.github.dockyardmc.dialog

import io.github.dockyardmc.dialog.body.DialogBody
import io.github.dockyardmc.dialog.button.DialogButton
import io.github.dockyardmc.dialog.input.DialogInput
import io.github.dockyardmc.registry.DialogTypes
import io.github.dockyardmc.registry.registries.DialogEntry
import io.github.dockyardmc.registry.registries.DialogRegistry
import io.github.dockyardmc.registry.registries.DialogType
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component

class NoticeDialog(
    override val title: Component,
    override val externalTitle: Component?,
    override val canCloseWithEsc: Boolean,
    override val body: List<DialogBody>,
    override val afterAction: AfterAction,
    override val inputs: Collection<DialogInput>,
    val button: DialogButton,
) : Dialog() {
    override val type: DialogType = DialogTypes.NOTICE

    override fun getNbt(): CompoundBinaryTag {
        return super.getNbt().put("action", button.getNbt())
    }

    class Builder : Dialog.Builder() {
        var button: DialogButton = DialogButton(Component.translatable("gui.ok"), null, 150, null)

        inline fun withButton(label: Component, block: DialogButton.Builder.() -> Unit = {}) {
            button = DialogButton.Builder(label).apply(block).build()
        }

        override fun build(): NoticeDialog {
            return NoticeDialog(
                title,
                externalTitle,
                canCloseWithEsc,
                body.toList(),
                afterAction,
                inputs.toList(),
                button
            )
        }
    }
}

inline fun createNoticeDialog(id: String, block: NoticeDialog.Builder.() -> Unit): DialogEntry {
    val entry = DialogEntry(id, NoticeDialog.Builder().apply(block).build())
    DialogRegistry.addEntry(entry)
    return entry
}
