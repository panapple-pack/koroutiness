package lesson10

class Quest(
    val id: String,
    val targetCharacter: String  // Примитивный квест на убийство персонажа
) {
    var isCompleted: Boolean = false
    // Проверка (флаг) выполнен ли квест

    fun register() {
        // register - регистрация квеста в EventBus - подписка на события

        EventBus.subscribe { event ->
            // Эта функция будет вызываться каждый раз, когда в игру публикуется событие

            when(event) {
                is GameEvent.CharacterDied -> {
                    if (event.characterName == targetCharacter && !isCompleted) {
                        // Если пришло событие смерти персонажа
                        // и квест все еще не выполнен

                        isCompleted = true
                        println("Квест '$id' выполнен! Персонаж ${event.characterName} убит")

                        EventBus.publish(
                            GameEvent.QuestCompleted(id)
                        )
                    }
                } else -> {
                    // else - все остальные события нас не интересуют, мы на них не подписаны (не слушаем их)
                }
            }
        }
    }
}
































