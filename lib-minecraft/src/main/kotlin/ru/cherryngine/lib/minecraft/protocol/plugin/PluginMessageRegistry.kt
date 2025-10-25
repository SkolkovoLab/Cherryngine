package ru.cherryngine.lib.minecraft.protocol.plugin

import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.plugin.messages.BrandPluginMessage
import ru.cherryngine.lib.minecraft.protocol.plugin.messages.PluginMessage
import ru.cherryngine.lib.minecraft.protocol.plugin.messages.RegisterPluginMessage
import ru.cherryngine.lib.minecraft.protocol.plugin.messages.UnregisterPluginMessage
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import kotlin.reflect.KClass

object PluginMessageRegistry {
    private val logger = LoggerFactory.getLogger(PluginMessageRegistry::class.java)

    private val playPluginMessageHandler = PlayPluginMessageHandler()

    private val playChannels: MutableMap<String, PluginMessageData<out PluginMessage>> = mutableMapOf()
    private val configurationChannels: MutableMap<String, PluginMessageData<out PluginMessage>> = mutableMapOf()

    init {
        registerBoth("minecraft:brand", BrandPluginMessage::class, BrandPluginMessage.STREAM_CODEC)

        registerPlay("minecraft:register", RegisterPluginMessage::class, RegisterPluginMessage.STREAM_CODEC, playPluginMessageHandler::handleRegister)
        registerPlay("minecraft:unregister", UnregisterPluginMessage::class, UnregisterPluginMessage.STREAM_CODEC, playPluginMessageHandler::handleUnregister)

    }

    fun <T : PluginMessage> registerBoth(channel: String, kClass: KClass<T>, streamCodec: StreamCodec<T>, handler: ((T, Connection) -> Unit)? = null) {
        val data = PluginMessageData<T>(channel, kClass, streamCodec, handler)
        register(Type.CONFIGURATION, data)
        register(Type.PLAY, data)
    }

    fun <T : PluginMessage> registerPlay(channel: String, kClass: KClass<T>, streamCodec: StreamCodec<T>, handler: ((T, Connection) -> Unit)? = null) {
        val data = PluginMessageData<T>(channel, kClass, streamCodec, handler)
        register(Type.PLAY, data)
    }

    fun <T : PluginMessage> registerConfiguration(channel: String, kClass: KClass<T>, streamCodec: StreamCodec<T>, handler: ((T, Connection) -> Unit)? = null) {
        val data = PluginMessageData<T>(channel, kClass, streamCodec, handler)
        register(Type.CONFIGURATION, data)
    }

    fun getByChannelOrNull(type: Type, channel: String): PluginMessageData<out PluginMessage>? {
        val map = when (type) {
            Type.CONFIGURATION -> configurationChannels
            Type.PLAY -> playChannels
        }
        return map[channel]
    }

    fun getByClassOrNull(type: Type, kClass: KClass<out PluginMessage>): PluginMessageData<out PluginMessage>? {
        val map = when (type) {
            Type.CONFIGURATION -> configurationChannels
            Type.PLAY -> playChannels
        }
        return map.values.firstOrNull { message -> message.kClass == kClass }
    }

    fun getByChannel(type: Type, channel: String): PluginMessageData<out PluginMessage> {
        return getByChannelOrNull(type, channel) ?: throw IllegalArgumentException("Plugin message with channel $channel has not been registered for type ${type.name}")
    }

    fun getByClass(type: Type, kclass: KClass<out PluginMessage>): PluginMessageData<out PluginMessage> {
        return getByClassOrNull(type, kclass) ?: throw IllegalArgumentException("Plugin message with class ${kclass.simpleName} has not been registered for type ${type.name}")
    }

    private fun register(type: Type, data: PluginMessageData<out PluginMessage>) {
        val map = when (type) {
            Type.CONFIGURATION -> configurationChannels
            Type.PLAY -> playChannels
        }

        if (map.containsKey(data.channel)) throw IllegalArgumentException("Plugin message with channel ${data.channel} is already registered for state ${type.name}")
        if (map.map { it.value.kClass }.contains(data.kClass)) throw IllegalArgumentException("Plugin message with class ${data.kClass.simpleName} is already registered for state ${type.name}. If plugin message has same contents, consider just sharing same inner stream codec")
        map[data.channel] = data

//        PlayerManager.sendPluginMessage(RegisterPluginMessage(listOf(data.channel)))
    }

    enum class Type {
        CONFIGURATION,
        PLAY
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : PluginMessage> handle(contents: PluginMessage.Contents, networkManager: Connection) {

//        val event = PluginMessageReceivedEvent(networkManager.player, contents, getPlayerEventContext(networkManager.player))
//        Events.dispatch(event)
//        if (event.cancelled) return

        val state = when (networkManager.state) {
            ProtocolState.PLAY -> Type.PLAY
            ProtocolState.CONFIGURATION -> Type.CONFIGURATION
            else -> throw IllegalStateException("Received plugin message in protocol state that does not accept plugin messages (${networkManager.state})")
        }

        val pluginMessageData = getByChannelOrNull(state, contents.channel) as PluginMessageData<PluginMessage>?
        if (pluginMessageData == null) {
            logger.warn("Received unhandled plugin message with channel ${contents.channel}!")
            contents.data.release()
            return
        }
        val decoded = pluginMessageData.streamCodec.read(contents.data)
        pluginMessageData.handler?.invoke(decoded, networkManager)
    }

    fun getChannels(type: Type): List<String> {
        val map = when (type) {
            Type.CONFIGURATION -> configurationChannels
            Type.PLAY -> playChannels
        }
        return map.keys.toList()
    }

    data class PluginMessageData<T : PluginMessage>(val channel: String, val kClass: KClass<T>, val streamCodec: StreamCodec<T>, val handler: ((T, Connection) -> Unit)? = null)
}