package lesson11

import lesson12.NpcState

sealed class GameEvent(open val playerId: String) {
    // Боевые события //
    data class CharacterDied(
        override val playerId: String,
        val characterName: String,
        val killerName: String
    ) : GameEvent(playerId)

    data class DamageDealt(
        override val playerId: String,
        val attackerName: String,
        val targetName: String,
        val amount: Int
    ) : GameEvent(playerId)

    data class EffectApply(
        override val playerId: String,
        val characterName: String,
        val effectName: String
    ) : GameEvent(playerId)

    data class EffectEnded(
        override val playerId: String,
        val characterName: String,
        val effectName: String
    ) : GameEvent(playerId)

    // ДИАЛОГИ И NPC //
    data class DialogueStarted(
        override val playerId: String,
        val npcName: String,
        val playerName: String
    ) : GameEvent(playerId)

    data class DialogueChoiceSelected(
        override val playerId: String,
        val npcName: String,
        val playerName: String,
        val choiceId: String
    ) : GameEvent(playerId)

    data class DialogueLineUnlocked(
        override val playerId: String,
        val npcName: String,
        val lineId: String
    ) : GameEvent(playerId)

    data class NpcStateChanged(
        override val playerId: String,
        val npcName: String,
        val oldState: NpcState,
        val newState: NpcState
    ) : GameEvent(playerId)

    // Квесты и прогресс

    data class QuestStarted(
        override val playerId: String,
        val questId: String
    ) : GameEvent(playerId)

    data class QuestStepCompleted(
        override val playerId: String,
        val questId: String,
        val stepId: String
    ) : GameEvent(playerId)

    data class QuestCompleted(
        override val playerId: String,
        val questId: String
    ) : GameEvent(playerId)

    // Достижения
    data class AchievementsUnlocked(
        override val playerId: String,
        val achievementId: String
    ) : GameEvent(playerId)

    data class PlayerProgressSaved(
        override val playerId: String,
        val questId: String,
        val stepId: String
    ) : GameEvent(playerId)
}