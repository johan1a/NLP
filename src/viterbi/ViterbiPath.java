package viterbi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

import nlp.Bigram;
import nlp.FormWithPos;
import sentenceProbability.Sentence;
import sentenceProbability.SentenceElement;
import viterbi.ViterbiColumn.SubElement;

public class ViterbiPath {
	LinkedList<ViterbiColumn> columns;
	private Sentence sentence;

	public ViterbiPath(Sentence sentence,
			TreeMap<String, HashSet<String>> possiblePos,
			HashMap<Bigram, Double> bigramProbabilities,
			HashMap<FormWithPos, Double> wordProbabilities) {
		columns = new LinkedList<ViterbiColumn>();
		this.sentence = sentence;
		initColumns(sentence, possiblePos);
		LinkedList<SubElement> bestPath = calculatePath(bigramProbabilities,
				wordProbabilities);
		if (sentence.taggingOK()) {
			tagSentence(bestPath);
		}
	}

	public Sentence getTaggedSentence() {
		return sentence;
	}

	private void tagSentence(LinkedList<SubElement> bestPath) {

		/* Don't include <eos> */
		for (int i = 0; i < sentence.getSize() - 1; i++) {
			sentence.getElement(i).setPredictedTag(bestPath.get(i).getPos());
		}
	}

	private LinkedList<SubElement> calculatePath(
			HashMap<Bigram, Double> bigramProbabilities,
			HashMap<FormWithPos, Double> wordProbabilities) {
		for (int i = 1; i < columns.size(); i++) {
			columns.get(i).initbestPreviousTerms(columns.get(i - 1),
					bigramProbabilities, wordProbabilities);
		}
		ViterbiColumn lastColumn = columns.get(columns.size() - 1);

		return lastColumn.backTrack();
	}

	public void initColumns(Sentence sentence,
			TreeMap<String, HashSet<String>> possiblePos) {
		SentenceElement e;
		boolean wasTagged = true;
		String form;
		for (int i = 0; i < sentence.getSize() - 1; i++) {
			e = sentence.getElement(i);
			form = e.getForm();
			HashSet<String> posTags = possiblePos.get(form);
			if (posTags != null) {
				columns.add(new ViterbiColumn(posTags, form));
			} else {
				wasTagged = false;
				break;
			}
		}
		sentence.setWasTagged(wasTagged);

		columns.get(0).setIsFirstColumn();
	}

}
