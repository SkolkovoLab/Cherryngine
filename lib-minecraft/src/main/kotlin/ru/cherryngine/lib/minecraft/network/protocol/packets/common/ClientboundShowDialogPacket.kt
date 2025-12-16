package ru.cherryngine.lib.minecraft.network.protocol.packets.common

import ru.cherryngine.lib.minecraft.codec.transcoder.BinaryTagTranscoder
import ru.cherryngine.lib.minecraft.dialog.Dialog
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.BinaryTagStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.RegistryOrXStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.utils.Either
import ru.cherryngine.lib.minecraft.utils.registry.RegistryKey

data class ClientboundShowDialogPacket(
    val dialog: Either<RegistryKey<Dialog>, Dialog>,
) : ClientboundPacket {
    companion object {
        private val dialogCodec: StreamCodec<Dialog> = BinaryTagStreamCodecs.STREAM.transform<Dialog>(
            { Dialog.CODEC.decode(BinaryTagTranscoder, it) },
            { Dialog.CODEC.encode(BinaryTagTranscoder, it) }
        )

        val registry = Registries.dialog
        val STREAM_CODEC = StreamCodec.of(
            RegistryOrXStreamCodec(registry, dialogCodec), ClientboundShowDialogPacket::dialog,
            ::ClientboundShowDialogPacket
        )
    }
}