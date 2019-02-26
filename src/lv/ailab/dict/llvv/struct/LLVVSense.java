package lv.ailab.dict.llvv.struct;

import lv.ailab.dict.llvv.analyzer.PhrasalExtractor;
import lv.ailab.dict.struct.Sense;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

/**
 * Pašlaik te nav būtiski paplašināta Sense funkcionalitāte, te ir iznestas
 * izgūšanas metodes.
 *
 * Izveidots 2019-02-25.
 * @author Lauma
 */
public class LLVVSense extends Sense
{
	public LLVVSense(Node nNode)
	{
		NodeList fields = nNode.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram"))
				grammar = new LLVVGram(field);
			else if (fieldname.equals("d"))
			{
				NodeList glossFields = field.getChildNodes();
				for (int j = 0; j < glossFields.getLength(); j++)
				{
					Node glossField = glossFields.item(j);
					String glossFieldname = glossField.getNodeName();
					if (glossFieldname.equals("t"))
					{
						if (gloss != null)
							gloss.text.add(glossField.getTextContent());
							//System.err.println("\'d\' elements satur vairāk kā vienu \'t\' vai \'n\' - vairāk kā vienu \'t\'");
						else gloss = new LLVVGloss(glossField);
					}
					else if (glossFieldname.equals("gram"))
					{
						if (grammar != null)
							grammar = new LLVVGram(grammar.freeText + "; " + field.getTextContent());
							//System.err.println("\'n\' elements satur gan ārējo \'gram\', gan \'gram\' iekšā \'d\'" + nNode.getTextContent());
						else grammar = new LLVVGram(field);
					}
					else if (!glossFieldname.equals("#text")) // Teksta elementus šeit ignorē.
						System.err.printf("\'d\' elements \'%s\' netiek apstrādāts\n", glossFieldname);
				}
			}
			else if (fieldname.equals("n"))
			{
				if (subsenses == null) subsenses = new LinkedList<>();
				subsenses.add(new LLVVSense(field));
			}
			else if (fieldname.equals("piem"))
			{
				PhrasalExtractor.Type piemType = PhrasalExtractor.determineType(field);
				if (piemType == PhrasalExtractor.Type.SAMPLE)
				{
					if (examples == null) examples = new LinkedList<>();
					examples.add(new LLVVSample(field));
				}
				else if (piemType == PhrasalExtractor.Type.PHRASAL)
				{
					if (phrases == null) phrases = new LinkedList<>();
					phrases.add(new LLVVPhrase(field, LLVVPhrase.Type.STABLE_UNIT));
				}
				else
					System.err.printf("\'piem\' ar tipu \'%s\' netiek apstrādāts\n", piemType);

			}
			else if (fieldname.equals("fraz"))
			{
				if (phrases == null) phrases = new LinkedList<>();
				phrases.add(new LLVVPhrase(field, LLVVPhrase.Type.PHRASEOLOGICAL));
			}
			else if (!fieldname.equals("#text")) // Teksta elementus šeit ignorē.
				System.err.printf("\'n\' elements \'%s\' netiek apstrādāts\n", fieldname);
		}
		ordNumber = ((org.w3c.dom.Element)nNode).getAttribute("nr");
		if ("".equals(ordNumber)) ordNumber = null;
	}
}
