package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetPartition {
	
	private List<Set<Object>> sets = new ArrayList<>();
	private List<Set<Object>> splits = new ArrayList<>();
	private Map<Object, Integer> elementSet = new HashMap<>();
	
	public SetPartition(Set<Object> set) {
		Set<Object> s = new HashSet<>(set);
		for (Object x : s) {
			elementSet.put(x, 0);
		}
		sets.add(s);
		splits.add(new HashSet<Object>());
	}
	
	public List<Tuple<Integer, Integer>> split(Set<Object> splitter) {
		List<Integer> w = new ArrayList<>();
		List<Tuple<Integer, Integer>> res = new ArrayList<>();
		
		for (Object x : splitter) {
			int i = elementSet.get(x);
			Set<Object> s1 = sets.get(i);
			Set<Object> s2 = splits.get(i);
			
			if (s2.isEmpty()) w.add(i);
			s1.remove(x);
			s2.add(x);
		}
		
		for (int i : w) {
			Set<Object> s1 = sets.get(i);
			Set<Object> s2 = splits.get(i);
			
			if (s1.isEmpty()) {
				sets.set(i, s2);
				splits.set(i, new HashSet<Object>());
			} else {
				sets.add(s2);
				splits.add(new HashSet<Object>());
				splits.set(i, new HashSet<Object>());
				
				int j = sets.size() - 1;
				for (Object x : s2) {
					elementSet.put(x, j);
				}
				
				Tuple<Integer, Integer> t = new Tuple<>(i, j);
				res.add(t);
			}
		}
		
		return res;
	}
	
	public Set<Object> findSet(Object element) {
		int i = elementSet.get(element);
		return sets.get(i);
	}
	
	public Set<Object> getSet(int index) {
		return sets.get(index);
	}
	
	public List<Set<Object>> getAllSets() {
		return sets;
	}
	
	public String toString() {
		return sets.toString();
	}
}
