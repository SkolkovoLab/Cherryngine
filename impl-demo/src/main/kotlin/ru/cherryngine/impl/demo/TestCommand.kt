package ru.cherryngine.impl.demo

import net.kyori.adventure.key.Key
import org.incendo.cloud.annotation.specifier.Greedy
import org.incendo.cloud.annotation.specifier.Range
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import ru.cherryngine.engine.core.commandmanager.CloudCommand
import ru.cherryngine.engine.core.commandmanager.CommandSender
import java.util.*

@CloudCommand
class TestCommand {

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

}