package ru.cherryngine.lib.minecraft.dialog.input

import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.extentions.modify
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.registry.DialogInputTypes
import ru.cherryngine.lib.minecraft.registry.registries.DialogInputType
import ru.cherryngine.lib.minecraft.utils.toNBT

class SingleOptionDialogInput(
    override val key: String,
    override val label: Component,
    val options: Collection<Option>,
    val width: Int,
    val labelVisible: Boolean,
) : DialogInput() {
    override val type: DialogInputType = DialogInputTypes.SINGLE_OPTION

    init {
        require(options.isNotEmpty()) { "options can't be empty" }
        require(width in 1..1024) { "width must be between 1 and 1024 (inclusive)" }
    }

    override fun getNbt(): CompoundBinaryTag {
        return super.getNbt().modify {
            withInt("width", width)
            withList("options", BinaryTagTypes.COMPOUND, options.map(NbtWritable::getNbtAsCompound))
            withBoolean("label_visible", labelVisible)
        }
    }

    /**
     * @property initial if this option is initially selected
     */
    class Option(
        val id: String,
        val label: Component,
        val initial: Boolean,
    ) : NbtWritable {
        override fun getNbt(): CompoundBinaryTag {
            return nbt {
                withString("id", id)
                withCompound("display", label.toNBT())
                withBoolean("initial", initial)
            }
        }

        class Builder(val id: String) {
            var label: Component = Component.empty()
            var initial: Boolean = false

            fun build(): Option {
                return Option(id, label, initial)
            }
        }
    }

    class Builder(key: String) : DialogInput.Builder(key) {
        val options = mutableListOf<Option>()
        var width: Int = 200
        var labelVisible: Boolean = true

        inline fun addOption(id: String, block: Option.Builder.() -> Unit) {
            options.add(Option.Builder(id).apply(block).build())
        }

        override fun build(): SingleOptionDialogInput {
            return SingleOptionDialogInput(
                key,
                label,
                options.toList(),
                width,
                labelVisible
            )
        }
    }
}