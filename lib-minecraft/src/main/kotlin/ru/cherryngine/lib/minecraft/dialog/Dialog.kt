package ru.cherryngine.lib.minecraft.dialog

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.ComponentCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.utils.toKey

sealed interface Dialog {
    val title: Component
    val externalTitle: Component?
    val canCloseWithEsc: Boolean
    val body: List<DialogBody>
    val pause: Boolean
    val inputs: List<DialogInput>
    val afterAction: AfterAction

    companion object {
        val CODEC = object : Codec<Dialog> {
            override fun <D> encode(transcoder: Transcoder<D>, value: Dialog): D {
                val (type: Key, codec: StructCodec<out Dialog>) = when (value) {
                    is Notice -> Notice.KEY to Notice.CODEC
                    is ServerLinks -> ServerLinks.KEY to ServerLinks.CODEC
                    is DialogList -> DialogList.KEY to DialogList.CODEC
                    is MultiAction -> MultiAction.KEY to MultiAction.CODEC
                    is Confirmation -> Confirmation.KEY to Confirmation.CODEC
                }

                @Suppress("UNCHECKED_CAST")
                codec as StructCodec<Dialog>

                return transcoder.encodeMap()
                    .put("type", Codec.KEY.encode(transcoder, type))
                    .also { codec.encodeToMap(transcoder, value, it) }
                    .build()
            }

            override fun <D> decode(transcoder: Transcoder<D>, value: D): Dialog {
                val map = transcoder.decodeMap(value)
                val type = Codec.KEY.decode(transcoder, map.getValue("type"))
                val codec = when (type) {
                    Notice.KEY -> Notice.CODEC
                    ServerLinks.KEY -> ServerLinks.CODEC
                    DialogList.KEY -> DialogList.CODEC
                    MultiAction.KEY -> MultiAction.CODEC
                    Confirmation.KEY -> Confirmation.CODEC
                    else -> throw IllegalArgumentException()
                }

                return codec.decode(transcoder, value)
            }
        }
    }

    class Notice(
        override val title: Component,
        override val externalTitle: Component?,
        override val canCloseWithEsc: Boolean = true,
        override val body: List<DialogBody>,
        override val pause: Boolean = true,
        override val afterAction: AfterAction = AfterAction.CLOSE,
        override val inputs: List<DialogInput>,
        val action: DialogButton,
    ) : Dialog {
        companion object {
            val KEY = "notice".toKey()
            val CODEC = StructCodec.of(
                "title", ComponentCodec, Notice::title,
                "external_title", ComponentCodec.optional(), Notice::externalTitle,
                "can_close_with_escape", Codec.BOOLEAN.default(true), Notice::canCloseWithEsc,
                "body", DialogBody.CODEC.listOrSingle().default(listOf()), Notice::body,
                "pause", Codec.BOOLEAN.default(true), Notice::pause,
                "after_action", Codec.enum<AfterAction>().default(AfterAction.CLOSE).default(AfterAction.CLOSE), Notice::afterAction,
                "inputs", DialogInput.CODEC.list().default(listOf()), Notice::inputs,
                "action", DialogButton.CODEC, Notice::action,
                ::Notice
            )
        }
    }

    class ServerLinks(
        override val title: Component,
        override val externalTitle: Component?,
        override val canCloseWithEsc: Boolean = true,
        override val body: List<DialogBody>,
        override val pause: Boolean = true,
        override val afterAction: AfterAction = AfterAction.CLOSE,
        override val inputs: List<DialogInput>,
        val exitAction: DialogButton? = null,
        val columns: Int = 2,
        val buttonWidth: Int = 150,
    ) : Dialog {
        companion object {
            val KEY = "server_links".toKey()
            val CODEC = StructCodec.of(
                "title", ComponentCodec, ServerLinks::title,
                "external_title", ComponentCodec.optional(), ServerLinks::externalTitle,
                "can_close_with_escape", Codec.BOOLEAN.default(true), ServerLinks::canCloseWithEsc,
                "body", DialogBody.CODEC.listOrSingle().default(listOf()), ServerLinks::body,
                "pause", Codec.BOOLEAN.default(true), ServerLinks::pause,
                "after_action", Codec.enum<AfterAction>().default(AfterAction.CLOSE), ServerLinks::afterAction,
                "inputs", DialogInput.CODEC.list().default(listOf()), ServerLinks::inputs,
                "exit_action", DialogButton.CODEC.optional(), ServerLinks::exitAction,
                "columns", Codec.INT, ServerLinks::columns,
                "button_width", Codec.INT, ServerLinks::buttonWidth,
                ::ServerLinks
            )
        }
    }

