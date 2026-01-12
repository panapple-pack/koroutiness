package lesson10

fun main() {
    println("==== СИСТЕМА ПОИСКА СОБЫТИЙ ====")

    val quest = Quest(
        id = "kill_sahar",
        targetCharacter = "Сахарок"
    )

    val npc = Npc("кирилл 42")

    // Создали нпс и квест для него, но как надо их зарегистрировать как слушателей событий
    quest.register()
    npc.register()

    // Вызываем игровое событие
    EventBus.publish(
        GameEvent.EffectApplied(
            "кирилл 42",
            "беспрерывного поноса"
        )
    )

    // Вызываем событие смерти сахара
    EventBus.publish(
        GameEvent.CharacterDied(
            characterName = "Сахарок"
        )
    )
}