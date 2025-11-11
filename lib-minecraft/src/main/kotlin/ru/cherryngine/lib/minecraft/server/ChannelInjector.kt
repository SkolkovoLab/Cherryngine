package ru.cherryngine.lib.minecraft.server

import io.netty.channel.Channel

interface ChannelInjector {
    fun inject(channel: Channel)
}