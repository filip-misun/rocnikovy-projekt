/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversions;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import rocnikovyprojekt.DFA;
import rocnikovyprojekt.FiniteDescription;
import rocnikovyprojekt.NFA;

/**
 * Class to confersion from non-deterministic finite automaton to deterministic
 * finite automaton.
 * @author Jozef Rajník
 */
public class NFAtoDFA implements Conversion{
    /**
     * This method converts Non-deterministic Finite Automaton a to Deterministic
     * finite automaton. It uses standard algorithm. The states of new DFA are 
     * sets of states of NFA which the NFA can be in.
     * @param a Non-deterministic finite automaton (NFA) to be converted.
     * @return Deterministic finite automaton equivalent to automaton a.
     */
    @Override
    public FiniteDescription convert(FiniteDescription a) {
        NFA afrom = (NFA) a;
        /* Set of states which has benn proceeded. */
        Set<Set<Object>> checked = new HashSet<>();
        /* Queue of states to proceed. */
        Queue<Set<Object>> queue = new LinkedList<>();
        /* Transition function of new DFA. */
        DFA.TransitionFunction delta = new DFA.TransitionFunction();
        /* Set of final states of new DFA. */
        Set<Object> finalStates = new HashSet<>();
        /* Alphabet of new DFA. */
        Set<Object> alphabet = afrom.getAlphabet();
        queue.add(Collections.singleton(afrom.getStartState()));
        while(!queue.isEmpty()){
            Set<Object> currState = queue.remove();
            if(checked.contains(currState)){
                continue;
            }
            checked.add(currState);
            for(Object symbol : alphabet){
                Set<Object> newState = new HashSet<>();
                for(Object state : currState){
                    newState.addAll(afrom.getDelta().get(state, symbol));
                    if(afrom.getFinalStates().contains(state)){
                        finalStates.add(currState);
                    }
                }
                delta.put(currState, symbol, newState);
                queue.add(newState);
            }
        }
        return new DFA(delta, Collections.singleton(afrom.getStartState()), finalStates);
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return NFA.class;
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return DFA.class;
    }
    
}
