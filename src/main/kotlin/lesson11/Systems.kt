package lesson11

class LogSystem {

    fun register() {
        EventBus.subscribe { event ->
            println("[INFO] Получено событие $event")
            // реагирует на все возможные события и логирует их в консоль
        }
    }
}

class AchievementsSystem {

    private var killCount: Int = 0

    fun register() {
        EventBus.subscribe { event ->
            when(event) {
                is GameEvent.CharacterDied -> {
                    if (event.killerName == "Oleg") {
                        killCount++
                        println("[!] Счетчик убийств игрока: $killCount")

                        if (killCount == 1) {
                            EventBus.post(GameEvent.AchievementsUnlocked("first_blood"))
                        }
                        if (killCount == 3) {
                            EventBus.post(GameEvent.AchievementsUnlocked("cho_ti_nadelal"))
                        }
                    }
                }
                else -> {}
            }
        }
    }
}