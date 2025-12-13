package ru.cherryngine.lib.minecraft.registry.registries.envattr

import net.kyori.adventure.util.TriState
import ru.cherryngine.lib.minecraft.r2.Registries
import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.utils.color.ARGBLikeImpl
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl
import ru.cherryngine.lib.minecraft.utils.color.rgbLikeOf

object EnvironmentAttributeRegistry : DynamicRegistry<EnvironmentAttribute<*>>("minecraft:environment_attribute") {
    init {
        register("visual/fog_color", EnvironmentAttributeType.RGB_COLOR, RGBLikeImpl.BLACK)
        register("visual/fog_start_distance", EnvironmentAttributeType.FLOAT, 0f)
        register("visual/fog_end_distance", EnvironmentAttributeType.FLOAT, 1024f)
        register("visual/sky_fog_end_distance", EnvironmentAttributeType.FLOAT, 512f)
        register("visual/cloud_fog_end_distance", EnvironmentAttributeType.FLOAT, 2048f)
        register("visual/water_fog_color", EnvironmentAttributeType.RGB_COLOR, rgbLikeOf(0x050533))
        register("visual/water_fog_start_distance", EnvironmentAttributeType.FLOAT, -8f)
        register("visual/water_fog_end_distance", EnvironmentAttributeType.FLOAT, 96f)
        register("visual/sky_color", EnvironmentAttributeType.RGB_COLOR, RGBLikeImpl.BLACK)
        register("visual/sunrise_sunset_color", EnvironmentAttributeType.ARGB_COLOR, ARGBLikeImpl.TRANSPARENT)
        register("visual/cloud_color", EnvironmentAttributeType.ARGB_COLOR, ARGBLikeImpl.TRANSPARENT)
        register("visual/cloud_height", EnvironmentAttributeType.FLOAT, 192.33f)
        register("visual/sun_angle", EnvironmentAttributeType.ANGLE_DEGREES, 0f)
        register("visual/moon_angle", EnvironmentAttributeType.ANGLE_DEGREES, 0f)
        register("visual/star_angle", EnvironmentAttributeType.ANGLE_DEGREES, 0f)
        register("visual/moon_phase", EnvironmentAttributeType.MOON_PHASE, MoonPhase.FULL_MOON)
        register("visual/star_brightness", EnvironmentAttributeType.FLOAT, 0f)
        register("visual/sky_light_color", EnvironmentAttributeType.RGB_COLOR, RGBLikeImpl.WHITE)
        register("visual/sky_light_factor", EnvironmentAttributeType.FLOAT, 1f)
        register("visual/default_dripstone_particle", EnvironmentAttributeType.PARTICLE, Registries.particle["dripping_dripstone_water"])
        register("visual/ambient_particles", EnvironmentAttributeType.AMBIENT_PARTICLES, emptyList())
        register("audio/background_music", EnvironmentAttributeType.BACKGROUND_MUSIC, BackgroundMusic.EMPTY)
        register("audio/music_volume", EnvironmentAttributeType.FLOAT, 1f)
        register("audio/ambient_sounds", EnvironmentAttributeType.AMBIENT_SOUNDS, AmbientSounds.EMPTY)
        register("audio/firefly_bush_sounds", EnvironmentAttributeType.BOOLEAN, false)
        register("gameplay/sky_light_level", EnvironmentAttributeType.FLOAT, 15f)
        register("gameplay/can_start_raid", EnvironmentAttributeType.BOOLEAN, true)
        register("gameplay/water_evaporates", EnvironmentAttributeType.BOOLEAN, false)
        register("gameplay/bed_rule", EnvironmentAttributeType.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK)
        register("gameplay/respawn_anchor_works", EnvironmentAttributeType.BOOLEAN, false)
        register("gameplay/nether_portal_spawns_piglin", EnvironmentAttributeType.BOOLEAN, false)
        register("gameplay/fast_lava", EnvironmentAttributeType.BOOLEAN, false)
        register("gameplay/increased_fire_burnout", EnvironmentAttributeType.BOOLEAN, false)
        register("gameplay/eyeblossom_open", EnvironmentAttributeType.TRI_STATE, TriState.NOT_SET)
        register("gameplay/turtle_egg_hatch_chance", EnvironmentAttributeType.FLOAT, 0f)
        register("gameplay/piglins_zombify", EnvironmentAttributeType.BOOLEAN, true)
        register("gameplay/snow_golem_melts", EnvironmentAttributeType.BOOLEAN, false)
        register("gameplay/creaking_active", EnvironmentAttributeType.BOOLEAN, false)
        register("gameplay/surface_slime_spawn_chance", EnvironmentAttributeType.FLOAT, 0f)
        register("gameplay/cat_waking_up_gift_chance", EnvironmentAttributeType.FLOAT, 0f)
        register("gameplay/bees_stay_in_hive", EnvironmentAttributeType.BOOLEAN, false)
        register("gameplay/monsters_burn", EnvironmentAttributeType.BOOLEAN, false)
        register("gameplay/can_pillager_patrol_spawn", EnvironmentAttributeType.BOOLEAN, true)
        register("gameplay/villager_activity", EnvironmentAttributeType.ACTIVITY, EntityActivity.IDLE)
        register("gameplay/baby_villager_activity", EnvironmentAttributeType.ACTIVITY, EntityActivity.IDLE)
    }

    fun <T> register(identifier: String, type: EnvironmentAttributeType<T>, default: T) {
        addEntry(EnvironmentAttribute("minecraft:$identifier", type, default))
    }
}
