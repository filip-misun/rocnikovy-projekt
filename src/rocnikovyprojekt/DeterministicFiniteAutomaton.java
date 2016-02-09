package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import rocnikovyprojekt.FiniteAutomaton.Configuration;

public class DeterministicFiniteAutomaton implements FiniteDescription{
	
	private TransitionFunction transitionFunction;
	private Object startState;
	private Set<Object> finalStates;
	private ArrayList<Configuration> lastComputation;
	
	public DeterministicFiniteAutomaton(TransitionFunction transitionFunction,
			Object startState, Set<Object> finalStates) {
		this.transitionFunction = transitionFunction;
		this.startState = startState;
		this.finalStates = finalStates;
	}
	
	public boolean accepts(Word word) {
		Object currentState = startState;
		lastComputation = new ArrayList<>();
		lastComputation.add(new Configuration(startState, 0));
		
		for (int i = 0; i < word.length(); i++) {
			if (!transitionFunction.containsKey(currentState, word.symbolAt(i)))	{
				return false;
			}
			currentState = transitionFunction.get(currentState, word.symbolAt(i));
			lastComputation.add(new Configuration(currentState, i+1));
		}
		return finalStates.contains(currentState);
	}
	
	public List<Configuration> getLastComputation() {
		return lastComputation;
	}
        
        public Object getStartState(){
            return startState;
        }
        
        public Set<Object> getFinalStates(){
            return finalStates;
        } 
        
        public TransitionFunction getDelta(){
            return transitionFunction;
        }
	
	public static class TransitionFunction {
		
		private HashMap<Input, Object> map = new HashMap<>();
		
		public void put(Object state, Object symbol, Object newState) {
			map.put(new Input(state, symbol), newState);
		}
		
		Object get(Object state, Object symbol) {
			return map.get(new Input(state, symbol));
		}
		
		public boolean containsKey(Object state, Object symbol) {
			return map.containsKey(new Input(state, symbol));
		}
		
                public class Input {
                    Object state;
                    Object symbol;
                    
                    public Input(Object state_, Object symbol_){
                        state = state_;
                        symbol = symbol_;
                    }
                }
	}
	
}
