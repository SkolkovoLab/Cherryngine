//package ru.cherryngine.lib.minecraft.data.components
//
//import ru.cherryngine.lib.minecraft.data.CRC32CHasher
//import ru.cherryngine.lib.minecraft.data.DataComponent
//import ru.cherryngine.lib.minecraft.data.HashHolder
//import ru.cherryngine.lib.minecraft.effects.AppliedPotionEffect
//import ru.cherryngine.lib.minecraft.extentions.*
//import ru.cherryngine.lib.minecraft.protocol.NetworkReadable
//import ru.cherryngine.lib.minecraft.protocol.readOptional
//import ru.cherryngine.lib.minecraft.protocol.writeOptional
//import ru.cherryngine.lib.minecraft.registry.registries.PotionType
//import ru.cherryngine.lib.minecraft.registry.registries.PotionTypeRegistry
//import ru.cherryngine.lib.minecraft.scroll.CustomColor
//import io.netty.buffer.ByteBuf
//
//class PotionContentsComponent(
//    val potion: PotionType?,
//    val customColor: CustomColor?,
//    val effects: List<AppliedPotionEffect>,
//    val customName: String?
//) : DataComponent() {
//
//    override fun hashStruct(): HashHolder {
//        return CRC32CHasher.of {
//            optional("potion", potion, CRC32CHasher::ofRegistryEntry)
//            optional("custom_color", customColor, CRC32CHasher::ofColor)
//            defaultStructList("custom_effects", effects, listOf(), AppliedPotionEffect::hashStruct)
//            optional("custom_name", customName, CRC32CHasher::ofString)
//        }
//    }
//
//    override fun write(buffer: ByteBuf) {
//        buffer.writeOptional(potion?.getProtocolId(), ByteBuf::writeVarInt)
//        buffer.writeOptional(customColor, CustomColor::writePackedInt)
//        AppliedPotionEffect.STREAM_CODEC.list().write(buffer, effects)
//        buffer.writeOptional(customName, ByteBuf::writeString)
//    }
//
//    companion object : NetworkReadable<PotionContentsComponent> {
//        override fun read(buffer: ByteBuf): PotionContentsComponent {
//            return PotionContentsComponent(
//                buffer.readOptional(ByteBuf::readVarInt)?.let { PotionTypeRegistry.getByProtocolId(it) },
//                buffer.readOptional(ByteBuf::readInt)?.let { CustomColor.fromRGBInt(it) },
//                AppliedPotionEffect.STREAM_CODEC.list().read(buffer),
//                buffer.readOptional(ByteBuf::readString)
//            )
//        }
//    }
//}