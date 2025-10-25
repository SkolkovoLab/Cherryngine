package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.registry.registries.BlockRegistry
import ru.cherryngine.lib.minecraft.registry.registries.RegistryBlock
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundBlockEventPacket(
    val location: Vec3I,
    val blockAction: Byte,
    val actionParameter: Byte,
    val blockType: RegistryBlock
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.BLOCK_POSITION, ClientboundBlockEventPacket::location,
            StreamCodec.BYTE, ClientboundBlockEventPacket::blockAction,
            StreamCodec.BYTE, ClientboundBlockEventPacket::actionParameter,
            RegistryStreamCodec(BlockRegistry), ClientboundBlockEventPacket::blockType,
            ::ClientboundBlockEventPacket
        )
    }
}