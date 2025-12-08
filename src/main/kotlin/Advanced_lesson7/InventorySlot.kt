package Advanced_lesson7

data class InventorySlot(
    val item: Item,
    var quantity: Int   // var - потому что значение будет меняться
    // quantity - кол-во предметов, которое сейчас лежит в ячейке инвентаря (слоте)
)