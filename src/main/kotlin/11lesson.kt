import java.io.File
import kotlin.random.Random

class SaveCharacter(val name: String, var health: Int) {

    // функция с возможной ошибкой расчета урона (потому что может поделиться на ноль)
    fun calculateDamageRatio(defence: Int): Double {
        return 100.0 / defence   // если в defence будет = 0 произойдет деление на ноль (ArithmeticException)
    }

    fun demonstrateCommonErrors() {
        try {
            // try - ключевое слово для перехвата ошибок (обозначает блок попытки)
            // код внутри try выполняется нормально
            println("Input number of attack power: ")
            val input = readln()
            // toInt() - метод, который преобразовывает строку в число
            // Он выбросит ошибку NumberFormatException - если введены не цифры
            val attackPower = input.toInt()
            println("Attack power is setup to: $attackPower")
        } catch (e: NumberFormatException) {
            // catch - ключевое слово для перехвата ошибки в случае возникновения ошибки
            // e: NumberFormatException - переменная е типа NumberFormatException
            // этот блок catch выполнится только если произошла ошибка указанного типа
            println("[ERROR] Input is not a number! Using default number: 10")
            val attackPower = 10
            println("Attack power is setup to $attackPower")
        }

        try {
            val items = arrayOf("sword", "shield", "potion")
            println("Choose number of item (1-3): ")
            val index = readln().toInt() - 1  // преобразуем в индекс (0-2)
            // [index] может выбросить ArrayIndexOutOfBoundsException
            val selectedItem = items[index]
            println("You choose $selectedItem")
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("wrong number of item, chosen default item")
            val selectedItem = "Shield"
            println("You choose: $selectedItem")
        } catch (e: NumberFormatException) {
            println("[ERROR] Input is not a number! Chosen default item")
            val selectedItem = "Shield"
            println("Attack power is setup to $selectedItem")
        }

        try {
            println("Input defend number (NOT A ZERO PLEASE)")
            val defence = readln().toInt()
            val ratio = calculateDamageRatio(defence)
            println("Damage Ratio: $ratio")
        } catch (e: ArithmeticException) {
            println("dude, you divide by zero, used default ratio: 1.0")
            val ratio = 1.0
            println("Damage Ratio: $ratio")
        }

        // Общий обработчик для любых исключений
        try {
            println("Input game difficult (1-easy, 2-medium, 3-INSANE): ")
            val difficulty = readln().toInt()
            val enemyhealth = when(difficulty) {
                1 -> 50
                2 -> 100
                3 -> 300
                else -> throw IllegalArgumentException("Unknown Difficult") // throw Сами бросаем исключение
            }
            println("Enemy Health: $enemyhealth")
        } catch (e: Exception) {
            // exception - базовый класс для всех возможных исключений (поймает любую ошибку)
            println("Error! ${e.message}. Used default difficult")
            val enemyHealth = 100
            println("Enemy Health: $enemyHealth")
        }
    }
    fun demonstrateFinally() {
        println("\n---Finally block---")
        try {
            println("Input attack bonus")
            val bonus = readln().toInt()
            println("Bonus used $bonus")
        } catch (e: NumberFormatException) {
            println("Unknown number format. Bonus not used")
        } finally {
            // finally - блок, который выполняется всегда, независимо от того, была ошибка или нет
            // обычно используется для завершения или очистки ресурсов (закрытие файлов, соединений и т.д.)
            println("Block finally is completed, resources cleared successfully")
        }
    }
}

class SafeInput {
    // Функция для безопасного получения числа
    fun getSaveInput(prompt: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int {
        while (true) {
            try {
                println(prompt)
                val input = readln()
                val number = input.toInt() // Может выдать ошибку

                // Проверка диапазона
                if (number in min..max) {
                    return number
                } else {
                    println("Number must be in range of $min to $max. Try again")
                }
            } catch (e: NumberFormatException) {
                println("ERROR please input integer number. Try again")
            }
        }
    }
}