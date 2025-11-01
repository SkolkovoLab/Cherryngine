package ru.cherryngine.lib.minecraft.protocol.plugin

import io.netty.channel.ChannelHandlerContext
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.extentions.sendPacket
import ru.cherryngine.lib.minecraft.protocol.packets.login.ClientboundLoginCustomQueryPacket
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger

class LoginPluginMessageHandler(val networkManager: Connection) {
    private val logger = LoggerFactory.getLogger(LoginPluginMessageHandler::class.java)

    companion object {
        val REQUEST_ID = AtomicInteger(0)
    }

    private val requestByMessage: MutableMap<Int, Request> = mutableMapOf()

    data class Request(
        val channel: String,
        val requestPayload: ByteArray,
    ) {
        val future = CompletableFuture<Response>()
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Request

            if (channel != other.channel) return false
            if (!requestPayload.contentEquals(other.requestPayload)) return false
            if (future != other.future) return false

            return true
        }

        override fun hashCode(): Int {
            var result = channel.hashCode()
            result = 31 * result + requestPayload.contentHashCode()
            result = 31 * result + future.hashCode()
            return result
        }
    }

    data class Response(
        val channel: String,
        val responsePayload: ByteArray? = null,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Response

            if (channel != other.channel) return false
            if (!responsePayload.contentEquals(other.responsePayload)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = channel.hashCode()
            result = 31 * result + (responsePayload?.contentHashCode() ?: 0)
            return result
        }
    }

    fun request(
        connection: ChannelHandlerContext,
        channel: String,
        requestPayload: ByteArray,
    ): CompletableFuture<Response> {
        val request = Request(channel, requestPayload)
        val id = REQUEST_ID.getAndIncrement()
        requestByMessage[id] = request
        connection.sendPacket(ClientboundLoginCustomQueryPacket(id, channel, requestPayload), networkManager)
        return request.future
    }

    fun handleResponse(messageId: Int, responsePayload: ByteArray?) {
        val request = requestByMessage.remove(messageId)
        if (request == null) {
            logger.warn("Received unexpected login plugin response with id $messageId of ${responsePayload?.size} bytes")
            return
        }

        try {
            val response = Response(request.channel, responsePayload)
            request.future.complete(response)
        } catch (exception: Exception) {
            logger.error("Error handling login plugin response on channel ${request.channel}:", exception)
        }
    }

}