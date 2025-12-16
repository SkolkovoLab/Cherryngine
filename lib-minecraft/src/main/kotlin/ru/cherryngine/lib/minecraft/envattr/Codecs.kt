package ru.cherryngine.lib.minecraft.envattr

import net.kyori.adventure.util.TriState
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.registry.Registries

val TMP_PARTICLE_CODEC = StructCodec.of(
    "type", Codec.KEY, { it.key },
    { Registries.particle[it] }
)

val TRI_STATE_CODEC = object : Codec<TriState> {
    override fun <D> decode(transcoder: Transcoder<D>, value: D): TriState {
        // Try boolean first
        val boolResult = runCatching { transcoder.decodeBoolean(value) }
        if (boolResult.isSuccess) return TriState.byBoolean(boolResult.getOrThrow())

        // Then try string
        return when (val string = transcoder.decodeString(value).lowercase()) {
            "true" -> TriState.TRUE
            "false" -> TriState.FALSE
            "default" -> TriState.NOT_SET
            else -> throw IllegalArgumentException("expected true, false, or \"default\", got: $string")
        }
    }

    override fun <D> encode(
        transcoder: Transcoder<D>,
        value: TriState,
    ): D {
        return when (value) {
            TriState.TRUE -> transcoder.encodeBoolean(true)
            TriState.FALSE -> transcoder.encodeBoolean(false)
            TriState.NOT_SET -> transcoder.encodeString("default")
        }
    }
}