package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.PlayerHand
import ru.cherryngine.lib.minecraft.tide.stream.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundUseItemPacket(
    val hand: PlayerHand,
    val sequence: Int,
    val yawPitch: YawPitch,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ByteEnumStreamCodec<PlayerHand>(), ServerboundUseItemPacket::hand,
            StreamCodec.VAR_INT, ServerboundUseItemPacket::sequence,
            LocationCodecs.YAW_PITCH, ServerboundUseItemPacket::yawPitch,
            ::ServerboundUseItemPacket
        )
    }
}