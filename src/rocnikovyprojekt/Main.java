package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.HashSet;

public class Main {

	public static void main(String[] args) {
            /*
		DeterministicFiniteAutomaton.TransitionFunction f1 = 
				new DeterministicFiniteAutomaton.TransitionFunction();
		f1.put(0, 'h', 1);
		f1.put(1, 'o', 0);
		HashSet<Object> finalStates = new HashSet<>();
		finalStates.add(0);
		DeterministicFiniteAutomaton m1 = new DeterministicFiniteAutomaton(
				f1, 0, finalStates);
		Word w = new Word();
		w.append('h');
		w.append('o');
		w.append('h');
		w.append('o');
		
		if (m1.accepts(w)) {
			System.out.println("M1 akceptuje");
		} else {
			System.out.println("M1 neakceptuje");
		}
		
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
		
		w = new Word();
		w.append('a');
		w.append('a');
		w.append('a');
		w.append('b');
		if (m2.accepts(w)) {
			System.out.println("M2 akceptuje");
		} else {
			System.out.println("M2 neakceptuje");
		}
		
		DeterministicPushdownAutomaton.TransitionFunction f3 =
				new DeterministicPushdownAutomaton.TransitionFunction();
		ArrayList<Object> list = new ArrayList<>();
		list.add('z');
		f3.put(0, 'a', 'z', 1, list);
		list = new ArrayList<>();
		list.add('z');
		list.add('a');
		f3.put(1, 'a', 'z', 1, list);
		list = new ArrayList<>();
		list.add('a');
		list.add('a');
		f3.put(1, 'a', 'a', 1, list);
		list = new ArrayList<>();
		f3.put(1, 'b', 'a', 2, list);
		f3.put(2, 'b', 'a', 2, list);
		f3.put(2, 'b', 'z', 0, list);
		f3.put(1, 'b', 'z', 0, list);
		set = new HashSet<>();
		set.add(0);
		DeterministicPushdownAutomaton m3 =
				new DeterministicPushdownAutomaton(f3, 0, 'z', set);
		
		w = new Word();
		w.append('a');
		w.append('a');
		w.append('a');
		w.append('b');
		w.append('b');
		w.append('b');
		if (m3.accepts(w)) {
			System.out.println("M3 akceptuje");
		} else {
			System.out.println("M3 neakceptuje");
		}*/

	}
}
