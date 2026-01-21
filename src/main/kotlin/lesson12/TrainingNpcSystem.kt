package lesson12

import lesson11.EventBus
import lesson11.GameEvent

class TrainingNpcSystem {
    private val npc = TrainingNpc("Trainer")

    fun register() {
        EventBus.subscribe { event ->
            when(event) {
                is GameEvent.DialogueStarted -> {
                    npc.onDialogueStarted(event.playerId)
                }
                is GameEvent.DialogueChoiceSelected -> {
                    var state = npc.state
                    npc.onDialogueChoiceSelected(event.playerId, event.choiceId)
                }
                else -> {}
            }
        }
    }

    fun playerApproaches(playerId: String) {
        npc.onPlayerApproached(playerId)
    }
}