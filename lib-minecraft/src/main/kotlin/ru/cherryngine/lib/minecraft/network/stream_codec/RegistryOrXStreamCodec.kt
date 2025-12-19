package ru.cherryngine.lib.minecraft.network.stream_codec

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.utils.Either
import ru.cherryngine.lib.minecraft.utils.registry.Registry
import ru.cherryngine.lib.minecraft.utils.registry.RegistryEntryHolder

class RegistryOrXStreamCodec<R : Any>(
    val registry: Registry<R>,
    val rightCodec: StreamCodec<R>,
) : StreamCodec<Either<RegistryEntryHolder.Key<R>, R>> {
    override fun write(buffer: ByteBuf, value: Either<RegistryEntryHolder.Key<R>, R>) {
        when (value) {
            is Either.Left -> {
                val id = registry[value.value].id
                StreamCodec.VAR_INT.write(buffer, id + 1)
            }

            is Either.Right -> {
                StreamCodec.VAR_INT.write(buffer, 0)
                rightCodec.write(buffer, value.value)
            }
        }
    }

    override fun read(buffer: ByteBuf): Either<RegistryEntryHolder.Key<R>, R> {
        val id = StreamCodec.VAR_INT.read(buffer) - 1
        return if (id >= 0) {
            Either.Left(RegistryEntryHolder.Key(registry.getKey(id)))
        } else {
            Either.Right(rightCodec.read(buffer))
        }
    }
}