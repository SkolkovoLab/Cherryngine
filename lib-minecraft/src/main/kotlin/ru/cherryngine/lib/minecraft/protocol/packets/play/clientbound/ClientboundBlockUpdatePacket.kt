package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.block.Block

data class ClientboundBlockUpdatePacket(
    val location: Vec3I,
    val block: Block,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.BLOCK_POSITION, ClientboundBlockUpdatePacket::location,
            Block.STREAM_CODEC, ClientboundBlockUpdatePacket::block,
            ::ClientboundBlockUpdatePacket
        )
    }
}