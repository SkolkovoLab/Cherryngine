package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.codec.OtherCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.registry.registries.DamageType
import ru.cherryngine.lib.minecraft.registry.registries.DamageTypeRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundDamageEventPacket(
    val entityId: Int,
    val type: DamageType,
    val attacker: Int?,
    val projectile: Int?,
    val location: Vec3D?,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundDamageEventPacket::entityId,
            DamageTypeRegistry.STREAM_CODEC, ClientboundDamageEventPacket::type,
            OtherCodecs.OPTIONAL_ENTITY_ID, ClientboundDamageEventPacket::attacker,
            OtherCodecs.OPTIONAL_ENTITY_ID, ClientboundDamageEventPacket::projectile,
            LocationCodecs.VEC_3D.optional(), ClientboundDamageEventPacket::location,
            ::ClientboundDamageEventPacket
        )
    }
}