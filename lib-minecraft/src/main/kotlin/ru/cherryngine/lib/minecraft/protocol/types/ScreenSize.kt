package ru.cherryngine.lib.minecraft.protocol.types

enum class ScreenSize(val inventoryType: InventoryType, val rows: Int, val columns: Int) {
    GENERIC_9X1(InventoryType.GENERIC_9X1, 1, 9),
    GENERIC_9X2(InventoryType.GENERIC_9X2, 2, 9),
    GENERIC_9X3(InventoryType.GENERIC_9X3, 3, 9),
    GENERIC_9X4(InventoryType.GENERIC_9X4, 4, 9),
    GENERIC_9X5(InventoryType.GENERIC_9X5, 5, 9),
    GENERIC_9X6(InventoryType.GENERIC_9X6, 6, 9);

//    fun getModifiableSlots(screen: Screen): Int {
//        return (this.rows + if(screen.isFullscreen) 3 else 0) * this.columns
//    }
}