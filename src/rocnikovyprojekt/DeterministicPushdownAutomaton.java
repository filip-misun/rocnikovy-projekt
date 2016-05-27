package rocnikovyprojekt;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
		Word stack = new Word();
		stack.append(startStackSymbol);
		
		for (int i = 0; i < word.length(); i++) {
			if (stack.isEmpty()) {
				return false;
			}
			currentStackSymbol = stack.pop();
			if (!transitionFunction.containsKey(currentState, word.symbolAt(i),
					currentStackSymbol)) {
				return false;
			}
			TransitionFunction.Output val = transitionFunction.get(currentState,
					word.symbolAt(i), currentStackSymbol);
			currentState = val.newState;
			stack.append(val.pushToStack);
		}
		return finalStates.contains(currentState);
	}
	
	public static class TransitionFunction {
		
		private HashMap<Input, Output> map = new HashMap<>();
		
		public void put(Object state, Object tapeSymbol, Object stackSymbol,
				Object newState, Word pushToStack) {
			map.put(new Input(state, tapeSymbol, stackSymbol),
                                new Output(newState, pushToStack));
		}
		
		public Output get(Object state, Object tapeSymbol, Object stackSymbol) {
			return map.get(new Input(state, tapeSymbol, stackSymbol));
		}
		
		public boolean containsKey(Object state, Object tapeSymbol, Object stackSymbol) {
			return map.containsKey(new Input(state, tapeSymbol, stackSymbol));
		}
		
                public static class Input {
                    Object state;
                    Object tapeSymbol;
                    Object stackSymbol;
                    
                    public Input(Object state, Object tapeSymbol, Object stackSymbol){
                        this.state = state;
                        this.tapeSymbol = tapeSymbol;
                        this.stackSymbol = stackSymbol;
                    }
                    
                    @Override
                    public boolean equals(Object o){
                        if(o instanceof Input){
                            Input in = (Input) o;
                            return in.state == this.state && 
                                    in.stackSymbol == this.stackSymbol &&
                                    in.tapeSymbol == this.tapeSymbol;
                        }
                        else return false;
                    }
                    
                    @Override
                    public int hashCode(){
                        return Objects.hash(state, tapeSymbol, stackSymbol);
                    }
                    
                    @Override
                    public String toString(){
                        return state + "," + tapeSymbol + "," + stackSymbol;
                    }
                }
                
		public static class Output {
			
			public Object newState;
			public Word pushToStack;
			
			public Output(Object newState, Word pushToStack) {
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
                        
                        @Override
                        public int hashCode(){
                            return Objects.hash(newState, pushToStack);
                        }
                        
                        @Override
                        public String toString(){
                            return "(" + newState + "," + pushToStack + ")";
                        }
		}
	}
	
}
