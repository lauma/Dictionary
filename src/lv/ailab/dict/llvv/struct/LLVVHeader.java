package lv.ailab.dict.llvv.struct;

import lv.ailab.dict.struct.Header;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Pašlaik te nav būtiski paplašināta Header funkcionalitāte, te ir iznestas
 * izgūšanas metodes. Šķirkļa galvas konstrukcija tāda pati kā MLVV - ārējā
 * Header elementā ir lemma priekš LemmaSign TLex XMLiem, bet lemmu virkne ir
 * dota kā AltLemmas.
 *
 * Izveidots 2019-02-25.
 * @author Lauma
 */
public class LLVVHeader extends Header
{
	public LLVVHeader (List<Node> vNodes)
	{
		if (vNodes == null || vNodes.isEmpty()) return;

		gram = new LLVVGram();
		gram.altLemmas = new ArrayList<>();
		for (Node vNode : vNodes)
			gram.altLemmas.add(new LLVVHeader(vNode));

		lemma = new LLVVLemma(gram.altLemmas.get(0).lemma.text);
	}

	public LLVVHeader(Node vNode)
	{
		NodeList fields = vNode.getChildNodes();
		LinkedList<Node> postponed = new LinkedList<>();
		for (int i = 0; i < fields.getLength(); i++)
		{
			Node field = fields.item(i);
			String fieldname = field.getNodeName();
			if (fieldname.equals("vf")) // lemma
			{
				if (lemma != null)
					System.err.printf("vf ar lemmu \"%s\" satur vēl vienu \'vf\'\n", lemma.text);
				lemma = new LLVVLemma(field);
			}
			else if (!fieldname.equals("#text")) // Teksta elementus šeit ignorē.
				postponed.add(field);
		}
		if (lemma == null)
			System.err.printf("Vārdnīcas v elements bez lemmas:\n %s", vNode.toString());

		for (Node field : postponed)
		{
			String fieldname = field.getNodeName();
			if (fieldname.equals("gram")) // grammar
				gram = new LLVVGram(field);
			else System.err.printf(
					"Elementā v lauks %s netika apstrādāts\n", fieldname);
		}
	}
}
