package ru.cherryngine.platform.minecraft.java.network

import io.netty.channel.Channel

interface ChannelInjector {
    fun inject(channel: Channel)
}