package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.EquipmentSlot
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

data class ClientboundSetEquipmentPacket(
    val entityId: Int,
    val equipment: Map<EquipmentSlot, ItemStack>,
) : ClientboundPacket {
    companion object {
        private val EQUIPMENT_STREAM_CODEC = object : StreamCodec<Map<EquipmentSlot, ItemStack>> {
            override fun write(buffer: ByteBuf, value: Map<EquipmentSlot, ItemStack>) {
                var index = 0
                val size = value.size

                for ((slot, item) in value) {
                    val last = index++ == size - 1
                    var slotEnum = slot.ordinal.toByte()
                    if (!last) slotEnum = (slotEnum.toInt() or 0x80).toByte()

                    StreamCodec.BYTE.write(buffer, slotEnum)
                    ItemStack.STREAM_CODEC.write(buffer, item)
                }
            }

            override fun read(buffer: ByteBuf): Map<EquipmentSlot, ItemStack> {
                val equipments = EnumMap<EquipmentSlot, ItemStack>(EquipmentSlot::class.java)

                var slot: Byte
                do {
                    slot = StreamCodec.BYTE.read(buffer)
                    val slotId = slot.toInt() and 0x7F
                    val equipmentSlot = EquipmentSlot.entries[slotId]
                    val item = ItemStack.STREAM_CODEC.read(buffer)

                    equipments[equipmentSlot] = item
                } while ((slot.toInt() and 0x80) == 0x80)

                return equipments
            }
        }

        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundSetEquipmentPacket::entityId,
            EQUIPMENT_STREAM_CODEC, ClientboundSetEquipmentPacket::equipment,
            ::ClientboundSetEquipmentPacket
        )
    }
}