package ru.cherryngine.integration.viaversion

import com.viaversion.viaversion.ViaManagerImpl
import com.viaversion.viaversion.api.Via
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.MinecraftServer
import ru.cherryngine.lib.viaversion.*

@Singleton
class ViaVersionRunner(
    val minecraftServer: MinecraftServer,
    val viaVersionConfig: MicronautViaVersionConfig,
    val viaBackwardsConfig: MicronautViaBackwardsConfig,
) {
    @PostConstruct
    fun init() {
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
}