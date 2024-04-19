package main.kotlin.simulator.automaton

class Transition(val originState: State, val destinationState: State, val word: Word) {
    override fun toString(): String {
        return "Transition{originState=$originState, destinationState=$destinationState, word=$word}"
    }
}
