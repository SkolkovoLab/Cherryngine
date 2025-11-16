package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import org.slf4j.LoggerFactory
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.impl.demo.ecs.testimpl.components.AxolotlModelComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.PacketsEvent
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ServerboundFinishConfigurationPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundGameEventPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundLoginPacket
import ru.cherryngine.lib.minecraft.protocol.types.GameMode
import ru.cherryngine.lib.minecraft.registry.DimensionTypes
import java.util.*

class PlayerInitSystem(
    val defaultViewContextID: String,
    val playerManager: PlayerManager,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    private val logger = LoggerFactory.getLogger(PlayerInitSystem::class.java)

    override fun onTick() {
        val skipCreate = mutableSetOf<UUID>()
        world.family { all(PlayerComponent) }.forEach {
            val playerComponent = it[PlayerComponent]
            if (playerComponent.uuid in playerManager.toCreatePlayers) {
                skipCreate.add(playerComponent.uuid)
            }
            if (playerComponent.uuid in playerManager.toRemovePlayers) {
//                it.remove()
            }
        }
        playerManager.toRemovePlayers.clear()

        playerManager.toCreatePlayers.forEach { player ->
            if (player in skipCreate) return@forEach
            logger.info("Creating ECS entity for player $player")
            world.entity {
                it += PlayerComponent(
                    player,
                    setOf(defaultViewContextID)
                )

                it += ViewableComponent(setOf(defaultViewContextID))

                it += AxolotlModelComponent
            }
        }
        playerManager.toCreatePlayers.clear()

        super.onTick()
    }

    override fun onTickEntity(entity: Entity) {
        val playerComponent = entity[PlayerComponent]
        val uuid = playerComponent.uuid
        val packets = playerManager.queues.remove(uuid) ?: return

        entity.configure {
            it += PacketsEvent(packets)
        }

        val player = playerManager.getPlayerNullable(uuid) ?: return

        packets.forEach { packet ->
            if (packet is ServerboundFinishConfigurationPacket) {
                player.connection.sendPacket(
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
                        GameMode.SURVIVAL,
                        GameMode.SURVIVAL,
                        false,
                        false,
                        null,
                        0,
                        32,
                        false
                    )
                )

                val positionComponent = entity.getOrNull(PositionComponent)

                if (positionComponent != null) {
                    player.teleport(positionComponent.position, positionComponent.yawPitch)
                }

                player.connection.sendPacket(
                    ClientboundGameEventPacket(
                        ClientboundGameEventPacket.GameEvent.START_WAITING_FOR_CHUNKS,
                        0f
                    )
                )
            }
        }
    }
}