package ru.cherryngine.lib.minecraft.protocol.types

import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class VillagerData(
    val type: Type,
    val profession: Profession,
    val level: Level,
) {
    companion object {
        val DEFAULT = VillagerData(Type.DESERT, Profession.NONE, Level.NOVICE)

        val STREAM_CODEC: StreamCodec<VillagerData> = StreamCodec.of(
            EnumStreamCodec<Type>(), VillagerData::type,
            EnumStreamCodec<Profession>(), VillagerData::profession,
            EnumStreamCodec<Level>(), VillagerData::level,
            ::VillagerData
        )
    }

    enum class Type(
        val identifier: String,
    ) {
        DESERT("minecraft:desert"),
        JUNGLE("minecraft:jungle"),
        PLAINS("minecraft:plains"),
        SAVANNA("minecraft:savanna"),
        SNOW("minecraft:snow"),
        SWAMP("minecraft:swamp"),
        TAIGA("minecraft:taiga")
    }

    enum class Profession {
        NONE,
        ARMORER,
        BUTCHER,
        CARTOGRAPHER,
        CLERIC,
        FARMER,
        FISHERMAN,
        FLETCHER,
        LEATHERWORKER,
        LIBRARIAN,
        MASON,
        NITWIT,
        SHEPHERD,
        TOOLSMITH,
        WEAPONSMITH;
    }

    enum class Level {
        NOVICE,
        APPRENTICE,
        JOURNEYMAN,
        EXPERT,
        MASTER;
    }
}
