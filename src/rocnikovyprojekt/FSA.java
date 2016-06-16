package rocnikovyprojekt;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class FSA implements FiniteDescription {

	private Set<Object> states, symbols, initialStates, finalStates, symbolsAndEpsilon;
	private Delta delta;
	
	public FSA(Set<Object> states, Set<Object> symbols, Delta delta,
			Set<Object> initialStates, Set<Object> finalStates) {
		
		this.states =states;
		this.symbols = symbols;
		this.initialStates = initialStates;
		this.finalStates = finalStates;
		this.delta = delta;
		symbolsAndEpsilon = new HashSet<Object>(symbols);
		symbolsAndEpsilon.add(Word.EMPTYWORD);		
	}
		
	/**
         * Initializes FSA from a given Scanner.
         * The inut format is following:
         * - first line contains start states (space-separated),
         * - second line contains final states (space-separated),
         * - following lines contain lines of transition function.
         * One line of transition function is of format
         * cstate character [state, state, ..., state],
         * where cstate is current state, character is charecter read from 
         * the tape and [state, ..., state] is set of new states.
         * @param s 
         */
	public FSA(Scanner s) {
		String line = s.nextLine();
                initialStates = new HashSet<>(Arrays.asList(line.split(" ")));
                line = s.nextLine();
		finalStates = new HashSet<>(Arrays.asList(line.split(" ")));
                delta = new Delta(s);
	}
	
	
	public Set<Object> getInitialStates() {
		return initialStates;
	}

	public Set<Object> getFinalStates() {
		return finalStates;
	}

	public Delta getDelta() {
		return delta;
	}

	public Set<Object> getSymbols() {
		return symbols;
	}
	
	public Set<Object> getStates() {
		return states;
	}
	
	public boolean accepts(Word w) {
		FSA m = determinize().complete();
		
		Object p = m.initialStates.iterator().next();
		
		for (int i = 0; i < w.length(); i++) {
			Object a = w.symbolAt(i);
			p = m.delta.get(p, a).iterator().next();
		}
		
		return m.finalStates.contains(p);
	}
	
	public FSA epsilonFree() {
		// TODO
		return null;
	}
	
	public FSA determinize() {
		// TODO
		return null;
	}
	
	public FSA singleInitialState() {
		Object init = new Object();
		
		Set<Object> Q = Sets.union(states, init);
		Set<Object> A = symbols;
		Delta D;
		Set<Object> I = Sets.singleton(init);
		Set<Object> T = finalStates;
		
		D = delta.clone();
		D.put(init, Word.EMPTYWORD, initialStates);
		
		return new FSA(Q, A, D, I, T);
	}
	
	
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
				val1 = m1.delta.get(p1, Word.EMPTYWORD);
				val2 = m2.delta.get(p2, Word.EMPTYWORD);
				Set<Object> v = Sets.union(Sets.product(val1, Sets.singleton(p2)),
						Sets.product(Sets.singleton(p1), val2));
				D.put(Sets.tuple(p1, p2), Word.EMPTYWORD, v);				
			}
		}
		
		return new FSA(Q, A, D, I, T);
	}
	
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
			Set<Object> val = m1.delta.get(p, Word.EMPTYWORD);
			D.put(p, Word.EMPTYWORD, Sets.union(val, m1.initialStates));
		}
		
		return new FSA(Q, A, D, I, T);
	}
	
	public FSA iteration() {
		FSA m = singleInitialState();
		
		Set<Object> Q = m.states;
		Set<Object> A = m.symbols;
		Delta D = m.delta.clone();
		Set<Object> I = m.initialStates;
		Set<Object> T = m.initialStates;
		
		for (Object p : m.finalStates) {
			Set<Object> val = m.delta.get(p, Word.EMPTYWORD);
			D.put(p, Word.EMPTYWORD, Sets.union(val, m.initialStates));			
		}

		return new FSA(Q, A, D, I, T);
	}
	
	public FSA complement() {
		FSA m = determinize().complete();
		
		Set<Object> Q = m.states;
		Set<Object> A = m.symbols;
		Delta D = m.delta;
		Set<Object> I = m.initialStates;
		Set<Object> T = Sets.difference(Q, m.finalStates);
		
		return new FSA(Q, A, D, I, T);
	}
	
	private Delta reverseDelta() {
		Delta D = new Delta();
		for (Object p : states) {
			for (Object a : symbolsAndEpsilon) {
				Set<Object> val = delta.get(p, a);
				for (Object q : val) {
					Set<Object> v = D.get(q, a);
					v.add(p);
				}
			}
		}
		return D;
	}
	
	public FSA reverse() {
		Set<Object> Q = states;
		Set<Object> A = symbols;
		Delta D = reverseDelta();
		Set<Object> I = finalStates;
		Set<Object> T = initialStates;
		
		return new FSA(Q, A, D, I, T);
	}

	public Set<Object> accessibleStates() {
		Queue<Object> queue = new LinkedList<>();
		Set<Object> accessible = new HashSet<>(initialStates);
		queue.add(initialStates);
		
		while (!queue.isEmpty()) {
			Object p = queue.remove();			
			for (Object a : symbolsAndEpsilon) {
				for (Object q : delta.get(p, a)) {
					if (accessible.contains(q)) continue;
					queue.add(q);
					accessible.add(q);
				}
			}
		}
		
		return accessible;
	}
	
	public Set<Object> coaccessibleStates() {
		return reverse().accessibleStates();
	}
	
	public Set<Object> usefulStates() {
		return Sets.intersection(accessibleStates(), coaccessibleStates());
	}
	
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
	
	public FSA complete() {
		Object trash = new Object();
		
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
			Set<Object> val = delta.get(p, Word.EMPTYWORD);
			D.put(p, Word.EMPTYWORD, val);
		}
		
		for (Object a : symbols) {
			D.put(trash, a, Sets.singleton(trash));
		}
		
		return new FSA(Q, A, D, I, T);		
	}
	
	private Set<Object> translateStates(Delta delta, Set<Object> states, Object symbol) {
		Set<Object> s = new HashSet<>();
		for (Object p : states) {
			Set<Object> val = delta.get(p, symbol);
			s.addAll(val);
		}
		return s;
	}
	
	public FSA minimize() {
		FSA m = determinize().complete();
		
		Delta rD = m.reverseDelta();
		
		Queue<Tuple<Integer, Object>> W = new LinkedList<>();
		Set<Tuple<Integer, Object>> inW = new HashSet<>();
		SetPartition P = new SetPartition(m.states);
		
		int k1 = m.finalStates.size();
		int k2 = m.states.size() - m.finalStates.size();
		
		if (k1 == 0 || k2 == 0) {
			
		} else {
			P.split(m.finalStates);
			int j = (k1 < k2) ? 0 : 1;
			
			for (Object a : symbols) {
				Tuple<Integer, Object> t = new Tuple<>(j, a);
				W.add(t);
				inW.add(t);
			}
			
			while (!W.isEmpty()) {
				Tuple<Integer, Object> tpl = W.remove();
				Set<Object> s = P.getSet(tpl.first);
				Object b = tpl.second;
				
				Set<Object> splitter = translateStates(rD, s, b);
				for (Tuple<Integer, Integer> t : P.split(splitter)) {
					int j1 = t.first;
					int j2 = t.second;
					
					for (Object a : symbols) {
						tpl = new Tuple<>(j1, a);
						if (inW.contains(tpl)) {
							tpl = new Tuple<>(j2, a);
							W.add(tpl);
							inW.add(tpl);
						} else {
							int i = (P.getSet(j1).size() < P.getSet(j2).size()) ? j1 : j2;
							tpl = new Tuple<>(i, a);
							W.add(tpl);
							inW.add(tpl);
						}
					}
				}
			}
		}
		
		Set<Object> Q = new HashSet<>();
		Set<Object> A = symbols;
		Delta D = new Delta();
		Set<Object> I;
		Set<Object> T = new HashSet<>();
		
		for (Set<Object> s : P.getAllSets()) {
			Q.add(s);
			Object p = s.iterator().next();
			if (finalStates.contains(p)) T.add(s);
			
			for (Object a : symbols) {
				Set<Object> val = m.delta.get(p, a);
				Object q = val.iterator().next();
				Set<Object> r = P.findSet(q);
				D.put(s, a, r);
			}
		}
		
		Object init = m.initialStates.iterator().next();
		I = Sets.singleton(P.findSet(init));
		
		return new FSA(Q, A, D, I, T);
	}
	
	public boolean isEmpty() {
		return Sets.intersects(accessibleStates(), finalStates);
	}
	
	// TODO: Ako nazvat tuto funkciu?
	public boolean isFull() {
		return complement().isEmpty();
	}
	
	private boolean findCycle(Object p, Map<Object, Integer> visited) {
		int v = visited.get(p);

		if (v == 1) return false;
		if (v == 2) return true;
		
		visited.put(p, 2);
		
		for (Object a : symbols) {
			for (Object q : delta.get(p, a)) {
				if (findCycle(q, visited)) return true;
			}
		}
		
		visited.put(p, 1);
		return false;
	}
	
	public boolean isFinite() {
		FSA m = singleInitialState().epsilonFree().trim();
		Object init = m.initialStates.iterator().next();
		return !m.findCycle(init, new HashMap<Object, Integer>());
	}
	
	public boolean intersects(FSA m) {
		return !intersection(m).isEmpty();
	}
		
	public boolean isSubsetOf(FSA m) {
		return !intersects(m.complement());
	}
	
	public boolean isEquivalentTo(FSA m) {
		return this.isSubsetOf(m) && m.isSubsetOf(this);
	}
	
	/**
	 * Prints text representation of this DFA to specified PrintStream. It
	 * prints this DFA as following: start state space-separeted final states
	 * lines with transition funcion (see TransitionFunction.print)
	 * 
	 * @param out
	 *            PrintStream which this shoud be printed to.
	 */
	public void print(PrintStream out) {
		out.println(Sets.toString(initialStates));
		out.println(Sets.toString(finalStates));
		delta.print(out);
	}

	public static class Delta {

		private HashMap<FAInput, Set<Object>> map = new HashMap<>();

		public Delta() {
		}
		
		public Delta(Scanner s){
                    while(s.hasNext()){
                        String[] line = s.nextLine().split(" ");
                        Object character = line[1];
                        if(line[1].toLowerCase().equals("epsilon")){
                            character = Word.EMPTYWORD;
                        }
                        HashSet<Object> set = new HashSet<>();
                        if(line[2].charAt(0) == '[' &&
                                line[2].charAt(line[2].length() - 1) == ']'){
                            String arg2 = new String(line[2].toCharArray(), 1, line[2].length() - 2);
                            set.addAll(Arrays.asList(arg2.split(", ")));
                        } else {
                            set.add(line[2]);
                        }
                        put(line[0], character, set);
                    }
                }
		
		public Delta(HashMap<FAInput, Set<Object>> map) {
			this.map = map;
		}

		public void put(Object state, Object symbol, Set<Object> newStates) {
			map.put(new FAInput(state, symbol), newStates);
		}

		Set<Object> get(Object state, Object symbol) {
			Set<Object> val = map.get(new FAInput(state, symbol));
			if (val == null) {
				return new HashSet<>();
			} else {
				return val;
			}
		}

		public Delta clone() {
			return new Delta(new HashMap<>(map));
		}

		/**
		 * Prints string representation of transition function to specified
		 * PrintStream. The printed string consists of several lines. Each line
		 * contains input state, read character and output state.
		 * 
		 * @param out
		 *            PrintStream which this shoud be printed to.
		 */
		public void print(PrintStream out) {
			for (Map.Entry<FAInput, Set<Object>> entry : map.entrySet()) {
				out.println(entry.getKey().state + " " + entry.getKey().symbol
						+ " " + entry.getValue());
			}
			out.flush();
		}
	}

}