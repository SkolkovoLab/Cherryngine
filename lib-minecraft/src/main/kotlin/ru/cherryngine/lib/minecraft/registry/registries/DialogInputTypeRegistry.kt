package ru.cherryngine.lib.minecraft.registry.registries

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object DialogInputTypeRegistry : DynamicRegistry<DialogInputType>() {
    override val identifier: String = "minecraft:input_control_type"

    init {
        addEntry(DialogInputType("minecraft:boolean"))
        addEntry(DialogInputType("minecraft:number_range"))
        addEntry(DialogInputType("minecraft:single_option"))
        addEntry(DialogInputType("minecraft:text"))
        updateCache()
    }

    override fun updateCache() {
        cachedPacket = ClientboundRegistryDataPacket(this)
    }
}

data class DialogInputType(
    val identifier: String,
) : RegistryEntry {
    override fun getNbt(): CompoundBinaryTag? {
        return null
    }

    override fun getProtocolId(): Int {
        return DialogInputTypeRegistry.getProtocolIdByEntry(this)
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}
