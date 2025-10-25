package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.registry.registries.Item
import io.github.dockyardmc.registry.registries.ItemRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

class RepairableComponent(
    val materials: List<Item>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofList(materials.map { material -> CRC32CHasher.ofRegistryEntry(material) }))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(ItemRegistry).list(), RepairableComponent::materials,
            ::RepairableComponent
        )
    }
}