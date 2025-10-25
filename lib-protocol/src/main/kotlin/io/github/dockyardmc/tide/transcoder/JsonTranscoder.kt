package io.github.dockyardmc.tide.transcoder

import com.google.gson.*

object JsonTranscoder : Transcoder<JsonElement> {
    override fun encodeNull(): JsonElement {
        return JsonNull.INSTANCE
    }

    override fun encodeBoolean(value: Boolean): JsonElement {
        return JsonPrimitive(value)
    }

    override fun encodeByte(value: Byte): JsonElement {
        return JsonPrimitive(value)
    }

    override fun encodeShort(value: Short): JsonElement {
        return JsonPrimitive(value)
    }

    override fun encodeInt(value: Int): JsonElement {
        return JsonPrimitive(value)
    }

    override fun encodeLong(value: Long): JsonElement {
        return JsonPrimitive(value)
    }

    override fun encodeFloat(value: Float): JsonElement {
        return JsonPrimitive(value)
    }

    override fun encodeDouble(value: Double): JsonElement {
        return JsonPrimitive(value)
    }

    override fun encodeString(value: String): JsonElement {
        return JsonPrimitive(value)
    }

    override fun encodeList(size: Int): Transcoder.ListBuilder<JsonElement> {
        val jsonList = JsonArray()
        return object : Transcoder.ListBuilder<JsonElement> {

            override fun add(value: JsonElement): Transcoder.ListBuilder<JsonElement> {
                jsonList.add(value)
                return this
            }

            override fun build(): JsonElement {
                return jsonList
            }
        }
    }

    override fun encodeMap(): Transcoder.VirtualMapBuilder<JsonElement> {
        val jsonMap = JsonObject()
        return object : Transcoder.VirtualMapBuilder<JsonElement> {

            override fun put(key: JsonElement, value: JsonElement): Transcoder.VirtualMapBuilder<JsonElement> {
                put(key.asString, value)
                return this
            }

            override fun put(key: String, value: JsonElement): Transcoder.VirtualMapBuilder<JsonElement> {
                jsonMap.add(key, value)
                return this
            }

            override fun build(): JsonElement {
                return jsonMap
            }
        }
    }

    override fun decodeMap(value: JsonElement): Transcoder.VirtualMap<JsonElement> {
        require(value is JsonObject) { "value is not JsonObject!" }
        return object : Transcoder.VirtualMap<JsonElement> {

            override fun getKeys(): Collection<String> {
                return value.keySet()
            }

            override fun hasValue(key: String): Boolean {
                return value.has(key)
            }

            override fun getValue(key: String): JsonElement {
                return value.get(key)
            }
        }
    }

    override fun decodeList(value: JsonElement): List<JsonElement> {
        require(value is JsonArray) { "value is not JsonArray" }
        return value.asList()
    }

    override fun decodeString(value: JsonElement): String {
        return value.asString
    }

    override fun decodeDouble(value: JsonElement): Double {
        return value.asDouble
    }

    override fun decodeFloat(value: JsonElement): Float {
        return value.asFloat
    }

    override fun decodeLong(value: JsonElement): Long {
        return value.asLong
    }

    override fun decodeInt(value: JsonElement): Int {
        return value.asInt
    }

    override fun decodeShort(value: JsonElement): Short {
        return value.asShort
    }

    override fun decodeByte(value: JsonElement): Byte {
        return value.asByte
    }

    override fun decodeBoolean(value: JsonElement): Boolean {
        return value.asBoolean
    }
}