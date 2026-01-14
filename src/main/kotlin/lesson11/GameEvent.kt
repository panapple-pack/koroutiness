package lesson11

sealed class GameEvent {
    // Боевые события //
    data class CharacterDied(
        val characterName: String,
        val killerName: String
    ) : GameEvent()

    data class DamageDealt(
        val attackerName: String,
        val targetName: String,
        val amount: Int
    ) : GameEvent()

    data class EffectApply(
        val characterName: String,
        val effectName: String
    ) : GameEvent()

    data class EffectEnded(
        val characterName: String,
        val effectName: String
    ) : GameEvent()

    // ДИАЛОГИ И NPC //
    data class DialogueStarted(
        val npcName: String,
        val playerName: String
    ) : GameEvent()

    data class DialogueChoiceSelected(
        val npcName: String,
        val playerName: String,
        val choiceId: String
    ) : GameEvent()

    data class DialogueLineUnlocked(
        val npcName: String,
        val lineId: String
    ) : GameEvent()

    // Квесты и прогресс

    data class QuestStarted(
        val questId: String
    ) : GameEvent()

    data class QuestStepCompleted(
        val questId: String,
        val stepId: String
    ) : GameEvent()

    data class QuestCompleted(
        val questId: String
    ) : GameEvent()

    // Достижения
    data class AchievementsUnlocked(
        val achievementId: String
    ) : GameEvent()
}