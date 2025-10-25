package io.github.dockyardmc.protocol.packets

import io.github.dockyardmc.tide.codec.CodecUtils.byteBufBytes
import io.github.dockyardmc.tide.stream.StreamCodec

class CachedPacket<T : Packet>(
    val original: T,
    val streamCodec: StreamCodec<T>,
) : ClientboundPacket {
    val byteArray: ByteArray = byteBufBytes { b ->
        streamCodec.write(b, original)
    }
}