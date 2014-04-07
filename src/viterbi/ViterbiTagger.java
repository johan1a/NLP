package viterbi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

import nlp.Bigram;
import nlp.FormWithPos;
import sentenceProbability.Sentence;

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
		ViterbiPath path = new ViterbiPath(sentence, possiblePos,
				bigramProbabilities, wordProbabilities);
	}
}
