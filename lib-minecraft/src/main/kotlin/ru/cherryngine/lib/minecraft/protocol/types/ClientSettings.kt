package ru.cherryngine.lib.minecraft.protocol.types

import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientSettings(
    val locale: String,
    val viewDistance: Byte,
    val chatMessageType: ChatMessageType,
    val chatColors: Boolean,
    val displayedSkinParts: DisplayedSkinParts,
    val mainHand: PlayerHand,
    val enableTextFiltering: Boolean,
    val allowServerListings: Boolean,
    val particleSettings: ClientParticleSettings
) {
    companion object {
        val DEFAULT = ClientSettings(
            "en_us",
            10,
            ChatMessageType.FULL,
            true,
            DisplayedSkinParts.ALL,
            PlayerHand.MAIN_HAND,
            true,
            true,
            ClientParticleSettings.ALL
        )

        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ClientSettings::locale,
            StreamCodec.BYTE, ClientSettings::viewDistance,
            EnumStreamCodec<ChatMessageType>(), ClientSettings::chatMessageType,
            StreamCodec.BOOLEAN, ClientSettings::chatColors,
            DisplayedSkinParts.STREAM_CODEC, ClientSettings::displayedSkinParts,
            EnumStreamCodec<PlayerHand>(), ClientSettings::mainHand,
            StreamCodec.BOOLEAN, ClientSettings::enableTextFiltering,
            StreamCodec.BOOLEAN, ClientSettings::allowServerListings,
            EnumStreamCodec<ClientParticleSettings>(), ClientSettings::particleSettings,
            ::ClientSettings
        )
    }

    enum class ChatMessageType {
        FULL,
        SYSTEM,
        NONE
    }

    enum class ClientParticleSettings {
        ALL,
        DECREASED,
        MINIMAL
    }
}