package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromStream
import ru.cherryngine.lib.minecraft.registry.RegistryException
import java.util.concurrent.atomic.AtomicInteger

@OptIn(ExperimentalSerializationApi::class)
object SoundRegistry {

    init {
        val inputStream = ClassLoader.getSystemResource("sound_event.json").openStream()
        val list = Json.decodeFromStream<JsonObject>(inputStream).keys
        list.forEach(::addEntry)
    }

    private val map: MutableMap<Int, String> = mutableMapOf()
    private val reversed: MutableMap<String, Int> = mutableMapOf()

    private val protocolIdCounter = AtomicInteger()

    fun getMaxProtocolId(): Int {
        return protocolIdCounter.get()
    }

    fun addEntry(entry: String) {
        val id = protocolIdCounter.getAndIncrement()
        map[id] = entry
        reversed[entry] = id
    }

    operator fun get(identifier: String): Int {
        return reversed[identifier] ?: throw RegistryException(identifier, map.size)
    }

    fun getOrNull(identifier: String): Int? {
        return reversed[identifier]
    }

    fun getByProtocolId(id: Int): String {
        return map[id] ?: throw RegistryException(id, map.size)
    }

    fun getByProtocolIdOrNull(id: Int): String? {
        return map[id]
    }

    fun getMap(): Map<Int, String> {
        return map
    }

    fun getReversed(): Map<String, Int> {
        return reversed
    }
}