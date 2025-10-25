package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key

class ProvidesTrimMaterialComponent(
    val materialIdentifier: Key
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofString(materialIdentifier.asString()))
    }

    companion object {
        val STREAM_CODEC = object : StreamCodec<ProvidesTrimMaterialComponent> {
            override fun write(buffer: ByteBuf, value: ProvidesTrimMaterialComponent) {
                StreamCodec.BOOLEAN.write(buffer, false)
                StreamCodec.KEY.write(buffer, value.materialIdentifier)
            }

            override fun read(buffer: ByteBuf): ProvidesTrimMaterialComponent {
                val direct = StreamCodec.BOOLEAN.read(buffer)
                if (direct) throw UnsupportedOperationException("Cannot read direct trim material") //TODO Add support for direct trim material
                return ProvidesTrimMaterialComponent(StreamCodec.KEY.read(buffer))
            }
        }
    }
}