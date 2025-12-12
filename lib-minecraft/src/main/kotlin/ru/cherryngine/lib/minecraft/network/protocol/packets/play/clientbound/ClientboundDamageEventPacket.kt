package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.DamageType
import ru.cherryngine.lib.minecraft.registry.registries.DamageTypeRegistry

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
            StreamCodec.OPT_INT, ClientboundDamageEventPacket::attacker,
            StreamCodec.OPT_INT, ClientboundDamageEventPacket::projectile,
            LocationStreamCodecs.VEC_3D.optional(), ClientboundDamageEventPacket::location,
            ::ClientboundDamageEventPacket
        )
    }
}