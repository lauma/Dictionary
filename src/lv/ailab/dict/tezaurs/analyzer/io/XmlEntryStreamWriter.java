package lv.ailab.dict.tezaurs.analyzer.io;

import javanet.staxutils.IndentingXMLStreamWriter;
import lv.ailab.dict.struct.Entry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

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
public class XmlEntryStreamWriter
{
	BufferedWriter outStreamWithEncoding;
	protected Transformer transf;

	public XmlEntryStreamWriter(String path)
	throws IOException, XMLStreamException,
			ParserConfigurationException, TransformerConfigurationException
	{
		outStreamWithEncoding = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path), "UTF-8"));
		outStreamWithEncoding.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		outStreamWithEncoding.write("<Dictionary>\n");
		outStreamWithEncoding.write("<Latvian>\n");

		transf = TransformerFactory.newInstance().newTransformer();
		transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transf.setOutputProperty(OutputKeys.INDENT, "yes");
		transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");

	}

	public void writeNextEntry(Entry e)
	throws ParserConfigurationException, TransformerException,
			XMLStreamException, IOException
	{
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		//Element dummyParent = doc.createElement("dictionary");
		//Element dummyParent2 = doc.createElement("latvian");
		//doc.appendChild(dummyParent);
		//dummyParent.appendChild(dummyParent2);
		Element elem = e.toXML(doc);
		//dummyParent2.appendChild(elem);

		DOMSource ds = new DOMSource(elem);
		StringWriter strWriter = new StringWriter();
		StreamResult res = new StreamResult(strWriter);

		transf.transform(ds, res);
		strWriter.flush();
		String strReprez = strWriter.toString();

		outStreamWithEncoding.write(strReprez);
		outStreamWithEncoding.flush();
	}

	public void finalize() throws XMLStreamException, IOException
	{
		outStreamWithEncoding.write("</Latvian>\n");
		outStreamWithEncoding.write("</Dictionary>\n");
		outStreamWithEncoding.close();
	}
}
