package rocnikovyprojekt;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.DataFormatException;
import rocnikovyprojekt.DeterministicPushdownAutomaton.TransitionFunction.Input;
import rocnikovyprojekt.DeterministicPushdownAutomaton.TransitionFunction.Output;

/**
 * Class representing transition function for pushdown automatons.
 * @author Jozef Rajník
 */
public class PDAdelta {

    private Map<Input, Set<Output>> map = new HashMap<>();
    
    public PDAdelta(){
        
    }
    
    public PDAdelta(Scanner s) throws DataFormatException{
        while(s.hasNext()){
            String line = s.nextLine();
            String args[] = line.split(" ");
            if(args.length < 4){
                throw new DataFormatException("Too few arguments in delta function line.");
            }
            int inLen = args[0].length() + args[1].length() + args[2].length() + 3;
            if(line.charAt(inLen) != '[' || line.charAt(line.length() - 1) != ']'){
                throw new DataFormatException("The output of delta function is not a set (missing []): " + line + ".");
            }
            String outputs = line.substring(inLen + 1, line.length() - 1);
            for(String str : outputs.split(", ")){
                if(!str.startsWith("(") || !str.endsWith(")")){
                    throw new DataFormatException("The output isn enclosed in (): " + line + ".");
                }
                String outargs[] = str.substring(1, str.length() - 1).split(",");
                if(outargs.length < 2){
                    throw new DataFormatException("The output has too few arguments: " + line + ".");
                }
                Object ch = args[1];
                if(args[1].equals("epsilon")){
                    ch = Word.EMPTYWORD;
                }
                Word w = Word.EMPTYWORD;
                if(!outargs[1].equals("epsilon")){
                    w = new Word(outargs[1].split(" "));
                }
                add(args[0], ch, args[2], outargs[0], w);
            }
        }
    }
    
    /**
     * Adds move from specified configuration into new state pushing specified
     * word to stack. Note that this method only add one from (possibly) many
     * moves from specified configuration.
     * @param state current state
     * @param tapeSymbol currently read symbol on tape or epsilon
     * @param stackSymbol the top stack symbol
     * @param newState new state
     * @param pushToStack word which will be pushed to stack 
     */
    public void add(Object state, Object tapeSymbol, Object stackSymbol,
            Object newState, Word pushToStack) {
        Input in = new Input(state, tapeSymbol, stackSymbol);
        Output out = new Output(newState, pushToStack);
        Set<Output> outputs = map.remove(in);
        if(outputs == null){
            outputs = new HashSet<>();
        }
        outputs.add(out);
        map.put(in, outputs);
    }

    public Set<Output> get(Object state, Object tapeSymbol, Object stackSymbol) {
        return map.getOrDefault(new Input(state, tapeSymbol, stackSymbol), new HashSet<>());
    }

    public boolean containsKey(Object state, Object tapeSymbol, Object stackSymbol) {
        return map.containsKey(new Input(state, tapeSymbol, stackSymbol));
    }
    
    public Set<Map.Entry<Input, Set<Output>>> entrySet(){
        return map.entrySet();
    }
    
    public Set<Object> getStates(){
        Set<Object> states = new HashSet<>();
        for(Map.Entry<Input, Set<Output>> entry : map.entrySet()){
            states.add(entry.getKey().state);
        }
        return states;
    }
    
    public Set<Object> getAlphabet(){
        Set<Object> alphabet = new HashSet<>();
        for(Map.Entry<Input, Set<Output>> entry : map.entrySet()){
            alphabet.add(entry.getKey().tapeSymbol);
        }
        return alphabet;
    }
    
    public Set<Object> getWorkingAlphabet(){
        Set<Object> walphabet = new HashSet<>();
        for(Map.Entry<Input, Set<Output>> entry : map.entrySet()){
            walphabet.add(entry.getKey().stackSymbol);
        }
        return walphabet;
    }
    
    public void print(PrintStream out){
        for(Map.Entry<Input, Set<Output>> entry : map.entrySet()){
            out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
