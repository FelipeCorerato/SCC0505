package main.kotlin.simulator.automaton

class FiniteAutomaton {
    private var transitions: MutableList<Transition> = ArrayList()
    private var alphabetSymbols: MutableList<Symbol> = ArrayList()
    var states: MutableList<State> = ArrayList()
    var initialState: State = State()
    var finalStates: MutableList<State> = ArrayList()
    var stringChains: MutableList<StringChain> = ArrayList()

    init {
        states = ArrayList()
        transitions = ArrayList()
        alphabetSymbols = ArrayList()
        initialState = State()
        finalStates = ArrayList()
        stringChains = ArrayList()
    }

    fun addAlphabetSymbol(symbol: String) {
        alphabetSymbols.add(Symbol(symbol))
    }

    fun addStateIfNotExists(value: Int) {
        val state = State(value.toString())
        if (!states.contains(state)) {
            states.add(state)
        }
    }

    fun addFinalStateIfNotExists(stateName: String) {
        val state = State(stateName)
        if (!finalStates.contains(state)) {
            finalStates.add(state)
        }
    }

    fun addTransitionIfNotExists(s: String, s1: String, s2: String) {
        val originState = State(s)
        val destinationState = State(s1)
        val transition = Transition(originState, destinationState, Word(s2))

        if (!transitions.contains(transition)) {
            transitions.add(transition)
        }
    }

    fun addStringChain(string: String) {
        stringChains.add(StringChain(string))
    }

    fun getTransition(currentState: State, symbol: String): List<Transition> {
        val validTransitions = ArrayList<Transition>()
        for (transition in transitions) {
            if (transition.originState.name == currentState.name && transition.word.value == symbol) {
                validTransitions.add(transition)
            }
        }

        return validTransitions
    }

    override fun toString(): String {
        return "Finite Automaton: {\n" +
                "states=$states\n\n" +
                "transitions=$transitions\n\n" +
                "alphabetSymbols=$alphabetSymbols\n\n" +
                "initialState=$initialState\n\n" +
                "finalStates=$finalStates\n\n" +
                "stringChains=$stringChains\n}"
    }
}
