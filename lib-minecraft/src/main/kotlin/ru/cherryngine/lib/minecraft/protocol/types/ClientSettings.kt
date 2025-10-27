package ru.cherryngine.lib.minecraft.protocol.types

import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientSettings(
    val locale: String,
    val viewDistance: Byte,
    val chatMessageType: ChatMessageType,
    val chatColors: Boolean,
    val displayedSkinParts: DisplayedSkinParts,
    val mainHand: MainHand,
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
            MainHand.RIGHT,
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
            EnumStreamCodec<MainHand>(), ClientSettings::mainHand,
            StreamCodec.BOOLEAN, ClientSettings::enableTextFiltering,
            StreamCodec.BOOLEAN, ClientSettings::allowServerListings,
            EnumStreamCodec<ClientParticleSettings>(), ClientSettings::particleSettings,
            ::ClientSettings
        )
    }

    data class DisplayedSkinParts(
        val cape: Boolean,
        val jacket: Boolean,
        val leftSleeve: Boolean,
        val rightSleeve: Boolean,
        val leftPants: Boolean,
        val rightPants: Boolean,
        val hat: Boolean,
    ) {
        @Suppress("BooleanLiteralArgument")
        companion object {
            val NONE = DisplayedSkinParts(false, false, false, false, false, false, false)
            val ALL = DisplayedSkinParts(true, true, true, true, true, true, true)

            fun toByte(parts: DisplayedSkinParts): Byte {
                var byte = 0
                if (parts.cape) byte = byte or 0x01
                if (parts.jacket) byte = byte or 0x02
                if (parts.leftSleeve) byte = byte or 0x04
                if (parts.rightSleeve) byte = byte or 0x08
                if (parts.leftPants) byte = byte or 0x10
                if (parts.rightPants) byte = byte or 0x20
                if (parts.hat) byte = byte or 0x40
                return byte.toByte()
            }

            fun fromByte(byte: Byte): DisplayedSkinParts {
                val byte = byte.toInt()
                return DisplayedSkinParts(
                    cape = (byte and 0x01) != 0,
                    jacket = (byte and 0x02) != 0,
                    leftSleeve = (byte and 0x04) != 0,
                    rightSleeve = (byte and 0x08) != 0,
                    leftPants = (byte and 0x10) != 0,
                    rightPants = (byte and 0x20) != 0,
                    hat = (byte and 0x40) != 0,
                )
            }

            val STREAM_CODEC = StreamCodec.BYTE.transform(::fromByte, ::toByte)
        }
    }

    enum class MainHand {
        LEFT,
        RIGHT
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