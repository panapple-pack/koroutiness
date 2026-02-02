package lesson13

import lesson11.EventBus
import lesson11.GameEvent

fun main() {
    val system = TrainingStateSystem()
    system.register()

    val player = "Oleg"

    EventBus.post(GameEvent.DialogueStarted(player, "Тренер", player))
    EventBus.processQueue()

    EventBus.post(GameEvent.DialogueStarted(player, "Тренер", player))
    EventBus.processQueue()

    EventBus.post(GameEvent.DialogueChoiceSelected(player, "Тренер", player, "accept"))
    EventBus.processQueue()

    EventBus.post(GameEvent.CharacterDied(player, "Макан", player))
    EventBus.processQueue()

    EventBus.post(GameEvent.DialogueChoiceSelected(player, "Тренер", player, "finish"))
    EventBus.processQueue()
}