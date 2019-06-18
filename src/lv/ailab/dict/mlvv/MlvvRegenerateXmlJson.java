package lv.ailab.dict.mlvv;

import java.io.File;

public class MlvvRegenerateXmlJson
{
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

			}
			else System.out.println(
						"Ups! Neparedzēta tipa fails \"" + fileName + "\"!");
		}
	}
}
