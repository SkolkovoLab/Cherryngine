package ru.cherryngine.lib.minecraft.dialog.body

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.registry.entries.DialogBodyType
import ru.cherryngine.lib.minecraft.registry.keys.DialogBodyTypes
import ru.cherryngine.lib.minecraft.utils.extentions.modify

/**
 * @param showDecorations show item count and durability
 * @param showTooltip show tooltip for the item
 */
data class DialogItemBody(
    val item: ItemStack,
    val description: PlainMessage?,
    val showDecorations: Boolean,
    val showTooltip: Boolean,
    val width: Int,
    val height: Int,
) : DialogBody() {
    override val type: DialogBodyType = DialogBodyTypes.ITEM

    init {
        require(width in 1..256) { "width must be between 1 and 256 (inclusive)" }
        require(height in 1..256) { "height must be between 1 and 256 (inclusive)" }
    }

    override fun getNbt(): CompoundBinaryTag {
        return super.getNbt().modify {
            withCompound("item", item.getNbt())
            description?.let {
                withCompound("description", it.getNbt())
            }
            withBoolean("show_decorations", showDecorations)
            withBoolean("show_tooltip", showTooltip)
            withInt("width", width)
            withInt("height", height)
        }
    }

    class Builder(val item: ItemStack) {
        var description: PlainMessage? = null
        var showDecorations: Boolean = true
        var showTooltip: Boolean = true
        var width: Int = 16
        var height: Int = 16

        inline fun withDescription(content: Component, block: PlainMessage.Builder.() -> Unit = {}) {
            val builder = PlainMessage.Builder(content)
            builder.apply(block)
            description = builder.build()
        }

        fun build(): DialogItemBody {
            return DialogItemBody(item, description, showDecorations, showTooltip, width, height)
        }
    }
}
