package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.entity.SalmonMeta
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class SalmonSizeComponent(
    val size: SalmonMeta.Size,
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofEnum(size))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<SalmonMeta.Size>(), SalmonSizeComponent::size,
            ::SalmonSizeComponent
        )
    }
}