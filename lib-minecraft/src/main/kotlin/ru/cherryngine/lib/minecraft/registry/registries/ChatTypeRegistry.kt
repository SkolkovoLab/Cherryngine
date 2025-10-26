package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object ChatTypeRegistry : DynamicRegistry<ChatType>() {
    override val identifier: String = "minecraft:chat_type"
    val STREAM_CODEC = RegistryStreamCodec(this)

    override fun updateCache() {
        cachedPacket = ClientboundRegistryDataPacket(this)
    }
}

class ChatType : RegistryEntry {

    override fun getProtocolId(): Int {
        throw UnsupportedOperationException()
    }

    override fun getEntryIdentifier(): String {
        throw UnsupportedOperationException()
    }
}