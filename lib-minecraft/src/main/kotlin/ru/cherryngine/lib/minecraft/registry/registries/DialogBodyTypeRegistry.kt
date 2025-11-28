package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.dialog.body.DialogItemBody
import ru.cherryngine.lib.minecraft.dialog.body.PlainMessage
import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.entries.DialogBodyType

object DialogBodyTypeRegistry : DynamicRegistry<DialogBodyType>(
    "minecraft:dialog_body_type"
) {
    init {
        addEntry(DialogBodyType("minecraft:item", DialogItemBody::class))
        addEntry(DialogBodyType("minecraft:plain_message", PlainMessage::class))
        updateCache()
    }
}

