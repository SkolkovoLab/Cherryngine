//package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound
//
//import ru.cherryngine.lib.minecraft.advancement.Advancement
//import ru.cherryngine.lib.minecraft.extentions.writeString
//import ru.cherryngine.lib.minecraft.extentions.writeStringArray
//import ru.cherryngine.lib.minecraft.extentions.writeVarInt
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//import ru.cherryngine.lib.minecraft.protocol.writeOptional
//import io.netty.buffer.ByteBuf
//
///**
// * it seems like clients never show advancement toasts when you set [reset] to true
// *
// * @param reset Whether to reset/clear the current advancements.
// * @param advancementsAdd map of identifiers to [Advancement] to be added
// * @param advancementsRemove array of identifiers to be removed
// * @param progress map of identifier to progress to be updated
// */
//class ClientboundUpdateAdvancementsPacket(
//    val reset: Boolean,
//    val advancementsAdd: Map<String, Advancement>,
//    val advancementsRemove: Collection<String>,
//    val progress: Map<String, Map<String, Long?>>
//) : ClientboundPacket() {
//    init {
//        buffer.writeBoolean(reset)
//
//        buffer.writeVarInt(advancementsAdd.size)
//        advancementsAdd.forEach {
//            buffer.writeString(it.key)
//            it.value.write(buffer)
//        }
//
//        buffer.writeStringArray(advancementsRemove)
//
//        buffer.writeVarInt(progress.size)
//        progress.forEach { (advId, advProgress) ->
//            buffer.writeString(advId)
//
//            buffer.writeVarInt(advProgress.size)
//            advProgress.forEach { (criteria, timestamp) ->
//                buffer.writeString(criteria)
//                buffer.writeOptional(timestamp, ByteBuf::writeLong)
//            }
//        }
//    }
//}