package io.github.dockyardmc.protocol.packets.registry

import io.github.dockyardmc.protocol.packets.Packet
import io.github.dockyardmc.protocol.packets.ProtocolState
import io.github.dockyardmc.tide.stream.StreamCodec
import kotlin.reflect.KClass

interface PacketRegistry {
    fun getFromId(state: ProtocolState, id: Int): KClass<out Packet>?
    fun getSkippedFromId(state: ProtocolState, id: Int): String?
    fun getId(state: ProtocolState, packet: KClass<out Packet>): Int?
    fun <T : Packet> getStreamCodec(packet: KClass<T>): StreamCodec<T>?
}
