package lesson12

enum class NpcState {
    IDLE,      // npc просто стоит и ничего не делает
    WAITING,   // NPC ждет игрока
    TALKING,   // NPC разговаривает с игроком
    REWARDED,  // NPC дал награду за квест
    ANGRY
}