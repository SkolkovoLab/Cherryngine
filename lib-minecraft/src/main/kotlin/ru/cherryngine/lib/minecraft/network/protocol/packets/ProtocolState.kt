package ru.cherryngine.lib.minecraft.network.protocol.packets

enum class ProtocolState {
    HANDSHAKE,
    STATUS,
    LOGIN,
    CONFIGURATION,
    PLAY,
}