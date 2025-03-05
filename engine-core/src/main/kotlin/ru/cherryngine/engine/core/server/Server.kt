package ru.cherryngine.engine.core.server

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import jakarta.inject.Singleton
import net.minestom.server.network.ConnectionState
import net.minestom.server.network.packet.PacketVanilla
import net.minestom.server.network.packet.server.common.KeepAlivePacket
import net.minestom.server.registry.Registries
import org.slf4j.Logger
import ru.cherryngine.engine.core.EngineCoreConfig
import java.io.EOFException
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketException
import java.net.StandardProtocolFamily
import java.nio.channels.ServerSocketChannel
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

@Singleton
class Server(
    val registries: Registries,
    val engineCoreConfig: EngineCoreConfig,
    val clientPacketListeners: List<ClientPacketListener>,
    val logger: Logger,
) {
    private val packetParser = PacketVanilla.CLIENT_PACKET_PARSER

    private val stopped = AtomicBoolean(false)
    private val server: ServerSocketChannel = ServerSocketChannel.open(StandardProtocolFamily.INET)

    private val connections = Collections.newSetFromMap(WeakHashMap<ClientConnection, Boolean>())

    val running get() = !stopped.get()

    @PostConstruct
    fun init() {
        val address = InetSocketAddress(engineCoreConfig.address, engineCoreConfig.port)
        server.bind(address)
        Thread(::listenConnections, "ConnectionListenerThread").start()
        logger.info("Server started on: $address")
    }

    @PreDestroy
    fun stop() {
        stopped.set(true)
    }

    fun listenConnections() {
        while (running) {
            try {
                val channel = server.accept()
                logger.info("Accepted connection from ${channel.remoteAddress}")
                val clientConnection = ClientConnection(
                    channel,
                    registries,
                    engineCoreConfig.compressionThreshold,
                    clientPacketListeners
                )
                connections += clientConnection

                Thread.startVirtualThread { playerReadLoop(clientConnection) }
                Thread.startVirtualThread { playerWriteLoop(clientConnection) }
                Thread.startVirtualThread { playerKeepAliveLoop(clientConnection) }
            } catch (e: IOException) {
                logger.error("Failed to accept connection", e)
            }
        }
    }

    fun playerReadLoop(clientConnection: ClientConnection) {
        while (running) {
            try {
                clientConnection.read(packetParser)
            } catch (_: EOFException) {
                clientConnection.disconnect()
                break
            } catch (e: Throwable) {
                val isExpected = e is SocketException && e.message == "Connection reset"
                if (!isExpected) logger.error("Failed to read packets", e)
                clientConnection.disconnect()
                break
            }
        }
    }

    fun playerWriteLoop(clientConnection: ClientConnection) {
        while (running) {
            try {
                clientConnection.flushSync()
            } catch (_: EOFException) {
                clientConnection.disconnect()
                break
            } catch (e: Throwable) {
                val isExpected = e is IOException && e.message == "Broken pipe"
                if (!isExpected) logger.error("Failed to flush packets", e)
                clientConnection.disconnect()
                break
            }

            if (!clientConnection.online) {
                try {
                    clientConnection.flushSync()
                    clientConnection.channel.close()
                    break
                } catch (_: IOException) {
                    break
                }
            }
        }
    }

    fun playerKeepAliveLoop(clientConnection: ClientConnection) {
        while (running) {
            if (!clientConnection.online) break
            when (clientConnection.connectionState) {
                ConnectionState.HANDSHAKE, ConnectionState.LOGIN -> Unit
                ConnectionState.STATUS -> break
                ConnectionState.CONFIGURATION, ConnectionState.PLAY -> {
                    clientConnection.sendPacket(KeepAlivePacket(System.currentTimeMillis()))
                }
            }

            Thread.sleep(20 * 1000)
        }
    }
}