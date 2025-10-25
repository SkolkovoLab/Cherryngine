//package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound
//
//import ru.cherryngine.lib.minecraft.extentions.writeEnum
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//import ru.cherryngine.lib.minecraft.world.waypoint.WaypointData
//
//data class ClientboundTrackedWaypointPacket(val operation: Operation, val waypointData: WaypointData) : ClientboundPacket() {
//
//    enum class Operation {
//        TRACK,
//        UNTRACK,
//        UPDATE
//    }
//
//    init {
//        buffer.writeEnum(operation)
//        waypointData.write(buffer)
//    }
//
//}