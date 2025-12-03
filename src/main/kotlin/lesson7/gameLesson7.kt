package lesson7

class Player(
    // Первичный конструктор, который должен быть обязательно задан при создании объекта
    val name: String
) {
    // Вторичный конструктор - эти св-ва все еще существуют, но их не обязательно использовать или перезаписывать при объявлении объекта
    val inventory = Inventory()
    // свойство-объект класса игрока
}

// Точка входа программы (то есть запуска компилятора и команды компилятору выполнить код внутри)
fun main() {

    println("=== ПРЕДМЕТЫ И ИНВЕНТАРЬ ===")

    // Создание нескольких предметов
    val sword = Item(
        id = 1,
        name = "Железный меч",
        description = "Простой, но не простой меч",
        price = 50,
        weight = 5
    )

    val apple = Item(
        id = 2,
        name = "Яблоко",
        description = "Вкусное яблоко, дает вкусного червячка",
        price = 5,
        weight = 1
    )

    val potion = Item(
        id = 3,
        name = "Зелье лечения",
        description = "Мгновенно восстанавливает много здоровья",
        price = 45,
        weight = 2
    )

    val shield = Item(
        id = 4,
        name = "Щит",
        description = "Защищает вас от мечей",
        price = 50,
        weight = 5
    )

    val player = Player(name = "Олег")

    player.inventory.addItem(sword)
    player.inventory.addItem(apple)
    player.inventory.addItem(potion)
    player.inventory.addItem(shield)
    println()
    println("Содержимое инвентаря после подбирания предметов")
    player.inventory.printInventory()
    player.inventory.findItemById(3)

    // Сделать вес предметов (ограничение инвентаря)
    // добавить в Item свойство weight: Int - вес предмета
    // В Inventory: добавить св-во максимального веса
    // Сделать функцию getCurrentWeight(): Int. которая суммирует все веса предметов в инвентаре
    // в addItem перед добавлением предмета проверять, не превышен ли лимит инвентаря
    // Если превышен - не добавлять, а просто вывести сообщение в консоль
}