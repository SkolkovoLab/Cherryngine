package ru.cherryngine.impl.demo

import io.github.dockyardmc.server.Connection

class Player(
    val connection: Connection,
    initScene: Scene,
) {
    var scene: Scene = initScene
        set(value) {
            field.removePlayer(this)
            field = value
            value.addPlayer(this)
        }

    init {
        initScene.addPlayer(this)
    }
}