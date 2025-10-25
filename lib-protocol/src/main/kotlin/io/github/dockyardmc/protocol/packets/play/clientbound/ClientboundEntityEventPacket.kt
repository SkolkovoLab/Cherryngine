package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundEntityEventPacket(
    val entityId: Int,
    val status: Byte
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.INT, ClientboundEntityEventPacket::entityId,
            StreamCodec.BYTE, ClientboundEntityEventPacket::status,
            ::ClientboundEntityEventPacket
        )
    }

    enum class EntityStatus(
        val id: Int
    ) {
        SPAWNS_HONEY_BLOCK_PARTICLES(53),

        ARROW_SPAWN_TIPPED_ARROW_PARTICLE(0),

        LIVING_ENTITY_PLAY_DEATH_SOUND(3),
        LIVING_ENTITY_SHIELD_BLOCK_SOUND(29),
        LIVING_ENTITY_SHIELD_BREAK_SOUND(30),
        LIVING_ENTITY_PLAY_TOTEM_UNDYING_ANIMATION(35),
        LIVING_ENTITY_SWAP_HAND_ITEMS(55),
        LIVING_ENTITY_SPAWN_DEATH_SMOKE_PARTICLES(60),

        PLAYER_ITEM_USE_FINISHED(9),
        PLAYER_ENABLE_DEBUG_SCREEN(22),
        PLAYER_DISABLE_DEBUG_SCREEN(23),
        PLAYER_SET_OP_PERMISSION_LEVEL_0(24),
        PLAYER_SET_OP_PERMISSION_LEVEL_1(25),
        PLAYER_SET_OP_PERMISSION_LEVEL_2(26),
        PLAYER_SET_OP_PERMISSION_LEVEL_3(27),
        PLAYER_SET_OP_PERMISSION_LEVEL_4(28),
        PLAY_SPAWN_CLOUD_PARTICLES(43),

        ANIMAL_SPAWN_LOVE_MODE_PARTICLES(18),

        OCELOT_SPAWN_SMOKE_PARTICLES(40),
        OCELOT_SPAWN_HEART_PARTICLE(41),

        RABBIT_JUMP_ANIMATION(1),

        SHEEP_EAT_GRASS(10),

        SNIFFER_PLAY_DIGGING_SOUND(63),

        RAVAGER_ATTACK_ANIMATION(4),
        RAVAGER_STUNNED(39),

        WARDEN_ATTACK(4),
        WARDEN_TENDRIL_SHAKING(61),
        WARDEN_SONIC_BOOM(62),
    }
}
