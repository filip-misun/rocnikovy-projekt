package conversions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import rocnikovyprojekt.FiniteDescription;

public class ConversionGraph {
	
	private Map<Class<? extends FiniteDescription>, Vertex> map = new HashMap<>();
	
	public void addEdge(Conversion conv) {
		Class<? extends FiniteDescription> afrom = conv.getFrom();
		Class<? extends FiniteDescription> ato = conv.getTo();
		if (!map.containsKey(afrom)) {
			map.put(afrom, new Vertex(afrom));
		}
		if (!map.containsKey(ato)) {
			map.put(ato, new Vertex(ato));
		}
		map.get(afrom).addEdge(map.get(ato), conv);
	}
	
	public ConversionPlan findOptimalConversionPlan(ConversionRequest request,
			ConversionWeightEvaluator weightEval) {
		
		int inLen = request.getInputClasses().size();
		List<Map<Class<? extends FiniteDescription>, Integer>> distMapList =
				new ArrayList<>();
		List<Map<Class<? extends FiniteDescription>, Conversion>> lastEdgeMapList =
				new ArrayList<>();
		
		for (int i = 0; i < inLen; i++) {
			Map<Class<? extends FiniteDescription>, Integer> dist = new HashMap<>();
			Map<Class<? extends FiniteDescription>, Conversion> lastEdge =
					new HashMap<>();
			findShortestPathsFrom(request.getInputClasses().get(i), dist,
					lastEdge, weightEval);
			distMapList.add(dist);
			lastEdgeMapList.add(lastEdge);
		}
		
		if (request.hasOutputClass()) {
			Map<Class<? extends FiniteDescription>, Integer> dist = new HashMap<>();
			Map<Class<? extends FiniteDescription>, Conversion> lastEdge =
					new HashMap<>();
			findShortestPathsInto(request.getOutputClass(), dist, lastEdge, weightEval);
			distMapList.add(dist);
			lastEdgeMapList.add(lastEdge);
		}
		
		int tuplesLen= (request.hasOutputClass() ? (inLen + 1) : inLen);
		int minWeight = Integer.MAX_VALUE;
		List<Class<? extends FiniteDescription>> minWeightTuple = null;
		for (List<Class<? extends FiniteDescription>> tuple :
				new ClassTuples(tuplesLen)) {
			if (request.getClassTuplesSelector().selects(tuple)) {
				int sum = 0;
				for (int i = 0; i < tuplesLen; i++) {
					Map<Class<? extends FiniteDescription>, Integer> distMap =
							distMapList.get(i);
					int w = distMap.get(tuple.get(i));
					if (w == Integer.MAX_VALUE) continue;
					sum += w;
				}
				if (sum < minWeight) {
					minWeight = sum;
					minWeightTuple = tuple;
				}
			}
		}
		
		if (minWeight == Integer.MAX_VALUE) return null;
		
		ConversionPlan plan = new ConversionPlan(inLen);
		for (int i = 0; i < inLen; i++) {
			List<Conversion> convPath = getInputConversionPath(lastEdgeMapList.get(i),
					request.getInputClasses().get(i), minWeightTuple.get(i));
			plan.setInputConversions(i, convPath);
		}
		if (request.hasOutputClass()) {
			List<Conversion> convPath = getOutputConversionPath(
					lastEdgeMapList.get(inLen), request.getOutputClass(),
					minWeightTuple.get(inLen));
			plan.setOutputConversions(convPath);
		}
		
		return plan;
	}
	
	private List<Conversion> getInputConversionPath(
			Map<Class<? extends FiniteDescription>, Conversion> lastEdgeMap,
			Class<? extends FiniteDescription> start,
			Class<? extends FiniteDescription> end) {
		
		return getConversionPath(lastEdgeMap, start, end, false);
	}

	private List<Conversion> getOutputConversionPath(
			Map<Class<? extends FiniteDescription>, Conversion> lastEdgeMap,
			Class<? extends FiniteDescription> start,
			Class<? extends FiniteDescription> end) {
		
		return getConversionPath(lastEdgeMap, start, end, true);
	}
	
