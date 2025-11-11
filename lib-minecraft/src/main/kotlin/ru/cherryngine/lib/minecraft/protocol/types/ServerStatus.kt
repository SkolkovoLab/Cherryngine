package ru.cherryngine.lib.minecraft.protocol.types

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.kotlinx.ComponentToJsonElementSerializer
import ru.cherryngine.lib.minecraft.utils.kotlinx.UuidSerializer
import java.util.*

@Serializable
data class ServerStatus(
    val version: Version,
    val players: Players? = null,
    @Serializable(ComponentToJsonElementSerializer::class)
    val description: Component? = null,
    val enforceSecureChat: Boolean? = null,
    val previewsChat: Boolean? = null,
    val favicon: String? = null,
) {
    @Serializable
    data class Players(
        val max: Int,
        val online: Int,
        val sample: List<ServerListPlayer>? = null,
    )

    @Serializable
    data class Version(
        val name: String,
        val protocol: Int,
    )

    @Serializable
    data class ServerListPlayer(
        val name: String,
        @Serializable(with = UuidSerializer::class)
        val id: UUID,
    )

    companion object {
        val STREAM_CODEC: StreamCodec<ServerStatus> = StreamCodec.STRING.transform({
            Json.decodeFromString<ServerStatus>(it)
        }, {
            Json.encodeToString<ServerStatus>(it)
        })
    }
}