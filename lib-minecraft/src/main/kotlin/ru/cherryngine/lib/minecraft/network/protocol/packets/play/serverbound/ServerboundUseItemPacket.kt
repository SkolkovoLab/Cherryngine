package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.PlayerHand
import ru.cherryngine.lib.minecraft.network.stream_codec.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ServerboundUseItemPacket(
    val hand: PlayerHand,
    val sequence: Int,
    val yawPitch: YawPitch,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ByteEnumStreamCodec<PlayerHand>(), ServerboundUseItemPacket::hand,
            StreamCodec.VAR_INT, ServerboundUseItemPacket::sequence,
            LocationStreamCodecs.YAW_PITCH, ServerboundUseItemPacket::yawPitch,
            ::ServerboundUseItemPacket
        )
    }
}