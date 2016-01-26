package lv.ailab.dict.tezaurs.analyzer.io;

/**
 * Izņēmumi, kas rodas, ar StAX parseri apstrādājot Tēzaura XML.
 * @author Lauma
 */
public class ThesaurusReadingException extends Exception
{
    public ThesaurusReadingException () { super(); }
    public ThesaurusReadingException (String message)
    {
        super(message);
    }
    public ThesaurusReadingException (String message, Throwable cause)
    {
        super(message, cause);
    }
}
