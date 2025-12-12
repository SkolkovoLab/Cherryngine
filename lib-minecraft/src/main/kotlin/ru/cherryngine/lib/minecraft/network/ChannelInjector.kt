package ru.cherryngine.lib.minecraft.network

import io.netty.channel.Channel

interface ChannelInjector {
    fun inject(channel: Channel)
}