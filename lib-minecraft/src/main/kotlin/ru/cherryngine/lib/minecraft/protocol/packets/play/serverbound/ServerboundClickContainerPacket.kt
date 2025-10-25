//package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound
//
//import ru.cherryngine.lib.minecraft.data.components.EquippableComponent
//import ru.cherryngine.lib.minecraft.events.Events
//import ru.cherryngine.lib.minecraft.events.InventoryClickEvent
//import ru.cherryngine.lib.minecraft.extentions.readEnum
//import ru.cherryngine.lib.minecraft.extentions.readVarInt
//import ru.cherryngine.lib.minecraft.inventory.InventoryClickHandler
//import ru.cherryngine.lib.minecraft.inventory.PlayerInventoryUtils
//import ru.cherryngine.lib.minecraft.item.HashedItemStack
//import ru.cherryngine.lib.minecraft.item.ItemStack
//import ru.cherryngine.lib.minecraft.item.clone
//import ru.cherryngine.lib.minecraft.maths.randomFloat
//import ru.cherryngine.lib.minecraft.player.Player
//import ru.cherryngine.lib.minecraft.protocol.PlayerNetworkManager
//import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
//import ru.cherryngine.lib.minecraft.protocol.types.EquipmentSlot
//import ru.cherryngine.lib.minecraft.registry.Sounds
//import ru.cherryngine.lib.minecraft.sounds.playSound
//import ru.cherryngine.lib.minecraft.ui.DrawableItemStack
//import ru.cherryngine.lib.minecraft.utils.getPlayerEventContext
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