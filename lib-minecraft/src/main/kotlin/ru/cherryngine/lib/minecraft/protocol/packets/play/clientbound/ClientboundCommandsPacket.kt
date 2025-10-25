//package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound
//
//import ru.cherryngine.lib.minecraft.commands.CommandNode
//import ru.cherryngine.lib.minecraft.commands.writeCommands
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//
//class ClientboundCommandsPacket(val commands: MutableMap<Int, CommandNode>) : ClientboundPacket() {
//
//    init {
//        buffer.writeCommands(commands)
//    }
//
//    fun clone(): ClientboundCommandsPacket = ClientboundCommandsPacket(commands.toMutableMap())
//}