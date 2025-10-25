package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.tide.stream.MapStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class DebugStickComponent(
    val state: Map<String, String>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        val hashedMap: Map<Int, Int> = state.mapValues { entry -> CRC32CHasher.ofString(entry.value) }.mapKeys { entry -> CRC32CHasher.ofString(entry.key) }
        return StaticHash(CRC32CHasher.ofMap(hashedMap))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            MapStreamCodec(StreamCodec.STRING, StreamCodec.STRING), DebugStickComponent::state,
            ::DebugStickComponent
        )
    }
}