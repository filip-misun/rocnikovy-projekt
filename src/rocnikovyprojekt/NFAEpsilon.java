/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import rocnikovyprojekt.FiniteAutomaton.Configuration;

/**
 *
 * @author Dodo
 */
public class NFAEpsilon extends NFA{

    public NFAEpsilon(TransitionFunction transitionFunction, Object startState, Set<Object> finalStates) {
        super(transitionFunction, startState, finalStates);
    }
    
    @Override
    public Collection<Configuration> nextConfigurations(Configuration conf, Word word){
        ArrayList<Configuration> newConfigurations = new ArrayList<>();
        for (Object state : transitionFunction.get(conf.getState(), Word.EMPTYWORD)) {
            newConfigurations.add(new Configuration(state, conf.getPosition()));
        }
        newConfigurations.addAll(super.nextConfigurations(conf, word));
        return newConfigurations;
    }
    
}
