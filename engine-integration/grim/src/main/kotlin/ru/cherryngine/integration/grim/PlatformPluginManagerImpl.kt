package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.PlatformPlugin
import ac.grim.grimac.platform.api.manager.PlatformPluginManager

class PlatformPluginManagerImpl : PlatformPluginManager {
    override fun getPlugins(): Array<out PlatformPlugin> {
        return arrayOf()
    }

    override fun getPlugin(pluginName: String?): PlatformPlugin? {
        return null
    }
}