//package ru.cherryngine.lib.minecraft.data.components
//
//import ru.cherryngine.lib.minecraft.data.CRC32CHasher
//import ru.cherryngine.lib.minecraft.data.DataComponent
//import ru.cherryngine.lib.minecraft.data.HashHolder
//import ru.cherryngine.lib.minecraft.data.StaticHash
//import ru.cherryngine.lib.minecraft.extentions.*
//import ru.cherryngine.lib.minecraft.network.protocol.DataComponentHashable
//import ru.cherryngine.lib.minecraft.network.protocol.NetworkReadable
//import ru.cherryngine.lib.minecraft.network.protocol.NetworkWritable
//import ru.cherryngine.lib.minecraft.network.protocol.types.writeList
//import ru.cherryngine.lib.minecraft.registry.entries.PotionEffect
//import ru.cherryngine.lib.minecraft.registry.registries.PotionEffectRegistry
//import ru.cherryngine.lib.minecraft.scheduler.runnables.inWholeMinecraftTicks
//import ru.cherryngine.lib.minecraft.scheduler.runnables.ticks
//import io.netty.buffer.ByteBuf
//import kotlin.time.Duration
//import kotlin.time.Duration.Companion.seconds
//
//class SuspiciousStewEffectsComponent(val effects: List<Effect>) : DataComponent() {
//
//    override fun write(buffer: ByteBuf) {
//        buffer.writeList(effects, Effect::write)
//    }
//
//    override fun hashStruct(): HashHolder {
//        return StaticHash(CRC32CHasher.ofList(effects.map { effect -> effect.hashStruct().getHashed() }))
//    }
//
//    data class Effect(val potionEffect: PotionEffect, val duration: Duration) : DataComponentHashable, NetworkWritable {
//
//        companion object : NetworkReadable<Effect> {
//            val DEFAULT_DURATION = 8.seconds
//
//            override fun read(buffer: ByteBuf): Effect {
//                return Effect(buffer.readRegistryEntry(PotionEffectRegistry), buffer.readVarInt().ticks)
//            }
//        }
//
//        override fun hashStruct(): HashHolder {
//            return CRC32CHasher.of {
//                static("id", CRC32CHasher.ofRegistryEntry(potionEffect))
//                default("duration", DEFAULT_DURATION.inWholeMinecraftTicks, duration.inWholeMinecraftTicks, CRC32CHasher::ofInt)
//            }
//        }
//
//        override fun write(buffer: ByteBuf) {
//            buffer.writeRegistryEntry(potionEffect)
//            buffer.writeVarInt(duration.inWholeMinecraftTicks)
//        }
//    }
//
//    companion object : NetworkReadable<SuspiciousStewEffectsComponent> {
//        override fun read(buffer: ByteBuf): SuspiciousStewEffectsComponent {
//            return SuspiciousStewEffectsComponent(buffer.readList(Effect::read))
//        }
//    }
//}