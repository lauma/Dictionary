package lv.ailab.dict.tezaurs.analyzer.io;

import javanet.staxutils.IndentingXMLStreamWriter;
import lv.ailab.dict.struct.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Drukā Tēzauru ārā pa vienam šķirklim. Lietojot kopā ar StaxReader sanāk, ka
 * atmiņā nekad nav nepieciešams turēt vairāk par vienu šķirkli, tādejādi
 * izvairoties no atmiņas piegruzīšanas ar visa Tēzaura pilno DOM koku, vai,
 * vēl ļaunāk - ar diviem pilniem DOM kokiem (pirms un pēc izmaiņām).
 *
 * Izveidots 2016-03-18.
 *
 * @author Lauma
 */
public class StaxWriter
{
	protected XMLStreamWriter writer;

	public StaxWriter(String path)
	throws FileNotFoundException, XMLStreamException,
			ParserConfigurationException
	{
		XMLOutputFactory factory = XMLOutputFactory.newFactory();
		writer = new IndentingXMLStreamWriter(
				factory.createXMLStreamWriter(new FileOutputStream(path), "UTF8"));
		writer.writeStartDocument();
		writer.writeStartElement("dictionary");
		writer.writeStartElement("latvian");
	}

	public void writeNode(Entry e)
	throws ParserConfigurationException, TransformerException,
			XMLStreamException
	{
		DOMSource ds = new DOMSource(e.toXML(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()));
		StAXResult sr = new StAXResult(writer);
		TransformerFactory.newInstance().newTransformer().transform(ds, sr);
		writer.flush();
	}

	public void finalize() throws XMLStreamException
	{
		writer.writeEndDocument();
		writer.close();
	}
}