    class DialogList(
        override val title: Component,
        override val externalTitle: Component?,
        override val canCloseWithEsc: Boolean = true,
        override val body: List<DialogBody>,
        override val pause: Boolean = true,
        override val afterAction: AfterAction = AfterAction.CLOSE,
        override val inputs: List<DialogInput>,
        val dialogs: String, // TODO сюда передаётся либо тэг, либо список ключей
        val exitAction: DialogButton?,
        val columns: Int,
        val buttonWidth: Int,
    ) : Dialog {
        companion object {
            val KEY = "dialog_list".toKey()
            val CODEC = StructCodec.of(
                "title", ComponentCodec, DialogList::title,
                "external_title", ComponentCodec.optional(), DialogList::externalTitle,
                "can_close_with_escape", Codec.BOOLEAN.default(true), DialogList::canCloseWithEsc,
                "body", DialogBody.CODEC.listOrSingle().default(listOf()), DialogList::body,
                "pause", Codec.BOOLEAN.default(true), DialogList::pause,
                "after_action", Codec.enum<AfterAction>().default(AfterAction.CLOSE), DialogList::afterAction,
                "inputs", DialogInput.CODEC.list().default(listOf()), DialogList::inputs,
                "dialogs", Codec.STRING, DialogList::dialogs,
                "exit_action", DialogButton.CODEC.optional(), DialogList::exitAction,
                "columns", Codec.INT, DialogList::columns,
                "button_width", Codec.INT, DialogList::buttonWidth,
                ::DialogList
            )
        }
    }

    class MultiAction(
        override val title: Component,
        override val externalTitle: Component?,
        override val canCloseWithEsc: Boolean = true,
        override val body: List<DialogBody>,
        override val pause: Boolean = true,
        override val afterAction: AfterAction = AfterAction.CLOSE,
        override val inputs: List<DialogInput>,
        val actions: List<DialogButton>,
        val exitAction: DialogButton? = null,
        val columns: Int = 2,
    ) : Dialog {
        init {
            require(actions.isNotEmpty()) { "actions can't be empty" }
        }

        companion object {
            val KEY = "multi_action".toKey()
            val CODEC = StructCodec.of(
                "title", ComponentCodec, MultiAction::title,
                "external_title", ComponentCodec.optional(), MultiAction::externalTitle,
                "can_close_with_escape", Codec.BOOLEAN.default(true), MultiAction::canCloseWithEsc,
                "body", DialogBody.CODEC.listOrSingle().default(listOf()), MultiAction::body,
                "pause", Codec.BOOLEAN.default(true), MultiAction::pause,
                "after_action", Codec.enum<AfterAction>().default(AfterAction.CLOSE), MultiAction::afterAction,
                "inputs", DialogInput.CODEC.list().default(listOf()), MultiAction::inputs,
                "actions", DialogButton.CODEC.list(), MultiAction::actions,
                "exit_action", DialogButton.CODEC.optional(), MultiAction::exitAction,
                "columns", Codec.INT, MultiAction::columns,
                ::MultiAction
            )
        }
    }

    class Confirmation(
        override val title: Component,
        override val externalTitle: Component?,
        override val canCloseWithEsc: Boolean = true,
        override val body: List<DialogBody>,
        override val pause: Boolean = true,
        override val afterAction: AfterAction = AfterAction.CLOSE,
        override val inputs: List<DialogInput>,
        val yes: DialogButton,
        val no: DialogButton,
    ) : Dialog {
        companion object {
            val KEY = "confirmation".toKey()
            val CODEC = StructCodec.of(
                "title", ComponentCodec, Confirmation::title,
                "external_title", ComponentCodec.optional(), Confirmation::externalTitle,
                "can_close_with_escape", Codec.BOOLEAN.default(true), Confirmation::canCloseWithEsc,
                "body", DialogBody.CODEC.listOrSingle().default(listOf()), Confirmation::body,
                "pause", Codec.BOOLEAN.default(true), Confirmation::pause,
                "after_action", Codec.enum<AfterAction>().default(AfterAction.CLOSE), Confirmation::afterAction,
                "inputs", DialogInput.CODEC.list().default(listOf()), Confirmation::inputs,
                "yes", DialogButton.CODEC, Confirmation::yes,
                "no", DialogButton.CODEC, Confirmation::no,
                ::Confirmation
            )
        }
    }

    enum class AfterAction {
        /** closes the dialog */
        CLOSE,

        /** actually nothing happens */
        NONE,

        /**
         * The server is expected to replace
         * current screen with another dialog btw */
        WAIT_FOR_RESPONSE;
    }
}

