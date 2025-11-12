package ru.cherryngine.lib.grimac

import ac.grim.grimac.platform.api.PlatformPlugin

class PlatformPluginImpl : PlatformPlugin {
    override fun isEnabled(): Boolean = true
    override fun getName(): String = "Grim"
    override fun getVersion(): String = "dev"
}