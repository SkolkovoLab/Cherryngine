//package ru.cherryngine.lib.minecraft.data.components
//
//import ru.cherryngine.lib.minecraft.codec.transcoder.CRC32CTranscoder
//import ru.cherryngine.lib.minecraft.data.CRC32CHasher
//import ru.cherryngine.lib.minecraft.data.DataComponent
//import ru.cherryngine.lib.minecraft.data.HashHolder
//import ru.cherryngine.lib.minecraft.data.StaticHash
//import ru.cherryngine.lib.minecraft.protocol.NetworkReadable
//import ru.cherryngine.lib.minecraft.protocol.types.ConsumeEffect
//import ru.cherryngine.lib.minecraft.tide.codec.StructCodec
//import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
//import io.netty.buffer.ByteBuf
//
//data class DeathProtectionComponent(val deathEffects: List<ConsumeEffect>) : DataComponent() {
//
//    companion object : NetworkReadable<DeathProtectionComponent> {
//
//        val CODEC = StructCodec.of(
//            "death_effects", ConsumeEffect.CODEC.list().default(listOf()), DeathProtectionComponent::deathEffects,
//            ::DeathProtectionComponent
//        )
//
//        val STREAM_CODEC = StreamCodec.of(
//            ConsumeEffect.STREAM_CODEC.list(), DeathProtectionComponent::deathEffects,
//            ::DeathProtectionComponent
//        )
//
//        override fun read(buffer: ByteBuf): DeathProtectionComponent {
//            return STREAM_CODEC.read(buffer)
//        }
//    }
//
//    override fun write(buffer: ByteBuf) {
//        STREAM_CODEC.write(buffer, this)
//    }
//
//    override fun hashStruct(): HashHolder {
//        return CRC32CHasher.of {
//            return StaticHash(CODEC.encode(CRC32CTranscoder, this@DeathProtectionComponent))
//        }
//    }
//}