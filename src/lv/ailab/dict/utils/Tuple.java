package lv.ailab.dict.utils;

/**
 * Ordered tuple.
 */
public class Tuple<E, F>
{
	public E first;
	public F second;
	
	public Tuple (E e, F f)
	{
		first = e;
		second = f;
	}

	static public <E,F> Tuple<E,F> of(E first, F second){
		return new Tuple<>(first, second);
	}
	
	// This is needed for putting Lemmas in hash structures (hasmaps, hashsets).
	@Override
	public boolean equals (Object o)
	{
		if (o == null) return false;
		if (this.getClass() != o.getClass()) return false;
		return (first == null && ((Tuple) o).first == null
					|| first != null && first.equals(((Tuple) o).first))
				&& (second == null && ((Tuple) o).second == null
					|| second != null && second.equals(((Tuple) o).second));
	}
	
	// This is needed for putting Lemmas in hash structures (hasmaps, hashsets).
	@Override
	public int hashCode()
	{
		return 2719 *(first == null ? 1 : first.hashCode())
				+ (second == null ? 1 : second.hashCode());
	}
}