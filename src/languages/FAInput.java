package languages;

import java.util.Objects;

/**
 * This class representes input in Finite Automaton. It encloses state and
 * symbol in one class.
 * 
 * @author Jozef Rajnik
 */
public class FAInput {

    public Object state;
    public Object symbol;

    public FAInput(Object state_, Object symbol_) {
        state = state_;
        symbol = symbol_;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FAInput) {
            FAInput i = (FAInput) o;
            return this.state.equals(i.state) && this.symbol.equals(i.symbol);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, symbol);
    }
}
