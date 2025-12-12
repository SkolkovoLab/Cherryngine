package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.Item
import ru.cherryngine.lib.minecraft.registry.registries.ItemRegistry

class RepairableComponent(
    val materials: List<Item>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofList(materials.map { material -> CRC32CHasher.ofRegistryEntry(material) }))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ItemRegistry.STREAM_CODEC.list(), RepairableComponent::materials,
            ::RepairableComponent
        )
    }
}