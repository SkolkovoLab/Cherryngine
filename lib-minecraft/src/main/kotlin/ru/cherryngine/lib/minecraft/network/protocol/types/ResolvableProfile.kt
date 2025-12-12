package ru.cherryngine.lib.minecraft.network.protocol.types


import net.kyori.adventure.key.Key
import net.kyori.adventure.text.`object`.PlayerHeadObjectContents
import ru.cherryngine.lib.minecraft.network.stream_codec.EitherStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.Either
import java.util.*

data class ResolvableProfile(
    val profile: Either<GameProfile, Partial>,
    val patch: PlayerSkinPatch,
) : PlayerHeadObjectContents.SkinSource {
    companion object {
        val EMPTY = ResolvableProfile(Either.Right(Partial.EMPTY), PlayerSkinPatch.EMPTY)

        val STREAM_CODEC =
            StreamCodec.of(
                EitherStreamCodec(GameProfile.STREAM_CODEC, Partial.STREAM_CODEC), ResolvableProfile::profile,
                PlayerSkinPatch.STREAM_CODEC, ResolvableProfile::patch,
                ::ResolvableProfile
            )

        fun fromPlayerHeadContents(contents: PlayerHeadObjectContents): ResolvableProfile {
            val texture: Key? = contents.texture()
            if (texture != null) {
                return ResolvableProfile(Partial.EMPTY, PlayerSkinPatch(texture))
            }

            val properties = contents.profileProperties().map { prop ->
                when (prop) {
                    is GameProfile.Property -> prop
                    else -> GameProfile.Property(prop.name(), prop.value(), prop.signature())
                }
            }

            return ResolvableProfile(Partial(contents.name(), contents.id(), properties))
        }
    }

    constructor(
        profile: GameProfile,
        patch: PlayerSkinPatch = PlayerSkinPatch.EMPTY,
    ) : this(Either.Left(profile), patch)

    constructor(
        partial: Partial,
        patch: PlayerSkinPatch = PlayerSkinPatch.EMPTY,
    ) : this(Either.Right(partial), patch)

    // Adventure mapping
    override fun applySkinToPlayerHeadContents(builder: PlayerHeadObjectContents.Builder) {
        patch.body?.let { builder.texture(it) }

        when (val p = profile) {
            is Either.Left -> {
                val gp = p.value
                builder.name(gp.username)
                builder.id(gp.uuid)
                gp.properties.forEach(builder::profileProperty)
            }

            is Either.Right -> {
                val partial = p.value
                builder.name(partial.name)
                builder.id(partial.uuid)
                partial.properties.forEach(builder::profileProperty)
            }
        }
    }

    data class Partial(
        val name: String?,
        val uuid: UUID?,
        val properties: List<GameProfile.Property>,
    ) {
        companion object {
            val EMPTY = Partial(null, null, emptyList())

            val STREAM_CODEC =
                StreamCodec.of(
                    StreamCodec.STRING.optional(), Partial::name,
                    StreamCodec.UUID.optional(), Partial::uuid,
                    GameProfile.Property.STREAM_CODEC.list(), Partial::properties,
                    ::Partial
                )
        }
    }

    data class PlayerSkinPatch(
        val body: Key? = null,
        val cape: Key? = null,
        val elytra: Key? = null,
        val slim: Boolean? = null,
    ) {

        companion object {
            val EMPTY = PlayerSkinPatch()

            val STREAM_CODEC =
                StreamCodec.of(
                    StreamCodec.KEY.optional(), PlayerSkinPatch::body,
                    StreamCodec.KEY.optional(), PlayerSkinPatch::cape,
                    StreamCodec.KEY.optional(), PlayerSkinPatch::elytra,
                    StreamCodec.BOOLEAN.optional(), PlayerSkinPatch::slim,
                    ::PlayerSkinPatch
                )
        }
    }

}
