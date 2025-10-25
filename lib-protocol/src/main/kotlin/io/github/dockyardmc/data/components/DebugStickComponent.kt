package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.MapStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

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