package rocnikovyprojekt;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeterministicPushdownAutomaton {
	
	private TransitionFunction transitionFunction;
	private Object startState;
	private Object startStackSymbol;
	private Set<Object> finalStates;
	
	public DeterministicPushdownAutomaton(TransitionFunction transitionFunction,
			Object startState, Object startStackSymbol, Set<Object> finalStates) {
		this.transitionFunction = transitionFunction;
		this.startState = startState;
		this.startStackSymbol = startStackSymbol;
		this.finalStates = finalStates;
	}
	
	public boolean accepts(Word word) {
		Object currentState = startState;
		Object currentStackSymbol;
		ArrayList<Object> stack = new ArrayList<>();
		stack.add(startStackSymbol);
		
		for (int i = 0; i < word.length(); i++) {
			if (stack.isEmpty()) {
				return false;
			}
			currentStackSymbol = stack.remove(stack.size() - 1);
			if (!transitionFunction.containsKey(currentState, word.symbolAt(i),
					currentStackSymbol)) {
				return false;
			}
			TransitionFunction.Value val = transitionFunction.get(currentState,
					word.symbolAt(i), currentStackSymbol);
			currentState = val.newState;
			stack.addAll(val.pushToStack);
		}
		return finalStates.contains(currentState);
	}
	
	public static class TransitionFunction {
		
		private HashMap<List<Object>, Value> map = new HashMap<>();
		
		public void put(Object state, Object tapeSymbol, Object stackSymbol,
				Object newState, List<Object> pushToStack) {
			List<Object> key = createKeyList(state, tapeSymbol, stackSymbol);
			Value value = new Value(newState, pushToStack);
			map.put(key, value);
		}
		
		public Value get(Object state, Object tapeSymbol, Object stackSymbol) {
			return map.get(createKeyList(state, tapeSymbol, stackSymbol));
		}
		
		public boolean containsKey(Object state, Object tapeSymbol, Object stackSymbol) {
			return map.containsKey(createKeyList(state, tapeSymbol, stackSymbol));
		}
		
		private List<Object> createKeyList(Object state, Object tapeSymbol, 
				Object stackSymbol) {
			ArrayList<Object> l = new ArrayList<>();
			l.add(state);
			l.add(tapeSymbol);
			l.add(stackSymbol);
			return l;
		}
		
		public static class Value {
			
			public Object newState;
			public List<Object> pushToStack;
			
			public Value(Object newState, List<Object> pushToStack) {
				this.newState = newState;
				this.pushToStack = pushToStack;
			}
			
			@Override
			public boolean equals(Object obj) {
				if (!(obj instanceof Value)) return false;
				Value val = (Value) obj;
				return this.newState.equals(val.newState) &&
						this.pushToStack.equals(val.pushToStack);
			}
		}
	}
	
}
