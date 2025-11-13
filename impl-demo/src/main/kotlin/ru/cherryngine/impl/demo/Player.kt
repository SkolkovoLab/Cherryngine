package ru.cherryngine.impl.demo

import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.impl.demo.view.Viewable
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundPlayerPositionPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.protocol.types.TeleportFlags
import ru.cherryngine.lib.minecraft.server.Connection

class Player(
    val connection: Connection,
) {
    val uuid = connection.gameProfile.uuid
    var clientPosition: Vec3D = Vec3D.ZERO
    var clientYawPitch: YawPitch = YawPitch.ZERO
    var clientMovePlayerFlags: MovePlayerFlags = MovePlayerFlags(false, false)
    val currentVisibleViewables: MutableSet<Viewable> = hashSetOf()
    val currentVisibleStaticViewables: MutableSet<StaticViewable> = hashSetOf()
    val chunksToRefresh: MutableSet<ChunkPos> = hashSetOf()

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
}