package ru.cherryngine.engine.mcprotocollib

import io.netty.buffer.Unpooled
import org.geysermc.mcprotocollib.network.Session
import org.geysermc.mcprotocollib.network.event.session.PacketSendingEvent
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundFinishConfigurationPacket
import org.geysermc.mcprotocollib.protocol.packet.configuration.clientbound.ClientboundRegistryDataPacket
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.TagRegistry
import ru.cherryngine.lib.minecraft.utils.registry.DataDrivenRegistry
import org.geysermc.mcprotocollib.protocol.packet.common.clientbound.ClientboundUpdateTagsPacket as McplUpdateTagsPacket

/**
 * Intercepts MCProtocolLib's default configuration-phase packets and replaces them
 * with the engine's registry data. MCProtocolLib's built-in ServerListener sends
 * registries from its embedded networkCodec.nbt which may not match the client version.
 * The engine's Registries contain the correct data.
 */
class McProtocolLibRegistryInterceptor : SessionAdapter() {
    private val log = LoggerFactory.getLogger(McProtocolLibRegistryInterceptor::class.java)

    @Volatile
    private var sendingOwnData = false

    @Volatile
    private var configDone = false

    override fun packetSending(event: PacketSendingEvent) {
        if (sendingOwnData || configDone) return

        when (event.packet) {
            is ClientboundRegistryDataPacket -> {
                // Cancel MCProtocolLib's default registry data packets
                event.isCancelled = true
            }
            is ClientboundFinishConfigurationPacket -> {
                // Cancel MCProtocolLib's FinishConfiguration and send our own data
                event.isCancelled = true
                configDone = true
                sendingOwnData = true
                try {
                    sendEngineConfiguration(event.session)
                } finally {
                    sendingOwnData = false
                }
            }
        }
    }

    private fun sendEngineConfiguration(session: Session) {
        // 1. Send tags
        sendTags(session)

        // 2. Send registry data
        sendRegistries(session)

        // 3. Send FinishConfiguration
        session.send(ClientboundFinishConfigurationPacket())

        log.debug("Sent engine registry data: {} registries, {} tag groups",
            Registries.dataDrivenRegistries.size, Registries.tagRegistries.size)
    }

    private fun sendTags(session: Session) {
        val buf = Unpooled.buffer()
        try {
            TagRegistry.STREAM_CODEC.list().write(buf, Registries.tagRegistries)
            val packet = McplUpdateTagsPacket(buf)
            session.send(packet)
        } finally {
            buf.release()
        }
    }

    private fun sendRegistries(session: Session) {
        Registries.dataDrivenRegistries.forEach { registry ->
            val buf = Unpooled.buffer()
            try {
                DataDrivenRegistry.STREAM_CODEC.write(buf, registry)
                val packet = ClientboundRegistryDataPacket(buf)
                session.send(packet)
            } finally {
                buf.release()
            }
        }
    }
}
