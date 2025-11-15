package ru.cherryngine.engine.core

import net.kyori.adventure.text.Component
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.engine.core.view.BlocksViewable
import ru.cherryngine.engine.core.view.Viewable
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundPlayerPositionPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundSystemChatPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.protocol.types.TeleportFlags
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.block.Block

class Player(
    val connection: Connection,
) : CommandSender {
    val gameProfile = connection.gameProfile
    val uuid get() = gameProfile.uuid
    val username get() = gameProfile.username

    var clientPosition: Vec3D = Vec3D.ZERO
    var clientYawPitch: YawPitch = YawPitch.ZERO
    var clientMovePlayerFlags: MovePlayerFlags = MovePlayerFlags(false, false)
    var isSneaking: Boolean = false

    val currentVisibleViewables: MutableSet<Viewable> = hashSetOf()
    val currentVisibleBlocksViewables: MutableList<BlocksViewable> = mutableListOf()
    val chunksToRefresh: MutableSet<ChunkPos> = hashSetOf()

    fun getBlock(pos: Vec3I): Block {
        val chunkPos = ChunkUtils.chunkPosFromVec3I(pos)
        val blockPos = Vec3I(
            ChunkUtils.globalToSectionRelative(pos.x),
            pos.y,
            ChunkUtils.globalToSectionRelative(pos.z)
        )
        val block = currentVisibleBlocksViewables.asReversed().firstNotNullOfOrNull {
            if (it.chunkPos != chunkPos) return@firstNotNullOfOrNull null
            it.getBlock(blockPos)
        }
        return block ?: Block.AIR
    }

    fun teleport(position: Vec3D, yawPitch: YawPitch) {
        clientPosition = position
        clientYawPitch = yawPitch

        connection.sendPacket(
            ClientboundPlayerPositionPacket(
                0,
                position,
                Vec3D.ZERO,
                yawPitch,
                TeleportFlags.EMPTY
            )
        )
    }

    override fun sendMessage(message: Component) {
        connection.sendPacket(ClientboundSystemChatPacket(message, false))
    }
}