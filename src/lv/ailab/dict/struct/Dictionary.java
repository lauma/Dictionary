/*******************************************************************************
 * Copyright 2013-2016 Institute of Mathematics and Computer Science, University of Latvia
 * Author: Lauma Pretkalniņa
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package lv.ailab.dict.struct;

import lv.ailab.dict.struct.Entry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

		Element root = doc.createElement("dictionary");
		Element subroot = doc.createElement("latvian");
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
}
