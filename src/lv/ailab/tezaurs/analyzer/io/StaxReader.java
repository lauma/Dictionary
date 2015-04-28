package lv.ailab.tezaurs.analyzer.io;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.*;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;

/**
 * Nolasa Tēzaura XML pa vienam šķirklim.
 * FIXME Ja risinājums ir par lēnu, atmest katra šķirkļa parsēšanu ar DOM.
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
     * @return nolasītā elementa DOM koks vai null, ja elementa nav.
     */
    public Node readNexEntry()
            throws XMLStreamException, ThesaurusReadingException, IOException,
            SAXException, ParserConfigurationException
    {
        StringWriter res = new StringWriter();
        XMLEventWriter writer =
                XMLOutputFactory.newInstance().createXMLEventWriter(res);
        // Aiztin līdz nākamajam interesantajam elementam.
        XMLEvent e = getNextInterestingEntry();
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
            //e.writeAsEncodedUnicode(res);
            writer.add(e);
            while (reader.hasNext())
            {
                e = reader.nextEvent();
                if (e.isEndDocument())
                    throw new ThesaurusReadingException(
                            "XML document unexpectedly ended in the midle ot the entry!");
                writer.add(e);
                //e.writeAsEncodedUnicode(res);     // Problēma ar apstrofiem atribūtos.
                if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals("s"))
                    break;
            }
        }
        writer.flush();
        writer.close();
        return parseNode(res.toString());
    }

    /**
     * Atrod nākamo elementu, kam ir tips EndDocument, EndElement vai
     * StartElement.
     * @return atrastais elements
     * @throws XMLStreamException
     */
    protected XMLEvent getNextInterestingEntry() throws XMLStreamException
    {
        XMLEvent e = reader.nextEvent();
        while (!e.isEndDocument() && !e.isEndElement() && !e.isStartElement())
        {
            // Pabrīdina, ja nu tomēr bija kaut kas interesants.
            if (!e.isCharacters() || !e.asCharacters().isWhiteSpace())
            {
                StringWriter sw = new StringWriter();
                e.writeAsEncodedUnicode(sw);
                System.err.println("Event " + sw.toString() + " ignored ...");
            }
            e = reader.nextEvent();
        }
        return e;
    }

    /**
     * No tekstā dota XML elementa izparsē DOM koku.
     * @param xml   parsējamais teksts
     * @return  izparsētais DOM koks
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    protected static Node parseNode(String xml)
    throws ParserConfigurationException, IOException, SAXException
    {
        try
        {
            return DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xml.getBytes("UTF8")))
                    .getDocumentElement();

        } catch (Exception e)
        {
            System.err.println("Neizdevās dabūt DOM koku:\n\"" + xml + "\"!");
            throw e;
        }
    }
}
