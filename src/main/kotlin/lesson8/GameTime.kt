package lesson8

class GameTime {
    private var lastTimeMillis: Long = System.currentTimeMillis()
    // private - модификатор доступа для того, чтобы время было видно только внутри класса GameTime
    // Считаем последние время в миллисекундах, на которое будет опираться игровое событие
    // Long - это целое длинное число. Может хранить до 4_294_967_295
    // System - класс для стандартной java - библиотеки
    // .currentTimeMills - метод, который возвращает текущее время в милисекундах

    var deltaTimeSeconds: Double = 0.0
    // delta - это время, прошедшее между двумя кадрами

    var totalTimeSeconds: Double = 0.0
    // подсчет, сколько всего прошло времени с момента запуска игры

    fun update() {
        // метод update мы будем вызывать каждый игровой кадр (Frame)
        // все, что он делает - обновляет deltaTimeSeconds и totalTimeSeconds

        val currentTimeMillis = System.currentTimeMillis()  // текущее время

        val deltaMillis = currentTimeMillis - lastTimeMillis
        // дельта - разница между текущим временем и прошлым кадром

        deltaTimeSeconds = deltaMillis / 1000.0
        // Просто перевод милисекунд в секунды

        totalTimeSeconds += deltaTimeSeconds
        // Накапливаем общее время между прошедшими кадрами за все время игры

        lastTimeMillis = currentTimeMillis
        // Обновляем время предыдущего кадра на текущее
        // В следующем кадре будем считтать разницу уже от этого временного момента
    }
}





// То есть время для нас сейчас не константа, мы каждый раз обновляем время, которое уже прошло
// и за счет этого будет двигаться наш мир и фиксироваться события в нем относительно друг друга