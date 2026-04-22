package ru.cherryngine.engine.core.instance

import jakarta.inject.Singleton
import kotlinx.coroutines.channels.Channel
import ru.cherryngine.engine.core.player.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Singleton
class InstanceRouter {
    private data class InstanceEntry(
        val id: String,
        val joinChannel: Channel<UUID>,
        val leaveChannel: Channel<Player>,
    )

    private val instances = ConcurrentHashMap<String, InstanceEntry>()
    private val playerInstance = ConcurrentHashMap<UUID, String>()

    fun register(id: String, joinChannel: Channel<UUID>, leaveChannel: Channel<Player>) {
        instances[id] = InstanceEntry(id, joinChannel, leaveChannel)
    }

    fun unregister(id: String) {
        instances.remove(id)
    }

    fun routePlayer(uuid: UUID, instanceId: String) {
        val entry = instances[instanceId] ?: error("Instance $instanceId not found")
        playerInstance[uuid] = instanceId
        entry.joinChannel.trySend(uuid)
    }

    fun transferPlayer(player: Player, targetInstanceId: String) {
        val currentId = playerInstance[player.uuid]
        val current = currentId?.let { instances[it] }
        val target = instances[targetInstanceId] ?: error("Instance $targetInstanceId not found")
        current?.leaveChannel?.trySend(player)
        playerInstance[player.uuid] = targetInstanceId
        target.joinChannel.trySend(player.uuid)
    }

    fun removePlayer(player: Player) {
        val instanceId = playerInstance.remove(player.uuid) ?: return
        instances[instanceId]?.leaveChannel?.trySend(player)
    }
}