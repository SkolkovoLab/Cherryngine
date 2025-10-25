package ru.cherryngine.lib.minecraft.protocol.packets.registry

import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.packets.common.*
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ServerboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.handshake.ServerboundIntentionPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundCustomQueryAnswerPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundEncryptionResponsePacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundHelloPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundLoginAcknowledgedPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.*
import ru.cherryngine.lib.minecraft.protocol.packets.status.ServerboundStatusRequestPacket

class ServerboundPacketRegistry : AbstractPacketRegistry() {
    init {
        register(ProtocolState.HANDSHAKE, ServerboundIntentionPacket::class, ServerboundIntentionPacket.STREAM_CODEC)

        register(ProtocolState.STATUS, ServerboundStatusRequestPacket::class, ServerboundStatusRequestPacket.STREAM_CODEC)
        register(ProtocolState.STATUS, ServerboundPingRequestPacket::class, ServerboundPingRequestPacket.STREAM_CODEC)

        register(ProtocolState.LOGIN, ServerboundHelloPacket::class, ServerboundHelloPacket.STREAM_CODEC)
        register(ProtocolState.LOGIN, ServerboundEncryptionResponsePacket::class, ServerboundEncryptionResponsePacket.STREAM_CODEC)
        register(ProtocolState.LOGIN, ServerboundCustomQueryAnswerPacket::class, ServerboundCustomQueryAnswerPacket.STREAM_CODEC)
        register(ProtocolState.LOGIN, ServerboundLoginAcknowledgedPacket::class, ServerboundLoginAcknowledgedPacket.STREAM_CODEC)
        skip(ProtocolState.LOGIN, "cookie response")

        register(ProtocolState.CONFIGURATION, ServerboundClientInformationPacket::class, ServerboundClientInformationPacket.STREAM_CODEC)
        skip(ProtocolState.CONFIGURATION, "cookie_response")
        register(ProtocolState.CONFIGURATION, ServerboundCustomPayloadPacket::class, ServerboundCustomPayloadPacket.STREAM_CODEC)
        register(ProtocolState.CONFIGURATION, ServerboundFinishConfigurationPacket::class, ServerboundFinishConfigurationPacket.STREAM_CODEC)
        register(ProtocolState.CONFIGURATION, ServerboundKeepAlivePacket::class, ServerboundKeepAlivePacket.STREAM_CODEC)
        register(ProtocolState.CONFIGURATION, ServerboundPongPacket::class, ServerboundPongPacket.STREAM_CODEC)
        register(ProtocolState.CONFIGURATION, ServerboundResourcePackPacket::class, ServerboundResourcePackPacket.STREAM_CODEC)
        skip(ProtocolState.CONFIGURATION, "known packs")
        register(ProtocolState.CONFIGURATION, ServerboundCustomClickActionPacket::class, ServerboundCustomClickActionPacket.STREAM_CODEC)

        register(ProtocolState.PLAY, ServerboundAcceptTeleportationPacket::class, ServerboundAcceptTeleportationPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "query block nbt")
        skip(ProtocolState.PLAY, "select bundle item")
        skip(ProtocolState.PLAY, "change difficulty")
        register(ProtocolState.PLAY, ServerboundChangeGameModePacket::class, ServerboundChangeGameModePacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "chat ack")
        register(ProtocolState.PLAY, ServerboundChatCommandPacket::class, ServerboundChatCommandPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "signed command")
        register(ProtocolState.PLAY, ServerboundChatPacket::class, ServerboundChatPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundChatSessionUpdatePacket::class, ServerboundChatSessionUpdatePacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "chunk batch received")
        register(ProtocolState.PLAY, ServerboundClientCommandPacket::class, ServerboundClientCommandPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundClientTickEndPacket::class, ServerboundClientTickEndPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundClientInformationPacket::class, ServerboundClientInformationPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundCommandSuggestionPacket::class, ServerboundCommandSuggestionPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "configuration ack")
        skip(ProtocolState.PLAY, "click container button")
        skip(ProtocolState.PLAY, "ServerboundClickContainerPacket") // addPlay, ServerboundClickContainerPacket::class, ServerboundClickContainerPacket") // addPlay, ServerboundClickContainerPacket.streamCodec)
        register(ProtocolState.PLAY, ServerboundContainerClosePacket::class, ServerboundContainerClosePacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "container slot state")
        skip(ProtocolState.PLAY, "cookie response")
        register(ProtocolState.PLAY, ServerboundCustomPayloadPacket::class, ServerboundCustomPayloadPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "debug sample subscription")
        skip(ProtocolState.PLAY, "edit book")
        skip(ProtocolState.PLAY, "query entity nbt")
        register(ProtocolState.PLAY, ServerboundInteractPacket::class, ServerboundInteractPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "generate structure")
        register(ProtocolState.PLAY, ServerboundKeepAlivePacket::class, ServerboundKeepAlivePacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "lock difficulty")
        register(ProtocolState.PLAY, ServerboundMovePlayerPosPacket::class, ServerboundMovePlayerPosPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundMovePlayerPosRotPacket::class, ServerboundMovePlayerPosRotPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundMovePlayerRotPacket::class, ServerboundMovePlayerRotPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundMovePlayerStatusOnlyPacket::class, ServerboundMovePlayerStatusOnlyPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundMoveVehiclePacket::class, ServerboundMoveVehiclePacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "steer boat")
        register(ProtocolState.PLAY, ServerboundPickItemFromBlockPacket::class, ServerboundPickItemFromBlockPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundPickItemFromEntityPacket::class, ServerboundPickItemFromEntityPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundPingRequestPacket::class, ServerboundPingRequestPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "place recipe")
        register(ProtocolState.PLAY, ServerboundPlayerAbilitiesPacket::class, ServerboundPlayerAbilitiesPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundPlayerActionPacket::class, ServerboundPlayerActionPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundPlayerCommandPacket::class, ServerboundPlayerCommandPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundPlayerInputPacket::class, ServerboundPlayerInputPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundPlayerLoadedPacket::class, ServerboundPlayerLoadedPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundPongPacket::class, ServerboundPongPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "set recipe book state")
        skip(ProtocolState.PLAY, "set recipe book seen")
        skip(ProtocolState.PLAY, "name item")
        register(ProtocolState.PLAY, ServerboundResourcePackPacket::class, ServerboundResourcePackPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "seen advancements")
        skip(ProtocolState.PLAY, "select trade")
        skip(ProtocolState.PLAY, "set beacon effect")
        register(ProtocolState.PLAY, ServerboundSetCarriedItemPacket::class, ServerboundSetCarriedItemPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "update command block")
        skip(ProtocolState.PLAY, "update command minecart")
        register(ProtocolState.PLAY, ServerboundSetCreativeModeSlotPacket::class, ServerboundSetCreativeModeSlotPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "jigsaw update")
        skip(ProtocolState.PLAY, "structure update")
        skip(ProtocolState.PLAY, "set test block packet")
        skip(ProtocolState.PLAY, "update sign")
        register(ProtocolState.PLAY, ServerboundSwingPacket::class, ServerboundSwingPacket.STREAM_CODEC)
        skip(ProtocolState.PLAY, "client spectate")
        skip(ProtocolState.PLAY, "test instance block action packet")
        register(ProtocolState.PLAY, ServerboundUseItemOnPacket::class, ServerboundUseItemOnPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundUseItemPacket::class, ServerboundUseItemPacket.STREAM_CODEC)
        register(ProtocolState.PLAY, ServerboundCustomClickActionPacket::class, ServerboundCustomClickActionPacket.STREAM_CODEC)
    }
}
