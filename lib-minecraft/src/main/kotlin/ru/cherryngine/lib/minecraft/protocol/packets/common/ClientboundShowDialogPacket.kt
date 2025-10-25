package ru.cherryngine.lib.minecraft.protocol.packets.common

import ru.cherryngine.lib.minecraft.codec.RegistryOrXStreamCodec
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.dialog.Dialog
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.registry.registries.DialogEntry
import ru.cherryngine.lib.minecraft.registry.registries.DialogRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.tide.types.Either

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