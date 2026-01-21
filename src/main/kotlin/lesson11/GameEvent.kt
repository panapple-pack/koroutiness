package lesson11

import lesson12.NpcState
import lesson12.TrainingNpc

sealed class GameEvent {
    // Боевые события //
    data class CharacterDied(
        val playerId: String,
        val characterName: String,
        val killerName: String
    ) : GameEvent()

    data class DamageDealt(
        val playerId: String,
        val attackerName: String,
        val targetName: String,
        val amount: Int
    ) : GameEvent()

    data class EffectApply(
        val playerId: String,
        val characterName: String,
        val effectName: String
    ) : GameEvent()

    data class EffectEnded(
        val playerId: String,
        val characterName: String,
        val effectName: String
    ) : GameEvent()

    // ДИАЛОГИ И NPC //
    data class DialogueStarted(
        val playerId: String,
        val npcName: String,
        val playerName: String
    ) : GameEvent()

    data class DialogueChoiceSelected(
        val playerId: String,
        val npcName: String,
        val playerName: String,
        val choiceId: String
    ) : GameEvent()

    data class DialogueLineUnlocked(
        val playerId: String,
        val npcName: String,
        val lineId: String
    ) : GameEvent()

    data class NpcStateChanged(
        val playerId: String,
        val npcName: String,
        val oldState: NpcState,
        val newState: NpcState
    ) : GameEvent()

    // Квесты и прогресс

    data class QuestStarted(
        val playerId: String,
        val questId: String
    ) : GameEvent()

    data class QuestStepCompleted(
        val playerId: String,
        val questId: String,
        val stepId: String
    ) : GameEvent()

    data class QuestCompleted(
        val playerId: String,
        val questId: String
    ) : GameEvent()

    // Достижения
    data class AchievementsUnlocked(
        val playerId: String,
        val achievementId: String
    ) : GameEvent()

    data class PlayerProgressSaved(
        val playerId: String,
        val questId: String,
        val stepId: String
    ) : GameEvent()
}