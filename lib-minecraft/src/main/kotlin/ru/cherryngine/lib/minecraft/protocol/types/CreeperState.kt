package ru.cherryngine.lib.minecraft.protocol.types

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

enum class CreeperState {
    IDLE,
    FUSE;

    companion object {
        val STREAM_CODEC = object : StreamCodec<CreeperState> {
            override fun write(
                buffer: ByteBuf,
                value: CreeperState,
            ) {
                val intValue = when (value) {
                    IDLE -> -1
                    FUSE -> 1
                }
                StreamCodec.VAR_INT.write(buffer, intValue)
            }

            override fun read(buffer: ByteBuf): CreeperState {
                val intValue = StreamCodec.VAR_INT.read(buffer)
                return when (intValue) {
                    -1 -> IDLE
                    1 -> FUSE
                    else -> throw IllegalArgumentException("Invalid state value $intValue")
                }
            }
        }
    }
}