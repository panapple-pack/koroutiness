package lesson8

fun main() {
    println("=== Простая имитация игрового времени и цикла ===")

    val gameTime = GameTime()

    val player = Player(
        x = 0.0,
        speed = 2.0,
        name = "Oleg"
    )

    val enemy = Enemy(
        x = 10.0,
        speed = -1.0,   // Скорость -1.0, т.к враг двигается влево (к игроку)
        id = 1
    )

    println("Начальное положение сущность ")
    player.printPosition()
    enemy.printPosition()

    println()
    println("Начало игры")

    while (true) {
        gameTime.update()

        val dt = gameTime.deltaTimeSeconds
        // dt - локальная переменная для deltaTime, чисто для удобства
        // сколько секунд прошло с прошлого кадра

        // обновляем позиции объектов по времени
        player.update(dt)
        enemy.update(dt)

        // Выводим информацию
        println()
        println("Прошло времени: ${"%.3f".format(gameTime.totalTimeSeconds)} сек")
        // "%.3f".format(...) - форматирование числа:
        // %.3f - число с 3 знаками после запятой (например 1.234)

        player.printPosition()
        enemy.printPosition()

        // Проверим, "столкнулись" ли игрок и враг
        val distance = enemy.x - player.x
        // distance - расстояние между врагом и игроком по оси Х
        // enemy.x - player.x - обычное вычитание двух Double

        println("Расстояние между игроком и врагом: ${"%.3f".format(distance)}")

        if (distance <= 0.0) {
            // Если расстояние <= 0, считаем, что они встретились или пересеклись
            println()
            println("Игрок и враг столкнулись! Останавливаем игровой цикл.")
            break
            // break - выходим из цикла while (true)
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