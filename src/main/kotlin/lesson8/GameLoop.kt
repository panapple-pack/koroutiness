package lesson8

import kotlin.random.Random
import kotlin.random.nextInt

fun main() {
    println("=== Простая имитация игрового времени и цикла ===")

    val gameTime = GameTime()
    val enemies: MutableList<Enemy> = mutableListOf()

    val player = Player(
        x = 0.0,
        speed = 2.0,
        name = "Oleg",
        maxHealth = 100,
    )

    for (i in Random.nextInt(2,4) - 1 .. 4) {
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

    while (player.isAlive || player.x <= 110) {
        gameTime.update()

        val dt = gameTime.deltaTimeSeconds
        // dt - локальная переменная для deltaTime, чисто для удобства
        // сколько секунд прошло с прошлого кадра

        // обновляем позиции объектов по времени
        player.update(dt)

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
            // distance - расстояние между врагом и игроком по оси Х
            // enemy.x - player.x - обычное вычитание двух Double

            println("Расстояние между игроком и врагом: ${"%.3f".format(distance)}")

            if (distance <= 0.0) {
                // Если расстояние <= 0, считаем, что они встретились или пересеклись
                println()
                println("Игрок и враг столкнулись!")
                while (!player.isAlive || !enemy.isAlive) {
                    player.attack(enemy)
                    enemy.attack(player)
                }
            }
            if (!player.isAlive) {
                break
            }

            // Немного "спим", чтобы цикл не крутился слишком быстро
            Thread.sleep(200)
            // Thread - класс, представляющий поток (здесь главный поток программы)
            // .sleep(200) - приостановить поток на 200 миллисекунд
            // Это нужно только для того, чтобы человек успевал видеть изменения в консоли
        }

    }

    println()
    println("=== Конец демонстрации игрового цикла ===")
}