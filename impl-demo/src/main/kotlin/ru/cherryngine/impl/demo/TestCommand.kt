package ru.cherryngine.impl.demo

import com.github.quillraven.fleks.Entity
import net.kyori.adventure.key.Key
import org.incendo.cloud.annotation.specifier.Greedy
import org.incendo.cloud.annotation.specifier.Range
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import ru.cherryngine.engine.core.Player
import ru.cherryngine.engine.core.commandmanager.CloudCommand
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.impl.demo.ecs.FleksWorld
import ru.cherryngine.impl.demo.ecs.testimpl.components.ApartComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.systems.CommandActionsSystem
import java.util.*

@CloudCommand
class TestCommand(
    private val demoInit: DemoInit,
) {
    @Command("testcommand <string> <int> <key> <uuid> <greedy>")
    @Permission("command.test")
    fun testCommand(
        sender: CommandSender,
        string: String,
        @Range(min = "5", max = "20") int: Int,
        key: Key,
        uuid: UUID,
        @Greedy greedy: String,
    ) {
        sender.sendMessage("$string + $int + $key + $uuid + $greedy")
    }

    private fun FleksWorld.getEntity(playerUuid: UUID): Entity {
        return family { all(PlayerComponent) }.firstOrNull {
            it[PlayerComponent].uuid == playerUuid
        } ?: throw RuntimeException("Entity for playerUUID $playerUuid doesn't exist!")
    }

    @Command("apart <apartId>")
    fun apartCommand(
        sender: Player,
        apartId: String,
    ) {
        demoInit.fleksWorld.system<CommandActionsSystem>().addAction {
            val entity = getEntity(sender.uuid)
            if (apartId == "null") {
                entity.configure {
                    it -= ApartComponent
                }
                sender.sendMessage("Apartment removed for ${sender.username}")
            } else {
                entity.configure {
                    it.getOrAdd(ApartComponent) { ApartComponent("") }.apartName = apartId
                }
                sender.sendMessage("Apartment set to $apartId for ${sender.username}")
            }
        }
    }

    @Command("swap <other>")
    fun swapCommand(
        sender: Player,
        other: Player,
    ) {
        demoInit.fleksWorld.system<CommandActionsSystem>().addAction {
            val entity = getEntity(sender.uuid)
            val otherPlayer = getEntity(other.uuid)
            val tmp = entity[PlayerComponent].uuid
            entity[PlayerComponent].uuid = otherPlayer[PlayerComponent].uuid
            otherPlayer[PlayerComponent].uuid = tmp
        }
    }
}