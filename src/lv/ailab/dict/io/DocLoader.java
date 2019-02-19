package lv.ailab.dict.io;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;

import java.io.File;
import java.io.IOException;

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
	 */
	public static String[] loadDoc(String path)
			throws IOException
	{
		File entryFile = new File(path);
		//FileInputStream fis = new FileInputStream(entryFile.getAbsolutePath());
		NPOIFSFileSystem fs = new NPOIFSFileSystem(entryFile);
		HWPFDocument doc = new HWPFDocument(fs.getRoot());
		WordExtractor entryExtract = new WordExtractor(doc);
		//WordExtractor entryExtract = new WordExtractor(new HWPFDocument(fis));
		return entryExtract.getParagraphText();
	}

}
