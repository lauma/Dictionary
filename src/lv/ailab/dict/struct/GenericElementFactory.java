package lv.ailab.dict.struct;

public class GenericElementFactory
{
	public Dictionary getNewDictionary() { return new Dictionary(); }
	public Entry getNewEntry() { return new Entry(); }
	public Flags getNewFlags() { return new Flags(); }
	public StructRestriction getNewStructRestriction() { return new StructRestriction(); }
	public Gloss getNewGloss() { return new Gloss(); }
	public Gram getNewGram() { return new Gram(); }
	public Header getNewHeader() { return new Header(); }
	public Lemma getNewLemma() { return new Lemma(); }
	public Phrase getNewPhrase() { return new Phrase(); }
	public Sample getNewSample() { return new Sample(); }
	public Sense getNewSense() { return new Sense(); }
	public Sources getNewSources() { return new Sources(); }
}
