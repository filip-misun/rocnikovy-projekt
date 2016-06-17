package languages;

import sets.Tuple;
import sets.Sets;
import sets.SetPartition;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class FSA implements FiniteDescription {

	private Set<Object> states, symbols, initialStates, finalStates;
	private Set<Object> symbolsAndEpsilon;
	private Delta delta;

	public FSA(Set<Object> states, Set<Object> symbols, Delta delta,
			Set<Object> initialStates, Set<Object> finalStates) {

		this.states = states;
		this.symbols = symbols;
		this.initialStates = initialStates;
		this.finalStates = finalStates;
		this.delta = delta;
		symbolsAndEpsilon = new HashSet<Object>(symbols);
		symbolsAndEpsilon.add(Word.EPSILON);
	}

	/**
	 * FSA is initialized from the input to the scanner. Format of the input is
	 * as follows: - 1.line: states of the FSA separated by space - 2.line:
	 * symbols of the FSA separated by space - 3.line: initial states of the FSA
	 * separated by space - 4.line: final states of the FSA separated by space -
	 * the remaining lines define the transition function, each line has the
	 * following format: p a -> q1 q2 q3 ... - this means, that FSA has
	 * transitions from the state p on symbol a to states q1, q2, q3, ...
	 */
	public FSA(Scanner s) {
		states = new HashSet<>();
		for (String str : s.nextLine().split(" ")) {
			states.add(str);
		}

		symbols = new HashSet<>();
		for (String str : s.nextLine().split(" ")) {
			symbols.add(str);
		}

		initialStates = new HashSet<>();
		for (String str : s.nextLine().split(" ")) {
			initialStates.add(str);
		}

		finalStates = new HashSet<>();
		for (String str : s.nextLine().split(" ")) {
			finalStates.add(str);
		}

		delta = new Delta();
		while (s.hasNext()) {
			String line = s.nextLine();
			String[] args = line.split(" -> ");
			if (args.length < 2)
				continue;

			String input[] = args[0].split(" ");
			if (input.length != 2)
				continue;
			String p = input[0];
			String a = input[1];

			Set<Object> output = new HashSet<>();
			for (String q : args[1].split(" ")) {
				output.add(q);
			}

			delta.put(p, a, output);
		}

		symbolsAndEpsilon = new HashSet<Object>(symbols);
		symbolsAndEpsilon.add(Word.EPSILON);
	}

	/**
	 * Returns the initial states of the FSA.
	 */
	public Set<Object> getInitialStates() {
		return initialStates;
	}

	/**
	 * Returns the final states of the FSA.
	 */
	public Set<Object> getFinalStates() {
		return finalStates;
	}

	/**
	 * Returns the transition function of the FSA.
	 */
	public Delta getDelta() {
		return delta;
	}

	/**
	 * Returns the symbols of the FSA.
	 */
	public Set<Object> getSymbols() {
		return symbols;
	}

	/**
	 * Returns the states of the FSA.
	 */
	public Set<Object> getStates() {
		return states;
	}

	/**
	 * Returns {@code true}, if the word w is accepted by the FSA.
	 */
	public boolean accepts(Word w) {
		FSA m = determinize();

		Object p = m.initialStates.iterator().next();

		for (int i = 0; i < w.length(); i++) {
			Object a = w.symbolAt(i);
			p = m.delta.get(p, a).iterator().next();
		}

		return m.finalStates.contains(p);
	}

	/**
	 * Returns an equivalent FSA, which contains no transitions on the empty
	 * word.
	 */
	public FSA epsilonFree() {
		Set<Object> Q = states;
		Set<Object> A = symbols;
		Delta D = new Delta();
		Set<Object> I = new HashSet<>();
		Set<Object> T = finalStates;

		/*
		 * Map which contains for each state q of FSA its epsilon tail, i.e.
		 * states p such that FSA can move from state q to state p on empty
		 * word.
		 */
		HashMap<Object, Set<Object>> tails = new HashMap<>();

		// We compute for each state its epsilon tail.
		for (Object p : states) {
			Set<Object> tail = new HashSet<>();
			Queue<Object> queue = new LinkedList<>();
			tail.add(p);
			queue.add(p);

			while (!queue.isEmpty()) {
				Object q = queue.remove();
				for (Object r : delta.get(q, Word.EPSILON)) {
					if (tail.contains(r))
						continue;
					tail.add(r);
					queue.add(r);
				}
			}

			tails.put(p, tail);
		}

		for (Object p : states) {
			for (Object a : symbols) {
				Set<Object> val = delta.get(p, a);
				Set<Object> newVal = new HashSet<>();
				for (Object q : val) {
					newVal.addAll(tails.get(q));
				}
				D.put(p, a, newVal);
			}
		}

		for (Object p : initialStates) {
			I.addAll(tails.get(p));
		}

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns an equivalent FSA, which is deterministic, contains no
	 * transitions on empty word and its transition function is complete.
	 */
	public FSA determinize() {
		FSA m = epsilonFree();

		Set<Object> Q = new HashSet<>();
		Set<Object> A = m.symbols;
		Delta D = new Delta();
		Set<Object> I = Sets.singleton(m.initialStates);
		Set<Object> T = new HashSet<>();

		Set<Set<Object>> checked = new HashSet<>();
		Queue<Set<Object>> queue = new LinkedList<>();
		queue.add(m.initialStates);
		while (!queue.isEmpty()) {
			Set<Object> currState = queue.remove();
			if (checked.contains(currState))
				continue;
			checked.add(currState);
			Q.add(currState);

			for (Object a : m.symbols) {
				Set<Object> newState = new HashSet<>();
				for (Object p : currState) {
					newState.addAll(m.delta.get(p, a));
					if (m.finalStates.contains(p)) {
						T.add(currState);
					}
				}
				D.put(currState, a, Sets.singleton(newState));
				queue.add(newState);
			}
		}

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns a string of the form "name<i>n</i>", where <i>n</i> is some
	 * number. It is guaranteed, that the FSA contains no state, which is equal
	 * to this string.
	 */
	private Object newState(String name) {
		int i = 1;
		while (true) {
			if (!states.contains(name + i))
				break;
		}
		return name + i;
	}

	/**
	 * Returns an equivalent FSA, which has single initial state.
	 */
	public FSA singleInitialState() {
		Object init = newState("i");

		Set<Object> Q = Sets.union(states, init);
		Set<Object> A = symbols;
		Delta D;
		Set<Object> I = Sets.singleton(init);
		Set<Object> T = finalStates;

		D = delta.clone();
		D.put(init, Word.EPSILON, initialStates);

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns an equivalent FSA, whose states are replaced by other objects.
	 * 
	 * @param map
	 *            - defines the renaming. If (<i>p</i>, <i>q</i>) is an entry in
	 *            {@code map}, state <i>p</i> is replaced by state <i>q</i>.
	 */
	public FSA renameStates(Map<Object, Object> map) {
		Set<Object> Q = Sets.translateSet(states, map);
		Set<Object> A = symbols;
		Delta D = new Delta();
		Set<Object> I = Sets.translateSet(initialStates, map);
		Set<Object> T = Sets.translateSet(finalStates, map);

		for (Object p : getStates()) {
			Object q = map.get(p);
			for (Object a : symbolsAndEpsilon) {
				Set<Object> val = delta.get(p, a);
				D.put(q, a, Sets.translateSet(val, map));
			}
		}

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns a FSA, whose language is equivalent to the union of languages of
	 * {@code this} and {@code m}.
	 */
	public FSA union(FSA m) {
		Map<Object, Object> map1 = new HashMap<>();
		Map<Object, Object> map2 = new HashMap<>();

		for (Object p : states) {
			map1.put(p, Sets.tuple(p, 1));
		}
		for (Object p : m.states) {
			map2.put(p, Sets.tuple(p, 2));
		}

		FSA m1 = renameStates(map1);
		FSA m2 = m.renameStates(map2);

		Set<Object> Q = Sets.union(m1.states, m2.states);
		Set<Object> A = Sets.union(m1.symbols, m2.symbols);
		Delta D = new Delta();
		Set<Object> I = Sets.union(m1.initialStates, m2.initialStates);
		Set<Object> T = Sets.union(m1.finalStates, m2.finalStates);

		for (Object p : m1.states) {
			for (Object a : m1.symbolsAndEpsilon) {
				Set<Object> val = m1.delta.get(p, a);
				D.put(p, a, val);
			}
		}

		for (Object p : m2.states) {
			for (Object a : m2.symbolsAndEpsilon) {
				Set<Object> val = m2.delta.get(p, a);
				D.put(p, a, val);
			}
		}

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns a FSA, whose language is equivalent to the intersection of
	 * languages of {@code this} and {@code m}.
	 */
	public FSA intersection(FSA m) {
		FSA m1 = this;
		FSA m2 = m;

		Set<Object> Q = Sets.product(m1.states, m2.states);
		Set<Object> A = Sets.intersection(m1.getSymbols(), m2.getSymbols());
		Delta D = new Delta();
		Set<Object> I = Sets.product(m1.initialStates, m2.initialStates);
		Set<Object> T = Sets.product(m1.finalStates, m2.finalStates);

		for (Object p1 : m1.states) {
			for (Object p2 : m2.states) {
				Set<Object> val1, val2;
				for (Object a : A) {
					val1 = m1.delta.get(p1, a);
					val2 = m2.delta.get(p2, a);
					D.put(Sets.tuple(p1, p2), a, Sets.product(val1, val2));
				}
				val1 = m1.delta.get(p1, Word.EPSILON);
				val2 = m2.delta.get(p2, Word.EPSILON);
				Set<Object> v = Sets.union(
						Sets.product(val1, Sets.singleton(p2)),
						Sets.product(Sets.singleton(p1), val2));
				D.put(Sets.tuple(p1, p2), Word.EPSILON, v);
			}
		}

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns a FSA, whose language is equivalent to the product of languages
	 * of {@code this} and {@code m} in this order.
	 */
	public FSA product(FSA m) {
		Map<Object, Object> map1 = new HashMap<>();
		Map<Object, Object> map2 = new HashMap<>();

		for (Object p : states) {
			map1.put(p, Sets.tuple(p, 1));
		}
		for (Object p : m.states) {
			map2.put(p, Sets.tuple(p, 2));
		}

		FSA m1 = renameStates(map1);
		FSA m2 = m.renameStates(map2);

		Set<Object> Q = Sets.union(m1.states, m2.states);
		Set<Object> A = Sets.union(m1.symbols, m2.symbols);
		Delta D = new Delta();
		Set<Object> I = m1.initialStates;
		Set<Object> T = m2.finalStates;

		for (Object p : m1.states) {
			for (Object a : m1.symbolsAndEpsilon) {
				Set<Object> val = m1.delta.get(p, a);
				D.put(p, a, val);
			}
		}

		for (Object p : m2.states) {
			for (Object a : m2.symbolsAndEpsilon) {
				Set<Object> val = m2.delta.get(p, a);
				D.put(p, a, val);
			}
		}

		for (Object p : m1.finalStates) {
			Set<Object> val = m1.delta.get(p, Word.EPSILON);
			D.put(p, Word.EPSILON, Sets.union(val, m2.initialStates));
		}

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns a FSA, whose language is equivalent to the iteration of the
	 * language of {@code this}.
	 */
	public FSA iteration() {
		FSA m = singleInitialState();

		Set<Object> Q = m.states;
		Set<Object> A = m.symbols;
		Delta D = m.delta.clone();
		Set<Object> I = m.initialStates;
		Set<Object> T = m.initialStates;

		for (Object p : m.finalStates) {
			Set<Object> val = m.delta.get(p, Word.EPSILON);
			D.put(p, Word.EPSILON, Sets.union(val, m.initialStates));
		}

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns a FSA, whose language is equivalent to the complement of the
	 * language of {@code this}.
	 */
	public FSA complement() {
		FSA m = determinize();

		Set<Object> Q = m.states;
		Set<Object> A = m.symbols;
		Delta D = m.delta;
		Set<Object> I = m.initialStates;
		Set<Object> T = Sets.difference(m.states, m.finalStates);

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns a transition function, such that if <i>p</i> is a state and
	 * <i>a</i> is a symbol, the output for the input (<i>p</i>, <i>a</i>) is a
	 * set of such a states <i>q</i>, so that this FSA has a transition from
	 * state <i>q</i> on symbol <i>a</i> to state <i>p</i>.
	 */
	private Delta reverseDelta() {
		Delta D = new Delta();
		for (Object p : states) {
			for (Object a : symbolsAndEpsilon) {
				Set<Object> val = delta.get(p, a);
				for (Object q : val) {
					Set<Object> v = D.get(q, a);
					v.add(p);
					D.put(q, a, v);
				}
			}
		}
		return D;
	}

	/**
	 * Returns a FSA, whose language is equivalent to the reverse of the
	 * language of {@code this}.
	 */
	public FSA reverse() {
		Set<Object> Q = states;
		Set<Object> A = symbols;
		Delta D = reverseDelta();
		Set<Object> I = finalStates;
		Set<Object> T = initialStates;

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns all the accessible states of this FSA. A state is accessible, if
	 * it can be reached by some sequence of transition from some initial state.
	 */
	public Set<Object> accessibleStates() {
		Set<Object> accessible = new HashSet<>(initialStates);
		Queue<Object> queue = new LinkedList<>(initialStates);

		while (!queue.isEmpty()) {
			Object p = queue.remove();
			for (Object a : symbolsAndEpsilon) {
				for (Object q : delta.get(p, a)) {
					if (accessible.contains(q))
						continue;
					queue.add(q);
					accessible.add(q);
				}
			}
		}

		return accessible;
	}

	/**
	 * Returns all the coaccessible states of this FSA. A state is coaccessible,
	 * if some final state can be reached by some sequence of transition from
	 * this state.
	 */
	public Set<Object> coaccessibleStates() {
		return reverse().accessibleStates();
	}

	/**
	 * Returns all the useful states of this FSA. A state is useful, if it is
	 * accessible and coaccessible at the same time, i.e. it can be reached from
	 * some initial state by some sequence of transitions and some final states
	 * can be reached from this state by some sequence of transitions.
	 */
	public Set<Object> usefulStates() {
		return Sets.intersection(accessibleStates(), coaccessibleStates());
	}

	/**
	 * Returns an equivalent FSA, which has only useful states (see
	 * {@link usefulStates}).
	 */
	public FSA trim() {
		Set<Object> Q = usefulStates();
		Set<Object> A = symbols;
		Delta D = new Delta();
		Set<Object> I = Sets.intersection(Q, initialStates);
		Set<Object> T = Sets.intersection(Q, finalStates);

		for (Object p : Q) {
			for (Object a : symbolsAndEpsilon) {
				Set<Object> val = delta.get(p, a);
				D.put(p, a, Sets.intersection(Q, val));
			}
		}

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns an equivalent FSA, whose transition function is complete, i.e.
	 * for each state <i>p</i> and each symbol <i>a</i> there is a transition
	 * from <i>p</i> on <i>a</i>.
	 */
	public FSA complete() {
		Object trash = newState("trash");

		Set<Object> Q = Sets.union(states, trash);
		Set<Object> A = symbols;
		Delta D = new Delta();
		Set<Object> I = initialStates;
		Set<Object> T = finalStates;

		for (Object p : states) {
			for (Object a : symbols) {
				Set<Object> val = delta.get(p, a);
				if (!val.isEmpty()) {
					D.put(p, a, val);
				} else {
					D.put(p, a, Sets.singleton(trash));
				}
			}
			Set<Object> val = delta.get(p, Word.EPSILON);
			D.put(p, Word.EPSILON, val);
		}

		for (Object a : symbols) {
			D.put(trash, a, Sets.singleton(trash));
		}

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns states, which can be reached by a single transitions from some
	 * state from {@code states} on the symbol {@code symbol}.
	 */
	private Set<Object> translateStates(Delta delta, Set<Object> states,
			Object symbol) {
		Set<Object> s = new HashSet<>();
		for (Object p : states) {
			Set<Object> val = delta.get(p, symbol);
			s.addAll(val);
		}
		return s;
	}

	/**
	 * Returns an equivalent FSA, which is deterministic, has no transitions on
	 * the empty word, its transition function is complete and the number of its
	 * states is minimal. The method is an implementation of the Hopcroft's
	 * algorithm.
	 */
	public FSA minimize() {
		FSA m = determinize();

		Delta rD = m.reverseDelta();

		Queue<Tuple<Integer, Object>> W = new LinkedList<>();
		Set<Tuple<Integer, Object>> inW = new HashSet<>();
		SetPartition P = new SetPartition(m.states);

		int k1 = m.states.size() - m.finalStates.size();
		int k2 = m.finalStates.size();

		if (k1 != 0 && k2 != 0) {
			P.split(m.finalStates);
			int j = (k1 < k2) ? 0 : 1;

			for (Object a : m.symbols) {
				Tuple<Integer, Object> t = new Tuple<>(j, a);
				W.add(t);
				inW.add(t);
			}

			while (!W.isEmpty()) {
				Tuple<Integer, Object> tpl = W.remove();
				inW.remove(tpl);
				Set<Object> s = P.getSet(tpl.first);
				Object b = tpl.second;

				Set<Object> splitter = translateStates(rD, s, b);
				for (Tuple<Integer, Integer> t : P.split(splitter)) {
					int j1 = t.first;
					int j2 = t.second;

					for (Object a : m.symbols) {
						Tuple<Integer, Object> tpl1 = new Tuple<>(j1, a);
						if (inW.contains(tpl1)) {
							Tuple<Integer, Object> tpl2 = new Tuple<>(j2, a);
							W.add(tpl2);
							inW.add(tpl2);
						} else {
							int i = (P.getSet(j1).size() < P.getSet(j2).size()) ? j1
									: j2;
							Tuple<Integer, Object> tpl2 = new Tuple<>(i, a);
							W.add(tpl2);
							inW.add(tpl2);
						}
					}
				}
			}
		}

		Set<Object> Q = new HashSet<>();
		Set<Object> A = m.symbols;
		Delta D = new Delta();
		Set<Object> I;
		Set<Object> T = new HashSet<>();

		for (Set<Object> s : P.getAllSets()) {
			Q.add(s);
			Object p = s.iterator().next();
			if (m.finalStates.contains(p))
				T.add(s);

			for (Object a : m.symbols) {
				Set<Object> val = m.delta.get(p, a);
				Object q = val.iterator().next();
				Set<Object> r = P.findSet(q);
				D.put(s, a, Sets.singleton(r));
			}
		}

		Object init = m.initialStates.iterator().next();
		I = Sets.singleton(P.findSet(init));

		return new FSA(Q, A, D, I, T);
	}

	/**
	 * Returns {@code true}, if the language accepted by this FSA is empty.
	 */
	public boolean isEmpty() {
		return !Sets.intersects(accessibleStates(), finalStates);
	}

	/**
	 * Returns {@code true}, if every word generated by the symbols of this FSA
	 * is accepted by this FSA.
	 */
	public boolean isFull() {
		return complement().isEmpty();
	}

	/**
	 * Returns {@code true}, if
	 */
	private boolean findCycle(Object p, Map<Object, Integer> visited) {
		Integer v = visited.get(p);

		if (v != null) {
			if (v.equals(1))
				return false;
			if (v.equals(2))
				return true;
		}

		visited.put(p, 2);

		for (Object a : symbols) {
			for (Object q : delta.get(p, a)) {
				if (findCycle(q, visited))
					return true;
			}
		}

		visited.put(p, 1);
		return false;
	}

	/**
	 * Returns {@code true}, if the language accepted by this FSA is finite.
	 */
	public boolean isFinite() {
		FSA m = singleInitialState().epsilonFree().trim();
		Object init = m.initialStates.iterator().next();
		return !m.findCycle(init, new HashMap<Object, Integer>());
	}

	/**
	 * Returns {@code true}, if the language accepted by this FSA intersects the
	 * language accepted by FSA {@code m}.
	 */
	public boolean intersects(FSA m) {
		return !intersection(m).isEmpty();
	}

	/**
	 * Returns {@code true}, if the language accepted by this FSA is a subset of
	 * the language accepted by FSA {@code m}.
	 */
	public boolean isSubsetOf(FSA m) {
		return !intersects(m.complement());
	}

	/**
	 * Returns {@code true}, if the language accepted by this FSA is a equal to
	 * the language accepted by FSA {@code m}.
	 */
	public boolean isEquivalentTo(FSA m) {
		return this.isSubsetOf(m) && m.isSubsetOf(this);
	}

	public void print(PrintStream out) {
		out.println(Sets.toString(states));
		out.println(Sets.toString(symbols));
		out.println(Sets.toString(initialStates));
		out.println(Sets.toString(finalStates));
		out.println(delta);
	}

	public static class Delta {

		private HashMap<Input, Set<Object>> map = new HashMap<>();

		public Delta() {
		}

		public Delta(HashMap<Input, Set<Object>> map) {
			this.map = map;
		}

		public void put(Object state, Object symbol, Set<Object> newStates) {
			map.put(new Input(state, symbol), newStates);
		}

		Set<Object> get(Object state, Object symbol) {
			Set<Object> val = map.get(new Input(state, symbol));
			if (val == null) {
				return new HashSet<>();
			} else {
				return val;
			}
		}

		public Delta clone() {
			return new Delta(new HashMap<>(map));
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<Input, Set<Object>> entry : map.entrySet()) {
				if (entry.getValue().isEmpty())
					continue;
				sb.append(entry.getKey().state + " " + entry.getKey().symbol
						+ " -> " + Sets.toString(entry.getValue()) + "\n");
			}
			return sb.toString();
		}

		public class Input {

			public Object state;
			public Object symbol;

			public Input(Object state, Object symbol) {
				this.state = state;
				this.symbol = symbol;
			}

			@Override
			public boolean equals(Object o) {
				if (o instanceof Input) {
					Input i = (Input) o;
					return this.state.equals(i.state)
							&& this.symbol.equals(i.symbol);
				} else {
					return false;
				}
			}

			@Override
			public int hashCode() {
				return state.hashCode() ^ symbol.hashCode();
			}
		}
	}
}