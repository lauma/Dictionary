package lv.ailab.tezaurs;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

/**
 * @author Lauma, Valters Rostoks
 */
public class DictionaryDic2Xml
{
	public static String inputDataPath = "./dati/dic/";
	public static String outputDataPath = "./dati/xml/";

	protected Document doc;
	protected Element fullEntries;
	protected Element refEntries;
	protected BufferedReader currentDicIn;
	protected PrintWriter log;

	protected String currentLine, prevLine;
	protected String entryName;
	protected int NS;
	protected boolean parcelts;

	public DictionaryDic2Xml()
	throws IOException, ParserConfigurationException
	{
		try
		{
			log = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
					outputDataPath + "dic2xml-log.txt"), "Windows-1257"), true);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.newDocument();

			fullEntries = doc.createElement("tezaurs");
			refEntries = doc.createElement("sliktie");

			fullEntries.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			fullEntries.setAttribute("xsi:noNamespaceSchemaLocation", "tezaurs.xsd");
		} catch (IOException|ParserConfigurationException e)
		{
			e.printStackTrace(log);
			throw e;
		}

		currentDicIn = null;
	}

	//Atgriež "true", ja vārdam ir nozīmju grupa (tas ir "labais" vārds)
	protected boolean mkVR(Element s) throws IOException
	{
		boolean labais = false;
		entryName = currentLine.substring(3);

		String RU = null;
		String IN = null;
		String GR = null;

		do
		{
			currentLine = currentDicIn.readLine();

			if (currentLine.indexOf("IN ") == 0) IN = currentLine.substring(3);
			if (currentLine.indexOf("RU ") == 0) RU = currentLine.substring(3);

			if (currentLine.indexOf("GR ") == 0)
			{
				GR = currentLine.substring(3);
				if (GR.charAt(GR.length() - 1) == '-')
					GR = (GR.substring(0, GR.length() - 1)).trim();
			}
		} while ((currentLine.indexOf("GR ") == 0) || (currentLine
				.indexOf("IN ") == 0) || (currentLine.indexOf("RU ") == 0));

		if (IN != null) s.setAttribute("i", IN);

		Element vf = doc.createElement("vf");
		if (RU != null) vf.setAttribute("ru", RU);
		vf.setTextContent(entryName);

		Element gram = doc.createElement("gram");
		if (GR != null) gram.setTextContent(GR);

		Element v = doc.createElement("v");
		v.appendChild(vf);
		if (GR != null)
		{
			v.appendChild(gram);
		}
		//else {System.err.println("Missing GR: " + entryName);}
		s.appendChild(v);

		NS = 0;
		if (currentLine.indexOf("NS ") == 0) currentLine = currentDicIn.readLine();

		Element g_n = null;
		while (currentLine.indexOf("NO ") == 0)
		{
			if (g_n == null) g_n = doc.createElement("g_n");
			mkNO(g_n);
		}

		if (g_n != null)
		{
			s.appendChild(g_n);
			labais = true;
		}

		Element g_fraz = null;
		Element g_de = null;

		if (currentLine.indexOf("FS ") == 0) currentLine = currentDicIn.readLine();

		while (currentLine.indexOf("FR ") == 0)
		{
			if (g_fraz == null) g_fraz = doc.createElement("g_fraz");
			mkFR(g_fraz);
		}

		if (currentLine.indexOf("DS ") == 0) currentLine = currentDicIn.readLine();
		if (currentLine == null) return labais;

		while (currentLine.indexOf("DE ") == 0)
		{
			if (g_de == null) g_de = doc.createElement("g_de");
			mkDE(g_de);
			if (currentLine == null) return labais;
		}

		if (g_fraz != null) s.appendChild(g_fraz);
		if (g_de != null) s.appendChild(g_de);
		if (currentLine.indexOf("DN ") == 0 || currentLine.indexOf("CD ") == 0)
		{
			makeLeaf("ref", currentLine, s);
			currentLine = currentDicIn.readLine();
		}
		if (currentLine.indexOf("LI ") == 0)
		{
			makeLeaf("avots", currentLine, s);
			currentLine = currentDicIn.readLine();
		}

		return labais;
	}

	protected void mkNO(Element g_n) throws IOException
	{
		NS++;

		Element n = doc.createElement("n");
		n.setAttribute("nr", String.valueOf(NS));
		Element d = doc.createElement("d");
		Element t = doc.createElement("t");

		t.setTextContent((currentLine.substring(3)).trim());
		d.appendChild(t);

		currentLine = currentDicIn.readLine();
		if (currentLine.indexOf("NG ") == 0) mkNG(n);

		n.appendChild(d);

		Element g_piem = null;
		Element g_an = null;

		int is_bad = 0;

		while ((currentLine.indexOf("AN ") == 0) || (currentLine
				.indexOf("PI ") == 0) || (currentLine
				.indexOf("PG ") == 0))
		{
			while (currentLine.indexOf("AN ") == 0)
			{
				if (g_an == null) g_an = doc.createElement("g_an");
				mkAN(g_an);
			}

			if (currentLine.indexOf("PG ") == 0) mkPG(null);

			while (currentLine.indexOf("PI ") == 0)
			{
				if (g_piem == null) g_piem = doc.createElement("g_piem");
				mkPI(g_piem);
			}
		}

		if (g_piem != null) n.appendChild(g_piem);
		if (g_an != null) n.appendChild(g_an);
		g_n.appendChild(n);
	}


	protected void mkNG(Element n) throws IOException
	{
		Element gram = doc.createElement("gram");
		currentLine = (currentLine.substring(3)).trim();
		if (currentLine.charAt(currentLine.length() - 1) == '-')
			currentLine = (currentLine.substring(0, currentLine.length() - 1)).trim();

		gram.setTextContent(currentLine);
		n.appendChild(gram);
		currentLine = currentDicIn.readLine();
	}

	protected void mkPN(Element piem) throws IOException
	{
		Element n = doc.createElement("n");
		Element d = doc.createElement("d");
		Element t = doc.createElement("t");

		t.setTextContent((currentLine.substring(3)).trim());
		d.appendChild(t);
		n.appendChild(d);
		piem.appendChild(n);
		currentLine = currentDicIn.readLine();
	}


	//--------------------------------------------------------------------------------------------------
	protected void mkPG(Element el) throws IOException
	{
		boolean apst = false;
		if (!parcelts)
		{

			if (el == null)
			{
				prevLine = currentLine;
				currentLine = currentDicIn.readLine();
				apst = true;
				log.println("!!! " + prevLine + " " + entryName);
			}
			if (apst)
			{
				parcelts = true;
				if (currentLine.indexOf("PN ") == 0)
				{
					String temp = currentLine;
					currentLine = prevLine;
					prevLine = temp;
					mkPG(el);
				}
				return;
			}
		}
		parcelts = false;

		if (el == null)
		{
			log.println("BAD!!! " + currentLine + " " + entryName);
			return;
		}

		Element a = doc.createElement("gram");
		currentLine = (currentLine.substring(3)).trim();
		if (currentLine.charAt(currentLine.length() - 1) == '-')
			currentLine = (currentLine.substring(0, currentLine.length() - 1)).trim();
		a.setTextContent(currentLine);
		el.appendChild(a);
		if (prevLine != null)
		{
			currentLine = prevLine;
			prevLine = null;
		} else currentLine = currentDicIn.readLine();
	}

	protected void mkAN(Element el) throws IOException
	{
		Element nel = doc.createElement("n");
		Element d = doc.createElement("d");
		Element t = doc.createElement("t");
		t.setTextContent((currentLine.substring(3)).trim());
		d.appendChild(t);
		currentLine = currentDicIn.readLine();
		if (currentLine.indexOf("AG ") == 0) mkNG(nel);
		nel.appendChild(d);
		if (currentLine.indexOf("PG ") == 0) mkPG(null);
		Element g_piem = null;
		while (currentLine.indexOf("PI ") == 0)
		{
			if (g_piem == null) {g_piem = doc.createElement("g_piem");}
			mkPI(g_piem);
		}
		if (g_piem != null) nel.appendChild(g_piem);
		el.appendChild(nel);
	}

	protected void mkPI(Element el) throws IOException
	{
		Element piemElem = doc.createElement("piem");
		makeLeaf("t", currentLine, piemElem, true);
		if (prevLine != null)
		{
			currentLine = prevLine;
			prevLine = null;
		} else currentLine = currentDicIn.readLine();
		if (currentLine.indexOf("PG ") == 0) mkPG(piemElem);
		while (currentLine.indexOf("PN ") == 0) mkPN(piemElem);
		el.appendChild(piemElem);
	}

	protected void mkFR(Element el) throws IOException
	{
		Element frazElem = doc.createElement("fraz");
		makeLeaf("t", currentLine, frazElem, true);
		currentLine = currentDicIn.readLine();

		if (currentLine.indexOf("FG ") == 0) mkNG(frazElem);
		if (currentLine.indexOf("FP ") == 0) mkFP(frazElem, false);
		while (currentLine.indexOf("FN ") == 0) mkFN(frazElem);
		el.appendChild(frazElem);
	}

	protected void mkFN(Element parent) throws IOException
	{
		Element nElem = doc.createElement("n");
		Element dElem = doc.createElement("d");
		makeLeaf("t", currentLine, dElem);
		currentLine = currentDicIn.readLine();
		nElem.appendChild(dElem);
		while (currentLine.indexOf("FP ") == 0) mkFP(nElem, true);
		parent.appendChild(nElem);
	}

	protected void mkFP(Element parent, boolean hasSenses)
	throws IOException
	{
		if (hasSenses)
		{
			Element piemElem = doc.createElement("piem");
			makeLeaf("t", currentLine, piemElem);
			parent.appendChild(piemElem);
		} else
		{
			Element nElem = doc.createElement("n");
			Element dElem = doc.createElement("d");
			Element tElem = doc.createElement("t");
			Element piemElem = doc.createElement("piem");
			makeLeaf("t", currentLine, piemElem);
			dElem.appendChild(tElem);
			nElem.appendChild(dElem);
			nElem.appendChild(piemElem);
			parent.appendChild(nElem);
		}
		currentLine = currentDicIn.readLine();
	}

	protected void mkDE(Element parent)
	throws IOException
	{
		Element deElem = doc.createElement("de");
		Element vElem = doc.createElement("v");
		makeLeaf("vf", currentLine, vElem);
		currentLine = currentDicIn.readLine();

		if (currentLine.indexOf("DG ") == 0) mkNG(vElem);
		deElem.appendChild(vElem);
		while (currentLine.indexOf("DP ") == 0) mkDP(deElem);
		parent.appendChild(deElem);
	}

	protected void mkDP(Element parent) throws IOException
	{
		Element piemElem = doc.createElement("piem");
		makeLeaf("t", currentLine, piemElem);
		currentLine = currentDicIn.readLine();

		if (currentLine.indexOf("DA ") == 0) mkNG(piemElem);
		if (currentLine.indexOf("DD ") == 0) mkPN(piemElem);
		parent.appendChild(piemElem);
	}
