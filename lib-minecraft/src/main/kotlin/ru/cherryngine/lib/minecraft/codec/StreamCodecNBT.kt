package ru.cherryngine.lib.minecraft.codec

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.BinaryTagType
import net.kyori.adventure.nbt.CompoundBinaryTag
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.BinaryTagUtils

object StreamCodecNBT {
    private val logger = LoggerFactory.getLogger(StreamCodecNBT::class.java)

    val STREAM = object : StreamCodec<BinaryTag> {
        override fun write(buffer: ByteBuf, value: BinaryTag) {
            ByteBufOutputStream(buffer).use { outputStream ->
                val type: BinaryTagType<out BinaryTag> = value.type()
                buffer.writeByte(type.id().toInt())
                @Suppress("UNCHECKED_CAST")
                (type as BinaryTagType<BinaryTag>).write(value, outputStream)
            }
        }

        override fun read(buffer: ByteBuf): BinaryTag {
            try {
                val typeId = buffer.readByte().toInt()
                val type = BinaryTagUtils.nbtTypeFromId(typeId)
                val inputStream = ByteBufInputStream(buffer) // bro is greedy and takes ALL THE BYTES to himself >:(
                val nbt: BinaryTag = inputStream.use {
                    val nbt = type.read(it)
                    buffer.readerIndex(buffer.writerIndex() - it.available()) // read the rest of the bytes leftover, put them back and reset the reader index
                    nbt
                }

                return nbt
            } catch (ex: Exception) {
                logger.error("Failed to read NBT. Buffer state: readableBytes=${buffer.readableBytes()}", ex)
                throw ex
            }
        }
    }

    val COMPOUND_STREAM: StreamCodec<CompoundBinaryTag> = STREAM.transform({ it as CompoundBinaryTag }, { it })
}