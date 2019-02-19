package lv.ailab.dict.utils;

/**
 * Generic tuple structure.
 * @author Lauma
 */
public class Trio<F,S,T>
{
	public F first;
	public S second;
	public T third;

	protected Trio(F first, S second, T third)
	{
		this.first = first;
		this.second = second;
		this.third = third;
	}

	static public <F,S,T> Trio<F,S,T> of(F first, S second, T third)
	{
		return new Trio<>(first, second, third);
	}

	static public <F,S,T> Trio<F,S,T> of(F first, Tuple<S,T> secondThird)
	{
		return new Trio<>(first, secondThird.first, secondThird.second);
	}

	static public <F,S,T> Trio<F,S,T> of(Tuple<F,S> firstSecond, T third)
	{
		return new Trio<>(firstSecond.first, firstSecond.second, third);
	}

	@Override
	public boolean equals (Object o)
	{
		if (this == o) return true;
		if (o == null) return false;
		try
		{
			return first.equals(((Trio)o).first) && second.equals(((Trio)o).second) && third.equals(((Trio)o).third);
		}
		catch (NullPointerException | ClassCastException e)
		{
			return false;
		}
	}

	@Override
	public int hashCode()
	{
		return (first == null ? 0 : first.hashCode()) * 19
				+ (second == null ? 0 : second.hashCode()) *2003
				+ (third == null ? 0 : third.hashCode()) * 503;
	}
}