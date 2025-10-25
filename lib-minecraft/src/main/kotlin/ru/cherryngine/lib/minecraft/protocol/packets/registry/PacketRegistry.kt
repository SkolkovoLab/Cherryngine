package ru.cherryngine.lib.minecraft.protocol.packets.registry

import ru.cherryngine.lib.minecraft.protocol.packets.Packet
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import kotlin.reflect.KClass

interface PacketRegistry {
    fun getFromId(state: ProtocolState, id: Int): KClass<out Packet>?
    fun getSkippedFromId(state: ProtocolState, id: Int): String?
    fun getId(state: ProtocolState, packet: KClass<out Packet>): Int?
    fun <T : Packet> getStreamCodec(packet: KClass<T>): StreamCodec<T>?
}
