package ru.cherryngine.lib.minecraft.protocol.packets

enum class ProtocolState {
    HANDSHAKE,
    STATUS,
    LOGIN,
    CONFIGURATION,
    PLAY,
}