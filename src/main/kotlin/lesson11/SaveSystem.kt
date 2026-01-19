package lesson11

class SaveSystem {
    private val progress: MutableMap<String, MutableMap<String, MutableSet<String>>> = mutableMapOf()
    // MutableMap<String, ...> - ключ: playerId
    // MutableMap<String, MutableSet<String>> - ключ: questId
    // MutableSet<String> - набор stepId, что уже выполнены

    fun register() {
        EventBus.subscribe { event ->
            when(event) {
                is GameEvent.PlayerProgressSaved -> {
                    saveStep(event.playerId, event.questId, event.stepId)
                }
                else -> {}
            }
        }
    }

    fun saveStep(playerId: String, questId: String, stepId: String) {
        val playerData = progress.getOrPut(playerId) {mutableMapOf()}
        // getOrPut - проверяет, если ключ есть (сейчас questId), то возвращаем его значение
        // если его нет, создаем его вручную через блок {.....} и кладем в map
        val questSteps = playerData.getOrPut(questId) {mutableSetOf()}
        // получаем квесты игрока и создаем набор шагов квеста

        val wasAdded = questSteps.add(stepId)
        // add вернет true если шаг добавился впервые, false если шаг уже был

        if (wasAdded) {
            println("[SAVE] Сохранено: player=$playerId quest=$questId step=$stepId")
        } else {
            println("[SAVE] Шаг квеста уже был сохранен ранее: player=$playerId quest=$questId step=$stepId")
        }
    }

    fun printProgress(playerId: String) {
        println("=== ПРОГРЕСС ДЛЯ ИГРОКА: $playerId ===")

        val playerData = progress[playerId]
        if (playerData == null) {
            println("ПРОГРЕСС ИГРОКА НЕ НАЙДЕН")
            return
        }
        for ((questId, steps) in playerData) {
            println("Квест: $questId")
            println("Шаги: $steps")
            println("=========================")
        }
    }
}