package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundPlayerCommandPacket(
    val entityId: Int,
    val action: Action,
    val data: Int // Only used by the “start jump with horse” action, in which case it ranges from 0 to 100. In all other cases it is 0.
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundPlayerCommandPacket::entityId,
            EnumStreamCodec<Action>(), ServerboundPlayerCommandPacket::action,
            StreamCodec.VAR_INT, ServerboundPlayerCommandPacket::data,
            ::ServerboundPlayerCommandPacket
        )
    }

    enum class Action {
        STOP_SLEEPING, // Leave bed is only sent when the “Leave Bed” button is clicked on the sleep GUI, not when waking up in the morning.
        START_SPRINTING,
        STOP_SPRINTING,
        START_RIDING_JUMP,
        STOP_RIDING_JUMP,
        OPEN_VEHICLE_INVENTORY, // Open vehicle inventory is only sent when pressing the inventory key (default: E) while on a horse or chest boat — all other methods of opening such an inventory (involving right-clicking or shift-right-clicking it) do not use this packet.
        START_FALL_FLYING
    }
}