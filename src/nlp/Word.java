package nlp;

public class Word {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FORM == null) ? 0 : FORM.hashCode());
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		result = prime * result + ((LEMMA == null) ? 0 : LEMMA.hashCode());
		result = prime * result + ((PLEMMA == null) ? 0 : PLEMMA.hashCode());
		result = prime * result + ((POS == null) ? 0 : POS.hashCode());
		result = prime * result + ((PPOS == null) ? 0 : PPOS.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (FORM == null) {
			if (other.FORM != null)
				return false;
		} else if (!FORM.equals(other.FORM))
			return false;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		if (LEMMA == null) {
			if (other.LEMMA != null)
				return false;
		} else if (!LEMMA.equals(other.LEMMA))
			return false;
		if (PLEMMA == null) {
			if (other.PLEMMA != null)
				return false;
		} else if (!PLEMMA.equals(other.PLEMMA))
			return false;
		if (POS == null) {
			if (other.POS != null)
				return false;
		} else if (!POS.equals(other.POS))
			return false;
		if (PPOS == null) {
			if (other.PPOS != null)
				return false;
		} else if (!PPOS.equals(other.PPOS))
			return false;
		return true;
	}

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
		if (line.length > 2) {
			LEMMA = line[2];
			if (line.length > 3) {
				PLEMMA = line[3];
				if (line.length > 4) {
					POS = line[4];
					if (line.length > 5) {
						PPOS = line[5];
					}
				}
			}
		}
	}

	public String getID() {
		return ID;
	}

	public String getForm() {
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
