/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversions;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import rocnikovyprojekt.CFGrammar;
import rocnikovyprojekt.CFGrammar.Rule;
import rocnikovyprojekt.DeterministicPushdownAutomaton.TransitionFunction.Output;
import rocnikovyprojekt.FiniteDescription;
import rocnikovyprojekt.PushdownAutomaton;
import rocnikovyprojekt.Word;

/**
 *
 * @author Dodo
 */
public class PDAtoCFG implements Conversion {
    
    private Set<Object> states;
    Set<Rule> rules;

    @Override
    public FiniteDescription convert(FiniteDescription a) {
        PushdownAutomaton afrom = (PushdownAutomaton) a;
        states = afrom.getStates();
        Set<Object> wa = afrom.getWorkingAlphabet();
        /* The set of rules of new grammar */
        rules = new HashSet<>();
        /* Start symbol of the new grammar. */
        Object startSymbol = "sigma";
        Set<Object> nonterminals = new HashSet<>();
        nonterminals.add(startSymbol);
        /* The set of nonterminals consist of [p,Z,q] for each states p, q
         * and each working symbol Z. We iterate through all such triplets
         * and for each such nonterminal we add rules into grammat;
         */
        //System.out.println(afrom.getDelta().get("0", Word.EMPTYWORD, "c"));
        for(Object p : states){
            for(Object Z : wa){
                for(Object q : states){
                    Object nonterm = Arrays.asList(p, Z, q);
                    nonterminals.add(nonterm);
                    //System.out.println(nonterm);
                    //System.out.println(afrom.getDelta().get("0", Word.EMPTYWORD, "c"));
                    for(Object ch : afrom.getAlphabet()){
                        //System.out.println(p + " " + ch + " " + Z + "->" + afrom.getDelta().get(p, ch, Z));
                        for(Output out : afrom.getDelta().get(p, ch, Z)){
                            if(out.pushToStack.isEmpty()){
                                if(q.equals(out.newState)){
                                    rules.add(new Rule(nonterm,
                                            new Word(Arrays.asList(ch))));
                                } 
                            } else {
                                Word w = Word.EMPTYWORD;
                                if(!ch.equals(Word.EMPTYWORD)){
                                    w = new Word(Arrays.asList(ch));
                                }
                                makeRules(p, q, new Word(out.pushToStack), nonterm,
                                        w);
                            }
                        }
                    }
                }
            }
            Word w = new Word();
            w = w.append(Arrays.asList(afrom.getStartState(), afrom.getStackStart(), p));
            rules.add(new Rule(startSymbol, w));
        }
        return new CFGrammar(nonterminals, afrom.getAlphabet(), rules, startSymbol);
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return PushdownAutomaton.class;
                
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return CFGrammar.class;
    }
    
    private void makeRules(Object from, Object to, Word stack,
            Object nonterminal, Word rule){
        if(stack.isEmpty()){
            return;
        }
        if(stack.length() == 1){
            rule = rule.append(Arrays.asList(from, stack.last(), to));
            rules.add(new Rule(nonterminal, rule));
            return;
        }
        Object Z = stack.last();
        for(Object state : states){
            Object next = Arrays.asList(from, Z, state);
            Word newRule = rule.append(next);
            makeRules(state, to, stack.pop(), nonterminal, newRule);
        }
    }
    
}
