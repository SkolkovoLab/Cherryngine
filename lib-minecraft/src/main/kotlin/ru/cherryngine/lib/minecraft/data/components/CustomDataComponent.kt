package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.BinaryTagCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.BinaryTagStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class CustomDataComponent(
    val nbt: CompoundBinaryTag,
) : DataComponent() {

    companion object {
        val CODEC = BinaryTagCodec.COMPOUND_CODEC.transform(
            ::CustomDataComponent,
            CustomDataComponent::nbt
        )
        val STREAM_CODEC = StreamCodec.of(
            BinaryTagStreamCodecs.COMPOUND_STREAM, CustomDataComponent::nbt,
            ::CustomDataComponent
        )
    }
}