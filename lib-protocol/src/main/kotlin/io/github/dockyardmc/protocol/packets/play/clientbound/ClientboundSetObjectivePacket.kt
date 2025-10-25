package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.codec.ActionStreamCodec
import io.github.dockyardmc.codec.ComponentCodecs
import io.github.dockyardmc.codec.StreamCodecNBT
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component

data class ClientboundSetObjectivePacket(
    val name: String,
    val action: Action,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ClientboundSetObjectivePacket::name,
            Action.STREAM_CODEC, ClientboundSetObjectivePacket::action,
            ::ClientboundSetObjectivePacket
        )
    }

    interface Action {
        data class Create(
            val value: Component,
            val type: ScoreboardType,
            val numberFormat: NumberFormat?,
        ) : Action {
            companion object {
                val STREAM_CODEC = StreamCodec.of(
                    ComponentCodecs.NBT, Create::value,
                    EnumStreamCodec<ScoreboardType>(), Create::type,
                    NumberFormat.STREAM_CODEC.optional(), Create::numberFormat,
                    ::Create
                )
            }
        }

        object Remove : Action {
            val STREAM_CODEC = StreamCodec.of { Remove }
        }

        data class EditText(
            val value: Component,
            val type: ScoreboardType,
            val numberFormat: NumberFormat?,
        ) : Action {
            companion object {
                val STREAM_CODEC = StreamCodec.of(
                    ComponentCodecs.NBT, EditText::value,
                    EnumStreamCodec<ScoreboardType>(), EditText::type,
                    NumberFormat.STREAM_CODEC.optional(), EditText::numberFormat,
                    ::EditText
                )
            }
        }

        enum class ScoreboardType {
            INTEGER,
            HEARTS
        }

        interface NumberFormat {
            object Blank : NumberFormat {
                val STREAM_CODEC = StreamCodec.of { Blank }
            }

            data class Styled(
                val styling: CompoundBinaryTag,
            ) : NumberFormat {
                companion object {
                    val STREAM_CODEC = StreamCodec.of(
                        StreamCodecNBT.COMPOUND_STREAM, Styled::styling,
                        ::Styled
                    )
                }
            }

            data class Fixed(
                val content: Component,
            ) : NumberFormat {
                companion object {
                    val STREAM_CODEC = StreamCodec.of(
                        ComponentCodecs.NBT, Fixed::content,
                        ::Fixed
                    )
                }
            }

            companion object {
                val STREAM_CODEC = ActionStreamCodec<NumberFormat>(
                    StreamCodec.VAR_INT,
                    ActionStreamCodec.Entry(Blank::class, Blank.STREAM_CODEC),
                    ActionStreamCodec.Entry(Styled::class, Styled.STREAM_CODEC),
                    ActionStreamCodec.Entry(Fixed::class, Fixed.STREAM_CODEC),
                )
            }
        }

        companion object {
            val STREAM_CODEC = ActionStreamCodec<Action>(
                StreamCodec.INT_BYTE,
                ActionStreamCodec.Entry(Create::class, Create.STREAM_CODEC),
                ActionStreamCodec.Entry(Remove::class, Remove.STREAM_CODEC),
                ActionStreamCodec.Entry(EditText::class, EditText.STREAM_CODEC),
            )
        }
    }
}
