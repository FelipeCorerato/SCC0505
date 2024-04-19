package main.kotlin.simulator.automaton

class Symbol(private var value: String = "") {
    override fun toString(): String {
        return "Symbol{symbol='$value'}"
    }
}
