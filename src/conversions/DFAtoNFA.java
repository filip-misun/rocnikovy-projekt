/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import rocnikovyprojekt.FiniteDescription;
import rocnikovyprojekt.DFA;
import rocnikovyprojekt.NFA;

/**
 *
 * @author Jozef Rajník
 */
public class DFAtoNFA implements Conversion{

    @Override
    public FiniteDescription convert(FiniteDescription a) {
        DFA afrom = (DFA) a;
        DFA.TransitionFunction delta = afrom.getDelta();
        NFA.TransitionFunction newDelta =
                new NFA.TransitionFunction();
        for(Map.Entry<DFA.TransitionFunction.Input,Object> entry : afrom.getDelta().entrySet()){
            Set<Object> output = new HashSet<Object>();
            output.add(entry.getValue());
            newDelta.put(entry.getKey().state, entry.getKey().symbol, output);
        }
        NFA ares = new NFA(newDelta, afrom.getStartState(), afrom.getFinalStates());
        return ares;
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return DFA.class;
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return NFA.class;
    }
    
}
