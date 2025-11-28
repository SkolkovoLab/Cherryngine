package ru.cherryngine.lib.minecraft.registry

import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.BiMap
import ru.cherryngine.lib.minecraft.utils.MutableBiMap

abstract class Registry<T : RegistryEntry>(
    val identifier: String
) {
    protected val protocolEntries: ObjectArrayList<T> = ObjectArrayList()
    protected val entryToProtocolId: Object2IntOpenHashMap<T> = Object2IntOpenHashMap()
    protected val entries: MutableBiMap<String, T> = MutableBiMap()

    open fun addEntry(entry: T) {
        val size = protocolEntries.size
        protocolEntries.add(entry)
        entries.put(entry.getEntryIdentifier(), entry)
        entryToProtocolId.put(entry, size)
    }

    operator fun get(identifier: String): T {
        return getOrNull(identifier) ?: throw RegistryException(identifier, entries.size)
    }

    operator fun get(key: Key): T {
        return get(key.asString())
    }

    fun getOrNull(identifier: String): T? {
        return entries.getByKeyOrNull(identifier)
    }

    fun getOrNull(key: Key): T? {
        return getOrNull(key.asString())
    }

    fun getProtocolIdByEntry(entry: T): Int {
        return entryToProtocolId.getInt(entry)
    }

    fun getByProtocolId(id: Int): T {
        return getByProtocolIdOrNull(id) ?: throw RegistryException(id, protocolEntries.size)
    }

    fun getByProtocolIdOrNull(id: Int): T? {
        return protocolEntries.getOrNull(id)
    }

    fun getEntries(): BiMap<String, T> {
        return entries.toBiMap()
    }

    fun getProtocolEntries(): List<T> {
        return protocolEntries.toList()
    }

    fun getMaxProtocolId(): Int {
        return entries.size
    }

    val STREAM_CODEC = RegistryStreamCodec(this)

    companion object {
        val STREAM_CODEC = object : StreamCodec<Registry<*>> {
            override fun write(buffer: ByteBuf, value: Registry<*>) {
                val size = value.getMaxProtocolId()

                StreamCodec.STRING.write(buffer, value.identifier)

                StreamCodec.VAR_INT.write(buffer, size)
                value.protocolEntries.forEach { entry ->
                    StreamCodec.STRING.write(buffer, entry.getEntryIdentifier())
                    StreamCodecNBT.STREAM.optional().write(buffer, entry.getNbt())
                }
            }

            override fun read(buffer: ByteBuf): Registry<*> {
                TODO("Not yet implemented")
            }
        }
    }
}
