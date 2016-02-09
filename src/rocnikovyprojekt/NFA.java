package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import rocnikovyprojekt.FiniteAutomaton.Configuration;

public class NFA implements FiniteDescription {
	
	private TransitionFunction transitionFunction;
	private Object startState;
	private Set<Object> finalStates;
	
	public NFA(TransitionFunction transitionFunction,
			Object startState, Set<Object> finalStates) {
		this.transitionFunction = transitionFunction;
		this.startState = startState;
		this.finalStates = finalStates;
	}
	
	public boolean accepts(Word word) {
		Set<Configuration> visited = new HashSet<>();
		Queue<Configuration> queue = new LinkedList<>();
		queue.add(new Configuration(startState, 0));
		
		while (!queue.isEmpty()) {
			Configuration conf = queue.remove();
			if (visited.contains(conf)) {
				continue;
			}
			if (conf.getPosition() == word.length() &&
					finalStates.contains(conf.getState())) {
				return true;
			}
			visited.add(conf);
			
			if (conf.getPosition() < word.length()) {
				for (Object state : transitionFunction.get(conf.getState(),
						word.symbolAt(conf.getPosition()))) {
					queue.add(new Configuration(state, conf.getPosition()+1));
				}
			}
			for (Object state : transitionFunction.get(conf.getState(), Word.EMPTYWORD)) {
				queue.add(new Configuration(state, conf.getPosition()));
			}
		}
		return false;
	}
	
	public static class TransitionFunction {
		
		private HashMap<Input, Set<Object>> map = new HashMap<>();
		
		public void put(Object state, Object symbol, Set<Object> newStates) {
			map.put(new Input(state, symbol), newStates);
		}
		
		Set<Object> get(Object state, Object symbol) {
			Set<Object> val = map.get(new Input(state, symbol));
			if (val == null) {
				/* defaultna funkcna hodnota je prazdna mnozina */
				return new HashSet<Object>();
			} else {
				return val;
			}
		}
		
		public boolean containsKey(Object state, Object symbol) {
			return map.containsKey(new Input(state, symbol));
		}
		
                public class Input {
                    public Object state;
                    public Object symbol;
                    
                    public Input(Object state, Object symbol){
                        this.state = state;
                        this.symbol = symbol;
                    }
                }
	}

}
