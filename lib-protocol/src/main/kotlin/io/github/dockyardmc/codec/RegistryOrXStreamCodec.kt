package io.github.dockyardmc.codec

import io.github.dockyardmc.registry.Registry
import io.github.dockyardmc.registry.RegistryEntry
import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.tide.types.Either
import io.netty.buffer.ByteBuf

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