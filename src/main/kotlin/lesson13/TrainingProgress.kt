package lesson13

import lesson11.GameEvent

class TrainingProgress {

    private val graph = TrainingStateGraph()

    private val currentStateByPlayer = mutableMapOf<String, TrainingState>()
    // playerId -> и его текущее состояние
    // т.е. все, что мы будем делать - это сохранят то, на каком состоянии он остановился

    fun getState(playerId: String): TrainingState {
        return currentStateByPlayer.getOrPut(playerId){ TrainingState.START }
        // getOrPut - получает, либо если не находит кладет по умолчанию то, что мы положили в {...}
    }

    fun handleEvent(playerId: String, event: GameEvent) {
        val currentState = getState(playerId)
        val node = graph.getNode(currentState)

        val nextState = node.getNextState(event)

        if (nextState != null) {
            println("[STATE GRAPH]: $playerId: $currentState -> $nextState")
            // если игрок кспешно перешел между состояниями - сохраняем
            currentStateByPlayer[playerId] = nextState
        } else {
            println("[STATE GRAPH] $playerId: игнорирует событие ${event::class.simpleName}")
        }
    }
}