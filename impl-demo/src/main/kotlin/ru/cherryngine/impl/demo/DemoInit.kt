package ru.cherryngine.impl.demo

import io.github.dockyardmc.DockyardServer
import io.github.dockyardmc.cherry.polar.PolarWorldGenerator
import io.github.dockyardmc.registry.DimensionTypes
import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.scene.Scene

@Singleton
class DemoInit(
    @Suppress("unused") dockyardServer: DockyardServer,
) {
    init {


        val packetHandler = TestPacketHandler(world)
        dockyardServer.start(packetHandler)
    }
}