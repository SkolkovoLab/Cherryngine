package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import net.minestom.server.FeatureFlag
import net.minestom.server.MinecraftServer
import net.minestom.server.gamedata.tags.TagManager
import net.minestom.server.network.packet.client.ClientPacket
import net.minestom.server.network.packet.client.configuration.ClientFinishConfigurationPacket
import net.minestom.server.network.packet.client.login.ClientLoginAcknowledgedPacket
import net.minestom.server.network.packet.client.login.ClientLoginStartPacket
import net.minestom.server.network.packet.client.status.StatusRequestPacket
import net.minestom.server.network.packet.server.CachedPacket
import net.minestom.server.network.packet.server.configuration.FinishConfigurationPacket
import net.minestom.server.network.packet.server.configuration.SelectKnownPacksPacket
import net.minestom.server.network.packet.server.configuration.UpdateEnabledFeaturesPacket
import net.minestom.server.network.packet.server.login.LoginSuccessPacket
import net.minestom.server.network.packet.server.status.ResponsePacket
import net.minestom.server.network.player.GameProfile
import net.minestom.server.registry.Registries
import org.intellij.lang.annotations.Language
import ru.cherryngine.engine.core.server.ClientConnection
import ru.cherryngine.engine.core.server.ClientPacketListener
import ru.cherryngine.engine.scenes.modules.client.ClientModule
import ru.cherryngine.engine.scenes.modules.client.FocusCamera
import ru.cherryngine.engine.scenes.modules.client.ThirdPersonController
import ru.cherryngine.engine.scenes.modules.physics.CuboidCollider
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3D

@Singleton
class DemoClientPacketListener(
    private val registries: Registries,
    private val tagManager: TagManager,
    private val demo: Demo,
) : ClientPacketListener {
    @SuppressWarnings("unstableApiUsage")
    val defaultTagsPacket: CachedPacket by lazy { CachedPacket(tagManager.packet(registries)) }

    data class StatusResponse(
        val versionName: String,
        val versionProtocol: Int,
        val playersMax: Int,
        val playersOnline: Int,
        val descriptionText: String,
        val enforcesSecureChat: Boolean,
        val previewsChat: Boolean,
    ) {
        @Language("JSON")
        fun toJson(): String {
            return """{"version":{"name":"$versionName","protocol":$versionProtocol},"players":{"max":$playersMax,"online":$playersOnline},"description":{"text":"$descriptionText"},"enforcesSecureChat":$enforcesSecureChat,"previewsChat":$previewsChat}"""
        }
    }

    private fun onStatusRequestPacket(clientConnection: ClientConnection, packet: StatusRequestPacket) {
        val jsonResponse = StatusResponse(
            MinecraftServer.VERSION_NAME,
            MinecraftServer.PROTOCOL_VERSION,
            1488,
            1337,
            "Cherryngine Demo",
            false,
            false
        ).toJson()
        clientConnection.sendPacket(ResponsePacket(jsonResponse))
    }

    private fun onClientLoginStartPacket(clientConnection: ClientConnection, packet: ClientLoginStartPacket) {
        clientConnection.sendPacket(LoginSuccessPacket(GameProfile(packet.profileId, packet.username)))
    }

    private fun onClientLoginAcknowledgedPacket(
        clientConnection: ClientConnection,
        packet: ClientLoginAcknowledgedPacket,
    ) {
        val excludeVanilla = true

        clientConnection.sendPacket(SelectKnownPacksPacket(listOf(SelectKnownPacksPacket.MINECRAFT_CORE)))

        val flags = listOf(
            FeatureFlag.REDSTONE_EXPERIMENTS,
            FeatureFlag.VANILLA,
            FeatureFlag.TRADE_REBALANCE,
            FeatureFlag.MINECART_IMPROVEMENTS
        )
        clientConnection.sendPacket(UpdateEnabledFeaturesPacket(flags.map(FeatureFlag::name)))

        sequenceOf(
            registries.chatType(),
            registries.dimensionType(),
            registries.biome(),
            registries.damageType(),
            registries.trimMaterial(),
            registries.trimPattern(),
            registries.bannerPattern(),
            registries.wolfVariant(),
            registries.enchantment(),
            registries.paintingVariant(),
            registries.jukeboxSong(),
            registries.instrument(),
        ).forEach { dynamicRegistry ->
            clientConnection.sendPacket(dynamicRegistry.registryDataPacket(registries, excludeVanilla))
        }

        clientConnection.sendPacket(defaultTagsPacket)

        clientConnection.sendPacket(FinishConfigurationPacket())
    }

    private fun onClientFinishConfigurationPacket(
        clientConnection: ClientConnection,
        packet: ClientFinishConfigurationPacket,
    ) {


        val player = demo.masterScene.createGameObject().apply {

            transform.translation = Vec3D(169.5, 73.5, 137.5)
            transform.scale = Vec3D(0.7, 1.8, 0.7)

            val camera = demo.masterScene.createGameObject()

            camera.transform.translation = Vec3D(169.5, 73.5, 137.5)

            //val clientModule = getOrCreateModule(ClientModule::class, clientConnection)
            val clientModule = camera.getOrCreateModule(ClientModule::class, clientConnection)
            val cameraModule = camera.getOrCreateModule(FocusCamera::class, clientModule, this)

            with(clientModule) {
                getOrCreateModule(ThirdPersonController::class, this, cameraModule)
                //getOrCreateModule(FirstPersonController::class, this)
                getOrCreateModule(Info::class, this)
                getOrCreateModule(Shooter::class, this)
                getOrCreateModule(Health::class, 100.0, this)
            }

            getOrCreateModule(PlayerModelRenderer::class)

            getOrCreateModule(
                CuboidCollider::class,
                Cuboid.fromTwoPoints(
                    transform.global.scale * Vec3D(-0.5, 0.0, -0.5),
                    transform.global.scale * Vec3D(0.5, 1.0, 0.5)
                )
            )
        }


//        demo.masterScene.createGameObject().apply {
//
//            transform.translation = Vec3D(169.5, 73.5, 137.5)
//            transform.scale = Vec3D(0.7, 1.8, 0.7)
//
//            val clientModule = getOrCreateModule(ClientModule::class, clientConnection)
//
//            with(clientModule) {
//                getOrCreateModule(FirstPersonController::class, this)
//                getOrCreateModule(Info::class, this)
//                getOrCreateModule(Shooter::class, this)
//            }
//
//            getOrCreateModule(PlayerModelRenderer::class)
//            getOrCreateModule(Health::class, 100.0)
//
//            getOrCreateModule(
//                CuboidCollider::class,
//                Cuboid.fromTwoPoints(
//                    transform.global.scale * Vec3D(-0.5, 0.0, -0.5),
//                    transform.global.scale * Vec3D(0.5, 1.0, 0.5)
//                )
//            )
//        }
    }

    override fun onPacketReceived(
        clientConnection: ClientConnection,
        packet: ClientPacket,
    ) {
        when (packet) {
            is StatusRequestPacket -> onStatusRequestPacket(clientConnection, packet)
            is ClientLoginStartPacket -> onClientLoginStartPacket(clientConnection, packet)
            is ClientLoginAcknowledgedPacket -> onClientLoginAcknowledgedPacket(clientConnection, packet)
            is ClientFinishConfigurationPacket -> onClientFinishConfigurationPacket(clientConnection, packet)
        }
    }

    override fun onDisconnect(clientConnection: ClientConnection) {
    }
}