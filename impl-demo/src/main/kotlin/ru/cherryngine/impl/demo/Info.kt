package ru.cherryngine.impl.demo

import io.micronaut.context.annotation.Parameter
import net.kyori.adventure.text.Component
import net.minestom.server.network.packet.server.play.SystemChatPacket
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.Scene
import ru.cherryngine.engine.scenes.event.Event
import ru.cherryngine.engine.scenes.modules.client.ClientModule

@ModulePrototype
class Info(
    @Parameter override val gameObject: GameObject,
    @Parameter val clientModule: ClientModule
) : Module {

    override fun onEvent(event: Event) {
        when (event) {
            is Scene.Events.Tick.Start -> {
                val tps = (1000 / scene.tickElapsedMils.toDouble()).coerceAtMost(20.0)
                val mspt = scene.tickElapsedMils
                val tpsFormated = String.format("%.1f", tps)
                var resultString = "$tpsFormated tps | $mspt mspt"
                gameObject.getModule(Health::class)?.let {
                    resultString += " | ${it.health.toInt()}/${it.maxHealth.toInt()} hp"
                }
                clientModule.connection.sendPacket(SystemChatPacket(Component.text(resultString), true))
            }
        }
    }

}
