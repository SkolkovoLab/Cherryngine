package ru.cherryngine.lib.minecraft.network.protocol.packets.common

import ru.cherryngine.lib.minecraft.dialog.Dialog
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.BinaryTagStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.RegistryOrXStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.DialogEntry
import ru.cherryngine.lib.minecraft.registry.registries.DialogRegistry
import ru.cherryngine.lib.minecraft.utils.Either

data class ClientboundShowDialogPacket(
    val dialog: Either<DialogEntry, Dialog>
) : ClientboundPacket {
    companion object {
        private val dialogCodec = BinaryTagStreamCodecs.STREAM.transform<Dialog>(
            { TODO("Implement NBT to Dialog transformation") },
            { it.getNbt() }
        )

        val STREAM_CODEC = StreamCodec.of(
            RegistryOrXStreamCodec(DialogRegistry, dialogCodec), ClientboundShowDialogPacket::dialog,
            ::ClientboundShowDialogPacket
        )
    }
}