package ru.cherryngine.lib.minecraft.dialog.action

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.network.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.registry.entries.DialogActionType

sealed class DialogAction : NbtWritable {
    abstract val type: DialogActionType

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("type", type.getEntryIdentifier())
        }
    }
}