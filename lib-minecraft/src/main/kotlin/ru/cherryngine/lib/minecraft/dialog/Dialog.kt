package ru.cherryngine.lib.minecraft.dialog

import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.dialog.body.DialogBody
import ru.cherryngine.lib.minecraft.dialog.body.DialogItemBody
import ru.cherryngine.lib.minecraft.dialog.body.PlainMessage
import ru.cherryngine.lib.minecraft.dialog.input.*
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.registry.registries.DialogType
import ru.cherryngine.lib.minecraft.registry.registries.Item
import ru.cherryngine.lib.minecraft.utils.toNBT

sealed class Dialog : NbtWritable {
    abstract val title: Component

    /** the title of this dialog on other screens, like [DialogListDialog] or pause menu */
    abstract val externalTitle: Component?
    abstract val canCloseWithEsc: Boolean
    abstract val body: List<DialogBody>
    abstract val type: DialogType
    abstract val inputs: Collection<DialogInput>

    /**
     * what happens after click or submit actions
     *
     * Default: [AfterAction.CLOSE]
     */
    abstract val afterAction: AfterAction

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("type", type.getEntryIdentifier())
            withCompound("title", title.toNBT())

            externalTitle?.let {
                withCompound("external_title", it.toNBT())
            }

            withBoolean("can_close_with_escape", canCloseWithEsc)
            withList("body", BinaryTagTypes.COMPOUND, body.map { it.getNbt() })
            // you can't play singleplayer on dockyard
            withBoolean("pause", false)
            withString("after_action", afterAction.name.lowercase())
            withList("inputs", BinaryTagTypes.COMPOUND, inputs.map { it.getNbt() })
        }
    }

    enum class AfterAction {
        /** closes the dialog */
        CLOSE,

        /** actually nothing happens */
        NONE,

        /**
         * The server is expected to replace
         * current screen with another dialog btw */
        WAIT_FOR_RESPONSE;
    }

    abstract class Builder {
        var title: Component = Component.empty()
        var externalTitle: Component? = null
        var canCloseWithEsc: Boolean = true
        val body = mutableListOf<DialogBody>()
        val inputs = mutableListOf<DialogInput>()
        var afterAction: AfterAction = AfterAction.CLOSE

        inline fun addItemBody(item: ItemStack, block: DialogItemBody.Builder.() -> Unit = {}) {
            val builder = DialogItemBody.Builder(item)
            builder.apply(block)
            body.add(builder.build())
        }

        inline fun addItemBody(item: Item, block: DialogItemBody.Builder.() -> Unit = {}) = addItemBody(item.toItemStack(), block)

        inline fun addPlainMessage(content: Component, block: PlainMessage.Builder.() -> Unit = {}) {
            val builder = PlainMessage.Builder(content)
            builder.apply(block)
            body.add(builder.build())
        }

        inline fun addTextInput(key: String, block: TextDialogInput.Builder.() -> Unit = {}) {
            val builder = TextDialogInput.Builder(key)
            builder.apply(block)
            inputs.add(builder.build())
        }

        inline fun addBooleanInput(key: String, block: BooleanDialogInput.Builder.() -> Unit = {}) {
            val builder = BooleanDialogInput.Builder(key)
            builder.apply(block)
            inputs.add(builder.build())
        }

        inline fun addNumberRangeInput(key: String, range: ClosedFloatingPointRange<Double>, block: NumberRangeDialogInput.Builder.() -> Unit = {}) {
            val builder = NumberRangeDialogInput.Builder(key, range)
            builder.apply(block)
            inputs.add(builder.build())
        }

        inline fun addSingleOptionInput(key: String, block: SingleOptionDialogInput.Builder.() -> Unit) {
            inputs.add(SingleOptionDialogInput.Builder(key).apply(block).build())
        }

        abstract fun build(): Dialog
    }
}
