package lv.ailab.dict.io;

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
 * Nolasa vārdnīcas XML pa vienam šķirklim.
 * FIXME Ja risinājums ir par lēnu, atmest katra šķirkļa parsēšanu ar DOM.
 * @author Lauma
 */
public class StaxReader
{
    protected XMLEventReader reader;
    protected String rootTag;
    protected String entryTag;
    protected boolean finished;
    protected boolean initiated;

    /**
     * Inicializē lasītāju, nolasa faila galvu.
     * @param path  ceļš uz lasāmo failu
     * @throws FileNotFoundException, XMLStreamException    tehniskas ķibeles
     * @throws DictionaryXmlReadingException    Tēzaura XML neatbilst gaidītajam
     */
    public StaxReader (String path, String rootTag, String entryTag)
            throws FileNotFoundException, XMLStreamException, DictionaryXmlReadingException
    {
    	if (rootTag == null || entryTag == null || rootTag.isEmpty() || entryTag.isEmpty())
			throw new IllegalArgumentException("" +
					"StaxReader needs names of dictionary's root element and entry element!");
		this.rootTag = rootTag;
    	this.entryTag = entryTag;
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        reader = inputFactory.createXMLEventReader(new FileInputStream(path), "UTF8");

        XMLEvent e = reader.nextEvent();
        if (!e.isStartDocument())
            throw new DictionaryXmlReadingException("Vārdnīcas XML ir tukšs!");

        while (!e.isStartElement() && reader.hasNext())
        {
            e = reader.nextEvent();
        }
        if (!e.isStartElement())
            throw new DictionaryXmlReadingException("Vārdnīcas XML trūkst saknes elementa!");
        StartElement root = e.asStartElement();
        if (!this.rootTag.equals(root.getName().getLocalPart()))
            throw new DictionaryXmlReadingException(
                    "sagaidāmā saknes elementa \"" + this.rootTag + "\" vietā atrasts "
                    + root.getName().getLocalPart() + "!");
        finished = false;
        initiated = true;
    }

    /**
     * Nolasa nākamo <entryTag></entryTag> elementu, ja tāds ir.
     * @return nolasītā elementa DOM koks vai null, ja elementa nav.
     */
    public Node readNexEntry()
            throws XMLStreamException, DictionaryXmlReadingException, IOException,
            SAXException, ParserConfigurationException
    {
        StringWriter res = new StringWriter();
        XMLEventWriter writer =
                XMLOutputFactory.newInstance().createXMLEventWriter(res);
        // Aiztin līdz nākamajam interesantajam elementam.
        XMLEvent e = getNextInterestingEntry();
        // Speciāladījumu apstrāde.
        if (finished && !e.isEndDocument())
            throw new DictionaryXmlReadingException("Vārdnīcai XML ir vairākas saknes!");

        if (e.isEndDocument())
            throw new DictionaryXmlReadingException(
                    "Vārdnīcas XML dokuments beidzas bez aizverošā \"" + rootTag + "\" taga!");
        if (e.isEndElement())
        {
            EndElement root = e.asEndElement();
            String name = root.getName().getLocalPart();
            if (!rootTag.equals(name))
                throw new DictionaryXmlReadingException(
                        "Aizverošā \"" + rootTag + "\" taga vietā atrasts " + name + "!");
            finished = true;
            return null;
        }
        if (e.isStartElement())
        {
            StartElement entry = e.asStartElement();
            String name = entry.getName().getLocalPart();
            if (!entryTag.equals(name))
                throw new DictionaryXmlReadingException(
                        "Šķirkļa elementa \"" + entryTag + "\" vietā atrasts " + name + "!");

            // Normālais gadījums, kad tiešām ir šķirklis.
            //e.writeAsEncodedUnicode(res);
            writer.add(e);
            while (reader.hasNext())
            {
                e = reader.nextEvent();
                if (e.isEndDocument())
                    throw new DictionaryXmlReadingException(
                            "Vārdnīcas XML ir negaidīti beidzies šķirkļa vidū!");
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
                System.err.println("Tika ignorēts notikums (event) " + sw.toString() + " ...");
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
