package ru.cherryngine.lib.jackson.serializer

import com.fasterxml.jackson.core.JsonParser

interface JacksonDeserializer<T : Any> : JacksonSerializationProcessor<T> {
    fun deserialize(parser: JsonParser): T
}