package ru.cherryngine.lib.jackson.serializer.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import jakarta.inject.Singleton
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.jackson.serializer.JacksonDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonKeyDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonSerializer

@Singleton
class KeySerializationProcessor : JacksonSerializer<Key>, JacksonDeserializer<Key>, JacksonKeyDeserializer<Key> {
    override fun serialize(value: Key, gen: JsonGenerator) {
        gen.writeString(value.asString())
    }

    override fun deserialize(parser: JsonParser): Key {
        return Key.key(parser.valueAsString)
    }

    override fun deserializeKey(key: String): Key {
        return Key.key(key)
    }
}
