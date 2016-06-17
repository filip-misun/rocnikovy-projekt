package sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a partition of a set. A partition of a set X is a set
 * of nonempty subsets of X (called blocks) such that every element x in X is in
 * exactly one of these subsets.
 */
public class SetPartition {

	private List<Set<Object>> sets = new ArrayList<>();
	private List<Set<Object>> splits = new ArrayList<>();
	private Map<Object, Integer> elementSet = new HashMap<>();

	/**
	 * Creates set partition of the set {@code set}. It consists of a single
	 * block, which contains all the elements of the set {@code set}.
	 */
	public SetPartition(Set<Object> set) {
		Set<Object> s = new HashSet<>(set);
		for (Object x : s) {
			elementSet.put(x, 0);
		}
		sets.add(s);
		splits.add(new HashSet<Object>());
	}

	/**
	 * Refines the partition in the following way: every block <i>P</i> is split
	 * into two blocks - one contains all the elements of the intersection with
	 * {@code splitter} (we denote it by <i>P1</i>), the other contains the rest
	 * of the elements of the block (we denote it by <i>P2</i>). This split is
	 * not done, if either <i>P1</i> or <i>P2</i> would be empty (i.e. if the
	 * split is trivial for this block). If <i>P</i> has index <i>i</i>, then
	 * <i>P2</i> has index <i>i</i> and <i>P1</i> gets some new index.
	 * 
	 * @return List of the tuples of integers. If (<i>i1</i>, <i>i2</i>) is in
	 *         this list, it means, that block of index <i>i1</i> was
	 *         nontrivially split and the new blocks have now indices <i>i1</i>
	 *         and <i>i2</i>.
	 */
	public List<Tuple<Integer, Integer>> split(Set<Object> splitter) {
		List<Integer> w = new ArrayList<>();
		List<Tuple<Integer, Integer>> res = new ArrayList<>();

		for (Object x : splitter) {
			int i = elementSet.get(x);
			Set<Object> s1 = sets.get(i);
			Set<Object> s2 = splits.get(i);

			if (s2.isEmpty())
				w.add(i);
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

	/**
	 * Returns the block, which contains the element {@code element}.
	 */
	public Set<Object> findSet(Object element) {
		int i = elementSet.get(element);
		return sets.get(i);
	}

	/**
	 * Returns the block of index {@code index}.
	 */
	public Set<Object> getSet(int index) {
		return sets.get(index);
	}

	/**
	 * Returns list of all the blocks of the partition.
	 */
	public List<Set<Object>> getAllSets() {
		return sets;
	}

	public String toString() {
		return sets.toString();
	}
}
