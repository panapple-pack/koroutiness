package lesson11

class NpcSystem {
    fun register() {
        EventBus.subscribe { event ->
            when(event) {
                is GameEvent.QuestStarted -> {
                    println("[NPC-СИСТЕМА] Старый теперь ждет результата выполнения квеста")
                    EventBus.post(GameEvent.DialogueLineUnlocked("Старый", "Completed"))
                }
                is GameEvent.QuestStepCompleted -> {
                    if (event.stepId == "kill_Tolik")
                    println("[NPC-СИСТЕМА] Открыта реплика 'ты убил толика?'")
                    EventBus.post(GameEvent.DialogueLineUnlocked("Старый", "kill_Tolik"))
                }
                is GameEvent.QuestCompleted -> {
                    println("[NPC-СИСТЕМА] Квест завершен, открываем диалог с наградой")
                    EventBus.post(GameEvent.DialogueLineUnlocked("Старый", "congrats"))
                }
                else -> {}
            }
        }
    }
}