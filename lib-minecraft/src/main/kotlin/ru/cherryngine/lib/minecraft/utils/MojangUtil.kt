package ru.cherryngine.lib.minecraft.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.protocol.types.GameProfile
import java.io.IOException
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

object MojangUtil {
    private val logger = LoggerFactory.getLogger(this::class.java)
    const val BASE_AUTH_URL: String =
        "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=%s&serverId=%s"
    const val PREVENT_PROXY_CONNECTIONS_AUTH_URL: String = "$BASE_AUTH_URL&ip=%s"

    private val httpClient: HttpClient = HttpClient.newHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    val uuidToSkinCache = mutableMapOf<UUID, GameProfile.Property>()
    val usernameToUuidCache = mutableMapOf<String, UUID>()

    fun authenticateSession(loginUsername: String, serverId: String): GameProfileResponse {
        val username = URLEncoder.encode(loginUsername, StandardCharsets.UTF_8)

        val url = String.format(BASE_AUTH_URL, username, serverId)
        val request = HttpRequest.newBuilder()
            .uri(URI(url))
            .GET()
            .build()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.body().isEmpty()) throw IOException("Mojang API down?")
        return json.decodeFromString<GameProfileResponse>(response.body())
    }

    fun getUUIDFromUsername(username: String): CompletableFuture<UUID?> {
        val future = CompletableFuture<UUID?>()

        if (usernameToUuidCache.containsKey(username)) {
            future.complete(usernameToUuidCache[username]!!)
        } else {
            val request = HttpRequest.newBuilder()
                .uri(URI("https://api.mojang.com/users/profiles/minecraft/$username"))
                .GET()
                .build()

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept { response ->
                if (response.body().isEmpty()) {
                    future.complete(null)
                    return@thenAccept
                }
                val decodedResponse = json.decodeFromString<ProfileResponse>(response.body())

                val uuid = decodedResponse.getUUID()
                future.complete(uuid)
            }
        }
        return future
    }

    fun getSkinFromUsername(username: String): CompletableFuture<GameProfile.Property?> {
        val future = CompletableFuture<GameProfile.Property?>()

        getUUIDFromUsername(username).thenAccept { uuid ->
            if (uuid == null) {
                future.complete(null)
                return@thenAccept
            }

            getSkinFromUUID(uuid).thenAccept { property ->
                future.complete(property)
            }
        }

        return future
    }

    fun getSkinFromUUID(uuid: UUID, forceUpdate: Boolean = false): CompletableFuture<GameProfile.Property?> {
        val future = CompletableFuture<GameProfile.Property?>()

        if (uuidToSkinCache.containsKey(uuid) && !forceUpdate) future.complete(uuidToSkinCache[uuid]!!)

        val request = HttpRequest.newBuilder()
            .uri(URI("https://sessionserver.mojang.com/session/minecraft/profile/$uuid?unsigned=false"))
            .GET()
            .timeout(5.seconds.toJavaDuration())
            .build()

        logger.info("Fetched skin of $uuid from Mojang API..")
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept { response ->
            if (response.body().isEmpty()) {
                future.complete(null)
                return@thenAccept
            }
            val decodedResponse = json.decodeFromString<GameProfileResponse>(response.body())
            val property = GameProfile.Property(
                "textures",
                decodedResponse.properties[0].value,
                decodedResponse.properties[0].signature
            )
            uuidToSkinCache[uuid] = property
            future.complete(property)

        }.exceptionally { throwable -> future.complete(null); null }

        return future
    }

    @Serializable
    private data class ProfileResponse(
        val id: String,
        val name: String,
    ) {
        fun getUUID() = uuidFromNoDashes(id)
    }

    @Serializable
    data class GameProfileResponse(
        val id: String,
        val name: String,
        val properties: List<GameProfile.Property>,
    ) {
        fun getUUID() = uuidFromNoDashes(id)
    }

    private fun uuidFromNoDashes(input: String): UUID {
        require(input.length == 32)
        val stringUUID = buildString {
            append(input, 0, 8)
            append('-')
            append(input, 8, 12)
            append('-')
            append(input, 12, 16)
            append('-')
            append(input, 16, 20)
            append('-')
            append(input, 20, 32)
        }
        return UUID.fromString(stringUUID)
    }
}