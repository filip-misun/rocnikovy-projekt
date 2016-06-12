package rocnikovyprojekt;

public class Tuple<F, S> {

	public final F first;
	public final S second;

	public Tuple(F first, S second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public int hashCode() {
		return first.hashCode() ^ second.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Tuple))
			return false;
		Tuple pairo = (Tuple) o;
		return this.first.equals(pairo.first)
				&& this.second.equals(pairo.second);
	}
}