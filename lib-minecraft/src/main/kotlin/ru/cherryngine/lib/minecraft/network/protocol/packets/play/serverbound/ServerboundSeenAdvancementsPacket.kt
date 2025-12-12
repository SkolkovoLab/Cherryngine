package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.ActionStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class ServerboundSeenAdvancementsPacket(
    val action: Action,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Action.STREAM_CODEC, ServerboundSeenAdvancementsPacket::action,
            ::ServerboundSeenAdvancementsPacket
        )
    }

    interface Action {
        data class OpenedTab(
            val tabId: Key,
        ) : Action {
            companion object {
                val STREAM_CODEC = StreamCodec.of(
                    StreamCodec.KEY, OpenedTab::tabId,
                    ::OpenedTab
                )
            }
        }

        object ClosedScreen : Action {
            val STREAM_CODEC = StreamCodec.of { ClosedScreen }
        }

        companion object {
            val STREAM_CODEC = ActionStreamCodec(
                StreamCodec.VAR_INT,
                ActionStreamCodec.Entry(OpenedTab::class, OpenedTab.STREAM_CODEC),
                ActionStreamCodec.Entry(ClosedScreen::class, ClosedScreen.STREAM_CODEC)
            )
        }
    }
}