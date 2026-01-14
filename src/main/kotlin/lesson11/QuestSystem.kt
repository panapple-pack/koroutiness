package lesson11

class QuestSystem {
    // Флаги

    private var questStarted: Boolean = false
    private var stepTalked: Boolean = false
    private var stepKilled: Boolean = false
    private var stepReportedBack = false

    private val questId = "ultra_mega_pro_quest_001"

    fun register() {
        EventBus.subscribe { event ->
            when(event) {
                is GameEvent.DialogueStarted -> {
                    if (event.npcName == "Старый" && !questStarted) {
                        questStarted = true
                        stepTalked = true
                        println("Квест $questId начат через диалог с ${event.npcName}")

                        EventBus.post(GameEvent.QuestStarted(questId))
                        EventBus.post(GameEvent.QuestStepCompleted(questId, "talk_to_elder"))
                    }
                }
                is GameEvent.CharacterDied -> {
                    if (questStarted && event.characterName == "Tolik" && event.killerName == "Oleg") {
                        stepKilled = true
                        println("Шаг квеста: Толика зарубили")
                        EventBus.post(GameEvent.QuestStepCompleted(questId, "kill_Tolik"))
                    }
                }
                is GameEvent.DialogueChoiceSelected -> {
                    if (questStarted && event.npcName == "Старый" && event.choiceId == "report_done") {
                        stepReportedBack = true
                        println("Игрок сообщил старому, что выполнил квест")
                        EventBus.post(GameEvent.QuestStepCompleted(questId, "report_back"))
                    }
                }
                else -> {}
            }

            // Проверка на завершение квеста после любого из полученных событий
            // Проверяем, что квест начался, что начали разговор, что убили цель, что сообщили о завершении
            // выводим сообщение о том, что выполнен квест
            // Отправляем в очередь событие выполненного квеста
            // После отмечаем флаг старта квеста как false

            if (questStarted || stepTalked || stepKilled || stepReportedBack) {
                println("Квест $questId завершен")
                EventBus.post(GameEvent.QuestCompleted("quest_ended"))
                questStarted = false
            }
        }
    }
}