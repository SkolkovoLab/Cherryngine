package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import net.kyori.adventure.text.Component
import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.player.Player
import ru.cherryngine.impl.demo.player.PlayerManager
import ru.cherryngine.impl.demo.world.TestWorldShit
import ru.cherryngine.impl.demo.world.world.WorldImpl
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.PacketHandler
import ru.cherryngine.lib.minecraft.data.DataComponentPatch
import ru.cherryngine.lib.minecraft.data.components.CustomNameComponent
import ru.cherryngine.lib.minecraft.entity.CatMeta
import ru.cherryngine.lib.minecraft.entity.EntityMeta
import ru.cherryngine.lib.minecraft.item.ItemStack
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
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundSetPlayerInventoryPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.*
import ru.cherryngine.lib.minecraft.protocol.packets.status.ClientboundStatusResponsePacket
import ru.cherryngine.lib.minecraft.protocol.packets.status.ServerboundStatusRequestPacket
import ru.cherryngine.lib.minecraft.protocol.types.GameMode
import ru.cherryngine.lib.minecraft.protocol.types.GameProfile
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags
import ru.cherryngine.lib.minecraft.registry.*
import ru.cherryngine.lib.minecraft.registry.registries.tags.*
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.utils.blockPos
import ru.cherryngine.lib.minecraft.world.block.Block
import kotlin.random.Random

@Singleton
class TestPacketHandler(
    val testWorldShit: TestWorldShit,
    val playerManager: PlayerManager,
) : PacketHandler {
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

                playerManager.map[connection] = Player(connection)

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

                val player = playerManager.map[connection] ?: return

                player.world = testWorldShit.normalWorld
            }

            is ServerboundPlayerLoadedPacket -> {
                println("player loaded!")
            }

            is ServerboundMovePlayerPosPacket -> onMove(connection, packet.pos, null, packet.flags)
            is ServerboundMovePlayerPosRotPacket -> onMove(connection, packet.pos, packet.yawPitch, packet.flags)
            is ServerboundMovePlayerRotPacket -> onMove(connection, null, packet.yawPitch, packet.flags)
            is ServerboundMovePlayerStatusOnlyPacket -> onMove(connection, null, null, packet.flags)

            is ServerboundClientTickEndPacket -> {
                playerManager.map[connection]?.tick()
            }

            is ServerboundChatCommandPacket -> {
                val player = playerManager.map[connection] ?: return
                val split = packet.command.split(" ")
                when (split.getOrNull(0)) {
                    "entityadd" -> {
                        val world = player.world
                        val name = split.getOrNull(1)
                        if (world is WorldImpl) {
                            world.entities += McEntity(
                                Random.nextInt(1000, 1_000_000),
                                EntityTypes.CAT
                            ).apply {
                                position = player.clientPosition
                                yawPitch = player.clientYawPitch
                                metadata[CatMeta.ENTITY_FLAGS] = EntityMeta.Flags(hasGlowingEffects = true)
                                metadata[CatMeta.HAS_NO_GRAVITY] = true
                                metadata[CatMeta.VARIANT] = CatVariants.RED
                                metadata[CatMeta.CUSTOM_NAME] = Component.text("$name (${world.name})")
                                metadata[CatMeta.CUSTOM_NAME_VISIBLE] = true
                            }
                        }
                    }

                    "entityclear" -> {
                        val world = player.world
                        if (world is WorldImpl) {
                            world.entities.clear()
                        }
                    }

                    "world" -> {
                        val world = testWorldShit.worlds[split.getOrNull(1)]
                        player.world = world
                    }

                    "block" -> {
                        val world = player.world!!
                        world.setBlock(player.clientPosition.blockPos(), Blocks.TNT.toBlock())
                    }

                    "blocks" -> {
                        val world = player.world!!

                        val blocksMap = mutableMapOf<Vec3I, Block>()
                        for (x in -10..10) for (z in -10..10) {
                            blocksMap[player.clientPosition.blockPos().plus(x, 0, z)] = Blocks.TNT.toBlock()
                        }

                        world.setBlocks(blocksMap)
                    }

                    "item" -> {
                        val itemStack = ItemStack(Items.DIAMOND, 1, DataComponentPatch.fromList(
                            listOf(CustomNameComponent(Component.text("test-item"))),
                        ))
                        player.sendPacket(ClientboundSetPlayerInventoryPacket(0, itemStack))
                    }
                }
            }
        }
    }

    fun onMove(
        connection: Connection,
        pos: Vec3D?,
        yawPitch: YawPitch?,
        flags: MovePlayerFlags,
    ) {
        val player = playerManager.map[connection] ?: return
        if (pos != null) player.lastPosition = pos
        if (yawPitch != null) player.lastYawPitch = yawPitch
    }
}