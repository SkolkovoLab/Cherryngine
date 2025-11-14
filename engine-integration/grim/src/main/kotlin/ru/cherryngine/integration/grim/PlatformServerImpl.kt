package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.PlatformServer
import ac.grim.grimac.platform.api.sender.Sender

class PlatformServerImpl : PlatformServer {
    override fun getPlatformImplementationString(): String {
        return "aboba"
    }

    override fun dispatchCommand(sender: Sender, command: String) {
        TODO("Not yet implemented")
    }

    override fun getConsoleSender(): Sender {
        return ConsoleSenderImpl
    }

    override fun registerOutgoingPluginChannel(name: String) {
        TODO("Not yet implemented")
    }

    override fun getTPS(): Double {
        return 20.0 // FIXME заглушка
    }
}