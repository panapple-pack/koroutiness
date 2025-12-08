package Advanced_lesson7

class Inventory(
    private val maxSlots: Int   // максимальное число слотов инвентаря
) {
    private val slots: MutableList<InventorySlot> = mutableListOf()

    fun addItem(item: Item, amount: Int = 1) {
        if (amount <= 0) {
            println("Нелья положить неположительное число х$amount предметов ${item.name}")
            return
        }
        var remaining = amount   // Сохранение числа предметов, которых еще можно добавить

        // 1. Пытаемся доложить в уже существующие слоты с таким же предметом, но не заполненные
        for (slot in slots) {
            // Перебираем все слоты в инвентаре - временная переменная слот будет кждую итерацию в себя каждый элемент списка слотов

            if (slot.item.id == item.id) {
                val freeSpace = item.maxStackSize - slot.quantity
                // Сколько еще предметов можно положить в слот до его заполнения
                // maxStackSize - максимальный стак предметов уже лежит в данном слоте
                // slot.quantity - сколько таких предметов уже лежит в даном слоте

                if (freeSpace > 0) {
                    // Проверка на то, есть ли место для докладки в слот
                    val toAdd = minOf(remaining, freeSpace)
                    // toAdd - сколько реально положим в данный слот
                    // берем минимально от:
                    // сколько осталось добавить (remaining)
                    // сколько помещается в слот (осталось свободного места с учетом лежащих там уже)

                    slot.quantity += toAdd
                    remaining -= toAdd  // Уменьшаем число предметов, которые еще не положены в инвентаре

                    println(" + добавлено x$toAdd предмета ${item.name} в существующий слот. Теперь в слоте: ${slot.quantity}")
                    if (remaining == 0) {
                        // если не осталось предметов для покладки в инвентарь, то закончить метод
                        return
                    }
                }
            }
        }

        // 2.  Если оставить предметы, то есть не поместились в существующие слоты, то пробуем создать новый слот
        while (remaining > 0) {
            // Выполняется пока осталось что класть в инвентарь

            if (slots.size >= maxSlots) {
                println("Инвентарь переполнен! Не удалось положить x$remaining ${item.name}")
                return
            }

            val toAdd = minOf(remaining, item.maxStackSize)

            val newSlot = InventorySlot(item, toAdd)

            println("Создан новый слот для ${item.name} с количеством $toAdd")

            remaining -= toAdd
        }
    }

    fun removeItem(item: Item, amount: Int = 1): Boolean {
        if (amount <= 0) {
            println("! знаешь что, я так подумал, ты обнаглел в край")
        }
        var remaining = amount
        for (i in slots.size -1 downTo 0) {
            // -1 - то для получения последнего индекса из списка
            // downTo 0 - счет перебора по убыванию вниз до 0 включительно
            val slot = slots[i]

            if (slot.item.id == item.id) {
                if (slot.quantity <= remaining) {
                    remaining -= slot.quantity
                    println("Удален слот с предметом ${item.name} в количестве ${slot.quantity}")
                    slots.removeAt(i)  // метод удаления элемента по индексу

                    if (remaining == 0) {
                        return true
                    }
                } else {
                    // Если предметов в ячейке больше, чем то число, которое мы хотим удалить
                    slot.quantity -= remaining
                    println("Уменьшено количество ${item.name} в слоте на $remaining")
                    return true
                }
            }
        }

        // Если сюда дошли - то есть не удалось удалить все нужное число предметов в слотах
        println("Не удалось удалить x$amount предмета ${item.name} таких предметов больше нет в инвентаре")
        return false
    }

    fun printInventory() {
        if (slots.isEmpty()) {
            println("Инвентарь пуст")
            return
        }

        println("\n=== Инвентарь (слотов ${slots.size} / $maxSlots) ===")

        for ((index, slot) in slots.withIndex()) {
            // withIndex - возвращает пару (индекс и значение данного индекса)
            // временные переменные index и slot нужно для хранения и обработки данной пары каждую итерацию

            println(
                "Слот ${index + 1}: ${slot.item.name} " +
                "| тип=${slot.item.type} " +
                "| кол-во=${slot.quantity} / ${slot.item.maxStackSize}"
            )
        }
    }

    fun getTotalCountOf(item: Item): Int {
        var total = 0
        for (slot in slots) {
            if (item.id == slot.item.id) {
                total ++;
            }
        }
        return total
    }
}

// Подсчитать общее кол-во предмета любого типа
// добавить в Inventory функцию
    // fun getTotalCountOf(item: Item): Int
    // Он должна пройти по всем slots вернуть суммарное кол-во quantity для слотов с этим item.id