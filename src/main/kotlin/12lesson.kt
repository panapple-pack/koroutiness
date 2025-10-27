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
    fun deleteFileSavely(filename: String): Boolean {
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
            content.lines().forEach { line ->
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
        } catch (e: ) // Перехватчик исключений на то, верный ли формат файла сохранения
    }
}