package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import ru.cherryngine.lib.minecraft.registry.RegistryException
import java.util.concurrent.atomic.AtomicInteger
import java.util.zip.GZIPInputStream

@OptIn(ExperimentalSerializationApi::class)
object SoundRegistry {

    init {
        val inputStream = ClassLoader.getSystemResource("registry/sound_registry.json.gz").openStream()
        val stream = GZIPInputStream(inputStream)
        val list = Json.decodeFromStream<List<String>>(stream)
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