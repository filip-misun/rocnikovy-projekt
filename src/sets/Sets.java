package sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Sets {

	public static Set<Object> union(Set<Object> s1, Set<Object> s2) {
		Set<Object> s3 = new HashSet<>();
		s3.addAll(s1);
		s3.addAll(s2);

		return s3;
	}

	public static Set<Object> union(Set<Object> set, Object m) {
		Set<Object> s = new HashSet<>(set);
		s.add(m);

		return s;
	}

	public static Set<Object> intersection(Set<Object> s1, Set<Object> s2) {
		Set<Object> s3 = new HashSet<>();

		for (Object m : s1) {
			if (s2.contains(m)) {
				s3.add(m);
			}
		}

		return s3;
	}

	public static boolean intersects(Set<Object> s1, Set<Object> s2) {
		for (Object m : s1) {
			if (s2.contains(m)) {
				return true;
			}
		}

		return false;
	}

	public static Set<Object> difference(Set<Object> s1, Set<Object> s2) {
		Set<Object> s3 = new HashSet<>();

		for (Object x : s1) {
			if (!s2.contains(x)) {
				s3.add(x);
			}
		}

		return s3;
	}

	public static Set<Object> product(Set<Object> s1, Set<Object> s2) {
		Set<Object> s3 = new HashSet<>();

		for (Object x : s1) {
			for (Object y : s2) {
				List<Object> t = new ArrayList<>();
				t.add(x);
				t.add(y);
				s3.add(t);
			}
		}

		return s3;
	}

	public static Set<Object> translateSet(Set<Object> set,
			Map<Object, Object> map) {
		Set<Object> s = new HashSet<>();
		for (Object x : set) {
			s.add(map.get(x));
		}
		return s;
	}

	public static Set<Object> singleton(Object obj) {
		Set<Object> s = new HashSet<>();
		s.add(obj);

		return s;
	}

	public static List<Object> tuple(Object o1, Object o2) {
		List<Object> t = new ArrayList<>();
		t.add(o1);
		t.add(o2);

		return t;
	}

	public static String toString(Set<Object> set) {
		StringBuilder sb = new StringBuilder();
		boolean space = false;
		for (Object o : set) {
			if (space) {
				sb.append(" ");
			}
			sb.append(o);
			space = true;
		}
		return sb.toString();
	}
}
