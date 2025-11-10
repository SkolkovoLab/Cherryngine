package ru.cherryngine.impl.demo.ecs

import com.github.quillraven.fleks.WorldConfiguration
import com.github.quillraven.fleks.configureWorld
import kotlin.time.Duration.Companion.milliseconds

class GameScene(
    cfg: WorldConfiguration.() -> Unit,
) {
    val tickDuration = 50.milliseconds
    val ticker = StableTicker(tickDuration, ::tick)

    val fleksWorld: FleksWorld = configureWorld(cfg = cfg)

    fun start() {
        ticker.start()
    }

    fun tick(tickIndex: Long, tickStartMs: Long) {
        fleksWorld.update(tickDuration)
    }
}