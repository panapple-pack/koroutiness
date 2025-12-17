package lesson8

import lesson7.Item
import kotlin.random.Random
import kotlin.random.nextInt

fun main() {
    println("=== Простая имитация игрового времени и цикла ===")

    val items: MutableList<Item> = mutableListOf()
    val gameTime = GameTime()
    val enemies: MutableList<Enemy> = mutableListOf()

    val sword = Item(
        id = 1,
        name = "sword",
        description = "damager",
        price = 0,
        weight = 0
    )

    val potion = Item(
        id = 2,
        name = "potion",
        description = "healer",
        price = 0,
        weight = 0
    )

    val shield = Item(
        id = 3,
        name = "shield",
        description = "protector",
        price = 0,
        weight = 0
    )

    items.add(sword)
    items.add(potion)
    items.add(shield)

    val player = Player(
        x = 0.0,
        speed = 2.0,
        name = "Oleg",
        maxHealth = 100,
    )

    for (i in Random.nextInt(2,5)  .. 5) {
        val enemy = Enemy(
            x = Random.nextDouble(25.0, 101.0),
            speed = 0.0,   // Скорость -1.0, т.к враг двигается влево (к игроку)
            id = i,
        )
        enemies.add(enemy)
    }


    println("Начальное положение сущность ")
    player.printPosition()
    for (enemy in enemies) {
        enemy.printPosition()
    }

    println()
    println("Начало игры")

    while (player.isAlive) {
        gameTime.update()

        val dt = gameTime.deltaTimeSeconds
        // dt - локальная переменная для deltaTime, чисто для удобства
        // сколько секунд прошло с прошлого кадра

        // обновляем позиции объектов по времени
        if (player.update(dt, items)) break

        // Выводим информацию
        println()
        println("Прошло времени: ${"%.3f".format(gameTime.totalTimeSeconds)} сек")
        // "%.3f".format(...) - форматирование числа:
        // %.3f - число с 3 знаками после запятой (например 1.234)

        player.printPosition()
        for (enemy in enemies) {
            enemy.printPosition()
        }

        // Проверим, "столкнулись" ли игрок и враг
        for (enemy in enemies) {
            val distance = enemy.x - player.x
            println("Расстояние между игроком и врагом ${enemy.id}: ${"%.3f".format(distance)}")
            // distance - расстояние между врагом и игроком по оси Х
            // enemy.x - player.x - обычное вычитание двух Double

        }
        for (enemy in enemies) {
            val distance = enemy.x - player.x
            if (distance <= 0.0) {
                // Если расстояние <= 0, считаем, что они встретились или пересеклись
                println()
                println("Игрок и враг столкнулись!")
                while (player.isAlive && enemy.isAlive) {
                    player.attack(enemy)
                    enemy.attack(player)
                    Thread.sleep(200)
                }
                enemies.remove(enemy)
                break
            }
            if (!player.isAlive) {
                break
            }
        }
        // Немного "спим", чтобы цикл не крутился слишком быстро
        Thread.sleep(200)
        // Thread - класс, представляющий поток (здесь главный поток программы)
        // .sleep(200) - приостановить поток на 200 миллисекунд
        // Это нужно только для того, чтобы человек успевал видеть изменения в консоли
    }

    println()
    println("=== Конец демонстрации игрового цикла ===")
}