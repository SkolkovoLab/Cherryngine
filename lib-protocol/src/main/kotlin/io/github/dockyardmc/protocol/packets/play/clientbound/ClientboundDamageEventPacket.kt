package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.cherry.math.Vec3D
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.codec.OtherCodecs
import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.registry.registries.DamageType
import io.github.dockyardmc.registry.registries.DamageTypeRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundDamageEventPacket(
    val entityId: Int,
    val type: DamageType,
    val attacker: Int?,
    val projectile: Int?,
    val location: Vec3D?
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundDamageEventPacket::entityId,
            RegistryStreamCodec(DamageTypeRegistry), ClientboundDamageEventPacket::type,
            OtherCodecs.OPTIONAL_ENTITY_ID, ClientboundDamageEventPacket::attacker,
            OtherCodecs.OPTIONAL_ENTITY_ID, ClientboundDamageEventPacket::projectile,
            LocationCodecs.VEC_3D.optional(), ClientboundDamageEventPacket::location,
            ::ClientboundDamageEventPacket
        )
    }
}