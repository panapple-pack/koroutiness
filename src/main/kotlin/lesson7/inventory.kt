package lesson7

class Inventory {
    // Обычный класс
    private val items: MutableList<Item> = mutableListOf()
    // private - модификатор доступа: это поле (кусок кода) виден только внутри класса Inventory
    // MutableList<Item> - это тип свойства:
    // MutableList - изменяемый список
    // <Item> - тип, который будет принимать и хранить свойство
    // Создаем пустой изменяемый список, который соответственно примет в себя только объекты класса Item
    val maxSize: Int = 10


    fun currentWeight(): Int {
        var total: Int = 0
        for (item in items) {
            total += item.weight
        }
        return total
    }

    fun findItemByName(name: String): Item? {
        for (item in items) {
            if (item.name == name) {
                println("Найден предмет ${item.name}")
                return item
            }
        }
        println("Ничего не найдено")
        return null
    }

    fun findItemById(id: Int): Item? {
        for (item in items) {
            if (item.id == id) {
                println("Найден предмет ${item.name}")
                return item
            }
        }
        println("Ничего не найдено")
        return null
    }


    fun addItem(item: Item) {
        val itemSize = currentWeight()
        if (itemSize >= maxSize) {
            println("У вас заполнен инвентарь")
            return
        }

        if (item.weight + itemSize < maxSize) {
            items.add(item)

            println("В инвентарь добавлен предмет: ${item.name}")
        } else {
            println("Предмет ${item.name} не поместился в инвентарь")
        }

    }


    fun removeItem(item: Item): Boolean {
        val removed = items.remove(item)

        if (removed) {
            println("Предмет ${item.name} удален")
        } else {
            println("не удалось удалить премет: ${item.name}")
        }
        return removed  // false или true
    }


    fun printInventory() {
        if (items.isEmpty()) {
            println("Инвентарь пуст...")
            return  // досрочно завершит функцию
        }

        println("==== ИНВЕНТАРЬ ИГРОКА ====")
        for (item in items) {
            println("${item.name} {id=${item.id}, цена=${item.price}, вес=${item.weight}}")
        }
    }
}