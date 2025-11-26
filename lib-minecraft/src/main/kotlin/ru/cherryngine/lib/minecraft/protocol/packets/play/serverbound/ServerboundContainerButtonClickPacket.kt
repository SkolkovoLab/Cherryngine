package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundContainerButtonClickPacket(
    val windowId: Int,
    val buttonId: Int,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundContainerButtonClickPacket::windowId,
            StreamCodec.VAR_INT, ServerboundContainerButtonClickPacket::buttonId,
            ::ServerboundContainerButtonClickPacket
        )
    }
}

/*
buttonId field:

+--------------------+-----------+--------------------------------------------------------------+
| Window type        | ID        | Meaning                                                      |
+--------------------+-----------+--------------------------------------------------------------+
| Enchantment Table  | 0         | Topmost enchantment.                                         |
| Enchantment Table  | 1         | Middle enchantment.                                          |
| Enchantment Table  | 2         | Bottom enchantment.                                          |
+--------------------+-----------+--------------------------------------------------------------+
| Lectern            | 1         | Previous page (which does give a redstone output).           |
| Lectern            | 2         | Next page.                                                   |
| Lectern            | 3         | Take Book.                                                   |
| Lectern            | 100+page  | Opened page number — 100 + number.                           |
+--------------------+-----------+--------------------------------------------------------------+
| Stonecutter        | (varies)  | Recipe button number = 4*row + col, depends on the item.     |
+--------------------+-----------+--------------------------------------------------------------+
| Loom               | (varies)  | Recipe button number = 4*row + col, depends on the item.     |
+--------------------+-----------+--------------------------------------------------------------+
 */