package io.github.dockyardmc.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3D
import io.github.dockyardmc.codec.ActionStreamCodec
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.types.PlayerHand
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundInteractPacket(
    val entity: Int,
    val action: Action,
    val sneaking: Boolean
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundInteractPacket::entity,
            Action.STREAM_CODEC, ServerboundInteractPacket::action,
            StreamCodec.BOOLEAN, ServerboundInteractPacket::sneaking,
            ::ServerboundInteractPacket
        )
    }

    interface Action {
        data class Interact(
            val hand: PlayerHand
        ) : Action {
            companion object {
                val STREAM_CODEC = StreamCodec.of(
                    EnumStreamCodec<PlayerHand>(), Interact::hand,
                    ::Interact
                )
            }
        }

        object Attack : Action {
            val STREAM_CODEC = StreamCodec.of { Attack }
        }

        data class InteractAt(
            val target: Vec3D,
            val hand: PlayerHand
        ) : Action {
            companion object {
                val STREAM_CODEC = StreamCodec.of(
                    LocationCodecs.VEC_3D_FLOAT, InteractAt::target,
                    EnumStreamCodec<PlayerHand>(), InteractAt::hand,
                    ::InteractAt
                )
            }
        }

        companion object {
            val STREAM_CODEC = ActionStreamCodec(
                StreamCodec.VAR_INT,
                ActionStreamCodec.Entry(Interact::class, Interact.STREAM_CODEC),
                ActionStreamCodec.Entry(Attack::class, Attack.STREAM_CODEC),
                ActionStreamCodec.Entry(InteractAt::class, InteractAt.STREAM_CODEC)
            )
        }
    }
}