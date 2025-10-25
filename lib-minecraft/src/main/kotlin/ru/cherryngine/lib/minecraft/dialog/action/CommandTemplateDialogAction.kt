package ru.cherryngine.lib.minecraft.dialog.action

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.extentions.modify
import ru.cherryngine.lib.minecraft.registry.DialogActionTypes
import ru.cherryngine.lib.minecraft.registry.registries.DialogActionType

/**
 * Command template will be processed by the client and then executed as a command
 *
 * @property template The template.
 *
 * Example: `say $(arg1)`.
 *
 * will replace the `$(arg1)` with the contents of the input with key `arg1`
 */
class CommandTemplateDialogAction(val template: String) : DialogAction() {
    override val type: DialogActionType = DialogActionTypes.DYNAMIC_RUN_COMMAND

    override fun getNbt(): CompoundBinaryTag {
        return super.getNbt().modify {
            withString("template", template)
        }
    }
}