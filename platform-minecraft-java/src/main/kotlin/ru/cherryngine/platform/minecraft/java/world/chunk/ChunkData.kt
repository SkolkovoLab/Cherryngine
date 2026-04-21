package ru.cherryngine.platform.minecraft.java.world.chunk

import net.minestom.server.coordinate.CoordConversion
import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.heightmap.Heightmap
import net.minestom.server.instance.palette.Palette
import net.minestom.server.network.NetworkBuffer
import ru.cherryngine.lib.math.Vec3I
import net.minestom.server.network.packet.server.play.data.ChunkData as MinestomChunkData

/**
 * Данные чанка, подготовленные для отправки клиенту.
 * Внутри используются Minestom `Section` и `Block`, а на момент сериализации
 * секции пакуются в byte[] через Minestom-овские codec'и палитр.
 */
class ChunkData(
    val heightmaps: Map<Heightmap.Type, LongArray>,
    val sections: List<Section>,
    val blockEntities: Map<Vec3I, Block>,
) {
    /**
     * Упаковывает секции в byte[] и отдаёт Minestom [MinestomChunkData] для пакета.
     * biomeCount нужен для корректного directBits у биомной палитры.
     */
    fun toMinestomChunkData(biomeCount: Int): MinestomChunkData {
        val biomeSerializer = Palette.biomeSerializer(biomeCount)
        val data = NetworkBuffer.makeArray { buffer ->
            for (section in sections) {
                buffer.write(NetworkBuffer.SHORT, section.blockPalette().count().toShort())
                buffer.write(Palette.BLOCK_SERIALIZER, section.blockPalette())
                buffer.write(biomeSerializer, section.biomePalette())
            }
        }
        val entries = blockEntities.mapKeys { (pos, _) -> CoordConversion.chunkBlockIndex(pos.x, pos.y, pos.z) }
        return MinestomChunkData(heightmaps, data, entries)
    }
}
