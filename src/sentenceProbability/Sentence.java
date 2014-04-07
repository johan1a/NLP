package sentenceProbability;

import java.util.ArrayList;

public class Sentence {
	private ArrayList<SentenceElement> elementList;
	double probability;
	private boolean wasTagged;

	public Sentence() {
		elementList = new ArrayList<SentenceElement>();
	}

	public SentenceElement getElement(int index) {
		return elementList.get(index);
	}

	public int getSize() {
		return elementList.size();
	}

	public void add(SentenceElement e) {
		elementList.add(e);

	}

	public ArrayList<SentenceElement> getElements() {
		return elementList;
	}

	public String toString() {
		String s = "";
		for (SentenceElement e : elementList) {
			s += e + " ";
		}
		s += "\n";
		for (SentenceElement e : elementList) {
			s += e.getPredictedPos() + " ";
		}
		s += "\n";
		return s;
	}

	public void addPredictedTag(int i, String tag) {
		elementList.get(i).setPredictedTag(tag);
	}

	public void setProbability(double d) {
		probability = d;
	}

	public double getProbability() {
		return probability;
	}

	public boolean taggingOK() {
		return wasTagged;
	}

	public void setWasTagged(boolean sentenceOK) {
		wasTagged = sentenceOK;
	}
}
