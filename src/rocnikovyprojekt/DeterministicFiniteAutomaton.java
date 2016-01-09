package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import rocnikovyprojekt.FiniteAutomaton.Configuration;

public class DeterministicFiniteAutomaton {
	
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
	
	public static class TransitionFunction {
		
		private HashMap<List<Object>, Object> map = new HashMap<>();
		
		public void put(Object state, Object symbol, Object newState) {
			map.put(createList(state, symbol), newState);
		}
		
		Object get(Object state, Object symbol) {
			return map.get(createList(state, symbol));
		}
		
		public boolean containsKey(Object state, Object symbol) {
			return map.containsKey(createList(state, symbol));
		}
		
		private List<Object> createList(Object state, Object symbol) {
			ArrayList<Object> l = new ArrayList<Object>(2);
			l.add(state);
			l.add(symbol);
			return l;
		}
		
	}
	
}
