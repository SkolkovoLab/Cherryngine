package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl

class DyedColorComponent(
    val color: RGBLike
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofColor(color))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RGBLikeImpl.NETWORK_TYPE, DyedColorComponent::color,
            ::DyedColorComponent
        )
    }
}