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
