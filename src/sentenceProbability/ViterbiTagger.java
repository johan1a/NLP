package sentenceProbability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import nlp.Bigram;
import nlp.FormWithPos;

public class ViterbiTagger {
	TreeMap<String, String> mostCommonPOSTags;
	HashMap<FormWithPos, Double> wordProbabilities;
	TreeMap<String, HashSet<String>> possiblePos;
	HashMap<Bigram, Double> bigramProbabilities;

	public ViterbiTagger(TreeMap<String, String> mostCommonPOSTags,
			HashMap<FormWithPos, Double> wordProbabilities,
			TreeMap<String, HashSet<String>> possiblePos,
			HashMap<Bigram, Double> bigramProbabilities) {
		this.mostCommonPOSTags = mostCommonPOSTags;
		this.wordProbabilities = wordProbabilities;
		this.possiblePos = possiblePos;
		this.bigramProbabilities = bigramProbabilities;
	}

	public void tagSentences(LinkedList<Sentence> sentences) {
		for (Sentence s : sentences) {
			tagSentence(s);
		}
	}

	public void tagSentence(Sentence sentence) {
		double sentenceProbability = 1, tagProbability, maxProbability;
		Double bigramProbability, wordProbability;
		String bestPos, lastPos;

		for (int i = 1; i < sentence.getSize() - 1; i++) {
			SentenceElement e = sentence.getElement(i);
			String form = e.getForm();

			bestPos = "";
			lastPos = "<bos>";
			bigramProbability = 0.0;
			wordProbability = 0.0;
			maxProbability = -1;

			HashSet<String> possiblePosTags = possiblePos.get(form);
			if (possiblePosTags != null) {
				for (String pos : possiblePosTags) {
					bigramProbability = bigramProbabilities.get(new Bigram(
							lastPos, pos));
					wordProbability = wordProbabilities.get(new FormWithPos(
							pos, form));

					bigramProbability = checkNull(bigramProbability);
					wordProbability = checkNull(wordProbability);

					tagProbability = bigramProbability * wordProbability;
					if (tagProbability > maxProbability) {
						maxProbability = tagProbability;
						bestPos = pos;
					}
				}
				e.setPredictedTag(bestPos);
				sentenceProbability = sentenceProbability * maxProbability;
				sentence.setWasTagged(true);
			} else {
				sentence.setWasTagged(false);
			}
		}

	}

	private static Double checkNull(Double d) {
		if (d == null) {
			return new Double(0);
		}
		return d;
	}
}
