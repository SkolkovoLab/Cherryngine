package ru.cherryngine.engine.core.player

import jakarta.inject.Singleton
import kotlinx.coroutines.channels.Channel
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Singleton
class InstanceRouter {
    private data class InstanceEntry(
        val id: String,
        val joinChannel: Channel<UUID>,
        val leaveChannel: Channel<UUID>,
    )

    private val instances = ConcurrentHashMap<String, InstanceEntry>()
    private val playerInstance = ConcurrentHashMap<UUID, String>()

    fun register(id: String, joinChannel: Channel<UUID>, leaveChannel: Channel<UUID>) {
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

    fun transferPlayer(uuid: UUID, targetInstanceId: String) {
        val currentId = playerInstance[uuid]
        val current = currentId?.let { instances[it] }
        val target = instances[targetInstanceId] ?: error("Instance $targetInstanceId not found")
        current?.leaveChannel?.trySend(uuid)
        playerInstance[uuid] = targetInstanceId
        target.joinChannel.trySend(uuid)
    }

    fun removePlayer(uuid: UUID) {
        val instanceId = playerInstance.remove(uuid) ?: return
        instances[instanceId]?.leaveChannel?.trySend(uuid)
    }
}
