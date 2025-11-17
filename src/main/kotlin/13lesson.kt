import jdk.dynalink.Operation
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.log

// Класс для логирования ошибок события игры
class GameLogger {
    private val logFile = "game_log.txt"
    private val fileManager = SaveFileManager()

    // МЕТОД ЗАПИСИ ЛОГОВ В ФАЙЛ
    fun log(message: String, level: String = "INFO") {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val logEntry = "[$timestamp] [$level] $message\n"
        // Проверка на дублирование в консоль важных сообщений

        if (level == "ERROR" || level == "WARN") {
            println("LOG [$level] $message")
        }

        // Запись в файл логирования
        fileManager.writeFileSafely(logFile, logEntry)
    }

    // Методы-помощники для разных уровней логирования
    fun info(message: String) = log(message, "INFO")
    fun warn(message: String) = log(message, "WARN")
    fun error(message: String) = log(message, "ERROR")
    fun debug(message: String) = log(message, "DEBUG")
}


// Базовый класс для всех игровых систем с обработкой ошибок
abstract class GameSystem(val systemName: String, protected val logger: GameLogger) {

    // МЕТОД ДЛЯ БЕЗОПАСНОГО ВЫПОЛНЕНИЯ ОПЕРАЦИЙ СИСТЕМЫ
    // <T> - Объявление обобщенного типа данных
    // T - плейсхолдер (заполнитель) для любого типа данных
    // Надо думать о <T> как о "чем-то временном или абстрактном" как ячейка, в которую подставим какой-то объект


    protected fun <T> executeSafely(operation: String, block: () -> T): T? {
        // Читаем верхнюю строку как:
        // Функция executeSafety работает (выполняется) с каким-то типом Т
        // Она принимает операцию (строку в данный момент) и блок кода, который возвращает Т
        // Сама функция после выполнения возвращает Т? (Т или null)
        try {
            logger.debug("$systemName: Начало операции: $operation")
            val result = block() // Выполняем переданный блок кода
            logger.debug("$systemName: Операция $operation завершена успешно")
            return result
            // Вернуть результат работы
        } catch (e: Exception) {
            logger.error("$systemName: Ошибка операции ${e.message}")
            return null
        }
    }

    // Абстрактный метод для инициализации системы
    abstract fun initialize(): Boolean

    // Абстрактный метод для экстренной остановки системы
    abstract fun emergencyShutDown()


    val resultName: String? = executeSafely("Получение имени игрока") {
        // Здесь может быть неограниченное число кода, которое после вычисления должен вернуть String
        "Oleg"
    }
    val resultInt: Int? = executeSafely("Расчет урона") {
        42
    }
    val resultBool: Boolean? = executeSafely("Проверка жизни") {
        true
    }
}

// Система боя с обработкой ошибок
class CombatSystem(logger: GameLogger): GameSystem("CombatSystem", logger) {
    private var isInitialized = false

    override fun initialize(): Boolean {
        return executeSafely("initialize") {
            // Имитиация инициализации системы боя
            logger.info("Инициализация системы боя...")
            Thread.sleep(100) // имитация загрузки элементов и процессов
            isInitialized = true
            logger.info("Система боя успешно инициализирована")
            true
        } ?: false
    }

    fun performAttack(attacker: String, target: String, damage: Int): Boolean {
        if (!isInitialized) {
            logger.warn("Попытка атаки при инициализированной системы боя")
            return false
        }
        return executeSafely("performAttack") {
            // Проверка корректности введенных параметров
            if (damage < 0) {
                throw IllegalArgumentException("Урон не может быть отрицательным: $damage")
            }
            if (attacker.isBlank() || target.isBlank()) {
                throw IllegalArgumentException("Имена персонажей не могут быть пустыми")
            }

            logger.info("$attacker атакует $target с уроном: $damage")
            true
        } ?: false
    }

    override fun emergencyShutDown() {
        logger.warn("Аварийное завершение системы боя")
        isInitialized = false
        // здесь в будущем освобождение ресурсов, сохранение состояний и т.д.
    }
}

class InventorySystem(logger: GameLogger): GameSystem("InventorySystem", logger) {
    private val items = mutableListOf<String>()
    private var isInitialized = false

    override fun initialize(): Boolean {
        return executeSafely("initialize") {
            logger.info("Инит. системы инвентаря...")
            // Загрузка предметов по умолчанию при создании игрока
            items.addAll(listOf("Старый меч", "Поношенный доспех"))
            isInitialized = true
            logger.info("Система инвентаря инит. успешно")
            true
        } ?: false
    }

    fun addItem(item: String): Boolean {
        if (!isInitialized) {
            logger.warn("Попытка добавить предмет в инвентарь без инит. системы")
            return false
        }
        return executeSafely("addItem") {
            if (item.isBlank()) {
                throw IllegalArgumentException("Название предмета не может быть пустым")
            }
            if (items.size >= 20) {
                throw IllegalArgumentException("Инвентарь переполнен (Максимум 20 предметов)")
            }

            items.add(item)
            logger.info("Предмет $item добавлен в инвентарь. Всего предметов: ${items.size}")

            true
        } ?: false
    }

    fun getItems(): List<String> {
        if (!isInitialized) {
            logger.warn("Попытка получить предметы без инит. системы")
            return emptyList()
        }
        return executeSafely("getItems") {
            items.toList() // Возвращение копии списка
        } ?: emptyList()
    }

    override fun emergencyShutDown() {
        if (!isInitialized) {
            logger.warn("Аварийное отключение системы инвентаря")
            return
        }
        // Логирование warn экстренного отключения системы инвентаря
        // Сохранение состояния инвентаря перед отключением
        // Создание бэкап-списка (использовать метод joinToString("\n"))
        // Используя метод File, в атрибут которого мы кладем название_файла_бэкапа.txt + записать в файл созданный бэкап список - writeText(бэкап-список)
        // Проверка на инициализацию должна стать false
    }
}
