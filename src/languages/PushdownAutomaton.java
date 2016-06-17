package languages;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.DataFormatException;

/**
 * Class representing push down automaton (PDA).
 *
 * @author Jozef Rajnik
 */
public class PushdownAutomaton implements FiniteDescription {

    private Set<Object> states;
    private Set<Object> alphabet;
    private Set<Object> workingAlphabet;
    private Object startState;
    private Object stackStart;
    private Delta delta;

    /**
     * Initializes PDA from given parameters, which are stated in formal
     * definition of PDA.
     *
     * @param K set of states
     * @param Sigma input alphabet - symbols which automaton reads from tape.
     * @param Gamma working alphabet - symbols which automaton puts to stack.
     * @param d transition function
     * @param q0 start state
     * @param Z0 start symbol on stack
     */
    public PushdownAutomaton(Set<Object> K, Set<Object> Sigma, Set<Object> Gamma,
            Delta d, Object q0, Object Z0) {
        states = K;
        alphabet = Sigma;
        workingAlphabet = Gamma;
        startState = q0;
        delta = d;
        stackStart = Z0;
    }

    /**
     * Initializes PDA from tranzition function, start state and start stack
     * symbol. The set of states and alphabets are computed from transition
     * function (as smallest possible sets).
     *
     * @param d transition function
     * @param q0 start state
     * @param Z0 start symbol on stack
     */
    public PushdownAutomaton(Delta d, Object q0,
            Object Z0) {
        startState = q0;
        delta = d;
        stackStart = Z0;
        updateSets();
    }

    /**
     * Initializes PDA from Scanner. Input format: First line contains start
     * state and start stack symbol. Rest of lines contains lines of
     * delta-function. Each of that lines has format state tape stack
     * [(output),(output),...,(output)], where (output) has format (newState
     * pushToStack).
     *
     * @param s Scanner, which automaton is initialized from.
     * @throws java.util.zip.DataFormatException if the file has wrong format.
     */
    public PushdownAutomaton(Scanner s) throws DataFormatException {
        String line = s.nextLine();
        while (line.startsWith("//")) {
            line = s.nextLine();
        }
        String args[] = line.split(" ");
        if (args.length < 2) {
            throw new DataFormatException("The first line does not contain start state and start stack symbol.");
        }
        startState = args[0];
        stackStart = args[1];
        delta = new Delta(s);
        updateSets();
    }

    /**
     * Private method which computes set of states and alphabets from transition
     * function.
     */
    private void updateSets() {
        states = delta.getStates();
        states.add(startState);
        alphabet = delta.getAlphabet();
        workingAlphabet = delta.getWorkingAlphabet();
        workingAlphabet.add(stackStart);
    }

    /**
     * Returns set of states of this automaton.
     */
    public Set<Object> getStates() {
        return states;
    }

    public Set<Object> getAlphabet() {
        return alphabet;
    }

    public Set<Object> getWorkingAlphabet() {
        return workingAlphabet;
    }

    public Object getStartState() {
        return startState;
    }

    public Object getStackStart() {
        return stackStart;
    }

    public Delta getDelta() {
        return delta;
    }

    /**
     * Prints this automaton to the specified PrintStream.
     *
     * @param out PrintStream which this automaton should be printed to.
     */
    public void print(PrintStream out) {
        out.println(startState + " " + stackStart);
        delta.print(out);
    }

    /**
     * Class representing configuration of PDA.
     */
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

    /**
     * Class representing transition function for pushdown automatons.
     */
    public static class Delta {

        private Map<Input, Set<Output>> map = new HashMap<>();

        public Delta() {

        }

        /**
         * Initializes delta function from scanner.
         */
        public Delta(Scanner s) throws DataFormatException {
            while (s.hasNext()) {
                String line = s.nextLine();
                String args[] = line.split(" ");
                if (args.length < 4) {
                    throw new DataFormatException("Too few arguments in delta function line.");
                }
                int inLen = args[0].length() + args[1].length() + args[2].length() + 3;
                if (line.charAt(inLen) != '[' || line.charAt(line.length() - 1) != ']') {
                    throw new DataFormatException("The output of delta function is not a set (missing []): " + line + ".");
                }
                String outputs = line.substring(inLen + 1, line.length() - 1);
                for (String str : outputs.split(", ")) {
                    if (!str.startsWith("(") || !str.endsWith(")")) {
                        throw new DataFormatException("The output isn enclosed in (): " + line + ".");
                    }
                    String outargs[] = str.substring(1, str.length() - 1).split(",");
                    if (outargs.length < 2) {
                        throw new DataFormatException("The output has too few arguments: " + line + ".");
                    }
                    Object ch = args[1];
                    if (args[1].equals("epsilon")) {
                        ch = Word.EPSILON;
                    }
                    Word w = Word.EPSILON;
                    if (!outargs[1].equals("epsilon")) {
                        w = new Word(outargs[1].split(" "));
                    }
                    add(args[0], ch, args[2], outargs[0], w);
                }
            }
        }

