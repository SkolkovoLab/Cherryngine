package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class TooltipStyleComponent(
    val style: String
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofString(style))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, TooltipStyleComponent::style,
            ::TooltipStyleComponent
        )
    }
}