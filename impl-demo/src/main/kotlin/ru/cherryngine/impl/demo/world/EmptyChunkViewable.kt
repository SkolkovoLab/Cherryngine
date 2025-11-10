package ru.cherryngine.impl.demo.world

import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundForgetLevelChunkPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.world.Light
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData

data class EmptyChunkViewable(
    override val chunkPos: ChunkPos,
) : StaticViewable {
    override val viewerPredicate: (Connection) -> Boolean = { true }

    override fun show(player: Connection) {
        player.sendPacket(ClientboundLevelChunkWithLightPacket(chunkPos, ChunkData.EMPTY, Light.EMPTY))
    }

    override fun hide(player: Connection) {
        player.sendPacket(ClientboundForgetLevelChunkPacket(chunkPos))
    }
}