package ru.cherryngine.lib.minecraft.network

import io.netty.buffer.ByteBuf
import net.minestom.server.network.NetworkBuffer
import net.minestom.server.registry.Registries

object NetworkBufferBridge {
    fun wrapRemaining(buffer: ByteBuf, registries: Registries? = null): NetworkBuffer {
        val bytes = ByteArray(buffer.readableBytes())
        buffer.readBytes(bytes)
        return NetworkBuffer.wrap(bytes, 0, bytes.size, registries)
    }

    fun <T> writeToArray(type: NetworkBuffer.Type<T>, value: T, registries: Registries? = null): ByteArray {
        return NetworkBuffer.makeArray(type, value, registries)
    }
}
