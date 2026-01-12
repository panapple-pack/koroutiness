package lesson10

import org.w3c.dom.events.EventListener

// Object - класс Singleton
// Singleton - это объект в единственном экземпляре
// Это очень важно, т.к. объект, отвечающий за события, должен быть только один
// то есть EventBus руководит и хранит в себе информацию о событиях (Сердце игровой логики)

object EventBus {
    // Список всех слушателей (подписчиков)
    private val listeners = mutableListOf<(GameEvent) -> Unit>()
    // (GameEvent) -> Unit - функция лямбда
    // Принимает GameEvent, а возвращает Unit (По умолчанию ничего, пустая)

    fun subscribe(listener: (GameEvent) -> Unit) {
        // subscribe - метод подписки на события

        listeners.add(listener)
        // add - добавления слушателя в конец списка слушателей

        println("Новый подписчик добавлен! Всего подписчиков: ${listeners.size}}")
    }

    fun publish(event: GameEvent) {
        // Публикация событий для слушателей
        println("Событие опубликовано: $event")

        // Проходимся по всем подписчикам данного события
        for (listener in listeners) {
            // Вызываем функцию слушателя с конкретным событием
            listener(event)
        }
    }
}
// То есть EventBus сам по себе не знает что такое NPC, Quest, UI
// Он просто как рассылка, которая говорит: вот событие - кто подписан - реагируйте