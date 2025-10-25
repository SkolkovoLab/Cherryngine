package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.tide.stream.MapStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ItemBlockStateComponent(
    val properties: Map<String, String>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        val map = properties.mapValues { value -> CRC32CHasher.ofString(value.value) }.mapKeys { key -> CRC32CHasher.ofString(key.key) }
        return StaticHash(CRC32CHasher.ofMap(map))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            MapStreamCodec(StreamCodec.STRING, StreamCodec.STRING), ItemBlockStateComponent::properties,
            ::ItemBlockStateComponent
        )
    }
}