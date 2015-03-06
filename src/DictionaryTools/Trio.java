package DictionaryTools;

/**
 * Generic tuple structure.
 * @author Lauma
 */
class Trio<F,S,T>
{
      public F first;
      public S second;
      public T third;

      public Trio(F first, S second, T third)
      {
        this.first = first;
        this.second = second;
        this.third = third;
      }

      static <F,S,T> Trio<F,S,T> of(F first, S second, T third){
          return new Trio<F,S,T>(first, second, third);
      }
      
      @Override
      public boolean equals (Object o)
      {
    	  if (this == o) return true;
    	  if (o == null) return false;
    	  try
    	  {
    		  return first.equals(((Trio)o).first) && second.equals(((Trio)o).second) && third.equals(((Trio)o).third);
    	  } catch (NullPointerException | ClassCastException e) { }
    	  return false;
      }
      
      @Override
      public int hashCode()
      {
    	  return (first == null ? 0 : first.hashCode()) * 19
    			  + (second == null ? 0 : second.hashCode()) *2003
    			  + (third == null ? 0 : second.hashCode()) * 503;
      }
}