package ru.cherryngine.engine.core.world

import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.view.BlocksViewable
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundForgetLevelChunkPacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.r2.DimensionType
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.light.LightData

data class EmptyChunkViewable(
    override val chunkPos: ChunkPos,
    val dimensionType: DimensionType,
) : BlocksViewable {
    override val viewerPredicate: (Player) -> Boolean = { true }

    override fun show(player: Player) {
        player.connection.sendPacket(
            ClientboundLevelChunkWithLightPacket(
                chunkPos,
                ChunkData.empty(dimensionType),
                LightData.EMPTY
            )
        )
    }

    override fun hide(player: Player) {
        player.connection.sendPacket(ClientboundForgetLevelChunkPacket(chunkPos))
    }

    override fun getBlockId(pos: Vec3I): Int {
        return 0
    }

    override fun getBlock(pos: Vec3I): Block {
        return Block.AIR
    }
}