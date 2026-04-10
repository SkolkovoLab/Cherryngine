package ru.cherryngine.engine.minecraft.commandmanager.commands.args

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
