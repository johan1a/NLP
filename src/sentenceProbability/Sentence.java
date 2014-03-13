package sentenceProbability;

import java.util.ArrayList;
import java.util.TreeMap;

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
		return s;
	}

	public void addPredictedTag(int i, String tag) {
		elementList.get(i).addPredictedTag(tag);

	}

	public void setProbability(double d) {
		probability = d;

	}

	public double getProbability() {
		return probability;
	}

	public boolean wasTagged() {
		return wasTagged;
	}

	public void setWasTagged(boolean sentenceOK) {
		wasTagged = sentenceOK;
	}
}
