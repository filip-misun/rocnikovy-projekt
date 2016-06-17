package conversions;

import java.util.ArrayList;
import java.util.List;

import languages.FiniteDescription;

/**
 * <p>
 * This class is used to describe certain problems, when one needs to make a
 * series of conversions on (possibly) a series of finite descriptions.
 * Instances of this class are used as an input for some functions in the class
 * {@link ConversionGraph}, which in turn find a solution to the problem
 * described by this instance, i.e. they suggest a conversion plan (see
 * {@link ConversionPlan}).
 * </p>
 * <p>
 * This class was designed to describe problems of the following type. One needs
 * to perform certain operation on a tuple of languages described by a tuple of
 * finite descriptions (we shall call these input finite descriptions). However,
 * before the desired operation can be done, some of these input finite
 * descriptions (possibly all) need to be converted into an equivalent finite
 * description of different type (e.g. because there is no implementation of the
 * operation which can work with the given types of finite descriptions). Also,
 * the operation might produce another finite description as an output. One
 * might require this produced finite description to be converted into different
 * type (the produced finite description after the conversion to the desired
 * type shall be called <em>output</em> finite description).
 * </p>
 * <p>
 * An instance of the ConversionRequest class specifies all the key parts of a
 * problem of this kind. It specifies the tuple of types of input finite
 * descriptions, the type of output finite description (if there is one) and
 * also a selector , which selects tuples of types of finite descriptions, such
 * that the desired operation can work with these types directly (see
 * {@link ClassTuplesSelector}). A solution to this problem is a series of
 * conversions for each one of the input finite description, which convert them
 * into such a form, so that the desired operation can be performed. The
 * solution will also contain a series of conversion, which will transform the
 * finite description produced by this operation (e.i. if it produces an finite
 * description as an output) into the specified output finite description type.
 * Such a solution is generally described by an instance of the
 * {@link ConversionPlan} class.
 * </p>
 */

public class ConversionRequest {

	private List<Class<? extends FiniteDescription>> inputClasses;
	private Class<? extends FiniteDescription> outputClass;
	private ClassTuplesSelector selector;

	public void setInputClasses(List<Class<? extends FiniteDescription>> inputClasses) {
		this.inputClasses = inputClasses;
	}

	public List<Class<? extends FiniteDescription>> getInputClasses() {
		return inputClasses;
	}

	public void setOutputClasses(Class<? extends FiniteDescription> outputClass) {
		this.outputClass = outputClass;
	}

	public Class<? extends FiniteDescription> getOutputClass() {
		return outputClass;
	}

	public List<Class<? extends FiniteDescription>> getAllClasses() {
		List<Class<? extends FiniteDescription>> res = new ArrayList<>(inputClasses);
		if (outputClass != null) {
			res.add(outputClass);
		}
		return res;
	}

	public void setClassTuplesSelector(ClassTuplesSelector selector) {
		this.selector = selector;
	}

	public ClassTuplesSelector getClassTuplesSelector() {
		return selector;
	}

	public boolean hasOutputClass() {
		return outputClass != null;
	}

}
