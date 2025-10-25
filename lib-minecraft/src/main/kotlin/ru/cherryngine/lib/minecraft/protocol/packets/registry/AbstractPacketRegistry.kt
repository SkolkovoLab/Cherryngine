package ru.cherryngine.lib.minecraft.protocol.packets.registry

import ru.cherryngine.lib.minecraft.protocol.packets.Packet
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

abstract class AbstractPacketRegistry : PacketRegistry {
    private val counters: MutableMap<ProtocolState, AtomicInteger> = mutableMapOf()
    private val idByPacket: MutableMap<ProtocolState, MutableMap<KClass<out Packet>, Int>> = mutableMapOf()
    private val packetById: MutableMap<ProtocolState, MutableMap<Int, KClass<out Packet>>> = mutableMapOf()
    private val skippedNames: MutableMap<ProtocolState, MutableMap<Int, String>> = mutableMapOf()
    private val codecs: MutableMap<KClass<out Packet>, StreamCodec<out Packet>> = mutableMapOf()

    protected fun <T : Packet> register(state: ProtocolState, packetClass: KClass<T>, streamCodec: StreamCodec<T>) {
        val id = counters.computeIfAbsent(state) { AtomicInteger() }.getAndIncrement()
        idByPacket.computeIfAbsent(state) { mutableMapOf() }[packetClass] = id
        packetById.computeIfAbsent(state) { mutableMapOf() }[id] = packetClass
        codecs[packetClass] = streamCodec
    }

    protected fun skip(state: ProtocolState, name: String) {
        val id = counters.computeIfAbsent(state) { AtomicInteger() }.getAndIncrement()
        skippedNames.computeIfAbsent(state) { mutableMapOf() }[id] = name
    }

    override fun getFromId(state: ProtocolState, id: Int): KClass<out Packet>? {
        return packetById[state]?.get(id)
    }

    override fun getSkippedFromId(state: ProtocolState, id: Int): String? {
        return skippedNames[state]?.get(id)
    }

    override fun getId(state: ProtocolState, packet: KClass<out Packet>): Int? {
        return idByPacket[state]?.get(packet)
    }

    override fun <T : Packet> getStreamCodec(packet: KClass<T>): StreamCodec<T>? {
        @Suppress("UNCHECKED_CAST")
        return codecs[packet] as StreamCodec<T>?
    }
}