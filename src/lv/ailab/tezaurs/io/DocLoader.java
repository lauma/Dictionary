package lv.ailab.tezaurs.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

/**
 * Object for reading .doc files with dictionary data.
 * @author Lauma, Gunārs Danovskis
 */
public class DocLoader
{
	/**
	 * Load contents of dictionary .doc file containing one entry per line.
	 * @param path wherefrom load.
	 * @return array of entries.
	 * @throws IOException
	 */
	public static String[] loadDictionary(String path)
			throws IOException
	{
		File entryFile = new File(path);
		FileInputStream fis = new FileInputStream(entryFile.getAbsolutePath());
		WordExtractor entryExtract = new WordExtractor(new HWPFDocument(fis));
		String[] entries = entryExtract.getParagraphText();
		return entries;
	}

}
