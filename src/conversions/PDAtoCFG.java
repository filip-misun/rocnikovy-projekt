/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversions;

import java.util.HashSet;
import java.util.Set;
import rocnikovyprojekt.CFGrammar;
import rocnikovyprojekt.CFGrammar.Rule;
import rocnikovyprojekt.DeterministicPushdownAutomaton.TransitionFunction.Output;
import rocnikovyprojekt.FiniteDescription;
import rocnikovyprojekt.PushdownAutomaton;

/**
 *
 * @author Dodo
 */
public class PDAtoCFG implements Conversion {

    @Override
    public FiniteDescription convert(FiniteDescription a) {
        PushdownAutomaton afrom = (PushdownAutomaton) a;
        Set<Object> states = afrom.getStates();
        Set<Object> wa = afrom.getWorkingAlphabet();
        /* The set of rules of new grammar */
        Set<Rule> rules = new HashSet<>();
        /* The set of nonterminals consist of [p,Z,q] for each states p, q
         * and each working symbol Z. We iterate through all such triplets
         * and for each such nonterminal we add rules into grammat;
         */
        for(Object p : states){
            for(Object Z : wa){
                for(Object q : states){
                    for(Object ch : afrom.getAlphabet()){
                        Set<Output> outs = afrom.getDelta().get(p, ch, Z);
                    }
                    
                }
            }
        }
        return null;
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return PushdownAutomaton.class;
                
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return CFGrammar.class;
    }
    
}
