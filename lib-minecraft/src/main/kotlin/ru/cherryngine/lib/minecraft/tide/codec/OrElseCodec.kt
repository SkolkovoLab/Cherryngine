package ru.cherryngine.lib.minecraft.tide.codec

import ru.cherryngine.lib.minecraft.tide.transcoder.Transcoder


data class OrElseCodec<T>(
    val primary: Codec<T>,
    val secondary: Codec<T>,
) : Codec<T> {
    override fun <D> decode(transcoder: Transcoder<D>, value: D): T {
        val primaryResult = runCatching { primary.decode(transcoder, value) }
        if (primaryResult.isSuccess) return primaryResult.getOrThrow()

        // Try secondary
        val secondaryResult = runCatching { secondary.decode(transcoder, value) }
        if (secondaryResult.isSuccess) return secondaryResult.getOrThrow()

        // Both failed → return primary error
        return primaryResult.getOrThrow()
    }

    override fun <D> encode(transcoder: Transcoder<D>, value: T): D {
        val primaryResult = runCatching { primary.encode(transcoder, value) }
        if (primaryResult.isSuccess) return primaryResult.getOrThrow()

        // Try secondary
        val secondaryResult = runCatching { secondary.encode(transcoder, value) }
        if (secondaryResult.isSuccess) return secondaryResult.getOrThrow()

        // Both failed → return primary error
        return primaryResult.getOrThrow()
    }
}
