package lesson11

import old_money.CombatSystem

fun main() {
    val logSystem = LogSystem()
    logSystem.register()

    val achievementsSystem = AchievementsSystem()
    achievementsSystem.register()

    val questSystem = QuestSystem()
    questSystem.register()

    val npcSystem = NpcSystem()
    npcSystem.register()

    EventBus.post(GameEvent.DialogueStarted("Старый", "Oleg"))
    EventBus.processQueue(50)

    println()
    val comboMenu = CombatSystemDemo()
    comboMenu.simulateFight()

    EventBus.processQueue(50)

    EventBus.post(
        GameEvent.DialogueChoiceSelected(
            "Старый",
            "Oleg",
            "done"
        )
    )

    EventBus.processQueue(50)

}