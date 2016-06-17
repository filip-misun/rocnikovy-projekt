package conversions;

import com.google.common.reflect.ClassPath;
import java.lang.reflect.InvocationTargetException;

import rocnikovyprojekt.FiniteDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rocnikovyprojekt.Word;

public class Conversions {

    private static ConversionGraph graph;
    private static ConversionWeightEvaluator weightEval
            = new ConversionWeightEvaluator() {

                @Override
                public int getWeight(Conversion conversion) {
                    return 1;
                }

            };

    static {
        //System.out.println("Initializing Conversions...");
        ArrayList<Class<? extends Conversion>> list = new ArrayList<>();
        try {
            ClassPath classpath = ClassPath.from(
                    Thread.currentThread().getContextClassLoader());
            /* Naplnenie list-u triedami implementujucimi Conversion */
            for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClassesRecursive(
                    "conversions")) {
                @SuppressWarnings("unchecked")
                Class<? extends Conversion> c = (Class<? extends Conversion>) Class.forName(classInfo.getName());
                if (Conversion.class.isAssignableFrom(c) && !c.equals(Conversion.class)) {
                    //System.out.println("Found " + classInfo.getName());
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
        //System.out.println("Initialization complete.");
    }

    /**
     * <p>
     * Sets the conversion weight evaluator to be used when looking for optimal
     * conversion paths.</p>
     *
     * @param weightEval - conversion weight evaluator to be used.
     */
    public static void setConversionWeightEvaluator(ConversionWeightEvaluator weightEval) {
        Conversions.weightEval = weightEval;
    }

    /**
     * <p>
     * Converts specified finite description to an equivalent finite description
     * of the given type, if possible.</p>
     * <p>
     * This method first looks for the shortest of all the available conversion
     * paths. Weight of available conversions is specified by weight evaluator,
     * which can be set by the method {@link setConversionWeightEvaluator}.</p>
     *
     * @param from - the finite description to be converted
     * @param toClass - specifies the type of finite description to which
     * {@code from} shall be converted
     * @return an equivalent finite description of specified type, if the
     * conversion can be done. Otherwise returns {@code null}.
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
        if (plan == null) {
            return null;
        }
        FiniteDescription res = plan.executeInputConversion(0, from);
        return res;
    }
    
    private static boolean unaryProblem(FiniteDescription a, String problem, Object arg) {
        ConversionRequest request = new ConversionRequest();
        List<Class<? extends FiniteDescription>> inputClasses = new ArrayList<>();
        inputClasses.add(a.getClass());
        request.setInputClasses(inputClasses);
        request.setClassTuplesSelector(new ClassTuplesSelector() {
            @Override
            public boolean selects(List<Class<? extends FiniteDescription>> tuple) {
                try {
                    tuple.get(0).getMethod(problem, Word.class);
                } catch (NoSuchMethodException ex) {
                    return false;
                } catch (SecurityException ex) {
                    Logger.getLogger(Conversions.class.getName()).log(Level.SEVERE, null, ex);
                }
                return true;
            }
        });
        ConversionPlan plan = graph.findOptimalConversionPlan(request, weightEval);
        if (plan == null) {
            return false;
        }
        FiniteDescription res = plan.executeInputConversion(0, a);
        try {
            Class<?> cl = (arg == null ? null : arg.getClass());
            if(arg.equals(Word.EMPTYWORD)){
                cl = Word.class;
            }
            return (boolean) res.getClass().getMethod(problem, cl).invoke(res, arg);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Conversions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean accepts(FiniteDescription a, Word w){
        return unaryProblem(a, "accepts", w);
    }
    
    public static boolean isEmpty(FiniteDescription a){
        return unaryProblem(a, "isEmpty", null);
    }
    
    public static boolean isFull(FiniteDescription a){
        return unaryProblem(a, "isFull", null);
    }
    
    public static boolean isFinite(FiniteDescription a){
        return unaryProblem(a, "isFinite", null);
    }

}
