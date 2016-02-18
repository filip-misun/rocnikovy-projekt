package rocnikovyprojekt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import rocnikovyprojekt.FiniteAutomaton.Configuration;

public class DFA implements FiniteDescription{
	
	private TransitionFunction transitionFunction;
	private Object startState;
	private Set<Object> finalStates;
	private ArrayList<Configuration> lastComputation;
	
	public DFA(TransitionFunction transitionFunction,
			Object startState, Set<Object> finalStates) {
		this.transitionFunction = transitionFunction;
		this.startState = startState;
		this.finalStates = finalStates;
	}
        
        public DFA(Scanner s){
            startState = s.nextLine();
            finalStates = new HashSet<>();
            for(String str : s.nextLine().split(" ")){
                finalStates.add(str);
            }
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
        
        public Set<Object> getAlphabet(){
            return transitionFunction.getAlphabet();
        }
        
        public Set<Object> getStates(){
            return transitionFunction.getStates();
        }
        
        public void print(PrintStream out){
            out.println(startState);
            boolean first = true;
            for(Object state : finalStates){
                if(!first){
                    out.print(" ");
                }
                out.print(state);
                first = false;
            }
            transitionFunction.print(out);
        }
	
	public static class TransitionFunction {
		
		private HashMap<FAInput, Object> map = new HashMap<>();
                
                public TransitionFunction(){}
                
                public TransitionFunction(Scanner s){
                    while(s.hasNext()){
                        String[] line = s.nextLine().split(" ");
                        put(line[0], line[1], line[2]);
                    }
                }
		
		public void put(Object state, Object symbol, Object newState) {
			map.put(new FAInput(state, symbol), newState);
		}
		
		Object get(Object state, Object symbol) {
			return map.get(new FAInput(state, symbol));
		}
		
		public boolean containsKey(Object state, Object symbol) {
			return map.containsKey(new FAInput(state, symbol));
		}
                
                public Set<Map.Entry<FAInput, Object>> entrySet(){
                    return map.entrySet();
                }
                
                public Set<Object> getAlphabet(){
                    Set<Object> alphabet = new HashSet<>();
                    for(Map.Entry<FAInput, Object> entry : map.entrySet()){
                        alphabet.add(entry.getKey().symbol);
                    }
                    return alphabet;
                }
                
                public Set<Object> getStates(){
                    Set<Object> states = new HashSet<>();
                    for(Map.Entry<FAInput, Object> entry : map.entrySet()){
                        states.add(entry.getKey().state);
                    }
                    return states;
                }
                
                public void print(PrintStream out){
                    for(Map.Entry<FAInput, Object> entry : map.entrySet()){
                        out.println(entry.getKey().state + " " +
                                entry.getKey().symbol + " " +
                                entry.getValue());
                    }
                    out.flush();
                }
	}
	
}