        /**
         * Adds move from specified configuration into new state pushing
         * specified word to stack. Note that this method only add one from
         * (possibly) many moves from specified configuration.
         *
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
            if (outputs == null) {
                outputs = new HashSet<>();
            }
            outputs.add(out);
            map.put(in, outputs);
        }

        /**
         * Returns set of outputs corresponding to given parameters.
         *
         * @param state state of automaton
         * @param tapeSymbol symbol read from input tape
         * @param stackSymbol symol on the top of the stck
         * @return
         */
        public Set<Output> get(Object state, Object tapeSymbol, Object stackSymbol) {
            return map.getOrDefault(new Input(state, tapeSymbol, stackSymbol), new HashSet<>());
        }

        public boolean containsKey(Object state, Object tapeSymbol, Object stackSymbol) {
            return map.containsKey(new Input(state, tapeSymbol, stackSymbol));
        }

        public Set<Map.Entry<Input, Set<Output>>> entrySet() {
            return map.entrySet();
        }

        /**
         * Returns all states contained in this transition function.
         */
        public Set<Object> getStates() {
            Set<Object> states = new HashSet<>();
            for (Map.Entry<Input, Set<Output>> entry : map.entrySet()) {
                states.add(entry.getKey().state);
            }
            return states;
        }

        /**
         * Returns all input symbols contained in this transition function.
         */
        public Set<Object> getAlphabet() {
            Set<Object> alphabet = new HashSet<>();
            for (Map.Entry<Input, Set<Output>> entry : map.entrySet()) {
                alphabet.add(entry.getKey().tapeSymbol);
            }
            return alphabet;
        }

        /**
         * Returns all stack symbols contained in this transition function.
         */
        public Set<Object> getWorkingAlphabet() {
            Set<Object> walphabet = new HashSet<>();
            for (Map.Entry<Input, Set<Output>> entry : map.entrySet()) {
                walphabet.add(entry.getKey().stackSymbol);
            }
            return walphabet;
        }

        public void print(PrintStream out) {
            for (Map.Entry<Input, Set<Output>> entry : map.entrySet()) {
                out.println(entry.getKey() + " " + entry.getValue());
            }
        }

        /**
         * Class representing Input of a transition function of a PDA.
         */
        public static class Input {

            Object state;
            Object tapeSymbol;
            Object stackSymbol;

            public Input(Object state, Object tapeSymbol, Object stackSymbol) {
                this.state = state;
                this.tapeSymbol = tapeSymbol;
                this.stackSymbol = stackSymbol;
            }

            @Override
            public boolean equals(Object o) {
                if (o instanceof Input) {
                    Input in = (Input) o;
                    return in.state.equals(this.state)
                            && in.stackSymbol.equals(this.stackSymbol)
                            && in.tapeSymbol.equals(this.tapeSymbol);
                } else {
                    return false;
                }
            }

            @Override
            public int hashCode() {
                return Objects.hash(state, tapeSymbol, stackSymbol);
            }

            @Override
            public String toString() {
                return state + " " + tapeSymbol + " " + stackSymbol;
            }
        }

        /**
         * Class representing Input of a transition function of a PDA.
         */
        public static class Output {

            public Object newState;
            public Word pushToStack;

            public Output(Object newState, Word pushToStack) {
                this.newState = newState;
                this.pushToStack = pushToStack;
            }

            /**
             * Initializes Output form String. The format of the string is
             * (state;word).
             *
             * @param s
             */
            public Output(String s) {
                String str = s.substring(1, s.length() - 1);
                newState = str.split(",")[0];
                pushToStack = new Word(str.split(",")[1].split(" "));
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof Output)) {
                    return false;
                }
                Output val = (Output) obj;
                return this.newState.equals(val.newState)
                        && this.pushToStack.equals(val.pushToStack);
            }

            @Override
            public int hashCode() {
                return Objects.hash(newState, pushToStack);
            }

            @Override
            public String toString() {
                return "(" + newState + "," + pushToStack + ")";
            }
        }
    }
}
