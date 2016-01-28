package conversions;

import com.google.common.reflect.ClassPath;

import rocnikovyprojekt.FiniteDescription;

import java.util.ArrayList;
import java.util.List;

public class Conversions {
	
	private static ConversionGraph graph;
	private static ConversionWeightEvaluator weightEval;

	static {
		System.out.println("Initializing Conversions...");
		ArrayList<Class<? extends Conversion>> list = new ArrayList<>();
		try {
			ClassPath classpath = ClassPath.from(
					Thread.currentThread().getContextClassLoader());
			/* Naplnenie list-u triedami implementujucimi Conversion */
			for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClassesRecursive(
					"rocnikovyprojekt")) {
				@SuppressWarnings("unchecked")
				Class<? extends Conversion> c = (Class<? extends Conversion>)
						Class.forName(classInfo.getName());
				if (Conversion.class.isAssignableFrom(c) && !c.equals(Conversion.class)) {
					System.out.println("Found " + classInfo.getName());
					list.add(c);
				}
			}
			/* Naplnenie grafu */
			graph = new ConversionGraph();
			for (Class<? extends Conversion> c : list) {
				Conversion conv = c.newInstance();
				graph.addEdge(conv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Initialization complete.");
	}
	
	/**
	 * <p>Sets the conversion weight evaluator to be used when looking for optimal
	 * conversion paths.</p>
	 * @param weightEval - conversion weight evaluator to be used.
	 */
	public static void setConversionWeightEvaluator(ConversionWeightEvaluator weightEval) {
		Conversions.weightEval = weightEval;
	}
	
	/**
	 * <p>Converts specified finite description to an equivalent finite description of the
	 * given type, if possible.</p>
	 * <p>This method first looks for the shortest of all the available conversion
	 * paths. Weight of available conversions is specified by weight evaluator, which
	 * can be set by the method {@link setConversionWeightEvaluator}.</p>
	 * @param from - the finite description to be converted
	 * @param toClass - specifies the type of finite description to which {@code from}
	 *        shall be converted
	 * @return an equivalent finite description of specified type, if the conversion
	 * can be done. Otherwise returns {@code null}.
	 */
	public static FiniteDescription convert(FiniteDescription from,
			Class<? extends FiniteDescription> toClass) {
		
		ConversionRequest request = new ConversionRequest();
		List<Class<? extends FiniteDescription>> inputClasses = new ArrayList<>();
		inputClasses.add(from.getClass());
		request.setInputClasses(inputClasses);
		request.setClassTuplesSelector(new ClassTuplesSelector() {
			@Override
			public boolean selects(List<Class<? extends FiniteDescription>> tuple) {
				return tuple.get(0).equals(toClass);
			}
		});
		ConversionPlan plan = graph.findOptimalConversionPlan(request, weightEval);
		if (plan == null) return null;
		FiniteDescription res = plan.executeInputConversion(0, from);
		return res;
	}
		
	void print() {
		System.out.println("Conversions:");
		graph.print();
	}
	
}
