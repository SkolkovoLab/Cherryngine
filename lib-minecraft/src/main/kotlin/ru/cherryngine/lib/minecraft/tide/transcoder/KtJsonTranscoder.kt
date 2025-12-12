package ru.cherryngine.lib.minecraft.tide.transcoder

import kotlinx.serialization.json.*


object KtJsonTranscoder : Transcoder<JsonElement> {
    override fun encodeNull(): JsonElement {
        return JsonNull
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
        val jsonList = mutableListOf<JsonElement>()
        return object : Transcoder.ListBuilder<JsonElement> {
            override fun add(value: JsonElement): Transcoder.ListBuilder<JsonElement> {
                jsonList.add(value)
                return this
            }

            override fun build(): JsonElement {
                return JsonArray(jsonList)
            }
        }
    }

    override fun encodeMap(): Transcoder.VirtualMapBuilder<JsonElement> {
        val jsonMap = mutableMapOf<String, JsonElement>()
        return object : Transcoder.VirtualMapBuilder<JsonElement> {
            override fun put(key: JsonElement, value: JsonElement): Transcoder.VirtualMapBuilder<JsonElement> {
                put(key.jsonPrimitive.toString(), value)
                return this
            }

            override fun put(key: String, value: JsonElement): Transcoder.VirtualMapBuilder<JsonElement> {
                jsonMap[key] = value
                return this
            }

            override fun build(): JsonElement {
                return JsonObject(jsonMap)
            }
        }
    }

    override fun decodeMap(value: JsonElement): Transcoder.VirtualMap<JsonElement> {
        require(value is JsonObject) { "value is not JsonObject!" }
        return object : Transcoder.VirtualMap<JsonElement> {
            override fun getKeys(): Collection<String> {
                return value.keys
            }

            override fun hasValue(key: String): Boolean {
                return key in value
            }

            override fun getValue(key: String): JsonElement {
                return value[key]!!
            }
        }
    }

    override fun decodeList(value: JsonElement): List<JsonElement> {
        return value.jsonArray
    }

    override fun decodeString(value: JsonElement): String {
        return value.jsonPrimitive.content
    }

    override fun decodeDouble(value: JsonElement): Double {
        return value.jsonPrimitive.double
    }

    override fun decodeFloat(value: JsonElement): Float {
        return value.jsonPrimitive.float
    }

    override fun decodeLong(value: JsonElement): Long {
        return value.jsonPrimitive.long
    }

    override fun decodeInt(value: JsonElement): Int {
        return value.jsonPrimitive.int
    }

    override fun decodeShort(value: JsonElement): Short {
        return value.jsonPrimitive.int.toShort()
    }

    override fun decodeByte(value: JsonElement): Byte {
        return value.jsonPrimitive.int.toByte()
    }

    override fun decodeBoolean(value: JsonElement): Boolean {
        return value.jsonPrimitive.boolean
    }
}