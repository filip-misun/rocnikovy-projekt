package rocnikovyprojekt;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeterministicPushdownAutomaton implements FiniteDescription {
	
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
			TransitionFunction.Output val = transitionFunction.get(currentState,
					word.symbolAt(i), currentStackSymbol);
			currentState = val.newState;
			stack.addAll(val.pushToStack);
		}
		return finalStates.contains(currentState);
	}
	
	public static class TransitionFunction {
		
		private HashMap<Input, Output> map = new HashMap<>();
		
		public void put(Object state, Object tapeSymbol, Object stackSymbol,
				Object newState, List<Object> pushToStack) {
			map.put(new Input(state, tapeSymbol, stackSymbol),
                                new Output(newState, pushToStack));
		}
		
		public Output get(Object state, Object tapeSymbol, Object stackSymbol) {
			return map.get(new Input(state, tapeSymbol, stackSymbol));
		}
		
		public boolean containsKey(Object state, Object tapeSymbol, Object stackSymbol) {
			return map.containsKey(new Input(state, tapeSymbol, stackSymbol));
		}
		
                public class Input {
                    Object state;
                    Object tapeSymbol;
                    Object stackSymbol;
                    
                    public Input(Object state, Object tapeSymbol, Object stackSymbol){
                        this.state = state;
                        this.tapeSymbol = tapeSymbol;
                        this.stackSymbol = stackSymbol;
                    }
                }
                
		public static class Output {
			
			public Object newState;
			public List<Object> pushToStack;
			
			public Output(Object newState, List<Object> pushToStack) {
				this.newState = newState;
				this.pushToStack = pushToStack;
			}
			
			@Override
			public boolean equals(Object obj) {
				if (!(obj instanceof Output)) return false;
				Output val = (Output) obj;
				return this.newState.equals(val.newState) &&
						this.pushToStack.equals(val.pushToStack);
			}
		}
	}
	
}
