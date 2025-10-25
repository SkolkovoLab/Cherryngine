//package io.github.dockyardmc.protocol.packets.play.serverbound
//
//import io.github.dockyardmc.data.components.EquippableComponent
//import io.github.dockyardmc.events.Events
//import io.github.dockyardmc.events.InventoryClickEvent
//import io.github.dockyardmc.extentions.readEnum
//import io.github.dockyardmc.extentions.readVarInt
//import io.github.dockyardmc.inventory.InventoryClickHandler
//import io.github.dockyardmc.inventory.PlayerInventoryUtils
//import io.github.dockyardmc.item.HashedItemStack
//import io.github.dockyardmc.item.ItemStack
//import io.github.dockyardmc.item.clone
//import io.github.dockyardmc.maths.randomFloat
//import io.github.dockyardmc.player.Player
//import io.github.dockyardmc.protocol.PlayerNetworkManager
//import io.github.dockyardmc.protocol.packets.ServerboundPacket
//import io.github.dockyardmc.protocol.types.EquipmentSlot
//import io.github.dockyardmc.registry.Sounds
//import io.github.dockyardmc.sounds.playSound
//import io.github.dockyardmc.ui.DrawableItemStack
//import io.github.dockyardmc.utils.getPlayerEventContext
//import io.netty.buffer.ByteBuf
//import io.netty.channel.ChannelHandlerContext
//import kotlin.math.ceil
//import kotlin.random.Random
//
//class ServerboundClickContainerPacket(
//    var windowId: Int,
//    var stateId: Int,
//    var slot: Int,
//    var button: Int,
//    var mode: ContainerClickMode,
//    var changedSlots: MutableMap<Int, HashedItemStack>,
//    var item: HashedItemStack,
//) : ServerboundPacket {
//    companion object {
//        fun read(buffer: ByteBuf): ServerboundClickContainerPacket {
//            val windowsId = buffer.readVarInt()
//            val stateId = buffer.readVarInt()
//            val slot = buffer.readShort().toInt()
//            val button = buffer.readByte().toInt()
//            val mode = buffer.readEnum<ContainerClickMode>()
//            val changedSlots = mutableMapOf<Int, HashedItemStack>()
//
//            val arraySize = buffer.readVarInt()
//            for (i in 0 until arraySize) {
//                val slotNumber = buffer.readShort().toInt()
//                val slotData = HashedItemStack.read(buffer)
//                changedSlots[slotNumber] = slotData
//            }
//
//            val carriedItem = HashedItemStack.read(buffer)
//
//            return ServerboundClickContainerPacket(
//                windowsId,
//                stateId,
//                slot,
//                button,
//                mode,
//                changedSlots,
//                carriedItem
//            )
//        }
//    }
//}
//
//enum class ContainerClickMode {
//    NORMAL,
//    NORMAL_SHIFT,
//    HOTKEY,
//    MIDDLE_CLICK,
//    DROP,
//    SLOT_DRAG,
//    DOUBLE_CLICK
//}
//
//enum class NormalButtonAction(val button: Int, val outsideInv: Boolean = false) {
//    LEFT_MOUSE_CLICK(0),
//    RIGHT_MOUSE_CLICK(1),
//    LEFT_CLICK_OUTSIDE_INVENTORY(0, true),
//    RIGHT_CLICK_OUTSIDE_INVENTORY(1, true),
//}
//
//enum class DragButtonAction(val button: Int, val outsideInv: Boolean = false) {
//    STARTING_LEFT_MOUSE_DRAG(0, true),
//    STARTING_RIGHT_MOUSE_DRAG(4, true),
//    STARTING_MIDDLE_MOUSE_DRAG(8, true),
//    ADD_SLOT_FOR_LEFT_MOUSE_DRAG(1),
//    ADD_SLOT_FOR_RIGHT_MOUSE_DRAG(5),
//    ADD_SLOT_FOR_MIDDLE_MOUSE_DRAG(9),
//    ENDING_LEFT_MOUSE_DRAG(2, true),
//    ENDING_RIGHT_MOUSE_DRAG(6, true),
//    ENDING_MIDDLE_MOUSE_DRAG(10, true)
//}
//
//enum class NormalShiftButtonAction(val button: Int, val outsideInv: Boolean = false) {
//    SHIFT_LEFT_MOUSE_CLICK(0),
//    SHIFT_RIGHT_MOUSE_CLICK(1),
//}
//
//enum class HotkeyButtonAction(val button: Int) {
//    CHANGE_TO_SLOT(0),
//    OFFHAND_SWAP(40)
//}
//
//enum class DropButtonAction(val button: Int, val outsideInv: Boolean = false) {
//    DROP(0),
//    CONTROL_DROP(1)
//}
//
//enum class DoubleClickButtonAction(val button: Int, val outsideInv: Boolean = false) {
//    DOUBLE_CLICK(0),
//    PICKUP_ALL(1)
//}