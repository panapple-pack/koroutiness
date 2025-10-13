//import kotlinx.coroutines.*  // Импорт библиотеки корутин (* - значит импортировать всё)
//import kotlin.system.measureTimeMillis // Функция измерения выполнения
//import kotlin.time.Duration
//import kotlin.time.DurationUnit
//import kotlin.time.ExperimentalTime
//
//// КЛАСС ДЛЯ УПРАВЛЕНИЯ ИГРОВЫМ ВРЕМЕНЕМ
//
//class GameTime {
//    // L - Long - тип данных для больших чисел (в миллисекундах)
//    private var lastFrameTime = 0L
//
//    val deltaTime: Float
//        get() {
//            val currentTime = System.currentTimeMillis()  // Возвращает текущее время в милисекундах
//            val delta = (currentTime - lastFrameTime) / 1000f  // 1000f - преобразование милисекунд в секунды
//            lastFrameTime = currentTime // Обновление времени последнего кадра
//
//            return delta
//        }
//
//    fun initialize() {
//        lastFrameTime = System.currentTimeMillis()  // Инициализируем время при старте игры
//    }
//}
//
//
//// Базовый класс для игровых объектов с анимацией
//open class GameObject(var x: Float, var y: Float) {
//    // Скорости в единицах в секунду (не в кадрах)
//    open val speed: Float = 50f  // 50 пикселей в секунду
//}
