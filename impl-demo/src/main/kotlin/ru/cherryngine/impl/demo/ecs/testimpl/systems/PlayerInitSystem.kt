package ru.cherryngine.impl.demo.ecs.testimpl.systems

import net.kyori.adventure.text.Component
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.EntityComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.view.ViewableProvider
import ru.cherryngine.lib.minecraft.PacketHandler
import ru.cherryngine.lib.minecraft.entity.AxolotlMeta
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundPongResponsePacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ClientboundUpdateTagsPacket
import ru.cherryngine.lib.minecraft.protocol.packets.common.ServerboundPingRequestPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ServerboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.handshake.ServerboundIntentionPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ClientboundLoginFinishedPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundHelloPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ServerboundLoginAcknowledgedPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundGameEventPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundLoginPacket
import ru.cherryngine.lib.minecraft.protocol.packets.status.ClientboundStatusResponsePacket
import ru.cherryngine.lib.minecraft.protocol.packets.status.ServerboundStatusRequestPacket
import ru.cherryngine.lib.minecraft.protocol.types.GameMode
import ru.cherryngine.lib.minecraft.protocol.types.GameProfile
import ru.cherryngine.lib.minecraft.registry.DimensionTypes
import ru.cherryngine.lib.minecraft.registry.EntityTypes
import ru.cherryngine.lib.minecraft.registry.RegistryManager
import ru.cherryngine.lib.minecraft.registry.registries.tags.*
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.*
import kotlin.random.Random

class PlayerInitSystem(
    val gameScene: GameScene,
    val defaultViewContextUUID: UUID,
) : GameSystem, PacketHandler {
    var queues = hashMapOf<Connection, MutableList<ServerboundPacket>>()

    override fun tick(tickIndex: Long, tickStartMs: Long) {
        val queues = this.queues
        this.queues = hashMapOf()
        gameScene.objectsWithComponent(PlayerComponent::class).forEach { gameObject ->
            val playerComponent = gameObject[PlayerComponent::class]!!
            val packets = queues[playerComponent.connection] ?: listOf()
            gameObject[PlayerComponent::class] = playerComponent.copy(packets = packets)
        }
    }

    override fun onPacket(connection: Connection, packet: ServerboundPacket) {
        when (packet) {
            is ServerboundIntentionPacket -> {
                connection.state = when (packet.intent) {
                    ServerboundIntentionPacket.Intent.STATUS -> ProtocolState.STATUS
                    ServerboundIntentionPacket.Intent.LOGIN -> ProtocolState.LOGIN
                    ServerboundIntentionPacket.Intent.TRANSFER -> ProtocolState.LOGIN
                }
            }

            is ServerboundPingRequestPacket -> {
                connection.sendPacket(ClientboundPongResponsePacket(packet.time))
            }

            is ServerboundStatusRequestPacket -> {
                connection.sendPacket(
                    ClientboundStatusResponsePacket(
                        """
                    {
                        "version": {
                            "name": "1.21.8",
                            "protocol": 772
                        },
                        "players": {
                            "max": 0,
                            "online": 0
                        },
                        "description": {
                            "text": "Hello, world!"
                        }
                    }
                    """.trimIndent()
                    )
                )
            }

            is ServerboundHelloPacket -> {
                val gameProfile = GameProfile(packet.uuid, packet.name, mutableListOf())
                connection.sendPacket(ClientboundLoginFinishedPacket(gameProfile))
            }

            is ServerboundLoginAcknowledgedPacket -> {
                connection.state = ProtocolState.CONFIGURATION

                val playerGameObject = gameScene.createGameObject()
                playerGameObject[PlayerComponent::class] = PlayerComponent(
                    connection,
                    listOf(),
                    defaultViewContextUUID
                )

                val entity = McEntity(Random.nextInt(1000, 1_000_000), EntityTypes.AXOLOTL).apply {
                    metadata[AxolotlMeta.HAS_NO_GRAVITY] = true
                    metadata[AxolotlMeta.VARIANT] = AxolotlMeta.Variant.entries.random()
                    metadata[AxolotlMeta.CUSTOM_NAME] = Component.text(connection.address.toString())
                    metadata[AxolotlMeta.CUSTOM_NAME_VISIBLE] = true
                    viewerPredicate = { it != connection }
                }

                playerGameObject[EntityComponent::class] = EntityComponent(entity)

                playerGameObject[ViewableComponent::class] = ViewableComponent(
                    defaultViewContextUUID,
                    setOf(ViewableProvider.Static(setOf(entity))),
                    setOf()
                )

                val cachedTagPacket = ClientboundUpdateTagsPacket(
                    listOf(
                        BiomeTagRegistry,
                        ItemTagRegistry,
                        BlockTagRegistry,
                        FluidTagRegistry,
                        EntityTypeTagRegistry
                    )
                )
                connection.sendPacket(cachedTagPacket)

                RegistryManager.dynamicRegistries.values.forEach { registry ->
                    connection.sendPacket(ClientboundRegistryDataPacket(registry))
                }
                connection.sendPacket(ClientboundFinishConfigurationPacket())
            }

            is ServerboundFinishConfigurationPacket -> {
                connection.state = ProtocolState.PLAY
                connection.sendPacket(
                    ClientboundLoginPacket(
                        0,
                        false,
                        listOf(),
                        20,
                        8,
                        8,
                        false,
                        true,
                        false,
                        DimensionTypes.OVERWORLD,
                        "world",
                        0L,
                        GameMode.CREATIVE,
                        GameMode.CREATIVE,
                        false,
                        false,
                        null,
                        0,
                        32,
                        false
                    )
                )

                connection.sendPacket(
                    ClientboundGameEventPacket(
                        ClientboundGameEventPacket.GameEvent.START_WAITING_FOR_CHUNKS,
                        0f
                    )
                )
            }

            else -> {
                val queue = queues.computeIfAbsent(connection) { arrayListOf() }
                queue.add(packet)
            }
        }
    }
}