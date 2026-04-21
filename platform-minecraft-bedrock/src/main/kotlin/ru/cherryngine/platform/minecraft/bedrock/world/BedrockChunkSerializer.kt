package ru.cherryngine.platform.minecraft.bedrock.world

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minestom.server.instance.Section
import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtUtils
import org.cloudburstmc.protocol.common.util.VarInts
import java.io.ByteArrayOutputStream

object BedrockChunkSerializer {

    private val EMPTY_CHUNK_DATA: ByteArray = run {
        val out = ByteArrayOutputStream()
        out.write(ByteArray(258))
        NbtUtils.createNetworkWriter(out).use { it.writeTag(NbtMap.EMPTY) }
        out.toByteArray()
    }

    private const val OVERWORLD_SECTIONS = 24

    fun serialize(
        sections: List<Section>,
        blockMapping: BedrockBlockMapping,
    ): Pair<Int, ByteBuf> {
        // Find topmost non-empty section
        var topSection = sections.size - 1
        while (topSection >= 0 && sections[topSection].blockPalette().isEmpty) {
            topSection--
        }
        val subChunkCount = topSection + 1

        if (subChunkCount == 0) {
            return 0 to Unpooled.wrappedBuffer(EMPTY_CHUNK_DATA)
        }

        val buf = Unpooled.buffer()

        // Subchunk sections (version 8 — no subChunkIndex needed)
        for (i in 0 until subChunkCount) {
            buf.writeByte(8)  // version 8 (simpler, no subChunkIndex)
            buf.writeByte(1)  // 1 storage layer

            writeBlockStorage(buf, sections[i], blockMapping)
        }

        // Biome data — use legacy 258-byte zero format (same as empty chunks)
        buf.writeZero(256) // 2D biome data (16x16 = 256 bytes, all zeros = ocean/plains)
        buf.writeByte(0)   // border blocks
        buf.writeByte(0)   // extra data count

        return subChunkCount to buf
    }

    private fun writeBlockStorage(buf: ByteBuf, section: Section, blockMapping: BedrockBlockMapping) {
        val bedrockPalette = mutableListOf<Int>()
        val paletteIndex = HashMap<Int, Int>()
        val indices = IntArray(4096)

        for (x in 0 until 16) {
            for (y in 0 until 16) {
                for (z in 0 until 16) {
                    val javaStateId = section.blockPalette().get(x, y, z)
                    val bedrockRuntimeId = blockMapping.getBedrockRuntimeId(javaStateId)
                    val idx = paletteIndex.getOrPut(bedrockRuntimeId) {
                        bedrockPalette.add(bedrockRuntimeId)
                        bedrockPalette.size - 1
                    }
                    // Bedrock XZY indexing
                    indices[(x shl 8) or (z shl 4) or y] = idx
                }
            }
        }

        if (bedrockPalette.size == 1) {
            // Singleton: header + entry only (NO size, NO words)
            buf.writeByte(0x01) // (0 << 1) | 1
            VarInts.writeInt(buf, bedrockPalette[0])
            return
        }

        val bitsPerEntry = pickBitArrayVersion(bedrockPalette.size)
        val entriesPerWord = 32 / bitsPerEntry
        val wordCount = (4096 + entriesPerWord - 1) / entriesPerWord

        // Header
        buf.writeByte((bitsPerEntry shl 1) or 1)

        // Bit array words (little-endian)
        val mask = (1 shl bitsPerEntry) - 1
        for (wordIdx in 0 until wordCount) {
            var word = 0
            for (entryIdx in 0 until entriesPerWord) {
                val blockIdx = wordIdx * entriesPerWord + entryIdx
                if (blockIdx < 4096) {
                    word = word or ((indices[blockIdx] and mask) shl (entryIdx * bitsPerEntry))
                }
            }
            buf.writeIntLE(word)
        }

        // Palette size + entries (signed VarInt)
        VarInts.writeInt(buf, bedrockPalette.size)
        for (runtimeId in bedrockPalette) {
            VarInts.writeInt(buf, runtimeId)
        }
    }

    private fun pickBitArrayVersion(paletteSize: Int): Int {
        val needed = ceilLog2(paletteSize)
        return when {
            needed <= 1 -> 1
            needed <= 2 -> 2
            needed <= 3 -> 3
            needed <= 4 -> 4
            needed <= 5 -> 5
            needed <= 6 -> 6
            needed <= 8 -> 8
            else -> 16
        }
    }

    private fun ceilLog2(value: Int): Int {
        if (value <= 1) return 1
        return 32 - Integer.numberOfLeadingZeros(value - 1)
    }
}
