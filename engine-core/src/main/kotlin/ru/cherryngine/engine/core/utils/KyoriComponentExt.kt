package ru.cherryngine.engine.core.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

fun String.component() =
    Component.text(this)

fun String.component(style: Style) =
    Component.text(this, style)

fun String.component(textColor: TextColor?) =
    Component.text(this, textColor)

fun String.component(textColor: TextColor?, vararg decorations: TextDecoration) =
    Component.text(this, textColor, *decorations)

fun String.component(textColor: TextColor?, decorations: Set<TextDecoration>) =
    Component.text(this, textColor, decorations)

fun resetComponent() = Component.text()
    .decorations(TextDecoration.entries.associateWith { TextDecoration.State.FALSE })
    .build()

fun String.miniMessage(replacement: Map<String, Any?> = mapOf()): Component {
    val tags = replacement
        .entries
        .map { (key, value) ->
            Placeholder.component(
                key,
                value as? ComponentLike ?: value.toString().component()
            )
        }
    return MiniMessage.miniMessage()
        .deserialize(this, TagResolver.resolver(tags))
}

fun Component.resetBegin() = resetComponent().append(this)

operator fun Component.plus(other: Component) = this.append(other)
operator fun Component.plus(other: String) = this.append(other.component())

//fun Component.plainString() = PlainTextComponentSerializer.plainText().serialize(this)

fun Component.noShadow() = shadowColor(ShadowColor.none())