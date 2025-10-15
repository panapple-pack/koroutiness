import kotlin.random.Random

class Item(
    val id: String,
    val name: String,
    val description: String,
    val value: Int = 0,
    val useEffect: (Player) -> Unit = {}  // Функция использования
){
    fun use(player: Player){
        println("Используется $name")
        useEffect(player)
    }

    fun displayInfo(){
        println("$name - $description (Ценность: $value)")
    }
}



class Inventory{
    // mutableListOf<Item>() - создает пустой изменяемый список, в который можно положить только предметы Item
    // private означает, что доступ к списку предметов есть только внутри класса Инвентаря
    private val items = mutableListOf<Item>()

    fun addItem(item: Item){
        items.add(item)
        println("Предмет '${item.name}' добавлен в инвентарь")
    }
    fun removeItem(index: Int): Boolean {
        // index in 0 until items.size - проверяет, находится ли индекс в диапазоне от 0 до размера списка (НЕ ВКЛЮЧИТЕЛЬНО)
        if (index in 0 until items.size) {
            val removedItem = items.removeAt(index)  // удаляет элемент по указанному индексу
            println("Предмет ${removedItem.name} удален из инвентаря")
            return true
        }
        println("Неверный индекс предмета! Такого в инвентаре нет")
        return false
    }
    fun useItem(index: Int, player: Player): Boolean {
        if (index in 0 until items.size) {
            val item = items[index]
            item.use(player)
            items.removeAt(index)
            return true
        }
        println("Неверный индекс предмета! Такого в инвентаре нет")
        return false
    }
    fun display(){
        if (items.isEmpty()) {
            println("Инвентарь пуст")
        } else {
            println("\n=== ИНВЕНТАРЬ ===")
            items.forEachIndexed { index, item ->
                println("${index + 1}. ${item.name} - ${item.description}")
            }
            println("Всего предметов ${items.size}")
        }
    }
    fun findItemById(itemId: String): Item? {
        // find { } ищет первый элемент, удовлетворяющий условию поиска
        // it - ключевое слово, обозначающее текущий элемент в поиске
        // ? - функция может вернуть null, если ничего не найдено
        return items.find { it.id == itemId }
    }
    fun hasItem(itemId: String): Boolean {
        // .any { } вернет true, если хотя бы один элемент удовлетворяет поиску
        return items.any { it.id == itemId }
    }
    fun countItems(itemId: String): Int {
        return items.count {it.id == itemId}
    }
}



open class Character(val name: String, var health: Int, var attack: Int) {
    val isAlive: Boolean get() = health > 0

    open fun takeDamage(damage: Int) {
        health -= damage
        println("$name получает $damage")
        if (health <= 0) println("$name пал в бою")
    }

    fun attack(target: Character) {
        if (!isAlive || !target.isAlive) return
        val damage = Random.nextInt(attack - 3, attack + 4)  // случайный урон в диапазоне
        println("$name атакует ${target.name}!")
        target.takeDamage(damage)
    }
}



class Quest(
    val id: String,
    val name: String,
    val description: String,
    val requiredItemsId: String? = null,  // id предмета необходимого для выполнения квеста (может быть null, т.е. не требовать null)
    val rewardGold: Int = 0,
    val rewardItem: Item? = null
) {
    var isCompleted: Boolean = false
    var isActive: Boolean = false

    fun checkCompletion(player: Player): Boolean {
        if (!isCompleted && isActive) {
            // Если квест требует предмет, проверяем его наличине у игрока
            if (requiredItemsId != null && player.inventory.hasItem(requiredItemsId)) {
                completeQuest(player)
                return true
            }
        }
        return false
    }
    private fun completeQuest(player: Player) {
        isCompleted = true
        isActive = false

        println("\n*** КВЕСТ $name ВЫПОЛНЕН ***")
        println("Награда: ")
        if (rewardGold > 0) {
            println("- Золото: $rewardGold")
            // В реальной игре тут будет метод добавления золота нашему игроку
        }

        if (rewardItem != null) {
            println("- Предмет: ${rewardItem.name}")
            player.inventory.addItem(rewardItem)
        }
    }
    fun displayInfo() {
        val status = when{
            isCompleted -> "ВЫПОЛНЕН"
            isActive -> "АКТИВЕН"
            else -> "НЕ АКТИВЕН"
        }
        println("[$status] $name: $description")
    }
}



class QuestManager{
    // mutableMapOf<String, Quest>() - Создаем изменяемый словарь с квестами, где:
    // String - типом данных ключа (id квеста)
    // Quest - тип значения (объект Квеста)
    private val quests = mutableMapOf<String, Quest>()

    fun addQuest(quest: Quest) {
        // quests[quest.id] = quest - добавляет в словарь по ключу quest.id
        quests[quest.id] = quest
    }
    fun getQuest(questId: String): Quest? {
        return quests[questId]
    }
}



class Player(
    name: String,
    health: Int,
    attack: Int
): Character(name, health, attack) {
    val inventory = Inventory()

    fun usePotion() {
        // используем поиск по id зелья здоровья
        val potion = inventory.findItemById("health_potion")
        if (potion != null) {
            potion.use(this)  // this - это ссылка на текущий объект player
        } else {
            println("У вас нет зельев здоровья!")
        }
    }
    fun pickUpItem(item: Item) {
        inventory.addItem(item)
    }
    fun showInventory(){
        inventory.display()
    }
}

fun main(){
    println("=== СИСТЕМА ИНВЕНТАРЯ ===")

    val player = Player("Oleg", 100, 15)
    val healthPotion = Item(
        "health_potion",
        "Зелье здоровья",
        "Восстанавливает 30 hp",
        25,
        { player ->
            player.health += 30
            println("${player.name} восстанавливает 30 hp!")
        }
    )
    val strengthPotion = Item(
        "strength_potion",
        "Зелье силы",
        "Увеличение урона на 10 (на 3 хода)",
        40,
        { player ->
            println("${player.name} урон усиливается на 10")
        }
    )
    val oldKey = Item(
        "old_key",
        "Старый ключ",
        "Может что то открыть",
        5
    ) // useEffect не указан - он по умолчанию остается {}

    println("=== ИГРА НАЧАЛАСЬ ===")

    println("Игрок нашел предметы")
    // Добавление предметов в инвентарь
    player.pickUpItem(healthPotion)
    player.pickUpItem(strengthPotion)
    player.pickUpItem(oldKey)
    player.pickUpItem(healthPotion)

    player.showInventory()

    println("--- Использование предметов ---")
    player.inventory.useItem(0, player)

    println("--- Поиск предметов ---")
    val foundKey = player.inventory.findItemById("old_key")
    if (foundKey != null) {
        println("Вы открываете дверь, но ключ рассыпается у вас в руках")
    } else {
        println("Мне нужен ключ от этой двери")
    }

    if (player.inventory.hasItem("health_potion")) {
        println("Вы можете похилиться, у вас есть зелье здоровья: ${player.inventory.countItems("health_potion")} штук")
    }
}