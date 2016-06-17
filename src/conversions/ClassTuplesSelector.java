package conversions;

import java.util.List;

import languages.FiniteDescription;

/**
 * <p>
 * Objects, which implement this interface, are generally created to be part of
 * a conversion request (see {@link ConversionRequest}). An class tuple
 * selector, which was designed for a given conversion request, generally works
 * in the following way. Every conversion request is usually associated with an
 * operation, which is required to be performed on the tuple of the languages
 * described by the input finite descriptions. The class tuple selector selects
 * such a tuple <i>c<sub>1</sub>, ..., c<sub>N+1</sub></i> (ommit the last
 * member of this tuple, if the conversion request specifies no output finite
 * description type!), so that the desired operation can be performed directly
 * on a tuple of finite descriptions <i>fd<sub>1</sub>, ..., fd<sub>N</sub></i>,
 * where <i>fd<sub>k</sub></i> is of type <i>c<sub>k</sub></i> for any given
 * number <i>k</i> such that 1 &le; <i>k</i> &le; <i>N</i> and the operation
 * produces an finite description of type <i>c<sub>N+1</sub></i> as an output
 * (again, if the conversion request doesn't specifie an output finite description
 * type, omit this condition).
 * </p> 
 */

public interface ClassTuplesSelector {

	public boolean selects(List<Class<? extends FiniteDescription>> tuple);

}
