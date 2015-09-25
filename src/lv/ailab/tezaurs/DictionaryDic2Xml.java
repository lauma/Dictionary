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
		if (currentLine.indexOf("DN ") == 0) mkDN(s);
		if (currentLine.indexOf("CD ") == 0) mkDN(s);
		if (currentLine.indexOf("LI ") == 0) mkLI(s);

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
		Element b = doc.createElement("piem");
		Element a = doc.createElement("t");
		currentLine = (currentLine.substring(3)).trim();
		if (currentLine.charAt(currentLine.length() - 1) == '-')
			currentLine = (currentLine.substring(0, currentLine.length() - 1)).trim();
		a.setTextContent(currentLine);
		b.appendChild(a);
		if (prevLine != null)
		{
			currentLine = prevLine;
			prevLine = null;
		} else currentLine = currentDicIn.readLine();
		if (currentLine.indexOf("PG ") == 0) mkPG(b);
		while (currentLine.indexOf("PN ") == 0) mkPN(b);
		el.appendChild(b);
	}

	protected void mkFR(Element el) throws IOException
	{
		Element fel = doc.createElement("fraz");
		Element t = doc.createElement("t");
		currentLine = (currentLine.substring(3)).trim();
		if (currentLine.charAt(currentLine.length() - 1) == '-')
			currentLine = (currentLine.substring(0, currentLine.length() - 1)).trim();
		t.setTextContent(currentLine);
		fel.appendChild(t);
		currentLine = currentDicIn.readLine();
		if (currentLine.indexOf("FG ") == 0) mkNG(fel);
		if (currentLine.indexOf("FP ") == 0) mkFP(fel, false);
		while (currentLine.indexOf("FN ") == 0) mkFN(fel);
		el.appendChild(fel);
	}

	protected void mkFN(Element el) throws IOException
	{
		Element nel = doc.createElement("n");
		Element d = doc.createElement("d");
		Element t = doc.createElement("t");
		t.setTextContent((currentLine.substring(3)).trim());
		d.appendChild(t);
		currentLine = currentDicIn.readLine();
		nel.appendChild(d);
		while (currentLine.indexOf("FP ") == 0) mkFP(nel, true);
		el.appendChild(nel);
	}

	protected void mkFP(Element el, boolean labs)
	throws IOException
	{
		if (labs)
		{
			Element b = doc.createElement("piem");
			Element a = doc.createElement("t");
			a.setTextContent((currentLine.substring(3)).trim());
			b.appendChild(a);
			el.appendChild(b);
		} else
		{
			Element nel = doc.createElement("n");
			Element d = doc.createElement("d");
			Element t = doc.createElement("t");
			d.appendChild(t);
			nel.appendChild(d);
			Element b = doc.createElement("piem");
			Element a = doc.createElement("t");
			a.setTextContent((currentLine.substring(3)).trim());
			b.appendChild(a);
			nel.appendChild(b);
			el.appendChild(nel);
		}
		currentLine = currentDicIn.readLine();
	}

	protected void mkDE(Element el)
	throws IOException
	{
		Element fel = doc.createElement("de");
		Element a = doc.createElement("v");
		Element t = doc.createElement("vf");
		t.setTextContent((currentLine.substring(3)).trim());
		a.appendChild(t);
		currentLine = currentDicIn.readLine();
		if (currentLine.indexOf("DG ") == 0) mkNG(a);
		fel.appendChild(a);
		while (currentLine.indexOf("DP ") == 0) mkDP(fel);
		el.appendChild(fel);
	}

	protected void mkDP(Element el) throws IOException
	{
		Element a = doc.createElement("piem");
		Element b = doc.createElement("t");
		b.setTextContent((currentLine.substring(3)).trim());
		a.appendChild(b);
		currentLine = currentDicIn.readLine();
		if (currentLine.indexOf("DA ") == 0) mkNG(a);
		if (currentLine.indexOf("DD ") == 0) mkPN(a);
		el.appendChild(a);
	}
//--------------------------------------------------------------------------------------------------


	protected void mkDN(Element s) throws IOException
	{
		Element ref = doc.createElement("ref");
		ref.setTextContent((currentLine.substring(3)).trim());
		s.appendChild(ref);
		currentLine = currentDicIn.readLine();
	}

	protected void mkLI(Element s) throws IOException
	{
		Element avots = doc.createElement("avots");
		avots.setTextContent((currentLine.substring(3)).trim());
		s.appendChild(avots);
		currentLine = currentDicIn.readLine();
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
}
