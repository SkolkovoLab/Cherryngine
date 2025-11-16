package ru.cherryngine.integration.grim.manager

import ac.grim.grimac.platform.api.PlatformPlugin
import ac.grim.grimac.platform.api.manager.PlatformPluginManager
import jakarta.inject.Singleton

@Singleton
class PlatformPluginManagerImpl : PlatformPluginManager {
    override fun getPlugins(): Array<out PlatformPlugin> {
        return arrayOf()
    }

    override fun getPlugin(pluginName: String?): PlatformPlugin? {
        return null
    }
}