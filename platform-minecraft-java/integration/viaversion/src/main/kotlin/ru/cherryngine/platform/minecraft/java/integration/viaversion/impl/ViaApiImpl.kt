package ru.cherryngine.platform.minecraft.java.integration.viaversion.impl

import com.viaversion.viaversion.ViaAPIBase
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion
import io.netty.buffer.ByteBuf
import ru.cherryngine.platform.minecraft.java.network.Connection

class ViaApiImpl : ViaAPIBase<Connection>() {
    override fun getPlayerProtocolVersion(player: Connection): ProtocolVersion {
        return ProtocolVersion.getProtocol(player.protocolVersion)
    }

    override fun sendRawPacket(player: Connection, buffer: ByteBuf) {
        sendRawPacket(player.gameProfile.uuid, buffer)
    }
}