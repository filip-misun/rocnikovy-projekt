package conversions;

import java.util.ArrayList;
import java.util.List;

import rocnikovyprojekt.FiniteDescription;

/**
 * <p>
 * Instances of this class are generally produced as an output of some functions
 * in the class {@link ConversionGraph}, which are trying to find a solution to
 * a problem described by a conversion request (see {@link ConversionRequest})
 * given to these functions as an input. It is supposed to describe a solution
 * to this conversion request. A conversion plan is generally suggested by these
 * functions in the following way. Let the symbols <i>in<sub>1</sub>, ... , in
 * <sub>N</sub></i> denote the members of the input finite description tuple
 * specified by the conversion request (where <i>N</i> is the length of this
 * tuple), let the symbol <i>out</i> denote the output finite description type
 * (if the conversion request specifies one). A solution to the conversion
 * request is a tuple <i>ic<sub>1</sub>, ..., ic<sub>N</sub></i> of sequences of
 * conversions (we shall call these <i>input</i> conversions) and a sequence
 * <i>oc</i> of conversions (we shall call it <i>output</i> conversion), so that
 * when a tuple <i>fd<sub>1</sub>, ... , fd<sub>N</sub></i> of finite
 * descriptions of types <i>in<sub>1</sub>, ... , in<sub>N</sub></i>
 * respectively is given and for every number <i>k</i> such that 1 &le; <i>k</i>
 * &le; <i>N</i> the sequence of conversions <i>ic<sub>k</sub></i> is applied on
 * the finite description <i>fd<sub>k</sub></i>, we obtain a tuple of finite
 * descriptions of such a types <i>c<sub>1</sub>, ..., c<sub>N</sub></i>, so
 * that the class tuple selector specified by the conversion request selects a
 * tuple <i>c<sub>1</sub>, ..., c<sub>N+1</sub></i> for some finite description
 * type <i>c<sub>N+1</sub></i> (the last member of this tuple is present however
 * only when the conversion request specifies an output finite description type)
 * and any given finite description of type <i>c<sub>N+1</sub></i> can be
 * converted by the sequence of conversions <i>oc</i> into a finite description
 * of type <i>out</i> (again, if the conversion request doesn't specifie an
 * output finite description type, omit this condition).
 * </p>
 *
 */

public class ConversionPlan {

	private List<List<Conversion>> inputConversions;
	private List<Conversion> outputConversions;

	public ConversionPlan(int numberOfInputs) {
		inputConversions = new ArrayList<>(numberOfInputs);
                for(int i = 0; i < numberOfInputs; i++){
                    inputConversions.add(new ArrayList<>());
                }
	}

	public void setInputConversions(int index, List<Conversion> conversions) {
		inputConversions.set(index, conversions);
	}

	public void setOutputConversions(List<Conversion> conversions) {
		outputConversions = conversions;
	}

	public List<Conversion> getInputConversions(int index) {
		return inputConversions.get(index);
	}

	public List<Conversion> getOutputConversions() {
		return outputConversions;
	}

	public FiniteDescription executeInputConversion(int conversionIndex, FiniteDescription fd) {
		return executeConversion(inputConversions.get(conversionIndex), fd);
	}

	public FiniteDescription executeOutputConversion(FiniteDescription fd) {
		return executeConversion(outputConversions, fd);
	}

	private FiniteDescription executeConversion(List<Conversion> conversions, FiniteDescription fd) {
		for (Conversion conv : conversions) {
			fd = conv.convert(fd);
		}
		return fd;
	}

}
