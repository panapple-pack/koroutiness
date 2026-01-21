package lesson12

import lesson11.EventBus
import lesson11.GameEvent


class TrainingNpc(val name: String, var state: NpcState = NpcState.IDLE) {
    fun onPlayerApproached(playerId: String) {
        // Игрок подошел
        if (state == NpcState.IDLE) {
            val oldState = state
            println("$name: Привет, $playerId")
            state = NpcState.WAITING
            println("[INFO] Теперь наш $name в состоянии WAITING")
            EventBus.post(GameEvent.NpcStateChanged(playerId, name, oldState, state))
            println("[INFO] NPC поменял состояние с $oldState на $state")
        }
    }

    fun onDialogueStarted(playerId: String) {
        if (state == NpcState.WAITING) {
            val oldState = state
            println("$name: Я научу тебя драться с бомжами")
            state = NpcState.TALKING
            println("[INFO] Теперь $name в состоянии TALKING")
            EventBus.post(GameEvent.NpcStateChanged(playerId, name, oldState, state))
            println("[INFO] NPC поменял состояние с $oldState на $state")
        }
    }

    fun onDialogueChoiceSelected(playerId: String, choiceId: String) {
        if (state == NpcState.TALKING && choiceId == "accept") {
            println("$name: Хорош, учить тебя не буду, держи награду за решимость")
            state = NpcState.REWARDED
            println("[INFO] Теперь $name в состоянии REWARDED")
        }
        if (state == NpcState.TALKING && choiceId == "deny") {
            val oldState = state
            println("$name: Я все равно не буду тебя учить + я angry")
            state = NpcState.ANGRY
            println("[INFO] Теперь $name в состоянии ANGRY")
            EventBus.post(GameEvent.NpcStateChanged(playerId, name, oldState, state))
            println("[INFO] NPC поменял состояние с $oldState на $state")
        }
    }
    // Конкретный NPC ничего не знает о событиях напрямую и о их существовании
    // События лишь будут влиять на его состояния state
}