package ru.cherryngine.engine.minecraft

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import net.minestom.server.MinecraftServer
import net.minestom.server.registry.Registries
import ru.cherryngine.lib.minecraft.network.NettyServer

@Factory
class MinecraftModule {
    /**
     * Поднимаем Minestom-овский глобальный `ServerProcess` (через `updateProcess()`).
     * Это обязательное требование: ряд Minestom-овских record-пакетов и data-классов
     * в своих `@JsonBackedConstructor` читают `MinecraftServer.process()` (например,
     * `ClientHandshakePacket.maxHandshakeLength()` смотрит в `process().auth()`),
     * поэтому без инициализации любой входящий пакет ломается на NPE.
     *
     * `updateProcess()` лишь собирает managers + dynamic registries, ни одного сокета
     * и тика не запускает — `start()` мы никогда не вызываем. ServerProcess уже
     * реализует `Registries`, так что отдаём его как наш registry-бин.
     */
    @Singleton
    fun getRegistries(): Registries = MinecraftServer.updateProcess()

    @Singleton
    fun getNettyServer(registries: Registries) = NettyServer(registries)
}
