package conversions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import rocnikovyprojekt.FAInput;
import rocnikovyprojekt.FiniteDescription;
import rocnikovyprojekt.NFA;
import rocnikovyprojekt.NFAEpsilon;
import rocnikovyprojekt.Word;

/**
 * This clas removes from NFA epsilon moves.
 * @author Jozef Rajník
 */
public class NFAEpsilonToNFA implements Conversion{
    
    /**
     * This method removes epsilon-moves in non-deterministic finite automaton.
     * It uses standard construction.
     * @param a NFA with epsilon-moves
     * @return NFA without epsilon-moves
     */
    @Override
    public FiniteDescription convert(FiniteDescription a) {
        NFAEpsilon afrom = (NFAEpsilon) a;
        /* Map which contains for each state q of NFA afrom its
           epsilon tail, i. e. states p of arom such that, afrom can move 
           from state q to state p on empty word. */
        HashMap<Object, Collection<Object>> tails = new HashMap<>();
        /* We compute for each state its epsilon tail. First, we initialize
           epsilon tail of s to {s}. Then for each state in tail, we add
           states which afrom can move on epsilon to. We repeat this,
           while we get new states in tail. */ 
        for(Object state : afrom.getStates()){
            Collection<Object> tail = new HashSet<>();
            tail.add(state);
            boolean changed = true;
            while(changed){
                changed = false;
                int before = tail.size();
                for(Object p : tail){
                    changed |= tail.addAll(afrom.getDelta().get(p, Word.EPSILON));
                }
            }
            tails.put(state, tail);
        }
        NFA.TransitionFunction delta = new NFA.TransitionFunction();
        /* For each state q excluding start state and for ecah character a,
           we add moves from q to each state of epsilon tail of p, where
           p is a state which automaton afrom moves on a to. */
        for(Map.Entry<FAInput, Set<Object>> entry : afrom.getDelta().entrySet()){
            if(afrom.getStartState().equals(entry.getKey().state)){
                continue;
            }
            HashSet<Object> result = new HashSet<>();
            for(Object state : entry.getValue()){
                result.addAll(tails.get(state));
            }
            delta.put(entry.getKey().state, entry.getKey().symbol, result);
        }
        /* Now we add moves from start state. For each character a, we take
           first all states q such that automaton afrom moves on a to
           state q from some state in epsilon tail of the start state (statesq). 
           Then we add move from start state on a to each state in epsilon tail
           of states q. */
        for(Object symbol : afrom.getAlphabet()){
            Set<Object> statesq = new HashSet<>();
            for(Object p : tails.get(afrom.getStartState())){
                statesq.addAll(afrom.getDelta().get(p, symbol));
            }
            Set<Object> result = new HashSet<>();
            for(Object state : statesq){
                result.addAll(tails.get(state));
            }
            delta.put(afrom.getStartState(), symbol, result);
        }
        /* If epsilon tail of start state contains some final state, we add
           start state to new set of final states. */
        Set<Object> finalStates = afrom.getFinalStates();
        for(Object state : tails.get(afrom.getStartState())){
            if(afrom.getFinalStates().contains(state)){
                finalStates.add(afrom.getStartState());
            }
        }
        /* At last, we remove epsilon-moves */
        for(Iterator<Map.Entry<FAInput, Set<Object>>> it = delta.entrySet().iterator();
                it.hasNext();){
            if(it.next().getKey().symbol.equals(Word.EPSILON)){
                it.remove();
            }
        }
        return new NFA(delta, afrom.getStartState(), finalStates);
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return NFAEpsilon.class;
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return NFA.class;
    }
    
}
