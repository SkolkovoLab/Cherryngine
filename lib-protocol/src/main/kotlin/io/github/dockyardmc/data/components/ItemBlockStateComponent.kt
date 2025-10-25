package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.MapStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

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