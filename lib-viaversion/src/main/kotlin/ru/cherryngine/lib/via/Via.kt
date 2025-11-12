package ru.cherryngine.lib.via

import com.viaversion.viabackwards.api.ViaBackwardsConfig
import com.viaversion.viaversion.ViaManagerImpl
import com.viaversion.viaversion.api.Via
import com.viaversion.viaversion.api.configuration.ViaVersionConfig
import ru.cherryngine.lib.minecraft.MinecraftServer

fun initViaVersion(
    minecraftServer: MinecraftServer,
    viaVersionConfig: ViaVersionConfig?,
    viaBackwardsConfig: ViaBackwardsConfig?
) {
    val viaVersionConfig = viaVersionConfig ?: ViaConfigImpl()
    viaVersionConfig.reload()
    val api = ViaApiImpl()
    val platform = ViaPlatformImpl(viaVersionConfig, api)
    val injector = ViaInjectorImpl(minecraftServer)
    val loader = ViaLoaderImpl()
    val backwardsPlatform = ViaBackwardsPlatformImpl(viaBackwardsConfig)

    val viaManager = ViaManagerImpl.builder()
        .platform(platform)
        .injector(injector)
        .loader(loader)
        .build()
    Via.init(viaManager)
    viaManager.init()
    backwardsPlatform.init(null)
    viaManager.onServerLoaded()
    backwardsPlatform.enable()
}