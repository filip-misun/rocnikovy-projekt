package rocnikovyprojekt;

import java.util.HashSet;

public class Main {

	public static void main(String[] args) {
		/* DeterministicFiniteAutomaton.TransitionFunction function = 
				new DeterministicFiniteAutomaton.TransitionFunction();
		function.put(0, 'h', 1);
		function.put(1, 'o', 0);
		HashSet<Object> finalStates = new HashSet<>();
		finalStates.add(0);
		DeterministicFiniteAutomaton machine = new DeterministicFiniteAutomaton(
				function, 0, finalStates);
		Word w = new Word();
		w.add('h');
		w.add('o');
		w.add('h');
		w.add('o');
		
		if (machine.accepts(w)) {
			System.out.println("akceptujem");
		} else {
			System.out.println("neakceptujem");
		}
		
		for (FiniteAutomaton.Configuration conf : machine.getLastComputation()) {
			System.out.println(conf.getPosition() + ", " + conf.getState());
		} */
		
		NondeterministicFiniteAutomaton.TransitionFunction f2 =
				new NondeterministicFiniteAutomaton.TransitionFunction();
		HashSet<Object> set = new HashSet<>();
		set.add(0);
		f2.put(0, 'a', set);
		set = new HashSet<>();
		set.add(1);
		f2.put(0, Word.EMPTYWORD, set);
		set = new HashSet<>();
		set.add(1);
		f2.put(1, 'b', set);
		
		set = new HashSet<>();
		set.add(0);
		set.add(1);
		NondeterministicFiniteAutomaton m2 =
				new NondeterministicFiniteAutomaton(f2, 0, set);
		
		Word w = new Word();
		w.append('a');
		w.append('a');
		w.append('a');
		w.append('b');
		if (m2.accepts(w)) {
			System.out.println("m2 akceptuje");
		} else {
			System.out.println("m2 neakceptuje");
		}
	}
}
