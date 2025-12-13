package ru.cherryngine.lib.minecraft.r2

import io.netty.buffer.ByteBuf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromStream
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.transcoder.BinaryTagTranscoder
import ru.cherryngine.lib.minecraft.codec.transcoder.KtJsonTranscoder
import ru.cherryngine.lib.minecraft.network.stream_codec.BinaryTagStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.toKey

class DynamicRegistry<T : Any>(
    override val key: Key,
    val codec: Codec<T>,
) : Registry<T> {
    private val idToValue: MutableList<T> = arrayListOf()
    private val idToKey: MutableList<RegistryKey<T>> = arrayListOf()
    private val keyToId: MutableMap<Key, Int> = hashMapOf()
    private val keyToValue: MutableMap<Key, T> = hashMapOf()
    private val valueToKey: MutableMap<T, RegistryKey<T>> = hashMapOf()

    override val tags: MutableMap<Key, MutableList<Key>> = hashMapOf()

    override fun getOrNull(id: Int): T? {
        return idToValue.getOrNull(id)
    }

    override fun getOrNull(key: Key): T? {
        return keyToValue[key]
    }

    override fun getKeyOrNull(id: Int): RegistryKey<T>? {
        return idToKey.getOrNull(id)
    }

    override fun getKeyOrNull(value: T): RegistryKey<T>? {
        return valueToKey[value]
    }

    override fun getKeyOrNull(key: Key): RegistryKey<T>? {
        if (key !in keyToValue) return null
        return RegistryKey.Impl(key)
    }

    override fun getIdOrNull(key: Key): Int? {
        return keyToId[key]
    }

    override val size: Int
        get() = idToValue.size
    override val keys: Collection<RegistryKey<T>>
        get() = idToKey
    override val values: Collection<T>
        get() = idToValue

    fun register(key: Key, value: T): RegistryKey<T> {
        val registryKey: RegistryKey<T> = RegistryKey.Impl(key)
        synchronized(REGISTRY_LOCK) {
            val id = keyToId[key] // Array set at home
            keyToValue[key] = value
            valueToKey[value] = registryKey
            if (id == null) {
                idToValue.add(value)
                idToKey.add(registryKey)
                keyToId[key] = idToValue.lastIndex
            } else {
                idToValue[id] = value
                idToKey[id] = registryKey
                keyToId[key] = id
            }

            return registryKey
        }
    }

    override val keyCodec: Codec<T> = Codec.KEY.transform(
        { get(it) },
        { getKey(it).key() }
    )
    override val streamCodec = Registry2StreamCodec(this)

    companion object {
        private val REGISTRY_LOCK: Any = Any()

        @OptIn(ExperimentalSerializationApi::class)
        fun <T : Any> create(
            key: Key,
            codec: Codec<T>,
            resourceName: String,
            idComparator: Comparator<String> = Comparator.naturalOrder(),
        ): DynamicRegistry<T> {
            val resource = ClassLoader.getSystemResource(resourceName)
                ?: throw IllegalStateException("Resource $resourceName not found for registry $key")

            val map: Map<String, JsonObject> = resource.openStream().use {
                Json.decodeFromStream<Map<String, JsonObject>>(it)
            }

            val registry = DynamicRegistry(key, codec)

            map.entries.sortedWith(compareBy(idComparator) { it.key }).forEach { (key, value) ->
                val entry = codec.decode(KtJsonTranscoder, value)
                registry.register(key.toKey(), entry)
            }

            StaticRegistry.createTags("tags/$resourceName").forEach { (key, value) ->
                registry.tags.computeIfAbsent(key) { arrayListOf() }.addAll(value)
            }

            return registry
        }

        val STREAM_CODEC = object : StreamCodec<DynamicRegistry<*>> {
            override fun write(buffer: ByteBuf, value: DynamicRegistry<*>) {
                StreamCodec.KEY.write(buffer, value.key)
                StreamCodec.VAR_INT.write(buffer, value.size)

                for (id in 0 until value.size) {
                    StreamCodec.KEY.write(buffer, value.getKey(id).key())
                    @Suppress("UNCHECKED_CAST")
                    val binaryTag = (value.codec as Codec<Any>).encode(BinaryTagTranscoder, value[id])
                    BinaryTagStreamCodecs.STREAM.optional().write(buffer, binaryTag)
                }
            }

            override fun read(buffer: ByteBuf): DynamicRegistry<*> {
                val key = StreamCodec.KEY.read(buffer)
                val size = StreamCodec.VAR_INT.read(buffer)

                val codec: Codec<Any> = TODO()
                val registry = DynamicRegistry(key, codec)

                repeat(size) {
                    val entryKey = StreamCodec.KEY.read(buffer)
                    val entryValueNbt = BinaryTagStreamCodecs.STREAM.optional().read(buffer) ?: TODO()
                    val entryValue = codec.decode(BinaryTagTranscoder, entryValueNbt)
                    registry.register(entryKey, entryValue)
                }

                return registry
            }
        }
    }
}