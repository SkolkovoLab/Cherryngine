package ru.cherryngine.lib.minecraft.entity

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.EndBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.math.rotation.QRot
import ru.cherryngine.lib.minecraft.codec.ComponentCodecs
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.protocol.types.*
import ru.cherryngine.lib.minecraft.registry.*
import ru.cherryngine.lib.minecraft.registry.registries.*
import ru.cherryngine.lib.minecraft.tide.stream.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.block.Block
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object Metadata {
    fun byte(value: Byte): Entry<Byte> =
        Entry(TYPE_BYTE, value, StreamCodec.BYTE)

    fun varInt(value: Int): Entry<Int> =
        Entry(TYPE_VAR_INT, value, StreamCodec.VAR_INT)

    fun varLong(value: Long): Entry<Long> =
        Entry(TYPE_VAR_LONG, value, StreamCodec.VAR_LONG)

    fun float(value: Float): Entry<Float> =
        Entry(TYPE_FLOAT, value, StreamCodec.FLOAT)

    fun string(value: String): Entry<String> =
        Entry(TYPE_STRING, value, StreamCodec.STRING)

    fun chat(value: Component): Entry<Component> =
        Entry(TYPE_CHAT, value, ComponentCodecs.NBT)

    fun optChat(value: Component?): Entry<Component?> =
        Entry(TYPE_OPT_CHAT, value, ComponentCodecs.NBT.optional())

    fun itemStack(value: ItemStack): Entry<ItemStack> =
        Entry(TYPE_ITEM_STACK, value, ItemStack.STREAM_CODEC)

    fun boolean(value: Boolean): Entry<Boolean> =
        Entry(TYPE_BOOLEAN, value, StreamCodec.BOOLEAN)

    fun rotation(value: Vec3D): Entry<Vec3D> =
        Entry(TYPE_ROTATION, value, LocationCodecs.VEC_3D)

    fun blockPosition(value: Vec3I): Entry<Vec3I> =
        Entry(TYPE_BLOCK_POSITION, value, LocationCodecs.BLOCK_POSITION)

    fun optBlockPosition(value: Vec3I?): Entry<Vec3I?> =
        Entry(TYPE_OPT_BLOCK_POSITION, value, LocationCodecs.BLOCK_POSITION.optional())

    fun direction(value: Direction): Entry<Direction> =
        Entry(TYPE_DIRECTION, value, EnumStreamCodec<Direction>())

    fun optUuid(value: UUID?): Entry<UUID?> =
        Entry(TYPE_OPT_UUID, value, StreamCodec.UUID.optional())

    fun blockState(value: Block): Entry<Block> =
        Entry(TYPE_BLOCKSTATE, value, Block.STREAM_CODEC)

    fun optBlockState(value: Block): Entry<Block> =
        Entry(TYPE_OPT_BLOCKSTATE, value, Block.STREAM_CODEC)

    fun nbt(nbt: BinaryTag): Entry<BinaryTag> =
        Entry(TYPE_NBT, nbt, StreamCodecNBT.STREAM)

    fun particle(particle: Particle): Entry<Particle> =
        Entry(TYPE_PARTICLE, particle, ParticleRegistry.STREAM_CODEC)

    fun particleList(particles: List<Particle>): Entry<List<Particle>> =
        Entry(TYPE_PARTICLE_LIST, particles, ParticleRegistry.STREAM_CODEC.list())

    fun villagerData(data: VillagerData): Entry<VillagerData> =
        Entry(TYPE_VILLAGERDATA, data, VillagerData.STREAM_CODEC)

    fun optVarInt(value: Int?): Entry<Int?> =
        Entry(TYPE_OPT_VARINT, value, object : StreamCodec<Int?> {
            override fun write(buffer: ByteBuf, value: Int?) {
                StreamCodec.VAR_INT.write(buffer, value?.let { it + 1 } ?: 0)
            }

            override fun read(buffer: ByteBuf): Int? {
                val value = StreamCodec.VAR_INT.read(buffer)
                return if (value == 0) null else value - 1
            }
        })

    fun pose(value: EntityPose): Entry<EntityPose> =
        Entry(TYPE_POSE, value, EnumStreamCodec<EntityPose>())

    fun catVariant(value: CatVariant): Entry<CatVariant> =
        Entry(TYPE_CAT_VARIANT, value, CatVariantRegistry.STREAM_CODEC)

    fun cowVariant(value: CowVariant): Entry<CowVariant> =
        Entry(TYPE_COW_VARIANT, value, CowVariantRegistry.STREAM_CODEC)

    fun wolfVariant(value: WolfVariant): Entry<WolfVariant> =
        Entry(TYPE_WOLF_VARIANT, value, WolfVariantRegistry.STREAM_CODEC)

    fun wolfSoundVariant(value: WolfSoundVariant): Entry<WolfSoundVariant> =
        Entry(TYPE_WOLF_SOUND_VARIANT, value, WolfSoundVariantRegistry.STREAM_CODEC)

    fun frogVariant(value: FrogVariant): Entry<FrogVariant> =
        Entry(TYPE_FROG_VARIANT, value, FrogVariantRegistry.STREAM_CODEC)

    fun pigVariant(value: PigVariant): Entry<PigVariant> =
        Entry(TYPE_PIG_VARIANT, value, PigVariantRegistry.STREAM_CODEC)

    fun chickenVariant(value: ChickenVariant): Entry<ChickenVariant> =
        Entry(TYPE_CHICKEN_VARIANT, value, ChickenVariantRegistry.STREAM_CODEC)

    fun paintingVariant(value: PaintingVariant): Entry<PaintingVariant> =
        Entry(TYPE_PAINTING_VARIANT, value, PaintingVariantRegistry.STREAM_CODEC)

    fun snifferState(value: SnifferState): Entry<SnifferState> =
        Entry(TYPE_SNIFFER_STATE, value, EnumStreamCodec<SnifferState>())

    fun armadilloState(value: ArmadilloState): Entry<ArmadilloState> =
        Entry(TYPE_ARMADILLO_STATE, value, EnumStreamCodec<ArmadilloState>())

    fun vector3(value: Vec3D): Entry<Vec3D> =
        Entry(TYPE_VECTOR3, value, LocationCodecs.VEC_3D)

    fun quaternion(value: QRot): Entry<QRot> =
        Entry(TYPE_QUATERNION, value, LocationCodecs.QUATERNION)

    // custom types

    inline fun <reified T : Enum<T>> enum(value: T): Entry<T> =
        Entry(TYPE_VAR_INT, value, EnumStreamCodec<T>())

    inline fun <reified T : Enum<T>> byteEnum(value: T): Entry<T> =
        Entry(TYPE_BYTE, value, ByteEnumStreamCodec<T>())

    fun creeperState(value: CreeperState): Entry<CreeperState> =
        Entry(TYPE_VAR_INT, value, CreeperState.STREAM_CODEC)

    fun shulkerColor(value: DyeColor?): Entry<DyeColor?> =
        Entry(
            TYPE_BYTE, value, StreamCodec.BYTE.transform(
                { DyeColor.entries.getOrNull(it.toInt()) },
                { (it?.ordinal ?: 16).toByte() })
        )

    fun displayedSkinParts(value: DisplayedSkinParts): Entry<DisplayedSkinParts> =
        Entry(TYPE_BYTE, value, DisplayedSkinParts.STREAM_CODEC)

    // Type constants
    private val NEXT_ID = AtomicInteger(0)

    val TYPE_BYTE: Int = NEXT_ID.getAndIncrement()
    val TYPE_VAR_INT: Int = NEXT_ID.getAndIncrement()
    val TYPE_VAR_LONG: Int = NEXT_ID.getAndIncrement()
    val TYPE_FLOAT: Int = NEXT_ID.getAndIncrement()
    val TYPE_STRING: Int = NEXT_ID.getAndIncrement()
    val TYPE_CHAT: Int = NEXT_ID.getAndIncrement()
    val TYPE_OPT_CHAT: Int = NEXT_ID.getAndIncrement()
    val TYPE_ITEM_STACK: Int = NEXT_ID.getAndIncrement()
    val TYPE_BOOLEAN: Int = NEXT_ID.getAndIncrement()
    val TYPE_ROTATION: Int = NEXT_ID.getAndIncrement()
    val TYPE_BLOCK_POSITION: Int = NEXT_ID.getAndIncrement()
    val TYPE_OPT_BLOCK_POSITION: Int = NEXT_ID.getAndIncrement()
    val TYPE_DIRECTION: Int = NEXT_ID.getAndIncrement()
    val TYPE_OPT_UUID: Int = NEXT_ID.getAndIncrement()
    val TYPE_BLOCKSTATE: Int = NEXT_ID.getAndIncrement()
    val TYPE_OPT_BLOCKSTATE: Int = NEXT_ID.getAndIncrement()
    val TYPE_NBT: Int = NEXT_ID.getAndIncrement()
    val TYPE_PARTICLE: Int = NEXT_ID.getAndIncrement()
    val TYPE_PARTICLE_LIST: Int = NEXT_ID.getAndIncrement()
    val TYPE_VILLAGERDATA: Int = NEXT_ID.getAndIncrement()
    val TYPE_OPT_VARINT: Int = NEXT_ID.getAndIncrement()
    val TYPE_POSE: Int = NEXT_ID.getAndIncrement()
    val TYPE_CAT_VARIANT: Int = NEXT_ID.getAndIncrement()
    val TYPE_COW_VARIANT: Int = NEXT_ID.getAndIncrement()
    val TYPE_WOLF_VARIANT: Int = NEXT_ID.getAndIncrement()
    val TYPE_WOLF_SOUND_VARIANT: Int = NEXT_ID.getAndIncrement()
    val TYPE_FROG_VARIANT: Int = NEXT_ID.getAndIncrement()
    val TYPE_PIG_VARIANT: Int = NEXT_ID.getAndIncrement()
    val TYPE_CHICKEN_VARIANT: Int = NEXT_ID.getAndIncrement()
    val TYPE_OPT_GLOBAL_POSITION: Int = NEXT_ID.getAndIncrement() // Unused by protocol it seems
    val TYPE_PAINTING_VARIANT: Int = NEXT_ID.getAndIncrement()
    val TYPE_SNIFFER_STATE: Int = NEXT_ID.getAndIncrement()
    val TYPE_ARMADILLO_STATE: Int = NEXT_ID.getAndIncrement()
    val TYPE_VECTOR3: Int = NEXT_ID.getAndIncrement()
    val TYPE_QUATERNION: Int = NEXT_ID.getAndIncrement()

    val EMPTY_VALUES = mapOf(
        TYPE_BYTE to byte(0),
        TYPE_VAR_INT to varInt(0),
        TYPE_VAR_LONG to varLong(0L),
        TYPE_FLOAT to float(0f),
        TYPE_STRING to string(""),
        TYPE_CHAT to chat(Component.empty()),
        TYPE_OPT_CHAT to optChat(null),
        TYPE_ITEM_STACK to itemStack(ItemStack.AIR),
        TYPE_BOOLEAN to boolean(false),
        TYPE_ROTATION to rotation(Vec3D.ZERO),
        TYPE_BLOCK_POSITION to blockPosition(Vec3I.ZERO),
        TYPE_OPT_BLOCK_POSITION to optBlockPosition(null),
        TYPE_DIRECTION to direction(Direction.DOWN),
        TYPE_OPT_UUID to optUuid(null),
        TYPE_BLOCKSTATE to blockState(Block.AIR),
        TYPE_OPT_BLOCKSTATE to optBlockState(Block.AIR),
        TYPE_NBT to nbt(EndBinaryTag.endBinaryTag()),
        TYPE_PARTICLE to particle(Particles.DUST),
        TYPE_PARTICLE_LIST to particleList(emptyList()),
        TYPE_VILLAGERDATA to villagerData(VillagerData.DEFAULT),
        TYPE_OPT_VARINT to optVarInt(null),
        TYPE_POSE to pose(EntityPose.STANDING),
        TYPE_CAT_VARIANT to catVariant(CatVariants.TABBY),
        TYPE_WOLF_VARIANT to wolfVariant(WolfVariants.PALE),
        TYPE_FROG_VARIANT to frogVariant(FrogVariants.TEMPERATE),
        // OptGlobalPos
        TYPE_PAINTING_VARIANT to paintingVariant(PaintingVariants.KEBAB),
        TYPE_SNIFFER_STATE to snifferState(SnifferState.IDLING),
        TYPE_ARMADILLO_STATE to armadilloState(ArmadilloState.IDLE),
        TYPE_VECTOR3 to vector3(Vec3D.ZERO),
        TYPE_QUATERNION to quaternion(QRot.IDENTITY),
    )

    @Suppress("UNCHECKED_CAST")
    class Entry<T>(
        val type: Int,
        val value: T,
        val serializer: StreamCodec<T>,
    ) {
        companion object {
            val STREAM_CODEC: StreamCodec<Entry<*>> = object : StreamCodec<Entry<*>> {
                override fun write(buffer: ByteBuf, value: Entry<*>) {
                    StreamCodec.VAR_INT.write(buffer, value.type)
                    (value.serializer as StreamCodec<Any?>).write(buffer, value.value)
                }

                override fun read(buffer: ByteBuf): Entry<*> {
                    val type = StreamCodec.VAR_INT.read(buffer)
                    val value = EMPTY_VALUES[type] ?: throw UnsupportedOperationException("Unknown value type: $type")
                    return Entry(type, value.serializer.read(buffer), value.serializer as StreamCodec<Any?>)
                }
            }
        }
    }
}