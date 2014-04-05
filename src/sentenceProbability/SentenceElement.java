package sentenceProbability;

import java.util.TreeMap;

public class SentenceElement {
	private String form, manualPOS, predictedPOS = "";
	private TreeMap<String, Double> posList;

	public SentenceElement(String form, String pOS) {
		this.form = form;
		manualPOS = pOS;
		posList = new TreeMap<String, Double>();
	}

	public String getForm() {
		return form;
	}

	public void addProbability(String pos, double probability) {
		posList.put(pos, probability);
	}

	public TreeMap<String, Double> getPosTags() {
		return posList;
	}

	public String toString() {
		return form;
	}

	public void setPredictedTag(String tag) {
		predictedPOS = tag;
	}

	public String getManualPos() {
		return manualPOS;
	}

	public String getPredictedPos() {
		return predictedPOS;
	}

	public boolean hasAlphabeticalForm() {
		for (char c : form.toCharArray()) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}
}
