package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class RabbitMeta : AgeableMobMeta() {
    companion object : RabbitMeta()

    val TYPE = index(
        MetadataEntry.Type.VAR_INT,
        Variant.BROWN,
        Variant.Companion::fromId,
        Variant::id
    )

    enum class Variant(
        val id: Int,
    ) {
        BROWN(0),
        WHITE(1),
        BLACK(2),
        WHITE_SPLOTCHED(3),
        GOLD(4),
        SALT(5),
        EVIL(99);

        companion object {
            private val BY_ID = Variant.entries.associateBy({ it.id }, { it })
            fun fromId(id: Int): Variant = BY_ID[id]!!
        }
    }
}