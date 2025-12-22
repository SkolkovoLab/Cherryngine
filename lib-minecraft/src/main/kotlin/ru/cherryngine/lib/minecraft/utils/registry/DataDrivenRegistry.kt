package ru.cherryngine.lib.minecraft.utils.registry

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

class DataDrivenRegistry<T : Any>(
    val registry: DynamicRegistry<T>,
    val codec: Codec<T>,
) : Registry<T> by registry {
    fun register(key: Key, value: T) {
        registry.register(key, value)
    }

    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        fun <T : Any> create(
            key: Key,
            codec: Codec<T>,
            resourceName: String,
            idComparator: Comparator<String> = Comparator.naturalOrder(),
        ): DataDrivenRegistry<T> {
            val resource = ClassLoader.getSystemResource(resourceName)
                ?: throw IllegalStateException("Resource $resourceName not found for registry $key")

            val map: Map<String, JsonObject> = resource.openStream().use {
                Json.decodeFromStream<Map<String, JsonObject>>(it)
            }

            val registry = DynamicRegistry<T>(key)

            map.entries.sortedWith(compareBy(idComparator) { it.key }).forEach { (key, value) ->
                val entry = codec.decode(KtJsonTranscoder, value)
                registry.register(key.toKey(), entry)
            }

            StaticRegistry.createTags("tags/$resourceName").forEach { (key, value) ->
                registry.tags.computeIfAbsent(key) { arrayListOf() }.addAll(value)
            }

            return DataDrivenRegistry(registry, codec)
        }

        val STREAM_CODEC = object : StreamCodec<DataDrivenRegistry<*>> {
            override fun write(buffer: ByteBuf, value: DataDrivenRegistry<*>) {
                StreamCodec.KEY.write(buffer, value.key)
                StreamCodec.VAR_INT.write(buffer, value.size)

                for (id in 0 until value.size) {
                    StreamCodec.KEY.write(buffer, value[id].key)
                    @Suppress("UNCHECKED_CAST")
                    val binaryTag = (value.codec as Codec<Any>).encode(BinaryTagTranscoder, value[id].value)
                    BinaryTagStreamCodecs.STREAM.optional().write(buffer, binaryTag)
                }
            }

            override fun read(buffer: ByteBuf): DataDrivenRegistry<*> {
                val key = StreamCodec.KEY.read(buffer)
                val size = StreamCodec.VAR_INT.read(buffer)

                val codec: Codec<Any> = TODO()
                val registry = DynamicRegistry<Any>(key)

                repeat(size) {
                    val entryKey = StreamCodec.KEY.read(buffer)
                    val entryValueNbt = BinaryTagStreamCodecs.STREAM.optional().read(buffer) ?: TODO()
                    val entryValue = codec.decode(BinaryTagTranscoder, entryValueNbt)
                    registry.register(entryKey, entryValue)
                }

                return DataDrivenRegistry(registry, codec)
            }
        }
    }
}