package lv.ailab.dict.utils;

public interface HasToJSON
{
	/**
	 * Atdod tikai to, kas būs JSON objektā iekšā - bez ietverošajām { }.
 	 */
	String toJSON();
}