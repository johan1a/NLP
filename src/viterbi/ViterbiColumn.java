package viterbi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import nlp.Bigram;
import nlp.FormWithPos;

/* Represents a word of a sentence in the Viterbi algorithm. */
public class ViterbiColumn {
	LinkedList<SubElement> subElements;

	public ViterbiColumn(HashSet<String> posTags, String form) {
		subElements = new LinkedList<SubElement>();
		for (String pos : posTags) {
			subElements.add(new SubElement(new FormWithPos(pos, form)));
		}
	}

	public void initbestPreviousTerms(ViterbiColumn prevColumn,
			HashMap<Bigram, Double> bigramProbabilities,
			HashMap<FormWithPos, Double> wordProbabilities) {

		for (SubElement e : subElements) {
			prevColumn.getBestTerm(e, bigramProbabilities,
					wordProbabilities.get(e.getFormWithPos()));
		}
	}

	private void getBestTerm(SubElement e,
			HashMap<Bigram, Double> bigramProbabilities, Double fwpProbability) {
		double probability, elementP, max = -1;
		Double bigramP;
		SubElement bestElement = null;
		for (SubElement prevElement : subElements) {
			bigramP = bigramProbabilities.get(new Bigram(prevElement.getPos(),
					e.getPos()));
			if (bigramP == null) {
				bigramP = new Double(0);
			}
			elementP = prevElement.getProbability();
			probability = bigramP * elementP;
			if (probability >= max) {
				max = probability;
				bestElement = prevElement;
			}
		}

		e.setProbability(max * fwpProbability);
		e.setPreviousElement(bestElement);
	}

	public void setIsFirstColumn() {
		if (subElements.size() != 1) {
			// felllll nejjj
			System.out.println("fel i viterbicolumn");
		} else {
			subElements.get(0).setProbability(1);
		}
	}

	public LinkedList<SubElement> backTrack() {
		double max = -1, probability;
		SubElement bestElement = null;
		for (SubElement e : subElements) {
			probability = e.getProbability();
			if (probability >= max) {
				max = probability;
				bestElement = e;
			}
		}
		return bestElement.getPath();
	}

	/*
	 * Represents a tagged form and points backwards to the previous path
	 * element
	 */
	public class SubElement {
		private SubElement bestPrevious;
		private FormWithPos fwp;
		private double probability;

		public SubElement(FormWithPos formWithPos) {
			fwp = formWithPos;
			probability = 0;
		}

		public LinkedList<SubElement> getPath() {
			if (getPos().equals("<bos>")) {
				LinkedList<SubElement> path = new LinkedList<SubElement>();
				path.add(this);
				return path;
			}
			LinkedList<SubElement> path = bestPrevious.getPath();
			path.add(this);
			return path;
		}

		public void setPreviousElement(SubElement bestElement) {
			bestPrevious = bestElement;
		}

		public double getProbability() {
			return probability;
		}

		public String getPos() {
			return fwp.getPOS();
		}

		public FormWithPos getFormWithPos() {
			return fwp;
		}

		public void setProbability(double p) {
			probability = p;
		}
	}
}
