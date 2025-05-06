package ru.cherryngine.lib.jackson.serializer

interface JacksonKeyDeserializer<T : Any> : JacksonSerializationProcessor<T> {
    fun deserializeKey(key: String): T
}