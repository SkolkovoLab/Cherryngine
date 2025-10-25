//package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound
//
//import ru.cherryngine.lib.minecraft.extentions.writeUUID
//import ru.cherryngine.lib.minecraft.player.PlayerInfoUpdate
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//import ru.cherryngine.lib.minecraft.protocol.types.writeRawList
//import ru.cherryngine.lib.minecraft.protocol.types.writeMap
//import io.netty.buffer.ByteBuf
//import java.util.*
//
//data class ClientboundPlayerInfoUpdatePacket(val actions: Map<UUID, List<PlayerInfoUpdate>>) : ClientboundPacket() {
//
//    init {
//        // this is bitmask.
//        actions
//            .asIterable()
//            .map { action ->
//                action.value
//                    .fold(0) { mask, update ->
//                        mask or update.type.mask
//                    }
//            }
//            .reduce { l, r ->
//                require(l == r) { "mismatched update length. all lists need to have same type bit mask and length" }
//                l
//            }
//            .let(buffer::writeByte)
//
//        buffer.writeMap(actions, ByteBuf::writeUUID) { buf, value ->
//            buf.writeRawList(
//                value.sortedBy { it.type.ordinal },
//                PlayerInfoUpdate::write
//            )
//        }
//    }
//}