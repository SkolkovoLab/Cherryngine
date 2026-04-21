package ru.cherryngine.platform.minecraft.java.player

import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import net.minestom.server.coordinate.Pos
import net.minestom.server.network.ConnectionState
import net.minestom.server.network.packet.client.common.ClientPingRequestPacket
import net.minestom.server.network.packet.client.configuration.ClientFinishConfigurationPacket
import net.minestom.server.network.packet.client.login.ClientLoginAcknowledgedPacket
import net.minestom.server.network.packet.client.play.*
import net.minestom.server.network.packet.client.status.StatusRequestPacket
import net.minestom.server.network.packet.server.common.TagsPacket
import net.minestom.server.network.packet.server.configuration.FinishConfigurationPacket
import net.minestom.server.network.packet.server.status.ResponsePacket
import net.minestom.server.registry.Registries
import ru.cherryngine.engine.core.player.InstanceRouter
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerRouter
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.platform.minecraft.java.ServerConsts
import ru.cherryngine.platform.minecraft.java.events.DisconnectEvent
import ru.cherryngine.platform.minecraft.java.events.PacketEvent
import ru.cherryngine.platform.minecraft.java.events.PlayerConfigurationAsyncEvent
import ru.cherryngine.platform.minecraft.java.events.PlayerCreatedEvent
import ru.cherryngine.platform.minecraft.java.network.Connection
import ru.cherryngine.platform.minecraft.java.utils.vec3D
import ru.cherryngine.platform.minecraft.java.utils.yawPitch
import ru.cherryngine.platform.minecraft.java.world.MovePlayerFlags

