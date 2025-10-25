package ru.cherryngine.lib.minecraft.protocol.packets

import ru.cherryngine.lib.minecraft.tide.codec.CodecUtils.byteBufBytes
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class CachedPacket<T : Packet>(
    val original: T,
    val streamCodec: StreamCodec<T>,
) : ClientboundPacket {
    val byteArray: ByteArray = byteBufBytes { b ->
        streamCodec.write(b, original)
    }
}