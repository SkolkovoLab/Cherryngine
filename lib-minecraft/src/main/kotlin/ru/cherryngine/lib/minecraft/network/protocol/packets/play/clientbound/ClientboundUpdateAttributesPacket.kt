package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.r2.Attribute
import ru.cherryngine.lib.minecraft.r2.Registries

data class ClientboundUpdateAttributesPacket(
    val entityId: Int,
    val properties: List<Property>,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundUpdateAttributesPacket::entityId,
            Property.STREAM_CODEC.list(), ClientboundUpdateAttributesPacket::properties,
            ::ClientboundUpdateAttributesPacket
        )
    }

    data class Property(
        val attribute: Attribute,
        val value: Double,
        val modifiers: List<Modifier>,
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.of(
                Registries.attribute.streamCodec, Property::attribute,
                StreamCodec.DOUBLE, Property::value,
                Modifier.STREAM_CODEC.list(), Property::modifiers,
                ::Property
            )
        }
    }

    data class Modifier(
        val id: Key,
        val amount: Double,
        val operation: Operation,
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.KEY, Modifier::id,
                StreamCodec.DOUBLE, Modifier::amount,
                ByteEnumStreamCodec<Operation>(), Modifier::operation,
                ::Modifier
            )
        }
    }

    enum class Operation {
        ADD_VALUE,
        ADD_MULTIPLIED_BASE,
        ADD_MULTIPLIED_TOTAL
    }
}