package ru.cherryngine.lib.minecraft.item

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.data.DataComponentPatch
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable
import ru.cherryngine.lib.minecraft.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.registry.Items
import ru.cherryngine.lib.minecraft.registry.registries.Item
import ru.cherryngine.lib.minecraft.registry.registries.ItemRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ItemStack(
    var material: Item,
    var amount: Int = 1,
    val components: DataComponentPatch,
) : DataComponentHashable, NbtWritable {
    init {
        require(amount >= 1) { "ItemStack amount cannot be less than 1" }
    }

    companion object {
        val AIR = ItemStack(Items.AIR, 1, DataComponentPatch.EMPTY)

        val STREAM_CODEC: StreamCodec<ItemStack> = streamCodec(true, true)

        fun streamCodec(isPatch: Boolean = true, isTrusted: Boolean = true) = object : StreamCodec<ItemStack> {
            override fun write(buffer: ByteBuf, value: ItemStack) {
                if (value.material == Items.AIR) {
                    StreamCodec.VAR_INT.write(buffer, 0)
                    return
                }

                StreamCodec.VAR_INT.write(buffer, value.amount)
                RegistryStreamCodec(ItemRegistry).write(buffer, value.material)
                DataComponentPatch.patchNetworkType(value.components.components).write(buffer)
            }

            override fun read(buffer: ByteBuf): ItemStack {
                val count = StreamCodec.VAR_INT.read(buffer)
                if (count <= 0) return AIR

                val itemId = StreamCodec.VAR_INT.read(buffer)

                val componentsPatch = DataComponentPatch.read(buffer, isPatch, isTrusted)
                return ItemStack(ItemRegistry.getByProtocolId(itemId), count, componentsPatch)
            }
        }
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("id", material.getEntryIdentifier())
            withInt("count", amount)
            withCompound("components", CompoundBinaryTag.empty())
        }
    }

    fun isEmpty(): Boolean = this.material == Items.AIR

    override fun toString(): String = "ItemStack(${material.identifier}, ${components}, $amount)"

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is ItemStack) return false
        return material.getProtocolId() == other.material.getProtocolId()
//                && attributes == other.attributes //TODO why no same??
                && this.components.getComparisonHash() == other.components.getComparisonHash()

    }

    override fun hashCode(): Int {
        var result = material.getProtocolId().hashCode()
        result = 31 * result + this.components.getComparisonHash().hashCode()
//        result = 31 * result + attributes.hashCode() //TODO why no same??
        return result
    }

    fun isSameAs(other: ItemStack): Boolean {
        return this == other
    }
}

fun ItemStack.clone(): ItemStack {
    return ItemStack(material, amount, components.clone())
}
