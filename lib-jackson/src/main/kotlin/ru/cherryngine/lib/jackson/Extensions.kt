package ru.cherryngine.lib.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import ru.cherryngine.lib.jackson.serializer.JacksonDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonKeyDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonSerializationProcessor
import ru.cherryngine.lib.jackson.serializer.JacksonSerializer
import kotlin.reflect.KClass

private val KClass<*>.firstGenericType get() = this.supertypes.first().arguments.first().type!!.classifier as KClass<*>

@Suppress("UNCHECKED_CAST")
val <T : Any> JacksonSerializationProcessor<T>.type get() = this::class.firstGenericType as KClass<T>

fun <T : Any> SimpleModule.addSerializationProcessor(sp: JacksonSerializationProcessor<T>) {
    if (sp is JacksonSerializer) {
        addSerializer(sp.type.java, object : StdSerializer<T>(sp.type.java) {
            override fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider?) =
                sp.serialize(value, gen)
        })
    }

    if (sp is JacksonDeserializer) {
        addDeserializer(sp.type.java, object : StdDeserializer<T>(sp.type.java) {
            override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext?) =
                sp.deserialize(jsonParser)
        })
    }

    if (sp is JacksonKeyDeserializer) {
        addKeyDeserializer(sp.type.java, object : KeyDeserializer() {
            override fun deserializeKey(key: String, ctxt: DeserializationContext?) =
                sp.deserializeKey(key)
        })
    }
}

fun Iterable<JacksonSerializationProcessor<*>>.module() =
    SimpleModule().apply { this@module.forEach { addSerializationProcessor(it) } }
