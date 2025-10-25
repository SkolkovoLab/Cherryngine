//package ru.cherryngine.lib.minecraft.protocol.packets.configurations
//
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//import ru.cherryngine.lib.minecraft.resourcepack.ResourcePack
//import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
//import java.util.*
//
//data class ClientboundConfigurationRemoveResourcePackPacket(val uuid: UUID? = null) : ClientboundPacket() {
//
//    constructor(resourcePack: ResourcePack) : this(resourcePack.uuid)
//
//    init {
//        StreamCodec.UUID.optional().write(buffer, uuid)
//    }
//}