package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.RegistryBlock
import ru.cherryngine.lib.minecraft.registry.registries.BlockRegistry

data class ClientboundBlockEventPacket(
    val location: Vec3I,
    val blockAction: Byte,
    val actionParameter: Byte,
    val blockType: RegistryBlock,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationStreamCodecs.BLOCK_POSITION, ClientboundBlockEventPacket::location,
            StreamCodec.BYTE, ClientboundBlockEventPacket::blockAction,
            StreamCodec.BYTE, ClientboundBlockEventPacket::actionParameter,
            BlockRegistry.STREAM_CODEC, ClientboundBlockEventPacket::blockType,
            ::ClientboundBlockEventPacket
        )
    }
}