	private List<Conversion> getConversionPath(
			Map<Class<? extends FiniteDescription>, Conversion> lastEdgeMap,
			Class<? extends FiniteDescription> start,
			Class<? extends FiniteDescription> end, boolean isOutputPath) {
		
		LinkedList<Conversion> res = new LinkedList<>();
		Class<? extends FiniteDescription> currentClass = end;
		while (!currentClass.equals(start)) {
			Conversion conv = lastEdgeMap.get(currentClass);
			if (!isOutputPath) {
				res.addFirst(conv);
				currentClass = conv.getFrom();
			} else {
				res.addLast(conv);
				currentClass = conv.getTo();
			}
		}
		return res;
	}
	
	private void findShortestPathsFrom(Class<? extends FiniteDescription> from,
			Map<Class<? extends FiniteDescription>, Integer> distance,
			Map<Class<? extends FiniteDescription>, Conversion> lastEdge,
			ConversionWeightEvaluator weightEval) {
		
		findShortestPaths(from, distance, lastEdge, weightEval, false);
	}
	
	private void findShortestPathsInto(Class<? extends FiniteDescription> into,
			Map<Class<? extends FiniteDescription>, Integer> distance,
			Map<Class<? extends FiniteDescription>, Conversion> lastEdge,
			ConversionWeightEvaluator weightEval) {
		
		findShortestPaths(into, distance, lastEdge, weightEval, true);
	}	
	
	/* Bellman-Ford algorithm */
	private void findShortestPaths(Class<? extends FiniteDescription> start,
			Map<Class<? extends FiniteDescription>, Integer> distance,
			Map<Class<? extends FiniteDescription>, Conversion> lastEdge,
			ConversionWeightEvaluator weightEval, boolean findPathsInto) {
		
                Collection<Vertex> vertices = map.values();
		for (Vertex v : vertices) {
			distance.put(v.getData(), Integer.MAX_VALUE);
			lastEdge.put(v.getData(), null);
		}
		
		distance.put(start, 0);
		
		for (int i = 0; i < vertices.size()-1; i++) {
			for (Vertex v : vertices) {
				for (Edge e : v.getOutcommingEdges()) {
					Class<? extends FiniteDescription> f;
					Class<? extends FiniteDescription> t;
					if (!findPathsInto) {
						f = e.getFrom().getData();
						t = e.getTo().getData();
					} else {
						f = e.getTo().getData();
						t = e.getFrom().getData();
					}
					long fDist = distance.get(f);
					long tDist = distance.get(t);
					long w = weightEval.getWeight(e.getConversion());
					if (fDist + w < tDist) {
						distance.put(t, (int)(fDist + w));
						lastEdge.put(t, e.getConversion());
					}
				}
			}
		}
	}
	
	void print() {
		for (Entry<Class<? extends FiniteDescription>, Vertex> entry : map.entrySet()) {
			Vertex v = entry.getValue();
			System.out.print(entry.getKey().getName() + ": ");
			for (Edge e : v.getOutcommingEdges()) {
				System.out.print(e.getConversion().getTo().getName() + ",");
			}
			System.out.println();
		}
	}
	
	private class ClassTuples implements Iterable<List<Class<? extends
			FiniteDescription>>> {
		
		private int length;
		
		public ClassTuples(int length) {
			this.length = length;
		}
		
		@Override
		public Iterator<List<Class<? extends FiniteDescription>>> iterator() {
			return new TuplesIterator();
		}
		
		private class TuplesIterator implements Iterator<List<Class<? extends
				FiniteDescription>>> {
			
			private List<Class<? extends FiniteDescription>> classes;
			private long index;
			private final long LASTINDEX;
			
			public TuplesIterator() {
				classes = new ArrayList<>(map.keySet());
				index = 0;
				LASTINDEX = (long) Math.pow(classes.size(), length) - 1;
			}
			
			@Override
			public boolean hasNext() {
				return index <= LASTINDEX;
			}

			@Override
			public List<Class<? extends FiniteDescription>> next() {
				List<Class<? extends FiniteDescription>> res = new ArrayList<>(length);
				long x = index;
				for (int i = 0; i < length; i++) {
					res.add(classes.get((int) (x % classes.size())));
					x /= classes.size();
				}
				index++;
				return res;
			}
			
		}
		
	}
	
}
