package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

data class ClientboundPlayerInfoRemovePacket(
    val uuids: List<UUID>
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.UUID.list(), ClientboundPlayerInfoRemovePacket::uuids,
            ::ClientboundPlayerInfoRemovePacket
        )
    }
}