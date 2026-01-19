package lesson11

class CombatSystemDemo {
    fun simulateFight() {
        EventBus.post(GameEvent.DamageDealt("Oleg", "Tolik", 10))
        EventBus.post(GameEvent.DamageDealt("Tolik", "Oleg", 5))

        EventBus.post(GameEvent.EffectApply("Oleg", "Яд"))

        EventBus.post(GameEvent.CharacterDied("Tolik", "Oleg"))
    }
}