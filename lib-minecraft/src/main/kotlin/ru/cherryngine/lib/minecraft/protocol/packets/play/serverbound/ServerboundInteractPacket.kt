package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.codec.ActionStreamCodec
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.PlayerHand
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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