package ru.cherryngine.lib.minecraft.protocol.types

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.`object`.PlayerHeadObjectContents
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

data class GameProfile(
    val uuid: UUID,
    val username: String,
    val properties: List<Property> = listOf()
) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.UUID, GameProfile::uuid,
            StreamCodec.STRING, GameProfile::username,
            Property.STREAM_CODEC.list(), GameProfile::properties,
            ::GameProfile
        )
    }

    init {
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(username.length <= 16) { "Username cannot be more than 16 characters" }
    }

    @Serializable
    data class Property(
        val name: String,
        val value: String,
        val signature: String? = null
    ) : PlayerHeadObjectContents.ProfileProperty {
        override fun name(): String = name
        override fun value(): String = value
        override fun signature(): String? = signature

        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, PlayerHeadObjectContents.ProfileProperty::name,
                StreamCodec.STRING, PlayerHeadObjectContents.ProfileProperty::value,
                StreamCodec.STRING.optional(), PlayerHeadObjectContents.ProfileProperty::signature,
                ::Property
            )
        }
    }
}