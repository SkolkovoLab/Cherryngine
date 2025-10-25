package io.github.dockyardmc

import io.github.dockyardmc.protocol.packets.registry.ClientboundPacketRegistry
import io.github.dockyardmc.protocol.packets.registry.ServerboundPacketRegistry
import io.github.dockyardmc.registry.RegistryManager
import io.github.dockyardmc.registry.registries.*
import io.github.dockyardmc.registry.registries.tags.*
import io.github.dockyardmc.server.NettyServer
import org.slf4j.LoggerFactory

class DockyardServer(
    ip: String,
    port: Int,
) {
    val logger = LoggerFactory.getLogger(DockyardServer::class.java)
    val nettyServer: NettyServer

    init {
//        SoundRegistry.initialize(RegistryManager.getStreamFromPath("registry/sound_registry.json.gz"))

        RegistryManager.register<Attribute>(AttributeRegistry)
        RegistryManager.register<RegistryBlock>(BlockRegistry)
        RegistryManager.register<EntityType>(EntityTypeRegistry)
        RegistryManager.register<DimensionType>(DimensionTypeRegistry)
        RegistryManager.register<BannerPattern>(BannerPatternRegistry)
        RegistryManager.register<DamageType>(DamageTypeRegistry)
        RegistryManager.register<JukeboxSong>(JukeboxSongRegistry)
        RegistryManager.register<TrimMaterial>(TrimMaterialRegistry)
        RegistryManager.register<TrimPattern>(TrimPatternRegistry)
        RegistryManager.register<ChatType>(ChatTypeRegistry)
        RegistryManager.register<Particle>(ParticleRegistry)
        RegistryManager.register<PaintingVariant>(PaintingVariantRegistry)
        RegistryManager.register<PotionEffect>(PotionEffectRegistry)
        RegistryManager.register<Biome>(BiomeRegistry)
        RegistryManager.register<Item>(ItemRegistry)
        RegistryManager.register<Fluid>(FluidRegistry)
        RegistryManager.register<PotionType>(PotionTypeRegistry)

        RegistryManager.register<WolfVariant>(WolfVariantRegistry)
        RegistryManager.register<WolfSoundVariant>(WolfSoundVariantRegistry)
        RegistryManager.register<CatVariant>(CatVariantRegistry)
        RegistryManager.register<CowVariant>(CowVariantRegistry)
        RegistryManager.register<PigVariant>(PigVariantRegistry)
        RegistryManager.register<FrogVariant>(FrogVariantRegistry)
        RegistryManager.register<ChickenVariant>(ChickenVariantRegistry)

        RegistryManager.register<Tag>(ItemTagRegistry)
        RegistryManager.register<Tag>(BlockTagRegistry)
        RegistryManager.register<Tag>(EntityTypeTagRegistry)
        RegistryManager.register<Tag>(FluidTagRegistry)
        RegistryManager.register<Tag>(BiomeTagRegistry)

        RegistryManager.register<DialogType>(DialogTypeRegistry)
        RegistryManager.register<DialogBodyType>(DialogBodyTypeRegistry)
        RegistryManager.register<DialogEntry>(DialogRegistry)
        RegistryManager.register<DialogInputType>(DialogInputTypeRegistry)
        RegistryManager.register<DialogActionType>(DialogActionTypeRegistry)

        val serverboundPacketRegistry = ServerboundPacketRegistry()
        val clientboundPacketRegistry = ClientboundPacketRegistry()

        nettyServer = NettyServer(
            ip,
            port,
            clientboundPacketRegistry,
            serverboundPacketRegistry,
        )
    }


    fun start(packetHandler: PacketHandler) {
        logger.info("Starting Server")
        nettyServer.start(packetHandler)
    }
}
