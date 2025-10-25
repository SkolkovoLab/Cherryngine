package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class BlockEntityDataComponent(
    val nbt: CompoundBinaryTag
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodecNBT.COMPOUND_STREAM, BlockEntityDataComponent::nbt,
            ::BlockEntityDataComponent
        )
    }
}