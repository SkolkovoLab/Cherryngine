package ru.cherryngine.lib.minecraft.codec

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.tide.types.Either

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