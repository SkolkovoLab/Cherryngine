package io.github.dockyardmc.tide.codec

import io.github.dockyardmc.tide.transcoder.Transcoder

interface StructCodec<R> : Codec<R> {

    fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T

    fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R

    override fun <T> encode(transcoder: Transcoder<T>, value: R): T {
        return encodeToMap<T>(transcoder, value, transcoder.encodeMap())
    }

    override fun <D> decode(transcoder: Transcoder<D>, value: D): R {
        return decodeFromMap(transcoder, transcoder.decodeMap(value))
    }

    companion object {
        const val INLINE = "_inline_"

        fun <R> of(new: () -> R): StructCodec<R> {
            return object : StructCodec<R> {

                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    return transcoder.emptyMap()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    return new.invoke()
                }

            }
        }

        fun <P1, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            new: (P1) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {

                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    return new.invoke(result1)
                }

            }
        }

        fun <P1, P2, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            new: (P1, P2) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {

                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    return new.invoke(result1, result2)
                }

            }
        }

        fun <P1, P2, P3, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            new: (P1, P2, P3) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    return new.invoke(result1, result2, result3)
                }
            }
        }

        fun <P1, P2, P3, P4, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            new: (P1, P2, P3, P4) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    return new.invoke(result1, result2, result3, result4)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            new: (P1, P2, P3, P4, P5) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    return new.invoke(result1, result2, result3, result4, result5)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            new: (P1, P2, P3, P4, P5, P6) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            new: (P1, P2, P3, P4, P5, P6, P7) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            new: (P1, P2, P3, P4, P5, P6, P7, P8) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            name11: String, codec11: Codec<P11>, getter11: (R) -> P11,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    put(transcoder, codec11, map, name11, getter11.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    val result11 = get(transcoder, codec11, name11, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            name11: String, codec11: Codec<P11>, getter11: (R) -> P11,
            name12: String, codec12: Codec<P12>, getter12: (R) -> P12,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    put(transcoder, codec11, map, name11, getter11.invoke(value))
                    put(transcoder, codec12, map, name12, getter12.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    val result11 = get(transcoder, codec11, name11, map)
                    val result12 = get(transcoder, codec12, name12, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            name11: String, codec11: Codec<P11>, getter11: (R) -> P11,
            name12: String, codec12: Codec<P12>, getter12: (R) -> P12,
            name13: String, codec13: Codec<P13>, getter13: (R) -> P13,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    put(transcoder, codec11, map, name11, getter11.invoke(value))
                    put(transcoder, codec12, map, name12, getter12.invoke(value))
                    put(transcoder, codec13, map, name13, getter13.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    val result11 = get(transcoder, codec11, name11, map)
                    val result12 = get(transcoder, codec12, name12, map)
                    val result13 = get(transcoder, codec13, name13, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            name11: String, codec11: Codec<P11>, getter11: (R) -> P11,
            name12: String, codec12: Codec<P12>, getter12: (R) -> P12,
            name13: String, codec13: Codec<P13>, getter13: (R) -> P13,
            name14: String, codec14: Codec<P14>, getter14: (R) -> P14,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    put(transcoder, codec11, map, name11, getter11.invoke(value))
                    put(transcoder, codec12, map, name12, getter12.invoke(value))
                    put(transcoder, codec13, map, name13, getter13.invoke(value))
                    put(transcoder, codec14, map, name14, getter14.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    val result11 = get(transcoder, codec11, name11, map)
                    val result12 = get(transcoder, codec12, name12, map)
                    val result13 = get(transcoder, codec13, name13, map)
                    val result14 = get(transcoder, codec14, name14, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            name11: String, codec11: Codec<P11>, getter11: (R) -> P11,
            name12: String, codec12: Codec<P12>, getter12: (R) -> P12,
            name13: String, codec13: Codec<P13>, getter13: (R) -> P13,
            name14: String, codec14: Codec<P14>, getter14: (R) -> P14,
            name15: String, codec15: Codec<P15>, getter15: (R) -> P15,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    put(transcoder, codec11, map, name11, getter11.invoke(value))
                    put(transcoder, codec12, map, name12, getter12.invoke(value))
                    put(transcoder, codec13, map, name13, getter13.invoke(value))
                    put(transcoder, codec14, map, name14, getter14.invoke(value))
                    put(transcoder, codec15, map, name15, getter15.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    val result11 = get(transcoder, codec11, name11, map)
                    val result12 = get(transcoder, codec12, name12, map)
                    val result13 = get(transcoder, codec13, name13, map)
                    val result14 = get(transcoder, codec14, name14, map)
                    val result15 = get(transcoder, codec15, name15, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            name11: String, codec11: Codec<P11>, getter11: (R) -> P11,
            name12: String, codec12: Codec<P12>, getter12: (R) -> P12,
            name13: String, codec13: Codec<P13>, getter13: (R) -> P13,
            name14: String, codec14: Codec<P14>, getter14: (R) -> P14,
            name15: String, codec15: Codec<P15>, getter15: (R) -> P15,
            name16: String, codec16: Codec<P16>, getter16: (R) -> P16,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    put(transcoder, codec11, map, name11, getter11.invoke(value))
                    put(transcoder, codec12, map, name12, getter12.invoke(value))
                    put(transcoder, codec13, map, name13, getter13.invoke(value))
                    put(transcoder, codec14, map, name14, getter14.invoke(value))
                    put(transcoder, codec15, map, name15, getter15.invoke(value))
                    put(transcoder, codec16, map, name16, getter16.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    val result11 = get(transcoder, codec11, name11, map)
                    val result12 = get(transcoder, codec12, name12, map)
                    val result13 = get(transcoder, codec13, name13, map)
                    val result14 = get(transcoder, codec14, name14, map)
                    val result15 = get(transcoder, codec15, name15, map)
                    val result16 = get(transcoder, codec16, name16, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15, result16)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            name11: String, codec11: Codec<P11>, getter11: (R) -> P11,
            name12: String, codec12: Codec<P12>, getter12: (R) -> P12,
            name13: String, codec13: Codec<P13>, getter13: (R) -> P13,
            name14: String, codec14: Codec<P14>, getter14: (R) -> P14,
            name15: String, codec15: Codec<P15>, getter15: (R) -> P15,
            name16: String, codec16: Codec<P16>, getter16: (R) -> P16,
            name17: String, codec17: Codec<P17>, getter17: (R) -> P17,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    put(transcoder, codec11, map, name11, getter11.invoke(value))
                    put(transcoder, codec12, map, name12, getter12.invoke(value))
                    put(transcoder, codec13, map, name13, getter13.invoke(value))
                    put(transcoder, codec14, map, name14, getter14.invoke(value))
                    put(transcoder, codec15, map, name15, getter15.invoke(value))
                    put(transcoder, codec16, map, name16, getter16.invoke(value))
                    put(transcoder, codec17, map, name17, getter17.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    val result11 = get(transcoder, codec11, name11, map)
                    val result12 = get(transcoder, codec12, name12, map)
                    val result13 = get(transcoder, codec13, name13, map)
                    val result14 = get(transcoder, codec14, name14, map)
                    val result15 = get(transcoder, codec15, name15, map)
                    val result16 = get(transcoder, codec16, name16, map)
                    val result17 = get(transcoder, codec17, name17, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15, result16, result17)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            name11: String, codec11: Codec<P11>, getter11: (R) -> P11,
            name12: String, codec12: Codec<P12>, getter12: (R) -> P12,
            name13: String, codec13: Codec<P13>, getter13: (R) -> P13,
            name14: String, codec14: Codec<P14>, getter14: (R) -> P14,
            name15: String, codec15: Codec<P15>, getter15: (R) -> P15,
            name16: String, codec16: Codec<P16>, getter16: (R) -> P16,
            name17: String, codec17: Codec<P17>, getter17: (R) -> P17,
            name18: String, codec18: Codec<P18>, getter18: (R) -> P18,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    put(transcoder, codec11, map, name11, getter11.invoke(value))
                    put(transcoder, codec12, map, name12, getter12.invoke(value))
                    put(transcoder, codec13, map, name13, getter13.invoke(value))
                    put(transcoder, codec14, map, name14, getter14.invoke(value))
                    put(transcoder, codec15, map, name15, getter15.invoke(value))
                    put(transcoder, codec16, map, name16, getter16.invoke(value))
                    put(transcoder, codec17, map, name17, getter17.invoke(value))
                    put(transcoder, codec18, map, name18, getter18.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    val result11 = get(transcoder, codec11, name11, map)
                    val result12 = get(transcoder, codec12, name12, map)
                    val result13 = get(transcoder, codec13, name13, map)
                    val result14 = get(transcoder, codec14, name14, map)
                    val result15 = get(transcoder, codec15, name15, map)
                    val result16 = get(transcoder, codec16, name16, map)
                    val result17 = get(transcoder, codec17, name17, map)
                    val result18 = get(transcoder, codec18, name18, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15, result16, result17, result18)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            name11: String, codec11: Codec<P11>, getter11: (R) -> P11,
            name12: String, codec12: Codec<P12>, getter12: (R) -> P12,
            name13: String, codec13: Codec<P13>, getter13: (R) -> P13,
            name14: String, codec14: Codec<P14>, getter14: (R) -> P14,
            name15: String, codec15: Codec<P15>, getter15: (R) -> P15,
            name16: String, codec16: Codec<P16>, getter16: (R) -> P16,
            name17: String, codec17: Codec<P17>, getter17: (R) -> P17,
            name18: String, codec18: Codec<P18>, getter18: (R) -> P18,
            name19: String, codec19: Codec<P19>, getter19: (R) -> P19,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    put(transcoder, codec11, map, name11, getter11.invoke(value))
                    put(transcoder, codec12, map, name12, getter12.invoke(value))
                    put(transcoder, codec13, map, name13, getter13.invoke(value))
                    put(transcoder, codec14, map, name14, getter14.invoke(value))
                    put(transcoder, codec15, map, name15, getter15.invoke(value))
                    put(transcoder, codec16, map, name16, getter16.invoke(value))
                    put(transcoder, codec17, map, name17, getter17.invoke(value))
                    put(transcoder, codec18, map, name18, getter18.invoke(value))
                    put(transcoder, codec19, map, name19, getter19.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    val result11 = get(transcoder, codec11, name11, map)
                    val result12 = get(transcoder, codec12, name12, map)
                    val result13 = get(transcoder, codec13, name13, map)
                    val result14 = get(transcoder, codec14, name14, map)
                    val result15 = get(transcoder, codec15, name15, map)
                    val result16 = get(transcoder, codec16, name16, map)
                    val result17 = get(transcoder, codec17, name17, map)
                    val result18 = get(transcoder, codec18, name18, map)
                    val result19 = get(transcoder, codec19, name19, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15, result16, result17, result18, result19)
                }
            }
        }

        fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, R> of(
            name1: String, codec1: Codec<P1>, getter1: (R) -> P1,
            name2: String, codec2: Codec<P2>, getter2: (R) -> P2,
            name3: String, codec3: Codec<P3>, getter3: (R) -> P3,
            name4: String, codec4: Codec<P4>, getter4: (R) -> P4,
            name5: String, codec5: Codec<P5>, getter5: (R) -> P5,
            name6: String, codec6: Codec<P6>, getter6: (R) -> P6,
            name7: String, codec7: Codec<P7>, getter7: (R) -> P7,
            name8: String, codec8: Codec<P8>, getter8: (R) -> P8,
            name9: String, codec9: Codec<P9>, getter9: (R) -> P9,
            name10: String, codec10: Codec<P10>, getter10: (R) -> P10,
            name11: String, codec11: Codec<P11>, getter11: (R) -> P11,
            name12: String, codec12: Codec<P12>, getter12: (R) -> P12,
            name13: String, codec13: Codec<P13>, getter13: (R) -> P13,
            name14: String, codec14: Codec<P14>, getter14: (R) -> P14,
            name15: String, codec15: Codec<P15>, getter15: (R) -> P15,
            name16: String, codec16: Codec<P16>, getter16: (R) -> P16,
            name17: String, codec17: Codec<P17>, getter17: (R) -> P17,
            name18: String, codec18: Codec<P18>, getter18: (R) -> P18,
            name19: String, codec19: Codec<P19>, getter19: (R) -> P19,
            name20: String, codec20: Codec<P20>, getter20: (R) -> P20,
            new: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20) -> R
        ): StructCodec<R> {
            return object : StructCodec<R> {
                override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
                    put(transcoder, codec1, map, name1, getter1.invoke(value))
                    put(transcoder, codec2, map, name2, getter2.invoke(value))
                    put(transcoder, codec3, map, name3, getter3.invoke(value))
                    put(transcoder, codec4, map, name4, getter4.invoke(value))
                    put(transcoder, codec5, map, name5, getter5.invoke(value))
                    put(transcoder, codec6, map, name6, getter6.invoke(value))
                    put(transcoder, codec7, map, name7, getter7.invoke(value))
                    put(transcoder, codec8, map, name8, getter8.invoke(value))
                    put(transcoder, codec9, map, name9, getter9.invoke(value))
                    put(transcoder, codec10, map, name10, getter10.invoke(value))
                    put(transcoder, codec11, map, name11, getter11.invoke(value))
                    put(transcoder, codec12, map, name12, getter12.invoke(value))
                    put(transcoder, codec13, map, name13, getter13.invoke(value))
                    put(transcoder, codec14, map, name14, getter14.invoke(value))
                    put(transcoder, codec15, map, name15, getter15.invoke(value))
                    put(transcoder, codec16, map, name16, getter16.invoke(value))
                    put(transcoder, codec17, map, name17, getter17.invoke(value))
                    put(transcoder, codec18, map, name18, getter18.invoke(value))
                    put(transcoder, codec19, map, name19, getter19.invoke(value))
                    put(transcoder, codec20, map, name20, getter20.invoke(value))
                    return map.build()
                }

                override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
                    val result1 = get(transcoder, codec1, name1, map)
                    val result2 = get(transcoder, codec2, name2, map)
                    val result3 = get(transcoder, codec3, name3, map)
                    val result4 = get(transcoder, codec4, name4, map)
                    val result5 = get(transcoder, codec5, name5, map)
                    val result6 = get(transcoder, codec6, name6, map)
                    val result7 = get(transcoder, codec7, name7, map)
                    val result8 = get(transcoder, codec8, name8, map)
                    val result9 = get(transcoder, codec9, name9, map)
                    val result10 = get(transcoder, codec10, name10, map)
                    val result11 = get(transcoder, codec11, name11, map)
                    val result12 = get(transcoder, codec12, name12, map)
                    val result13 = get(transcoder, codec13, name13, map)
                    val result14 = get(transcoder, codec14, name14, map)
                    val result15 = get(transcoder, codec15, name15, map)
                    val result16 = get(transcoder, codec16, name16, map)
                    val result17 = get(transcoder, codec17, name17, map)
                    val result18 = get(transcoder, codec18, name18, map)
                    val result19 = get(transcoder, codec19, name19, map)
                    val result20 = get(transcoder, codec20, name20, map)
                    return new.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9, result10, result11, result12, result13, result14, result15, result16, result17, result18, result19, result20)
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun <D, T> put(transcoder: Transcoder<D>, codec: Codec<T>, map: Transcoder.VirtualMapBuilder<D>, key: String, value: T): D {

            if (key == INLINE) {
                val encodeCodec: Codec<T> = when (codec) {
                    is OptionalCodec<*> -> codec.inner as Codec<T>
                    is DefaultCodec<*> -> codec.inner as Codec<T>
                    is RecursiveCodec<*> -> codec.self as Codec<T>
                    else -> codec
                }
                if (encodeCodec !is StructCodec<T>) {
                    throw Codec.EncodingException("Provided codec for inline $key is not StructCodec")
                }

                return encodeCodec.encodeToMap(transcoder, value, map)
            }

            val result = codec.encode(transcoder, value)
            map.put(key, result)
            return result
        }

        @Suppress("UNCHECKED_CAST")
        private fun <D, T> get(transcoder: Transcoder<D>, codec: Codec<T>, key: String, map: Transcoder.VirtualMap<D>): T {

            if (key == INLINE) {
                val decodeCodec: Codec<T> = when (codec) {
                    is OptionalCodec<*> -> codec.inner as Codec<T>
                    is DefaultCodec<*> -> codec.inner as Codec<T>
                    is RecursiveCodec<*> -> codec.self as Codec<T>
                    else -> codec
                }
                if (decodeCodec !is StructCodec<*>) {
                    throw Codec.EncodingException("Provided codec for inline $key is not StructCodec")
                }

                val result = runCatching {
                    decodeCodec.decodeFromMap(transcoder, map)
                }
                if (result.isFailure && codec is DefaultCodec<*>) {
                    return codec.default as T
                }
                if (result.isFailure && codec is OptionalCodec<*>) {
                    return null as T
                }
                return result.getOrThrow() as T
            }

            if (codec is DefaultCodec<*> && !map.hasValue(key)) {
                return codec.default as T
            }

            if (codec is OptionalCodec<*> && !map.hasValue(key)) {
                return null as T
            }

            return codec.decode(transcoder, map.getValue(key))
        }
    }
}