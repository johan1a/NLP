package sentenceProbability;

import java.util.ArrayList;
import java.util.TreeMap;

public class Sentence {

	private ArrayList<SentenceElement> elementList;

	public Sentence(TreeMap<String, TreeMap<String, Double>> matrix) {
		elementList = new ArrayList<SentenceElement>();
		for (String form : matrix.keySet()) {
			SentenceElement element = new SentenceElement(form,
					matrix.get(form));
			elementList.add(element);
		}
	}

	public SentenceElement getElement(int index) {
		return elementList.get(index);

	}

	public int getSize() {
		return elementList.size();
	}

}
