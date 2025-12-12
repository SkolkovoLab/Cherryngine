package ru.cherryngine.lib.minecraft.network.protocol

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag


interface NbtWritable {
    fun getNbt(): BinaryTag

    fun getNbtAsCompound(): CompoundBinaryTag {
        return getNbt() as CompoundBinaryTag
    }
}