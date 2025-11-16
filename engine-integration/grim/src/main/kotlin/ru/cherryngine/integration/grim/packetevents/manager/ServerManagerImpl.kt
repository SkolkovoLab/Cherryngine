package ru.cherryngine.integration.grim.packetevents.manager

import com.github.retrooper.packetevents.manager.server.ServerManager
import com.github.retrooper.packetevents.manager.server.ServerVersion
import com.github.retrooper.packetevents.protocol.player.ClientVersion
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder
import ru.cherryngine.lib.minecraft.ServerConsts

class ServerManagerImpl : ServerManager {
    private val serverVersion: ServerVersion by lazy {
        ServerVersion.entries.last { it.protocolVersion == ServerConsts.PROTOCOL_VERSION }
    }

    override fun getVersion() = serverVersion

    override fun getRegistryCacheKey(user: User, version: ClientVersion): Any? {
        return GlobalRegistryHolder.getGlobalRegistryCacheKey(user, version)
    }
}
