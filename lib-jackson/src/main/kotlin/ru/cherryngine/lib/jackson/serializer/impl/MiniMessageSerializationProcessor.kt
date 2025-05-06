package ru.cherryngine.lib.jackson.serializer.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import jakarta.inject.Singleton
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import ru.cherryngine.lib.jackson.serializer.JacksonDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonSerializer

@Singleton
class MiniMessageSerializationProcessor : JacksonSerializer<Component>, JacksonDeserializer<Component> {
    private val miniMessage: MiniMessage = MiniMessage.miniMessage()

    override fun serialize(value: Component, gen: JsonGenerator) {
        gen.writeString(miniMessage.serialize(value))
    }

    override fun deserialize(parser: JsonParser): Component {
        return miniMessage.deserialize(parser.text)
    }
}
