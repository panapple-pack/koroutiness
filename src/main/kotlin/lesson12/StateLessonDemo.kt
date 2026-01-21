package lesson12

import lesson11.EventBus
import lesson11.GameEvent


fun main() {
    val npcSystem = TrainingNpcSystem()
    npcSystem.register()

    val playerId = "Oleg"

    println("$playerId подходит к NPC")
    npcSystem.playerApproaches(playerId)

    println("$playerId начинает диалог")
    EventBus.post(GameEvent.DialogueStarted(playerId, "Trainer", playerId))
    EventBus.processQueue()

    println("$playerId выбирает вариант 'accept'")
    EventBus.post(GameEvent.DialogueChoiceSelected(playerId, "Trainer", playerId, "accept"))
    EventBus.processQueue()
}