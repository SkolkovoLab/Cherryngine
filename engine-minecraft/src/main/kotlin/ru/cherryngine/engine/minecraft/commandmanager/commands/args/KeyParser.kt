package ru.cherryngine.engine.minecraft.commandmanager.commands.args

import jakarta.inject.Singleton
import net.kyori.adventure.key.Key
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import org.incendo.cloud.parser.ArgumentParseResult
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.engine.minecraft.commandmanager.SArgumentParser
import ru.cherryngine.lib.minecraft.network.protocol.types.ArgumentParserType
import ru.cherryngine.lib.minecraft.utils.toKey

@Singleton
class KeyParser() : SArgumentParser<Key> {
    override val type: Class<Key> = Key::class.java
    override val argumentParserType: ArgumentParserType = ArgumentParserType.ResourceLocation

    override fun parse(
        commandContext: CommandContext<CommandSender>,
        commandInput: CommandInput,
    ): ArgumentParseResult<Key> {
        return ArgumentParseResult.success(commandInput.readString().toKey())
    }
}