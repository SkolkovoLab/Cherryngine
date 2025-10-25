package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.cherry.math.Vec3I
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.registry.registries.BlockRegistry
import io.github.dockyardmc.registry.registries.RegistryBlock
import io.github.dockyardmc.tide.stream.StreamCodec

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