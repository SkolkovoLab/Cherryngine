package ru.cherryngine.lib.minecraft.codec

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.registry.Registry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.tide.types.Either

class RegistryOrXStreamCodec<T : RegistryEntry, R>(
    val registry: Registry<T>,
    val rightCodec: StreamCodec<R>
) : StreamCodec<Either<T, R>> {
    override fun write(buffer: ByteBuf, value: Either<T, R>) {
        when (value) {
            is Either.Left -> {
                val id = registry.getProtocolIdByEntry(value.value)
                StreamCodec.VAR_INT.write(buffer, id + 1)
            }

            is Either.Right -> {
                StreamCodec.VAR_INT.write(buffer, 0)
                rightCodec.write(buffer, value.value)
            }
        }
    }

    override fun read(buffer: ByteBuf): Either<T, R> {
        val id = StreamCodec.VAR_INT.read(buffer) - 1
        return if (id >= 0) {
            Either.Left(registry.getByProtocolId(id))
        } else {
            Either.Right(rightCodec.read(buffer))
        }
    }
}