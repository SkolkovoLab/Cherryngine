package ru.cherryngine.engine.core.commandmanager.brigadier

import org.incendo.cloud.internal.CommandNode
import org.incendo.cloud.parser.ArgumentParser
import org.incendo.cloud.parser.standard.EitherParser
import org.incendo.cloud.parser.standard.LiteralParser
import ru.cherryngine.engine.core.commandmanager.CommandSender

class WrappedNode(
    val node: CommandNode<CommandSender>,
    val name: String?,
    val redirect: WrappedNode?,
    val parser: ArgumentParser<CommandSender, *>?,
) {
    val wrappedChildren by lazy {
        if (redirect != null) return@lazy emptyList()
        node.children().flatMap { child ->
            val component = child.component()
            val parser = component.parser()
            var sequence: Sequence<WrappedNode> = sequenceOf()
            if (parser is LiteralParser) {
                val wrappedChild = WrappedNode(child, component.name(), null, parser)
                sequence += wrappedChild
                sequence += parser.alternativeAliases().asSequence().map { WrappedNode(child, it, wrappedChild, parser) }
            } else if (parser is EitherParser<*, *, *>) {
                @Suppress("UNCHECKED_CAST")
                sequence += WrappedNode(child, "${component.name()}_primary", null, parser.primary().parser() as ArgumentParser<CommandSender, *>)
                @Suppress("UNCHECKED_CAST")
                sequence += WrappedNode(child, "${component.name()}_fallback", null, parser.fallback().parser() as ArgumentParser<CommandSender, *>)
            } else {
                sequence += WrappedNode(child, component.name(), null, parser)
            }
            sequence
        }
    }
}