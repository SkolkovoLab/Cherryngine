package ru.cherryngine.lib.minecraft.dialog.action

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.extentions.modify
import ru.cherryngine.lib.minecraft.registry.DialogActionTypes
import ru.cherryngine.lib.minecraft.registry.registries.DialogActionType

/**
 * Client will send a [ru.cherryngine.lib.minecraft.protocol.packets.common.ServerboundCustomClickActionPacket]
 * with the data defined by dialog's *inputs*
 *
 * @property id ID of the custom click action
 * @property additions will be added to payload NBT tag by client
 */
class CustomDialogAction(val id: String, val additions: CompoundBinaryTag?) : DialogAction() {
    override val type: DialogActionType = DialogActionTypes.DYNAMIC_CUSTOM

    override fun getNbt(): CompoundBinaryTag {
        return super.getNbt().modify {
            withString("id", id)
            additions?.let {
                withCompound("additions", it)
            }
        }
    }
}