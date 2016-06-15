package rocnikovyprojekt;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.DataFormatException;

public class PushdownAutomaton implements FiniteDescription {

    private Set<Object> states;
    private Set<Object> alphabet;
    private Set<Object> workingAlphabet;
    private Object startState;
    private Object stackStart;
    private PDAdelta delta;
    
    public PushdownAutomaton(Set<Object> K, Set<Object> Sigma, Set<Object> Gamma,
            PDAdelta d, Object q0, Object Z0){
        states = K;
        alphabet = Sigma;
        workingAlphabet = Gamma;
        startState = q0;
        delta = d;
        stackStart = Z0;
    }
    
    public PushdownAutomaton(PDAdelta d, Object q0,
            Object Z0){
        startState = q0;
        delta = d;
        stackStart = Z0;
        updateSets();
    }
    
    /**
     * Initializes PDA from Scanner.
     * Input format:
     * First line contains start state and start stack symbol.
     * Rest of lines contains lines of delta-function.
     * Each of that lines has format
     * state tape stack [(output),(output),...,(output)],
     * where (output) has format (newState pushToStack).
     * @param s Scanner, which automaton is initialized from.
     * @throws java.util.zip.DataFormatException if the file has wrong format.
     */
    public PushdownAutomaton(Scanner s) throws DataFormatException{
        String line = s.nextLine();
        while(line.startsWith("//")){
            line = s.nextLine();
        }
        String args[] = line.split(" ");
        if(args.length < 2){
            throw new DataFormatException("The first line does not contain start state and start stack symbol.");
        }
        startState = args[0];
        stackStart = args[1];
        delta = new PDAdelta(s);
        updateSets();
    }
    
    private void updateSets(){
        states = delta.getStates();
        states.add(startState);
        alphabet = delta.getAlphabet();
        workingAlphabet = delta.getWorkingAlphabet();
        workingAlphabet.add(stackStart);
    }
    
    public Set<Object> getStates(){
        return states;
    }
    
    public Set<Object> getAlphabet(){
        return alphabet;
    }
    
    public Set<Object> getWorkingAlphabet(){
        return workingAlphabet;
    }
    
    public Object getStartState(){
        return startState;
    }
    
    public Object getStackStart(){
        return stackStart;
    }
    
    public PDAdelta getDelta(){
        return delta;
    }
    
    /**
     * Prints this automaton to the specified PrintStream.
     * @param out 
     */
    public void print(PrintStream out){
        out.println(startState + " " + stackStart);
        delta.print(out);
    }
    
    public static class Configuration implements FiniteDescription {

        private Object state;
        private List<Object> stack;
        private int position;

        public Configuration() {
        }

        public Configuration(Object state, List<Object> stack, int position) {
            this.state = state;
            this.stack = stack;
            this.position = position;
        }

        public Object getState() {
            return this.state;
        }

        public void setState(Object state) {
            this.state = state;
        }

        public List<Object> getStack() {
            return this.stack;
        }

        public void setStack(List<Object> stack) {
            this.stack = stack;
        }

        public int getPosition() {
            return this.position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

}
