import java.io.File
import java.io.IOException
import java.security.Security

// Класс для безопасной работы с файлами
class SaveFileManager {
    // Метод для безопасного чтения содержимого файла

    fun readFileSafely(filename: String): String? {
        return try {
            // Пытаемся прочитать файл
            val file = File(filename)
            if (!file.exists()) {
                // Если файл не существует - это не исключение, это ошибка логики
                println("Error: file '$filename' don't exist")
                return null
            }
            file.readText() // Прочитать в виде текста содержимое файла
        } catch (e: SecurityException) {  // Нет прав на чтение файла
            println("Mismatch permissions for this file: $filename")
            null
        } catch (e: IOException) {  // Общая ошибка ввода-вывода (появляется в случаях переполнения диска, повреждения файла или диска) и т.д.
            println("Cannot read file: '$filename' - ${e.message}")
            null
        } catch (e: Exception) {
            println("Unknown error: ${e.message}")
            null
        }
    }
    fun writeFileSafely(filename: String, content: String): Boolean {
        return try {
            val file = File(filename)

            // Проверка, можно ли создать файл
            if (file.parentFile != null && !file.parentFile.exists()) {
                // Создаем директорию, если такой еще нет
                file.parentFile.mkdirs()
            }

            file.writeText(content)
            println("File: '$filename' successfully saved")
            true
        } catch (e: SecurityException) {
            println("Mismatch permissions for this file: $filename")
            false
        } catch (e: IOException) {
            println("Cannot write to file: '$filename' - ${e.message}")
            false
        }
    }
    fun deleteFileSafely(filename: String): Boolean {
        return try {
            val file = File(filename)
            if (!file.exists()) {
                println("File $filename dont exist")
                return false
            }

            if (file.delete()) {
                println("File $filename deleted successfully")
                true
            } else {
                println("Cant delete a file: $filename")
                false
            }
        } catch (e: SecurityException) {
            println("Mismatch permissions for this file: $filename")
            false
        }
    }
}

// Класс для системы сохранения игры с обработкой ошибок
class RobustSaveSystem{
    private val fileManager = SaveFileManager()
    private val saveDir = "game_saves"

    // Функция сохранения данных игрока
    fun savePlayerData(playerName: String, health: Int, level: Int, inventory: List<String>): Boolean {
        // Создание содержимого для сохранения
        val content = """
            Игрок: $playerName
            Здоровье: $health
            Уровень: $level
            Инвентарь: ${inventory.joinToString(", ")}
        """.trimIndent()

        // Создаем имя файла с timestamp меткой для уникальности сохранения
        val timestamp = System.currentTimeMillis()
        val filename = "$saveDir/save_${playerName}_$timestamp.txt"

        return fileManager.writeFileSafely(filename, content)

        // Функция для загрузки данных игрока из сохранения
    }
    fun loadPlayerData(filename: String): Map<String, String>? {
        val content = fileManager.readFileSafely(filename)
        // Проверка на то, не пустой ли наш content

        return try {
            // Парсим содержимое файла
            val data = mutableMapOf<String, String>()
            content?.lines()?.forEach { line ->
                if (line.contains(":")) {
                    val parts = line.split(":", limit = 2)
                    if (parts.size == 2) {
                        val key = parts[0].trim()
                        val value = parts[1].trim()
                        data[key] = value
                    }
                }
            }
            data
        } catch(e: Exception) {
            println("Parsing error: undefined file type - ${e.message}")
            null
        } // Перехватчик исключений на то, верный ли формат файла сохранения
    }

    // Функция получения списка сохранений
    fun listsaves(): List<String> {
        return try {
            val dir = File(saveDir)
            if (!dir.exists()) {
                return emptyList()
            }
            dir.list()?.toList() ?: emptyList()
        } catch (e: Exception) {
            println("ERROR: You have not permissions to save directory")
            emptyList()
        }
    }

    // ФУНКЦИЯ ДЛЯ СОЗДАНИЯ РЕЗЕРВНОЙ КОПИИ
    fun  createBackup(originalFilename: String): Boolean {
        return try {
            val originalFile = File(originalFilename)
            if (!originalFile.exists()) {
                println("Original file doesnt exists: $originalFilename")
                return false
            }

            val backupFilename = "$originalFilename.backup"
            val content = originalFile.readText()
            fileManager.writeFileSafely(backupFilename, content)
        } catch (e: Exception) {
            println("ERROR write create backup: ${e.message}")
            false
        }
    }
}

// КЛАСС ВАЛИДАЦИИ ДАННЫХ ИГРЫ
class GameDataValidator {

    // Метод для проверки данных игрока
    fun validatePlayerData(name: String, health: Int, level: Int): Boolean {
        val errors = mutableListOf<String>()

        // Проверка имени игрока
        if (name.isBlank()) {
            errors.add("Name cannot be empty")
        }
        if (name.length < 2) {
            errors.add("Name cannot be less than 2 characters")
        }
        if (name.length > 20) {
            errors.add("Name cannot be more than 20 characters")
        }
        if (name.any {it.isDigit()}) {
            errors.add("Name cannot contains numbers")
        }

        // Проверка здоровья
        if (health < 0) {
            errors.add("Health cannot be negative")
        }
        if (health > 1000) {
            errors.add("health cannot be more, than 1000")
        }

        // Проверка уровней
        if (level < 1) {
            errors.add("Level cannot be less than 1")
        }
        if (level > 100) {
            errors.add("Level cannot be more, than 100")
        }

        if (errors.isNotEmpty()) {
            // throw - явный метод чтобы сказать программе что выполнение завершилось с исключением
            throw GameDataException("GameDataValidateError", errors)
        }
        return true
    }
}

// Кастомное исключение для ошибок данных нашей игры

class GameDataException(message: String, val validationErrors: List<String>): Exception(message) {
    fun printValidationErrors() {
        println("DETECTED VALIDATION ERRORS:")
        validationErrors.forEachIndexed { index, error ->
            println(" ${index + 1}. $error")
        }
    }
}

fun main() {
    println("=== СИСТЕМА СОХРАНЕНИЯ ИГРЫ ===")

    val saveSystem = RobustSaveSystem()
    val validator = GameDataValidator()
    val saveInput = SafeInput()

    // Демонстрация сохранения игры
    println("--- Сохранение данных игрока ---")

    val playerName = "Oleg"
    val health = 100
    val level = 5
    val inventory = listOf("Меч", "Щит", "Зелье маны")

    if (saveSystem.savePlayerData(playerName, health, level, inventory)) {
        println("Player data saved successfully")
    } else {
        println("Failed to save player data")
    }

    println("\n--- Загрузка данных игрока ---")
    val saves = saveSystem.listsaves()
    if (saves.isNotEmpty()) {
        val firstSave = saves.first()
        val loadedData = saveSystem.loadPlayerData("game_saves/$firstSave")

        if (loadedData != null) {
            println("Loaded data")
            loadedData.forEach {(key, value) ->
                println("$key: $value")
            }
        }
    } else {
        println("Saves not found")
    }

    // Валидация данных
    try {
        validator.validatePlayerData("Oleg", 100, 10)
        println("Player data is correct")
    } catch (e: GameDataException) {
        e.printValidationErrors()
    }

    try {
        validator.validatePlayerData("g", -3, 10000)
        println("Player data is correct")
    } catch (e: GameDataException) {
        e.printValidationErrors()
    }
}