package lesson11

object EventBus {
    // Типы слушателей (callback)

    typealias Listener = (GameEvent) -> Unit
    // typealias - псевдоним для типов данных (очень грубо говоря переменная, хранящая в себе какой то тип данных)
    // Список подписчиков
    private val listeners = mutableMapOf<Int, Listener>()

    private var nextId: Int = 1
    private val eventQueue = ArrayDeque<GameEvent>()
    // ArrayDeque - двусторонняя очередь
    // с ней можно взаимодействовать как с начала, так и с конца
    // Здесь будем хранить события, которые будем обрабатывать либо позже, либо когда получим подтверждение

    fun subscribe(listener: Listener): Int {
        val id = nextId
        nextId++;

        listeners[id] = listener
        println("Подписчик добавлен id=$id. Всего подписчиков: ${listeners.size}")
        return id
    }

    fun unsubscribe(id: Int) {
        val removed = listeners.remove(id)
        // removed может быть null, потому что id может быть получен неправильно

        if (removed != null) {
            println("Подписчик удален id=$id")
        } else {
            println("Не удалось отписаться, неправильный id=$id")
        }
    }

    fun subscribeOnce(listener: Listener): Int {
        // Подписка на 1 раз, после одного полученного события слушатель сам отписывается
        var id: Int = -1
        id = subscribe { event ->
            // Создание обычной подписки
            listener(event)
            // Вызов слушателя исходного
            unsubscribe(id)
            // Вызов отписки после первого прослушивания
        }
        return id
    }

    fun publish(event: GameEvent) {
        println("Событие опубликовано: $event")

        for (listener in listeners.values) {
            // listeners.values - все значение словаря перебираем (все функции-слушатели)
            listener(event)
        }
    }

    fun post(event: GameEvent) {
        // post - кладем события в очередь (то есть не обрабатываем сразу)
        eventQueue.addLast(event)
        // addLast - добавить в конец очереди

        println("Событие добавлено в очередь $event (в очереди: ${eventQueue.size - 1}")
    }

    fun processQueue(maxEvents: Int = 10) {
        // Метод обработки событий из очереди

        var processed = 0  // Сколько обработали событий
        while (processed < maxEvents && eventQueue.isNotEmpty()) {
            val event = eventQueue.removeFirst() // Возвращает первый элемент их очереди и удаляет его

            publish(event)
            processed++
        }
    }
}