//--------------------------------------------------------------------------------------------------


	public static void main(String[] args)
	throws IOException, ParserConfigurationException, TransformerException
	{
		File folder = new File(inputDataPath);
		if (!folder.exists())
		{
			System.out.println(
					"Ups! Nevar atrast ieejas datu mapi \"" + inputDataPath + "\"!");
			return;
		}
		File dicFolder = new File(outputDataPath);
		if (!dicFolder.exists()) dicFolder.mkdirs();

		DictionaryDic2Xml transformator = new DictionaryDic2Xml();

		File[] listOfFiles = folder.listFiles();
		for (File f : listOfFiles)
		{
			String fileName = f.getName();
			if (!fileName.endsWith(".dic"))
				continue;
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(inputDataPath + fileName));
				transformator.addDicToXml(in);
				System.out.println(fileName + " [Pabeigts]");
			} catch (Exception e)
			{
				e.printStackTrace(System.err);
				e.printStackTrace(transformator.log);
				System.out.println(fileName + " [Problēma]");
			}

		}
		transformator.writeEverything();
		System.out.println("Viss pabeigts!");
	}

	/**
	 * Izanalizē ieejas plūsmā (failā) dotos datus un pievieno tos DOM kokam.
	 * @param reader .dic formāta plūsma
	 */
	public void addDicToXml(BufferedReader reader)
	throws IOException
	{
		currentDicIn = reader;
		while (true)
		{
			do
			{
				currentLine = currentDicIn.readLine();
				if (currentLine == null) break;
				currentLine = currentLine.trim();
			} while (currentLine.indexOf("VR ") != 0);

			if (currentLine == null) break;

			Element s = doc.createElement("s");
			if (mkVR(s)) fullEntries.appendChild(s);
			else refEntries.appendChild(s);

			if (currentLine == null) break;
			if (currentLine.length() > 0)
			{
				log.println(currentLine + "  " + entryName);
			}
		}
		currentDicIn = null;
	}

	/**
	 * Pabeidz apstrādes procesu, izdrukājot izveidotos XMLus un aizverot
	 * logošanas plūsmu.
	 */
	public void writeEverything()
	throws TransformerException, IOException
	{
		try
		{
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();

			doc.appendChild(fullEntries);

			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDataPath + "entries.xml"), "UTF8")));
			Source src = new DOMSource(doc);
			Result target = new StreamResult(out);
			transformer.transform(src, target);

			doc.removeChild(fullEntries);
			doc.appendChild(refEntries);

			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDataPath + "references.xml"), "UTF8")));
			src = new DOMSource(doc);
			target = new StreamResult(out);
			transformer.transform(src, target);

			log.close();
		}
		catch (TransformerException|IOException e)
		{
			e.printStackTrace(log);
			throw e;
		}
	}

	/**
	 * Izveido jaunu elementu ar tekstuālu saturu.
	 * @param name			izvedojamā elementa vārds
	 * @param line			rindiņa, no kuras iegūt izveidojamā elementa saturu
	 * @param parent		vecāks, kuram piestiprināt izveidoto elementu
	 * @param removeDash	vai, ja elementa teksts beidzas ar defisi, to novākt?
	 */
	protected void makeLeaf(String name, String line, Element parent, boolean removeDash)
	{
		Element leaf = doc.createElement(name);
		int startIndex = line.indexOf(" ");

		String contents = startIndex >= 0 ?
				line.substring(line.indexOf(" ")).trim() : "";
		if (removeDash && contents.endsWith("-"))
			contents = contents.substring(0, contents.length() - 1).trim();
		leaf.setTextContent(contents);
		parent.appendChild(leaf);
	}

	/**
	 * Izveido jaunu elementu ar tekstuālu saturu.
	 * @param name		izvedojamā elementa vārds
	 * @param line		rindiņa, no kuras iegūt izveidojamā elementa saturu
	 * @param parent	vecāks, kuram piestiprināt izveidoto elementu
	 */
	protected void makeLeaf(String name, String line, Element parent)
	{
		makeLeaf(name, line, parent, false);
	}
}
