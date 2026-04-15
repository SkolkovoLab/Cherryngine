package ru.cherryngine.engine.core.player

interface PlayerRouter {
    fun getInitialInstance(player: Player): String
}
