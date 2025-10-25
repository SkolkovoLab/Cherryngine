package ru.cherryngine.lib.minecraft.protocol.packets.common

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

data class ServerboundResourcePackPacket(
    val uuid: UUID,
    val response: Status
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.UUID, ServerboundResourcePackPacket::uuid,
            ByteEnumStreamCodec.Companion<Status>(), ServerboundResourcePackPacket::response,
            ::ServerboundResourcePackPacket
        )
    }

    enum class Status {
        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_TO_DOWNLOAD,
        ACCEPTED,
        DOWNLOADED,
        INVALID_URL,
        FAILED_TO_RELOAD,
        DISCARDED
    }
}