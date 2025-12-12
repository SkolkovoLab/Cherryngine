package ru.cherryngine.lib.minecraft.dialog.body

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.network.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.registry.entries.DialogBodyType

sealed class DialogBody : NbtWritable {
    abstract val type: DialogBodyType

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("type", type.getEntryIdentifier())
        }
    }
}