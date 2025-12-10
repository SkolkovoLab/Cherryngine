package ru.cherryngine.lib.minecraft.entity

sealed class CopperGolemMeta : MobMeta() {
    companion object : CopperGolemMeta()

    val WEATHER_STATE = index(MetadataEntry.Type.WEATHER_STATE, WeatherState.UNAFFECTED)
    val STATE = index(MetadataEntry.Type.COPPER_GOLEM_STATE, State.IDLE)

    enum class State {
        IDLE,
        GETTING_ITEM,
        GETTING_NO_ITEM,
        DROPPING_ITEM,
        DROPPING_NO_ITEM;
    }

    enum class WeatherState {
        UNAFFECTED,
        EXPOSED,
        WEATHERED,
        OXIDIZED;
    }
}