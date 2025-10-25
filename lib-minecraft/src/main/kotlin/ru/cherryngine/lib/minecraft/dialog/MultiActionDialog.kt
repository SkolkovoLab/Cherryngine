package ru.cherryngine.lib.minecraft.dialog

import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.dialog.body.DialogBody
import ru.cherryngine.lib.minecraft.dialog.button.DialogButton
import ru.cherryngine.lib.minecraft.dialog.input.DialogInput
import ru.cherryngine.lib.minecraft.extentions.putList
import ru.cherryngine.lib.minecraft.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.registry.DialogTypes
import ru.cherryngine.lib.minecraft.registry.registries.DialogEntry
import ru.cherryngine.lib.minecraft.registry.registries.DialogRegistry
import ru.cherryngine.lib.minecraft.registry.registries.DialogType

class MultiActionDialog(
    override val title: Component,
    override val externalTitle: Component?,
    override val canCloseWithEsc: Boolean,
    override val body: List<DialogBody>,
    override val afterAction: AfterAction,
    override val inputs: Collection<DialogInput>,
    val actions: Collection<DialogButton>,
    val exitAction: DialogButton? = null,
    val columns: Int = 2,
) : Dialog() {
    override val type: DialogType = DialogTypes.MULTI_ACTION

    init {
        require(actions.isNotEmpty()) { "actions can't be empty" }
    }

    override fun getNbt(): CompoundBinaryTag {
        var nbt = super.getNbt()
        nbt = nbt.putList("actions", BinaryTagTypes.COMPOUND, actions.map(NbtWritable::getNbtAsCompound))
        exitAction?.let {
            nbt = nbt.put("exit_action", it.getNbt())
        }
        nbt = nbt.putInt("columns", columns)

        return nbt
    }

    class Builder : Dialog.Builder() {
        val actions = mutableListOf<DialogButton>()
        var exitAction: DialogButton? = null
        var columns: Int = 2

        inline fun addAction(label: Component, block: DialogButton.Builder.() -> Unit = {}) {
            actions.add(
                DialogButton.Builder(label).apply(block).build()
            )
        }

        override fun build(): MultiActionDialog {
            return MultiActionDialog(
                title,
                externalTitle,
                canCloseWithEsc,
                body.toList(),
                afterAction,
                inputs.toList(),
                actions.toList(),
                exitAction,
                columns
            )
        }
    }
}

inline fun createMultiActionDialog(id: String, block: MultiActionDialog.Builder.() -> Unit): DialogEntry {
    val entry = DialogEntry(id, MultiActionDialog.Builder().apply(block).build())
    DialogRegistry.addEntry(entry)
    return entry
}
