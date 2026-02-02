package lesson13

import lesson11.GameEvent

class TrainingStateGraph {
    // Все узлы графа
    private val nodes = mutableMapOf<TrainingState, StateNode>()

    // init - блок, который выполняется при создании объекта
    // Создаем подготовленные узлы
    init {
        val start = StateNode(TrainingState.START)
        val approached = StateNode(TrainingState.APPROACHED)
        val talking = StateNode(TrainingState.TALKING)
        val accepted = StateNode(TrainingState.ACCEPTED)
        val dummyKilled = StateNode(TrainingState.DUMMY_KILLED)
        val completed = StateNode(TrainingState.COMPLETED)
        val refuse = StateNode(TrainingState.FAILED)

        // Описание переходов (Это и будет наш граф)

        start.addTransition(
            GameEvent.DialogueStarted::class.java,
            TrainingState.APPROACHED
        )
        approached.addTransition(
            GameEvent.DialogueChoiceSelected::class.java,
            TrainingState.TALKING
        )
        talking.addTransition(
            GameEvent.DialogueChoiceSelected::class.java,
            TrainingState.ACCEPTED
        )
        accepted.addTransition(
            GameEvent.CharacterDied::class.java,
            TrainingState.DUMMY_KILLED
        )
        dummyKilled.addTransition(
            GameEvent.DialogueChoiceSelected::class.java,
            TrainingState.COMPLETED
        )

        // Кладем узлы на карту
        nodes[start.state] = start
        nodes[approached.state] = approached
        nodes[talking.state] = talking
        nodes[accepted.state] = accepted
        nodes[dummyKilled.state] = dummyKilled
        nodes[completed.state] = completed
    }

    fun getNode(state: TrainingState):  StateNode {
        return nodes[state]!!
        // !! - явно и уверенно заявляем, что узел существует (не будет null)
    }
}