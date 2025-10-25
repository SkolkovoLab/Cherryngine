package io.github.dockyardmc.tide.stream

import io.github.dockyardmc.tide.types.Either
import io.netty.buffer.ByteBuf

class EitherStreamCodec<L, R>(
    val leftCodec: StreamCodec<L>,
    val rightCodec: StreamCodec<R>
) : StreamCodec<Either<L, R>> {
    override fun write(buffer: ByteBuf, value: Either<L, R>) {
        when (value) {
            is Either.Left -> {
                StreamCodec.BOOLEAN.write(buffer, true)
                leftCodec.write(buffer, value.value)
            }

            is Either.Right -> {
                StreamCodec.BOOLEAN.write(buffer, false)
                rightCodec.write(buffer, value.value)
            }
        }
    }

    override fun read(buffer: ByteBuf): Either<L, R> {
        return if (StreamCodec.BOOLEAN.read(buffer)) Either.Left(leftCodec.read(buffer)) else Either.Right(rightCodec.read(buffer))
    }
}