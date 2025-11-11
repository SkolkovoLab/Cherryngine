package ru.cherryngine.lib.via

import com.viaversion.viaversion.ViaManagerImpl
import com.viaversion.viaversion.api.Via
import ru.cherryngine.lib.minecraft.MinecraftServer
import java.io.File

fun initVia(minecraftServer: MinecraftServer) {
    val config = ViaConfigImpl().apply { reload() }
    val api = ViaApiImpl()
    val platform = ViaPlatformImpl(config, api)
    val injector = ViaInjectorImpl(minecraftServer)
    val loader = ViaLoaderImpl()
    val backwardsPlatform = ViaBackwardsPlatformImpl()

    val viaManager = ViaManagerImpl.builder()
        .platform(platform)
        .injector(injector)
        .loader(loader)
        .build()
    Via.init(viaManager)
    viaManager.init()
    backwardsPlatform.init(File("./via/backwards_config.yml"))
    viaManager.onServerLoaded()
    backwardsPlatform.enable()
}