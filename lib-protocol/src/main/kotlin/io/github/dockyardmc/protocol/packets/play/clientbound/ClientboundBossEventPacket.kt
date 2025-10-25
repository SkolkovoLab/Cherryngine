package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.codec.ActionStreamCodec
import io.github.dockyardmc.codec.ComponentCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import java.util.*

data class ClientboundBossEventPacket(
    val uuid: UUID,
    val action: Action,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC: StreamCodec<ClientboundBossEventPacket> = StreamCodec.of(
            StreamCodec.UUID, ClientboundBossEventPacket::uuid,
            Action.STREAM_CODEC, ClientboundBossEventPacket::action,
            ::ClientboundBossEventPacket
        )
    }

    sealed interface Action {
        data class Add(
            val title: Component,
            val health: Float,
            val color: BossBar.Color,
            val overlay: BossBar.Overlay,
            val flags: Byte,
        ) : Action {
            companion object {
                val STREAM_CODEC = StreamCodec.of(
                    ComponentCodecs.NBT, Add::title,
                    StreamCodec.FLOAT, Add::health,
                    EnumStreamCodec<BossBar.Color>(), Add::color,
                    EnumStreamCodec<BossBar.Overlay>(), Add::overlay,
                    StreamCodec.BYTE, Add::flags,
                    ::Add
                )
            }
        }

        object Remove : Action {
            val STREAM_CODEC = StreamCodec.of { Remove }
        }

        data class UpdateHealth(
            val health: Float,
        ) : Action {
            companion object {
                val STREAM_CODEC = StreamCodec.of(
                    StreamCodec.FLOAT, UpdateHealth::health,
                    ::UpdateHealth
                )
            }
        }

        data class UpdateTitle(
            val title: Component,
        ) : Action {
            companion object {
                val STREAM_CODEC = StreamCodec.of(
                    ComponentCodecs.NBT, UpdateTitle::title,
                    ::UpdateTitle
                )
            }
        }

        data class UpdateStyle(
            val color: BossBar.Color,
            val overlay: BossBar.Overlay,
        ) : Action {
            companion object {
                val STREAM_CODEC = StreamCodec.of(
                    EnumStreamCodec<BossBar.Color>(), UpdateStyle::color,
                    EnumStreamCodec<BossBar.Overlay>(), UpdateStyle::overlay,
                    ::UpdateStyle
                )
            }
        }

        data class UpdateFlags(
            val flags: Byte,
        ) : Action {
            companion object {
                val STREAM_CODEC = StreamCodec.of(
                    StreamCodec.BYTE, UpdateFlags::flags,
                    ::UpdateFlags
                )
            }
        }

        companion object {
            val STREAM_CODEC = ActionStreamCodec(
                StreamCodec.VAR_INT,
                ActionStreamCodec.Entry(Add::class, Add.STREAM_CODEC),
                ActionStreamCodec.Entry(Remove::class, Remove.STREAM_CODEC),
                ActionStreamCodec.Entry(UpdateHealth::class, UpdateHealth.STREAM_CODEC),
                ActionStreamCodec.Entry(UpdateTitle::class, UpdateTitle.STREAM_CODEC),
                ActionStreamCodec.Entry(UpdateStyle::class, UpdateStyle.STREAM_CODEC),
                ActionStreamCodec.Entry(UpdateFlags::class, UpdateFlags.STREAM_CODEC),
            )
        }
    }
}