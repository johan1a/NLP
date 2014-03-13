package nlp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

import sentenceProbability.Path;
import sentenceProbability.SearchTreeViterbi;
import sentenceProbability.Sentence;
import sentenceProbability.SentenceElement;

public class BaselineTagger {
	/* The most common POS tags of the words */
	TreeMap<String, String> mostCommonPos;
	private HashMap<FormWithPos, Double> wordProbabilities;
	private TreeMap<String, HashSet<String>> possiblePos;
	private Sentence sentence;
	private HashMap<Bigram, Double> bigramProbabilities;

	public BaselineTagger(TreeMap<String, String> mostCommonPos,
			HashMap<FormWithPos, Double> wordProb,
			TreeMap<String, HashSet<String>> possibleP,
			HashMap<Bigram, Double> bigramProbabilities) {
		this.mostCommonPos = mostCommonPos;
		this.wordProbabilities = wordProb;
		this.possiblePos = possibleP;
		this.bigramProbabilities = bigramProbabilities;

	}

	public void tag(LinkedList<Word> words) {
		for (Word word : words) {
			word.setPPOS(mostCommonPos.get(word.getForm()));
		}
	}

	public void tagViterbi(LinkedList<String> words) {
		System.out.println("Tagging Viterbi...");
		sentence = new Sentence();

		for (String w : words) {
			String form = w;
			SentenceElement e = new SentenceElement(form, "");
			sentence.add(e);

			if (possiblePos.get(form) != null) {
				for (String pos : possiblePos.get(form)) {
					e.addProbability(pos, 1);
				}
			}
		}
		SearchTreeViterbi tree = new SearchTreeViterbi(sentence,
				wordProbabilities, bigramProbabilities);

		System.out.println(tree.getBestPathViterbi());
	}

	public void tagMarkov(LinkedList<Word> words) {
		System.out.println("Tagging...");
		sentence = new Sentence();

		for (Word w : words) {
			String form = w.getForm();
			SentenceElement e = new SentenceElement(form, w.getPOS());
			sentence.add(e);

			if (possiblePos.get(form) != null) {
				for (String pos : possiblePos.get(form)) {

					FormWithPos fwp = new FormWithPos(pos, form);

					Double probability = wordProbabilities.get(fwp);

					if (probability == null) {
						probability = (double) 0;
					}

					e.addProbability(pos, probability);
				}
			}
		}
	}

	public void printMarkovMatrix() {
		for (SentenceElement e : sentence.getElements()) {
			String form = e.getForm();
			TreeMap<String, Double> map = e.getPosTags();
			System.out.print(form + ": ");
			for (String pos : map.keySet()) {
				System.out.print("(" + pos + ": " + map.get(pos) + ")");
			}
			System.out.println();
		}
	}

	public void tagViterbiSentences(LinkedList<Sentence> sentences) {
		boolean sentenceOK;
		for (Sentence sentence : sentences) {
			sentenceOK = true;
			
			for (SentenceElement e : sentence.getElements()) {
				String form = e.getForm();

				if (possiblePos.get(form) != null) {
					for (String pos : possiblePos.get(form)) {
						e.addProbability(pos, 1);
					}
				} else {
					/* No possible tags, probability = 0 */
					sentenceOK = false;
					break;
				}
			}
			// System.out.print(sentenceOK + " " +sentence);
			if (sentenceOK) {

				SearchTreeViterbi tree = new SearchTreeViterbi(sentence,
						wordProbabilities, bigramProbabilities);
				Path p = tree.getBestPathViterbi();
				String[] predictedTags = p.getString().split(" ");
				//System.out.println(p.getString() + " " + p.getProbability());
				
				
				// System.out.print(" " +p.getProbability());
				sentence.setProbability(p.getProbability());

				for (int i = 0; i < sentence.getSize(); i++) {
					sentence.addPredictedTag(i, predictedTags[i]);
				}

			}
			// System.out.println();
			sentence.setWasTagged(sentenceOK);
		}

	}
}
