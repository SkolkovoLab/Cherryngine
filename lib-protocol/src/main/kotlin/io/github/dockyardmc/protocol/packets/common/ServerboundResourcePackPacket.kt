package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.ByteEnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import java.util.UUID

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