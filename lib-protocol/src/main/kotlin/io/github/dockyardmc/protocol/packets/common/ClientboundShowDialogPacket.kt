package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.codec.RegistryOrXStreamCodec
import io.github.dockyardmc.codec.StreamCodecNBT
import io.github.dockyardmc.dialog.Dialog
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.registry.registries.DialogEntry
import io.github.dockyardmc.registry.registries.DialogRegistry
import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.tide.types.Either

data class ClientboundShowDialogPacket(
    val dialog: Either<DialogEntry, Dialog>
) : ClientboundPacket {
    companion object {
        private val dialogCodec = StreamCodecNBT.STREAM.transform<Dialog>(
            { TODO("Implement NBT to Dialog transformation") },
            { it.getNbt() }
        )

        val STREAM_CODEC = StreamCodec.Companion.of(
            RegistryOrXStreamCodec(DialogRegistry, dialogCodec), ClientboundShowDialogPacket::dialog,
            ::ClientboundShowDialogPacket
        )
    }
}