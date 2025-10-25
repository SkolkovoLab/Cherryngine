package io.github.dockyardmc.dialog.body

import io.github.dockyardmc.extentions.modify
import io.github.dockyardmc.registry.DialogBodyTypes
import io.github.dockyardmc.registry.registries.DialogBodyType
import io.github.dockyardmc.utils.toNBT
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component

class PlainMessage(
    val content: Component,
    val width: Int = 200,
) : DialogBody() {
    override val type: DialogBodyType = DialogBodyTypes.PLAIN_MESSAGE

    init {
        require(width in 1..1024) { "width must be between 1 and 1024 (inclusive)" }
    }

    override fun getNbt(): CompoundBinaryTag {
        return super.getNbt().modify {
            withCompound("contents", content.toNBT())
            withInt("width", width)
        }
    }

    class Builder(val content: Component) {
        var width: Int = 200

        fun build(): PlainMessage {
            return PlainMessage(content, width)
        }
    }
}