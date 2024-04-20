package main.kotlin.simulator

import main.kotlin.simulator.automaton.FiniteAutomaton
import main.kotlin.simulator.automaton.State
import main.kotlin.simulator.automaton.StringChain
import main.kotlin.utils.LabelsUtils
import java.io.File
import java.io.FileNotFoundException
import java.util.Scanner

fun main() {
    val finiteAutomaton = FiniteAutomaton()

    val scanner = Scanner(System.`in`)
    println(LabelsUtils.START_INPUT)

    val fileName = scanner.nextLine()
    try {
        File(fileName).bufferedReader().useLines { lines ->
            val iterator = lines.iterator()
            for (i in 0..<iterator.next().toInt()) {
                finiteAutomaton.addStateIfNotExists(i)
            }

            finiteAutomaton.initialState = finiteAutomaton.states[0]

            val countOfSymbolsParts = iterator.next().split(" ")
            for (i in 1..<countOfSymbolsParts.size) {
                finiteAutomaton.addAlphabetSymbol(countOfSymbolsParts[i])
            }

            val countOfFinalStatesParts = iterator.next().split(" ")
            for (i in 1..<countOfFinalStatesParts.size) {
                finiteAutomaton.addFinalStateIfNotExists(countOfFinalStatesParts[i])
            }

            for (i in 0..<iterator.next().toInt()) {
                val transition = iterator.next().split(" ")
                finiteAutomaton.addTransitionIfNotExists(transition[0], transition[2], transition[1])
            }

            for (i in 0..<iterator.next().toInt()) {
                if (iterator.hasNext()) {
                    val line = iterator.next()
                    if (line == "-") {
                        finiteAutomaton.addStringChain("")
                    } else {
                        finiteAutomaton.addStringChain(line)
                    }
                }
            }
        }
    } catch (ex: FileNotFoundException) {
        throw RuntimeException(ex)
    }

    val answers = simulateAutomaton(finiteAutomaton)
    generateResult(fileName, finiteAutomaton, answers)
}

fun simulateAutomaton(automaton: FiniteAutomaton): Map<String, Boolean> {
    val answers = mutableMapOf<String, Boolean>()

    for (stringChain in automaton.stringChains) {
        var actualState = automaton.initialState
        var answer = false

        if (stringChain.value.isEmpty()) {
            if (isFinalState(actualState, automaton)) {
                answers[stringChain.value] = true
                continue
            }

            answers[stringChain.value] = false
            continue
        }

        val symbol = stringChain.value[0].toString()
        val transitions = automaton.getTransition(actualState, symbol)

        for (transition in transitions) {
            actualState = transition.destinationState
            answer = simulateInnerAutomaton(automaton, actualState,  1, stringChain)
            if (answer) {
                break
            }
        }

        answers[stringChain.value] = answer
    }

    return answers
}

fun isFinalState(actualState: State, automaton: FiniteAutomaton): Boolean {
    for (finalState in automaton.finalStates) {
        if (actualState.name == finalState.name) {
            return true
        }
    }

    return false
}

fun simulateInnerAutomaton(automaton: FiniteAutomaton, actualState: State, stringChainIndex: Int, stringChain: StringChain): Boolean {
    if (stringChainIndex == stringChain.value.length) {
        if (isFinalState(actualState, automaton)) {
            return true
        }
        return false
    }

    val actualSymbol = stringChain.value[stringChainIndex].toString()
    val actualTransitions = automaton.getTransition(actualState, actualSymbol)

    for (transition in actualTransitions) {
        val result = simulateInnerAutomaton(automaton, transition.destinationState, stringChainIndex + 1, stringChain)
        if (result) {
            return true
        }
    }

    return false
}

fun generateResult(fileName: String, finiteAutomaton: FiniteAutomaton, answers: Map<String, Boolean>) {
    val newFileName = fileName.substring(0, fileName.lastIndexOf('.'))
    val outputFileName = "$newFileName.out"
    try {
        val outputFile = File(outputFileName)
        outputFile.createNewFile()
        outputFile.bufferedWriter().use { writer ->
            for (stringChain in finiteAutomaton.stringChains) {
                val label = "${if (answers[stringChain.value] == true) LabelsUtils.ACCEPTED else LabelsUtils.REJECTED}\n"
                writer.write(label)
            }
        }
    } catch (ex: Exception) {
        throw RuntimeException(ex)
    }

    println("${LabelsUtils.RESULTS_INPUT} $outputFileName.")
}
