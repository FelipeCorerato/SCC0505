package main.kotlin.simulator.automaton

class State(var name: String = "") {
    override fun toString(): String {
        return "State{state='$name'}"
    }
}
