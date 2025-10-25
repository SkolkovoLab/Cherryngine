package ru.cherryngine.lib.minecraft.dialog.body

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.registry.registries.DialogBodyType

sealed class DialogBody : NbtWritable {
    abstract val type: DialogBodyType

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("type", type.getEntryIdentifier())
        }
    }
}