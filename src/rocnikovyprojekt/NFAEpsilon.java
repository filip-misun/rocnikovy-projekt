package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;
import rocnikovyprojekt.FiniteAutomaton.Configuration;

/**
 *
 * @author Jozef Rajník
 */
public class NFAEpsilon extends NFA{

    public NFAEpsilon(TransitionFunction transitionFunction, Object startState, Set<Object> finalStates) {
        super(transitionFunction, startState, finalStates);
    }
    
    public NFAEpsilon(Scanner s){
        super(s);
    }
    
    @Override
    public Collection<Configuration> nextConfigurations(Configuration conf, Word word){
        ArrayList<Configuration> newConfigurations = new ArrayList<>();
        for (Object state : transitionFunction.get(conf.getState(), Word.EPSILON)) {
            newConfigurations.add(new Configuration(state, conf.getPosition()));
        }
        newConfigurations.addAll(super.nextConfigurations(conf, word));
        return newConfigurations;
    }
}
