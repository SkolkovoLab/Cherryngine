package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.Item

class RepairableComponent(
    val materials: List<Item>,
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofList(materials.map { material ->
            CRC32CHasher.ofRegistryEntry(
                Registries.item,
                material
            )
        }))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Registries.item.streamCodec.list(), RepairableComponent::materials,
            ::RepairableComponent
        )
    }
}