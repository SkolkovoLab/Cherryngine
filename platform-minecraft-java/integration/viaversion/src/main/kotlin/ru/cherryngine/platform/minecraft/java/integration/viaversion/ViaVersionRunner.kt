package ru.cherryngine.platform.minecraft.java.integration.viaversion

import com.viaversion.viaversion.ViaManagerImpl
import com.viaversion.viaversion.api.Via
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
import ru.cherryngine.platform.minecraft.java.integration.viaversion.impl.*
import ru.cherryngine.platform.minecraft.java.network.NettyServer

@Singleton
class ViaVersionRunner(
    val nettyServer: NettyServer,
    val viaVersionConfig: MicronautViaVersionConfig,
    val viaBackwardsConfig: MicronautViaBackwardsConfig,
) {
    @PostConstruct
    fun init() {
        val api = ViaApiImpl()
        val platform = ViaPlatformImpl(viaVersionConfig, api)
        val injector = ViaInjectorImpl(nettyServer)
        val loader = ViaLoaderImpl()
        val backwardsPlatform = ViaBackwardsPlatformImpl()

        val viaManager = ViaManagerImpl.builder()
            .platform(platform)
            .injector(injector)
            .loader(loader)
            .build()
        Via.init(viaManager)
        viaManager.init()
        backwardsPlatform.init(viaBackwardsConfig)
        viaManager.onServerLoaded()
        backwardsPlatform.enable()
    }
}