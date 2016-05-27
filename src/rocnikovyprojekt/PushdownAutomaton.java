package rocnikovyprojekt;

import java.io.PrintStream;
import java.util.List;
import java.util.Set;

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
    
    public PushdownAutomaton(PDAdelta d, Object q0,
            Object Z0){
        startState = q0;
        delta = d;
        stackStart = Z0;
    }
    
    public void print(PrintStream out){
        out.println(startState + " " + stackStart);
        out.println();
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
