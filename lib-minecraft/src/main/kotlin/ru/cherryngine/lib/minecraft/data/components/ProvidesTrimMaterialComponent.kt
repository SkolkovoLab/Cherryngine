package ru.cherryngine.lib.minecraft.data.components

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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