@Singleton
class MinecraftConnectionService(
    private val playerManager: PlayerManager,
    private val instanceRouter: InstanceRouter,
    private val playerRouter: PlayerRouter,
    private val registries: Registries,
    val playerCreatedEventPublisher: ApplicationEventPublisher<PlayerCreatedEvent>,
    val playerConfigurationAsyncEventPublisher: ApplicationEventPublisher<PlayerConfigurationAsyncEvent>,
) {
    companion object {
        private const val STATUS_JSON_TEMPLATE = """{"version":{"name":"%s","protocol":%d},"players":{"online":13,"max":37,"sample":[]},"description":{"text":"Cherryngine"}}"""
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        val (connection, packet) = event
        when (packet) {
            is StatusRequestPacket -> {
                val json = STATUS_JSON_TEMPLATE.format(ServerConsts.MINECRAFT_VERSION, ServerConsts.PROTOCOL_VERSION)
                connection.sendPacket(ResponsePacket(json))
            }

            is ClientLoginAcknowledgedPacket -> {
                sendRegistryData(connection)

                val uuid = connection.gameProfile.uuid()
                val existing = playerManager.getPlayerNullable(uuid)
                val player: MinecraftPlayer = if (existing == null) {
                    val created = MinecraftPlayer(connection)
                    playerManager.register(created)
                    playerCreatedEventPublisher.publishEvent(PlayerCreatedEvent(created))
                    created
                } else {
                    existing as MinecraftPlayer
                }

                Thread.startVirtualThread {
                    playerConfigurationAsyncEventPublisher.publishEvent(PlayerConfigurationAsyncEvent(player))
                    connection.sendPacket(FinishConfigurationPacket())
                }
            }

            is ClientFinishConfigurationPacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) ?: return
                instanceRouter.routePlayer(player.uuid, playerRouter.getInitialInstance(player))
            }

            is ClientPlayerPositionPacket -> onMove(
                connection,
                packet.position.vec3D(),
                null,
                flagsFromByte(packet.flags),
            )

            is ClientPlayerPositionAndRotationPacket -> {
                val p: Pos = packet.position
                onMove(
                    connection,
                    p.vec3D(),
                    p.yawPitch(),
                    flagsFromByte(packet.flags),
                )
            }

            is ClientPlayerRotationPacket -> onMove(
                connection,
                null,
                YawPitch(packet.yaw, packet.pitch),
                flagsFromByte(packet.flags),
            )

            is ClientPlayerPositionStatusPacket -> onMove(
                connection,
                null, null, flagsFromByte(packet.flags),
            )

            is ClientCommandChatPacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) as? MinecraftPlayer ?: return
                player.pendingCommands.offer(packet.message)
            }

            is ClientTabCompletePacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) as? MinecraftPlayer ?: return
                player.pendingSuggestions.offer(Pair(packet.transactionId, packet.text.removePrefix("/")))
            }

            is ClientInputPacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) as? MinecraftPlayer ?: return
                player.isSneaking = true
            }

            is ClientPingRequestPacket -> {
                // отвечает за Connection, игнорируем здесь
            }
        }
    }

    /**
     * Шлёт все RegistryData- и TagsPacket, которые клиент ждёт в CONFIGURATION-фазе.
     * Зеркалит `ConnectionManager.doConfiguration` из Minestom, excludeVanilla=false
     * — не используем knownPacks-optimization, клиент получит полные данные реестров.
     */
    private fun sendRegistryData(connection: Connection) {
        val excludeVanilla = false
        connection.sendPacket(registries.chatType().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.biome().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.dialog().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.damageType().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.trimMaterial().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.trimPattern().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.bannerPattern().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.enchantment().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.paintingVariant().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.jukeboxSong().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.instrument().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.wolfVariant().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.wolfSoundVariant().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.catVariant().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.chickenVariant().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.cowVariant().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.frogVariant().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.pigVariant().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.zombieNautilusVariant().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.timeline().registryDataPacket(registries, excludeVanilla))
        connection.sendPacket(registries.dimensionType().registryDataPacket(registries, excludeVanilla))

        val tagEntries = listOf(
            registries.blocks().tagRegistry(),
            registries.entityType().tagRegistry(),
            registries.fluid().tagRegistry(),
            registries.gameEvent().tagRegistry(),
            registries.material().tagRegistry(),
            registries.chatType().tagRegistry(),
            registries.biome().tagRegistry(),
            registries.dialog().tagRegistry(),
            registries.damageType().tagRegistry(),
            registries.trimMaterial().tagRegistry(),
            registries.trimPattern().tagRegistry(),
            registries.bannerPattern().tagRegistry(),
            registries.enchantment().tagRegistry(),
            registries.paintingVariant().tagRegistry(),
            registries.jukeboxSong().tagRegistry(),
            registries.instrument().tagRegistry(),
            registries.wolfVariant().tagRegistry(),
            registries.wolfSoundVariant().tagRegistry(),
            registries.catVariant().tagRegistry(),
            registries.chickenVariant().tagRegistry(),
            registries.cowVariant().tagRegistry(),
            registries.frogVariant().tagRegistry(),
            registries.pigVariant().tagRegistry(),
            registries.zombieNautilusVariant().tagRegistry(),
            registries.timeline().tagRegistry(),
            registries.dimensionType().tagRegistry(),
        )
        connection.sendPacket(TagsPacket(tagEntries))
    }

    private fun flagsFromByte(flags: Byte): MovePlayerFlags {
        val i = flags.toInt()
        return MovePlayerFlags(isOnGround = (i and 0x01) != 0, horizontalCollision = (i and 0x02) != 0)
    }

    private fun onMove(
        connection: Connection,
        pos: Vec3D?,
        yawPitch: YawPitch?,
        flags: MovePlayerFlags,
    ) {
        val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) as? MinecraftPlayer ?: return
        if (pos != null) player.clientPosition = pos
        if (yawPitch != null) player.clientYawPitch = yawPitch
        player.clientMovePlayerFlags = flags
    }

    @EventListener
    fun onDisconnect(event: DisconnectEvent) {
        val connection = event.connection
        if (connection.state == ConnectionState.PLAY || connection.state == ConnectionState.CONFIGURATION) {
            val uuid = connection.gameProfile.uuid()
            val player = playerManager.getPlayerNullable(uuid)
            if (player != null) {
                instanceRouter.removePlayer(player)
            }
            playerManager.unregister(uuid)
        }
    }
}
