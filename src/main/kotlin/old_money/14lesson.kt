package old_money

import kotlinx.coroutines.*
import kotlin.text.iterator

// Корутины - легкие потоки, они позволяют выполнять сразу нескорлько задач одновременно без основного потока
// GlobalScope - область видимости для корутин, где будут "жить" корутины во время всего приложения
// launch - запуск новой корутины
// delay - приостановка корутины на указанное нами время, не блокируя нам другие потоки (корутины)

// Тестовый класс с демонстрацией корутин
class BasicCoroutinesDemo {
    // Функция с задержкой
    // suspend - ключевое слово, которое отмечает функцию, которую можно остановить
    // !!! suspend функции можно вызвать только из других suspend функции или корутин
    suspend fun simpleDelayDemo() {
        println("=== ДЕМО ЗАДЕРЖКИ КОРУТИНЫ ===")
        println("Начало выполнения: ${System.currentTimeMillis()}")

        // 1000L - время в миллисекундах (1000 мс - 1 сек)
        delay(1000L) // Приостановка корутины на 1 сек
        println("Прошла 1 секунда: ${System.currentTimeMillis()}")

        delay(500L)
        println("Прошло еще 0.5 секунд: ${System.currentTimeMillis()}")
    }

    fun multipleCoroutinesDemo() {
        println("=== Demo запуска несколько корутин ===")

        // GlobalScope.launch - запускает НОВУЮ корутину в глобальной области видимости
        // Внутри GlobalScope {} - лямбда функция или же тело запускаемой корутины
        GlobalScope.launch {
            // Это первая корутина
            println("Корутина 1 - начала работу")
            delay(1000L)
            println("Корутина 1 завершила свое выполнение")
        }

        GlobalScope.launch {
            // Это вторая корутина
            println("Корутина 2 - начала работу")
            delay(500L)
            println("Корутина 2 завершила свое выполнение")
        }

        GlobalScope.launch {
            // Это третья корутина
            println("Корутина 3 - начала работу")
            delay(2000L)
            println("Корутина 3 завершила свое выполнение")
        }

        println("Все корутины завершены! Основной поток продолжает свою работу...")
    }

    suspend fun animationDemo() {
        println("\n === DEMO анимации ===")

        val playerName = "Oleg"

        // Анимация появления текста по буквам
        for (i in 1..4) {
            print(".")
            delay(200L)
        }
        println()

        val message = "Добро пожаловать, $playerName!"
        for (char in message) {
            print(char)
            delay(100L)
        }
        println()
        println("\nЗагрузка игры...")
        val loadingFrames = listOf("⣾", "⣽", "⣻", "⢿", "⡿", "⣟", "⣯", "⣷")

        repeat(17) { frame ->
            // % - оператор деления с остатком
            val frameChar = loadingFrames[frame % loadingFrames.size]
            print("\r$frameChar Загрузка... ${frame * 6}%") // \r - возврат каретки (перезапись прошлой строки)
            delay(100L)
        }
        println("\nЗагрузка завершена")
    }

    // Ожидание завершения корутин
    suspend fun waitingForCoroutines() {
        // job1 - объект, который представляет в себе запущенную корутину
        // job1 нужен для управления корутиной (например отменой выполнения или ожиданием)
        val job1 = GlobalScope.launch {
            println("JOB1: Пример долгой корутины... (задачи)")
            delay(2500L)
            println("JOB1: Долгая задача завершена")
        }
        val job2 = GlobalScope.launch {
            println("JOB2: Пример быстрой корутины... (задачи)")
            delay(500L)
            println("JOB2: Быстрая задача завершена")
        }

        println("Ожидаем завершения обеих задач...")

        // join() - приостанавливает текущую корутину до завершения job
        job1.join() // ждем завершения первой корутины
        job2.join() // ждем завершения второй корутины
        println("Все задачи завершены")
    }


}


// Пример работы с корутинами
// runBlocking - специальная функция, которая создает корутину и блокирует текущий поток до ее завершения
fun main() = runBlocking{
    // old_money.main - основная корутина, которая блокирует поток программы до завершения всех корутин внутри нее
    val demo = BasicCoroutinesDemo()

    // последовательный запуск демонстраций
    demo.simpleDelayDemo()
    demo.multipleCoroutinesDemo()

    // Ждать нужно чтобы асинхронные корутины успели выполниться
    delay(2000L)

    demo.animationDemo()
    demo.waitingForCoroutines()
    println("=== ЗАВЕРШЕНИЕ ТЕСТА КОРУТИН ===")
}

























