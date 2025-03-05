package ru.cherryngine.engine.scenes.modules.client

import io.micronaut.context.annotation.Parameter
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.RelativeFlags
import net.minestom.server.network.packet.server.ServerPacket
import net.minestom.server.network.packet.server.play.*
import ru.cherryngine.engine.core.minestomPos
import ru.cherryngine.engine.core.server.ClientConnection
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.event.Event
import ru.cherryngine.engine.scenes.event.impl.DisconnectEvent
import ru.cherryngine.engine.scenes.view.Viewer

@ModulePrototype
class ClientModule(
    @Parameter override val gameObject: GameObject,
    @Parameter val connection: ClientConnection,
) : Module, Viewer {

    val viewDistance = 8

    override fun enable() {
        spawn()
    }

    fun spawn() {
        val position = gameObject.transform.translation.minestomPos()

        val packets: MutableList<ServerPacket.Play> = ArrayList()

        packets += JoinGamePacket(
            -1, false, listOf(), 0,
            viewDistance, viewDistance,
            false, true, false,
            0, "world",
            0, GameMode.SURVIVAL, null, false, true,
            null, 0, 63, false
        )

        packets += SpawnPositionPacket(position, 0f)
        packets += PlayerPositionAndLookPacket(0, position, Vec.ZERO, 0f, 0f, RelativeFlags.NONE)

        packets += UpdateViewDistancePacket(viewDistance)
        packets += UpdateViewPositionPacket(position.chunkX(), position.chunkZ())

        packets += ChangeGameStatePacket(ChangeGameStatePacket.Reason.LEVEL_CHUNKS_LOAD_START, 0f)

        connection.sendPackets(packets)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is DisconnectEvent -> {
                if (event.clientConnection != this.connection) return
                gameObject.destroy()
            }
        }
    }
}