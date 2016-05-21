/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocnikovyprojekt;

import java.util.List;
import java.util.Map;
import java.util.Set;
import rocnikovyprojekt.DeterministicPushdownAutomaton.TransitionFunction.Input;
import rocnikovyprojekt.DeterministicPushdownAutomaton.TransitionFunction.Output;

/**
 *
 * @author Dodo
 */
public class PDAdelta {

    private Map<Input, Set<Output>> map;

    public void add(Object state, Object tapeSymbol, Object stackSymbol,
            Object newState, Word pushToStack) {
        Input in = new Input(state, tapeSymbol, stackSymbol);
        
    }

    public Output get(Object state, Object tapeSymbol, Object stackSymbol) {
        return map.get(new Input(state, tapeSymbol, stackSymbol));
    }

    public boolean containsKey(Object state, Object tapeSymbol, Object stackSymbol) {
        return map.containsKey(new Input(state, tapeSymbol, stackSymbol));
    }
}
