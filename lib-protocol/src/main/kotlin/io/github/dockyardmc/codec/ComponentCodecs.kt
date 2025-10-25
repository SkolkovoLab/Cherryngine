package io.github.dockyardmc.codec

import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer

object ComponentCodecs {
    val NBT = object : StreamCodec<Component> {
        private val SERIALIZER = NBTComponentSerializer.nbt()

        override fun write(buffer: ByteBuf, value: Component) {
            val nbt = SERIALIZER.serialize(value)
            StreamCodecNBT.STREAM.write(buffer, nbt)
        }

        override fun read(buffer: ByteBuf): Component {
            val nbt = StreamCodecNBT.STREAM.read(buffer)
            return SERIALIZER.deserialize(nbt)
        }
    }

    val JSON = object : StreamCodec<Component> {
        private val SERIALIZER = JSONComponentSerializer.json()

        override fun write(buffer: ByteBuf, value: Component) {
            val json = SERIALIZER.serialize(value)
            StreamCodec.STRING.write(buffer, json)
        }

        override fun read(buffer: ByteBuf): Component {
            val json = StreamCodec.STRING.read(buffer)
            return SERIALIZER.deserialize(json)
        }
    }
}