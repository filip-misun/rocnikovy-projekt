package conversions;

import rocnikovyprojekt.CFGrammar;
import rocnikovyprojekt.CFGrammar.Rule;
import rocnikovyprojekt.FiniteDescription;
import rocnikovyprojekt.PDAdelta;
import rocnikovyprojekt.PushdownAutomaton;
import rocnikovyprojekt.Sets;
import rocnikovyprojekt.Word;

/**
 *
 * @author Jozef Rajník
 */
public class CFGtoPDA implements Conversion{

    @Override
    public FiniteDescription convert(FiniteDescription a) {
        CFGrammar g = (CFGrammar) a;
        PDAdelta delta = new PDAdelta();
        Object state = g.getStartSymbol();
        for(Rule r : g.getRules()){
            delta.add(state, Word.EMPTYWORD, r.nonterminal, state, r.word.reverse());
        }
        for(Object ch : g.getTerminals()){
            delta.add(state, ch, ch, state, Word.EMPTYWORD);
        }
        return new PushdownAutomaton(delta, state, state);
    }

    @Override
    public Class<? extends FiniteDescription> getFrom() {
        return CFGrammar.class;
    }

    @Override
    public Class<? extends FiniteDescription> getTo() {
        return PushdownAutomaton.class;
    }
    
}
