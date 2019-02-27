package lv.ailab.dict.struct;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Virsstruktūra nelielām vārdnīcām. Nodrošina šķirkļu saraksta uzglabāšanu
 * un exportu uz XML DOM.
 * Jārēķinās, ka izmantot šo virsstruktūru Tēzauram ir riskanti, jo Tēzaurs ir
 * ārkārtīgi liels DOM prasībām.
 *
 * Izveidots 2016-02-01.
 *
 * @author Lauma
 */
public class Dictionary
{
	public ArrayList<Entry> entries = new ArrayList<>();

	/**
	 * Šķirkļi atrodas trešajā elementu līmenī, lai XML varētu ērti lādēt iekšā
	 * TLex.
	 */
	public Document toXML() throws ParserConfigurationException
	{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.newDocument();

		Element root = doc.createElement("Dictionary");
		Element subroot = doc.createElement("Latvian");
		root.appendChild(subroot);
		doc.appendChild(root);

		for (Entry e : entries) e.toXML(subroot);

		return doc;
	}

	public void toXMLFile(PrintWriter out)
	throws TransformerException, ParserConfigurationException
	{
		// Dabū datus.
		Document doc = toXML();

		// Maģija, lai smuki indento.
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");

		// Raksta.
		Source src = new DOMSource(doc);
		Result target = new StreamResult(out);
		transformer.transform(src, target);

	}

	public void toJSONFile(BufferedWriter out) throws IOException
	{
		out.write("[\n");
		int count = 0;
		for (Entry e : entries)
		{
			if (count > 0) out.write(",\n");
			out.write(e.toJSON());
			count++;
		}
		out.write("\n]");
	}
}
