package ru.cherryngine.lib.minecraft.network.protocol.packets

import ru.cherryngine.lib.minecraft.codec.CodecUtils.byteBufBytes
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class CachedPacket<T : Packet>(
    val original: T,
    val streamCodec: StreamCodec<T>,
) : ClientboundPacket {
    val byteArray: ByteArray = byteBufBytes { b ->
        streamCodec.write(b, original)
    }
}