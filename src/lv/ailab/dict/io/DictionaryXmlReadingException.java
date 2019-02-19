package lv.ailab.dict.io;

/**
 * Izņēmumi, kas rodas, ar StAX parseri apstrādājot vārdnīcas XML.
 * @author Lauma
 */
public class DictionaryXmlReadingException extends Exception
{
    public DictionaryXmlReadingException()
    {
        super();
    }

    public DictionaryXmlReadingException(String message)
    {
        super(message);
    }

    public DictionaryXmlReadingException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
