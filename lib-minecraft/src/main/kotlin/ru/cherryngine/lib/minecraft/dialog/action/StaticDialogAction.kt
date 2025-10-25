package ru.cherryngine.lib.minecraft.dialog.action

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.event.ClickEvent
import ru.cherryngine.lib.minecraft.registry.registries.DialogActionType
import ru.cherryngine.lib.minecraft.registry.registries.DialogActionTypeRegistry

/**
 * Just a wrapper for [ClickEvent]
 *
 * Yes. the client will just do whatever the click event is supposed to do.
 *
 * With an exception `open_file`. it's not allowed from a server
 */
class StaticDialogAction(
    val clickEvent: ClickEvent
) : DialogAction() {
    override val type: DialogActionType = DialogActionTypeRegistry[clickEvent.action().name]

    init {
        require(clickEvent.action() != ClickEvent.Action.OPEN_FILE) { "open_file is not allowed from a server" }
    }

    override fun getNbt(): CompoundBinaryTag {
        TODO()
    }
}