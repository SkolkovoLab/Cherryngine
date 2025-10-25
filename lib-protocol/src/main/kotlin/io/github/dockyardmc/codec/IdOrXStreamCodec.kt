package io.github.dockyardmc.codec

import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.tide.types.Either
import io.netty.buffer.ByteBuf

class IdOrXStreamCodec<R>(
    val rightCodec: StreamCodec<R>
) : StreamCodec<Either<Int, R>> {
    override fun write(buffer: ByteBuf, value: Either<Int, R>) {
        when (value) {
            is Either.Left -> {
                StreamCodec.VAR_INT.write(buffer, value.value + 1)
            }

            is Either.Right -> {
                StreamCodec.VAR_INT.write(buffer, 0)
                rightCodec.write(buffer, value.value)
            }
        }
    }

    override fun read(buffer: ByteBuf): Either<Int, R> {
        val id = StreamCodec.VAR_INT.read(buffer) - 1
        return if (id >= 0) {
            Either.Left(id)
        } else {
            Either.Right(rightCodec.read(buffer))
        }
    }
}