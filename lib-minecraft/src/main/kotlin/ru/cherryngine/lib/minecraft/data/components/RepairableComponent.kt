package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.registry.registries.Item
import ru.cherryngine.lib.minecraft.registry.registries.ItemRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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