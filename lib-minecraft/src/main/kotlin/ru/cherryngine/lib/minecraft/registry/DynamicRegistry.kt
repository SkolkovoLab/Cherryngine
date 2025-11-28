package ru.cherryngine.lib.minecraft.registry

import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket

abstract class DynamicRegistry<T : RegistryEntry>(
    identifier: String,
) : Registry<T>(identifier) {
    protected lateinit var cachedPacket: ClientboundRegistryDataPacket

    @JvmName("getCachedPacketMethod")
    fun getCachedPacket(): ClientboundRegistryDataPacket {
        if (!this::cachedPacket.isInitialized) updateCache()
        return cachedPacket
    }

    fun updateCache() {
        cachedPacket = ClientboundRegistryDataPacket(this)
    }
}