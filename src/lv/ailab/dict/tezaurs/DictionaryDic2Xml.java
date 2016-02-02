package lv.ailab.dict.tezaurs;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

/**
 * Lielākā daļa koda ir paņemta no vecāka projekta, pamainot vienīgi saskarni.
 * @author Normunds Grūzītis, Lauma
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
			fullEntries.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			fullEntries.setAttribute("xsi:noNamespaceSchemaLocation", "tezaurs.xsd");

			refEntries = doc.createElement("tezaurs");
			refEntries.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			refEntries.setAttribute("xsi:noNamespaceSchemaLocation", "tezaurs.xsd");

		} catch (IOException|ParserConfigurationException e)
		{
			e.printStackTrace(log);
			throw e;
		}

		currentDicIn = null;
	}

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
			if (!fileName.endsWith(".dic")) continue;
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


			Entry entry = processEntry();
			if (entry.senseNumber > 0) fullEntries.appendChild(entry.sElem);
			else refEntries.appendChild(entry.sElem);

			if (currentLine == null) break;
			if (currentLine.length() > 0)
				log.println(currentLine + "  " + entry.entryName);
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
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");

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
	 * Apstrādā šķirkli, sākot ar VR ... rindiņu.
	 */
	protected Entry processEntry() throws IOException
	{
		Entry res = new Entry();

		processEntryHeader(res);
		processSenses(res);
		processEntryFooter(res);

		return res;
	}

	/**
	 * Apstrādā šķirkļa galvai atbilstošos datus - šķirkļa vārdu, izrunu,
	 * galveno gramatiku un homonīmu indeksu - un izveido šķirkļa galvu - vf
	 * elementu.
	 * @param entry	šķirklis, kam pievienot jaunos elementus
	 */
	protected void processEntryHeader(Entry entry) throws IOException
	{
		String inLine = null;
		String ruLine = null;
		String grLine = null;
		entry.entryName = currentLine.substring(2).trim(); // Nocērt šķirkļa vārda VR

		do
		{
			currentLine = currentDicIn.readLine();
			if (currentLine.indexOf("IN ") == 0) inLine = currentLine;
			if (currentLine.indexOf("RU ") == 0) ruLine = currentLine;
			if (currentLine.indexOf("GR ") == 0) grLine = currentLine;

		} while ((currentLine.indexOf("GR ") == 0) || (currentLine
				.indexOf("IN ") == 0) || (currentLine.indexOf("RU ") == 0));

		if (inLine != null && inLine.length() > 2)
			entry.sElem.setAttribute("i", inLine.substring(2).trim());

		Element vfElem = doc.createElement("vf");
		if (ruLine != null && ruLine.length() > 3)
			vfElem.setAttribute("ru", ruLine.substring(2).trim());
		vfElem.setTextContent(entry.entryName);

		Element vElem = doc.createElement("v");
		vElem.appendChild(vfElem);
		if (grLine != null && grLine.length() > 3)
			makeLeaf("gram", grLine, vElem, true);
		entry.sElem.appendChild(vElem);
	}

	/**
	 * Apstrādā nozīmju bloku un frāžu bloku.
	 * @param entry	šķirklis, kam pievienot jaunos elementus
	 */
	protected void processSenses(Entry entry) throws IOException
	{
		entry.senseNumber = 0;
		if (currentLine.indexOf("NS ") == 0) currentLine = currentDicIn.readLine();

		Element g_n = null;
		while (currentLine.indexOf("NO ") == 0)
		{
			if (g_n == null) g_n = doc.createElement("g_n");
			mkNO(g_n, entry);
		}
		if (g_n != null) entry.sElem.appendChild(g_n);

		Element g_fraz = null;
		if (currentLine.indexOf("FS ") == 0) currentLine = currentDicIn.readLine();
		while (currentLine.indexOf("FR ") == 0)
		{
			if (g_fraz == null) g_fraz = doc.createElement("g_fraz");
			mkFR(g_fraz);
		}
		if (g_fraz != null) entry.sElem.appendChild(g_fraz);

		return;
	}

	/**
	 * Apstrādā šķirkļa "apakšgalu" - atvasinājumus un atsucēm.
	 * @param entry	šķirklis, kam pievienot jaunos elementus
	 */
	protected void processEntryFooter(Entry entry) throws IOException
	{
		Element g_de = null;
		if (currentLine.indexOf("DS ") == 0) currentLine = currentDicIn.readLine();
		while (currentLine != null && currentLine.indexOf("DE ") == 0)
		{
			if (g_de == null) g_de = doc.createElement("g_de");
			mkDE(g_de);
		}
		if (g_de != null) entry.sElem.appendChild(g_de);

		if (currentLine != null &&
				(currentLine.indexOf("DN ") == 0 || currentLine.indexOf("CD ") == 0))
		{
			makeLeaf("ref", currentLine, entry.sElem);
			currentLine = currentDicIn.readLine();
		}

		if (currentLine != null && currentLine.indexOf("LI ") == 0)
		{
			makeLeaf("avots", currentLine, entry.sElem);
			currentLine = currentDicIn.readLine();
		}

	}

	//--------------------------------------------------------------------------

	protected void mkNO(Element g_nElem, Entry entry) throws IOException
	{
		entry.senseNumber++;

		Element nElem = doc.createElement("n");
		nElem.setAttribute("nr", String.valueOf(entry.senseNumber));
		Element dElem = doc.createElement("d");
		makeLeaf("t", currentLine, dElem);
		currentLine = currentDicIn.readLine();

		if (currentLine.indexOf("NG ") == 0)
		{
			makeLeaf("gram", currentLine, nElem, true);
			currentLine = currentDicIn.readLine();
		}

		nElem.appendChild(dElem);

		Element g_piemElem = null;
		Element g_anElem = null;

		while ((currentLine.indexOf("AN ") == 0) || (currentLine
				.indexOf("PI ") == 0) || (currentLine
				.indexOf("PG ") == 0))
		{
			while (currentLine.indexOf("AN ") == 0)
			{
				if (g_anElem == null) g_anElem = doc.createElement("g_an");
				mkAN(g_anElem, entry);
			}

			if (currentLine.indexOf("PG ") == 0) mkPG(null, entry);

			while (currentLine.indexOf("PI ") == 0)
			{
				if (g_piemElem == null) g_piemElem = doc.createElement("g_piem");
				mkPI(g_piemElem, entry);
			}
		}

		if (g_piemElem != null) nElem.appendChild(g_piemElem);
		if (g_anElem != null) nElem.appendChild(g_anElem);
		g_nElem.appendChild(nElem);
	}

	protected void mkPN(Element piemElem) throws IOException
	{
		Element nElem = doc.createElement("n");
		Element dElem = doc.createElement("d");
		makeLeaf("t", currentLine, dElem);
		currentLine = currentDicIn.readLine();

		nElem.appendChild(dElem);
		piemElem.appendChild(nElem);
	}


	protected void mkPG(Element piemElem, Entry entry)
	throws IOException
	{
		boolean apst = false;
		if (!parcelts)
		{

			if (piemElem == null)
			{
				prevLine = currentLine;
				currentLine = currentDicIn.readLine();
				apst = true;
				log.println("!!! " + prevLine + " " + entry.entryName);
			}
			if (apst)
			{
				parcelts = true;
				if (currentLine.indexOf("PN ") == 0)
				{
					String temp = currentLine;
					currentLine = prevLine;
					prevLine = temp;
					mkPG(piemElem, entry);
				}
				return;
			}
		}
		parcelts = false;

		if (piemElem == null)
		{
			log.println("BAD!!! " + currentLine + " " + entry.entryName);
			return;
		}

		makeLeaf("gram", currentLine, piemElem, true);
		if (prevLine != null)
		{
			currentLine = prevLine;
			prevLine = null;
		} else currentLine = currentDicIn.readLine();
	}

	protected void mkAN(Element g_anElem, Entry entry) throws IOException
	{
		Element nElem = doc.createElement("n");
		Element dElem = doc.createElement("d");
		makeLeaf("t", currentLine, dElem);
		currentLine = currentDicIn.readLine();

		if (currentLine.indexOf("AG ") == 0)
		{
			makeLeaf("gram", currentLine, nElem, true);
			currentLine = currentDicIn.readLine();
		}

		nElem.appendChild(dElem);
		if (currentLine.indexOf("PG ") == 0) mkPG(null, entry);
		Element g_piem = null;
		while (currentLine.indexOf("PI ") == 0)
		{
			if (g_piem == null) {g_piem = doc.createElement("g_piem");}
			mkPI(g_piem, entry);
		}
		if (g_piem != null) nElem.appendChild(g_piem);
		g_anElem.appendChild(nElem);
	}

	protected void mkPI(Element g_piemElem, Entry entry) throws IOException
	{
		Element piemElem = doc.createElement("piem");
		makeLeaf("t", currentLine, piemElem, true);
		if (prevLine != null)
		{
			currentLine = prevLine;
			prevLine = null;
		}
		else currentLine = currentDicIn.readLine();

		if (currentLine.indexOf("PG ") == 0) mkPG(piemElem, entry);
		while (currentLine.indexOf("PN ") == 0) mkPN(piemElem);
		g_piemElem.appendChild(piemElem);
	}

	protected void mkFR(Element g_frazElem) throws IOException
	{
		Element frazElem = doc.createElement("fraz");
		makeLeaf("t", currentLine, frazElem, true);
		currentLine = currentDicIn.readLine();

		if (currentLine.indexOf("FG ") == 0)
		{
			makeLeaf("gram", currentLine, frazElem, true);
			currentLine = currentDicIn.readLine();
		}

		if (currentLine.indexOf("FP ") == 0) mkFP(frazElem, false);
		while (currentLine.indexOf("FN ") == 0) mkFN(frazElem);
		g_frazElem.appendChild(frazElem);
	}

	protected void mkFN(Element frazElem) throws IOException
	{
		Element nElem = doc.createElement("n");
		Element dElem = doc.createElement("d");
		makeLeaf("t", currentLine, dElem);
		currentLine = currentDicIn.readLine();
		nElem.appendChild(dElem);
		while (currentLine.indexOf("FP ") == 0) mkFP(nElem, true);
		frazElem.appendChild(nElem);
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

	protected void mkDE(Element g_deElem)
	throws IOException
	{
		Element deElem = doc.createElement("de");
		Element vElem = doc.createElement("v");
		makeLeaf("vf", currentLine, vElem);
		currentLine = currentDicIn.readLine();

		if (currentLine.indexOf("DG ") == 0)
		{
			makeLeaf("gram", currentLine, vElem, true);
			currentLine = currentDicIn.readLine();
		}

		deElem.appendChild(vElem);
		while (currentLine.indexOf("DP ") == 0) mkDP(deElem);
		g_deElem.appendChild(deElem);
	}

	protected void mkDP(Element deElem) throws IOException
	{
		Element piemElem = doc.createElement("piem");
		makeLeaf("t", currentLine, piemElem);
		currentLine = currentDicIn.readLine();

		if (currentLine.indexOf("DA ") == 0)
		{
			makeLeaf("gram", currentLine, piemElem, true);
			currentLine = currentDicIn.readLine();
		}
		if (currentLine.indexOf("DD ") == 0) mkPN(piemElem);
		deElem.appendChild(piemElem);
	}
//--------------------------------------------------------------------------------------------------


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

	/**
	 * Informācija par šobrīd apstrādājamo šķirkli.
	 */
	public class Entry
	{
		public Element sElem = doc.createElement("s");
		public int senseNumber = 0;
		protected String entryName;

	}
}
