package lv.ailab.dict.mlvv;

import lv.ailab.dict.io.DictionaryXmlReadingException;
import lv.ailab.dict.mlvv.stdxmlloader.MlvvXmlLoader;
import lv.ailab.dict.mlvv.struct.MLVVElementFactory;
import lv.ailab.dict.struct.Dictionary;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MlvvRegenerateXmlJson
{
	public final static class Config
	{
		public final static boolean PRINT_XML = true;
		public final static boolean PRINT_JSON = true;
	}

	public String inputDataPath;
	public String outputDataPath;
	public String fileNameStub;
	public Dictionary dict;

	public MlvvRegenerateXmlJson(String inputPath, String outputPath, String inputFileName)
	{
		inputDataPath = inputPath;
		outputDataPath = outputPath;
		File dicFolder = new File(outputDataPath);
		fileNameStub = inputFileName;
		Matcher matcher = Pattern.compile("(.*)\\.([xX][mM][lL]|[jJ][sS][oO][nN])").matcher(inputFileName);
		if (matcher.matches())
		{
			fileNameStub = matcher.group(1);
		}
		if (!dicFolder.exists()) dicFolder.mkdirs();
	}

	/**
	 * @param args pirmais arguments - ceļš uz vietu, kur stāv apstrādājamie XML
	 *             faili
	 */
	public static void main (String[] args)
	{
		String path = args[0];
		if (!path.endsWith("/") && !path.endsWith("\\"))
			path = path + "\\";
		File folder = new File(path);
		if (!folder.exists())
		{
			System.out.println(
					"Ups! Nevar atrast ieejas datu mapi \"" + path + "\"!");
			return;
		}

		File[] listOfFiles = folder.listFiles();
		if (listOfFiles != null) for (File f : listOfFiles)
		{
			String fileName = f.getName();
			if (f.isDirectory() || f.getName().startsWith("~")) continue;
			if (fileName.endsWith(".xml"))
			{
				System.out.println("Apstrādā failu " + fileName + ".");
				MlvvRegenerateXmlJson converter = new MlvvRegenerateXmlJson(
						path, path + "result\\", fileName);
				converter.loadDictionary(fileName);
				converter.printDictionary();
			}
			else System.out.println(
						"Ups! Neparedzēta tipa fails \"" + fileName + "\"!");
		}
	}

	protected void loadDictionary(String fileName)
	{
		try
		{
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(inputDataPath + fileName);
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			dict = MlvvXmlLoader.me().makeDictionary(doc, MLVVElementFactory.me());
		}
		catch (ParserConfigurationException|IOException|SAXException e)
		{
			e.printStackTrace(System.err);
			System.out.println("Neizdodas ielasīt vārdnīcas XML failu " + inputDataPath + fileName + "!");
		}
		catch (DictionaryXmlReadingException e)
		{
			e.printStackTrace(System.err);
			System.out.println("Nav saprasta XML struktūra failam" + inputDataPath + fileName + "! Vai tas tiešām ir standartstruktūras vārdnīcas fails?");
		}
	}

	protected void printDictionary ()
	{
		if (Config.PRINT_XML) try
		{
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputDataPath + fileNameStub + ".xml"),
					StandardCharsets.UTF_8)));
			dict.toXMLFile(out);
			out.close();

		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Neizdodas izdrukāt rezultātu XML failā " + outputDataPath + fileNameStub + ".xml!");
		}

		if (Config.PRINT_JSON) try
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputDataPath + fileNameStub + ".json"),
					StandardCharsets.UTF_8));
			dict.toJSONFile(out);
			out.close();

		} catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.out.println("Neizdodas izdrukāt rezultātu JSON failā " + outputDataPath + fileNameStub +".json!");
		}
	}
}
