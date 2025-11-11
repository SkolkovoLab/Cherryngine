package ru.cherryngine.lib.minecraft.protocol

import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.utils.ObjectPool
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

object NetworkCompression {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val INFLATER_POOL = ObjectPool(::Inflater)
    private val DEFLATER_POOL = ObjectPool(::Deflater)

    fun decompress(input: ByteArray): ByteArray {
        val inflater = INFLATER_POOL.get()
        inflater.setInput(input)

        val outputStream = ByteArrayOutputStream()
        val output = ByteArray(1024)
        try {
            while (!inflater.finished()) {
                val decompressionSize = inflater.inflate(output)
                outputStream.write(output, 0, decompressionSize)
            }
        } catch (ex: Exception) {
            logger.error("Data of the input is not valid compressed data", ex)
        } finally {
            inflater.reset()
            INFLATER_POOL.add(inflater)
        }

        return outputStream.toByteArray()
    }

    fun compress(input: ByteArray): ByteArray {
        val deflater = DEFLATER_POOL.get()
        deflater.setInput(input)
        deflater.finish()

        val outputStream = ByteArrayOutputStream()
        val output = ByteArray(1024)

        while (!deflater.finished()) {
            val compressedSize = deflater.deflate(output)
            outputStream.write(output, 0, compressedSize)
        }
        deflater.reset()

        DEFLATER_POOL.add(deflater)
        return outputStream.toByteArray()
    }
}