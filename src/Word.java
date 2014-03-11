public class Word {
	String ID; // the word index in the sentence;
	String FORM;// the word;
	String LEMMA;// the manually annotated lemma;
	String PLEMMA;// the automatically computed lemma.
	String POS;// is the part of speech manually given by the annotators, while
	String PPOS;// is the part of speech predicted by an automatic POS tagger.

	public Word(String ID, String FORM, String LEMMA, String PLEMMA,
			String POS, String PPOS) {
		this.ID = ID;
		this.FORM = FORM;
		this.LEMMA = LEMMA;
		this.PLEMMA = PLEMMA;
		this.POS = POS;
		this.PPOS = PPOS;
	}

	public Word(String[] line) {
		ID = line[0];
		FORM = line[1];
		LEMMA = line[2];
		PLEMMA = line[3];
		POS = line[4];
		PPOS = line[5];
	}

	public String getID() {
		return ID;
	}

	public String getFORM() {
		return FORM;
	}

	public String getLEMMA() {
		return LEMMA;
	}

	public String getPLEMMA() {
		return PLEMMA;
	}

	public String getPOS() {
		return POS;
	}

	public String getPPOS() {
		return PPOS;
	}

	public void setPPOS(String string) {
		PPOS = string;
	}
}
