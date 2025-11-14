package ru.cherryngine.engine.core.commandmanager.commands.args

data class LocationCoordinate(
    val type: Type,
    val coordinate: Double
) {
    enum class Type {
        ABSOLUTE,
        RELATIVE,
        LOCAL
    }
}
