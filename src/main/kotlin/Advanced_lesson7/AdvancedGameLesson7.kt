package Advanced_lesson

import Advanced_lesson7.Inventory
import Advanced_lesson7.Item
import Advanced_lesson7.ItemType

class Player(
    val name: String,
    val inventory: Inventory
)

fun main() {
    val inventory = Inventory(maxSlots = 5)
    val player = Player("Oleg", inventory)

    val sword = Item(
        1,
        "меч",
        "Обычный меч",
        35,
        ItemType.WEAPON,
        1
    )

    val helmet = Item (
        2,
        "Шлем кожанный",
        "Шлем из кожи хейтеров",
        60,
        ItemType.ARMOR,
        1
    )

    val potion = Item(
        3,
        "Зелье здоровья",
        "Хилит на 20 хп",
        25,
        ItemType.CONSUMABLE,
        10
    )

    player.inventory.addItem(sword)
    player.inventory.addItem(helmet)
    player.inventory.addItem(potion, 7)
    player.inventory.addItem(potion, 12)

    player.inventory.printInventory()

    player.inventory.addItem(sword)

    player.inventory.removeItem(potion, 5)
    player.inventory.removeItem(helmet)

    player.inventory.printInventory()

    player.inventory.addItem(potion, 50)

    player.inventory.printInventory()
}