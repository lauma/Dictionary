package lv.ailab.tezaurs.analyzer.io;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;

/**
 * Nolasa Tēzaura XML pa vienam šķirklim.
 * @author Lauma
 */
public class StaxReader
{
    protected XMLEventReader reader;
    protected boolean finished;
    protected boolean initiated;

    /**
     * Inicializē lasītāju, nolasa faila galvu.
     * @param path  ceļš uz lasāmo failu
     * @throws FileNotFoundException, XMLStreamException    tehniskas ķibeles
     * @throws ThesaurusReadingException    Tēzaura XML neatbilst gaidītajam
     */
    public StaxReader (String path)
            throws FileNotFoundException, XMLStreamException, ThesaurusReadingException
    {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        reader = inputFactory.createXMLEventReader(new FileInputStream(path), "UTF8");

        XMLEvent e = reader.nextEvent();
        if (!e.isStartDocument())
            throw new ThesaurusReadingException("Dictionary XML is empty!");

        while (!e.isStartElement() && reader.hasNext())
        {
            e = reader.nextEvent();
        }
        if (!e.isStartElement())
            throw new ThesaurusReadingException("Dictionary XML has no root element!");
        StartElement root = e.asStartElement();
        if (!"tezaurs".equals(root.getName().getLocalPart()))
            throw new ThesaurusReadingException(
                    "\"tezaurs\" expected as root element, "
                    + root.getName().getLocalPart() + " got!");
        finished = false;
        initiated = true;
    }

    /**
     * Nolasa nākamo <s></s> elementu, ja tāds ir.
     * @return nolasītā elementa XML vai null, ja elementa nav.
     */
    public String nexEntry()
            throws XMLStreamException, ThesaurusReadingException
    {
        //StringBuilder res = new StringBuilder();
        StringWriter res = new StringWriter();

        XMLEvent e = reader.nextEvent();
        // Aiztin līdz nākamajam interesantajam elementam.
        while (!e.isEndDocument() && !e.isEndElement() && !e.isStartElement())
        {
            System.err.println("Event " + e + "ignored ...");
            e = reader.nextEvent();
        }
        // Speciāladījumu apstrāde.
        if (finished && !e.isEndDocument())
            throw new ThesaurusReadingException("XML document with multiple roots!");

        if (e.isEndDocument())
            throw new ThesaurusReadingException(
                    "XML document end reached without closing \"tezaurs\" tag!");
        if (e.isEndElement())
        {
            EndElement root = e.asEndElement();
            String name = root.getName().getLocalPart();
            if (!"tezaurs".equals(name))
                throw new ThesaurusReadingException(
                        "Closing \"tezaurs\" expected, " + name + "got!");
            finished = true;
            return null;
        }
        if (e.isStartElement())
        {
            StartElement entry = e.asStartElement();
            String name = entry.getName().getLocalPart();
            if (!"s".equals(name))
                throw new ThesaurusReadingException(
                        "Entry element \"s\" expected," + name + "got!");

            // Normālais gadījums, kad tiešām ir šķirklis.
            e.writeAsEncodedUnicode(res);
            while (reader.hasNext())
            {
                e = reader.nextEvent();
                if (e.isEndDocument())
                    throw new ThesaurusReadingException(
                            "XML document unexpectedly ended in the midle ot the entry!");
                e.writeAsEncodedUnicode(res);
                if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals("s"))
                    break;
            }

        }

        return res.toString();
    }

}
