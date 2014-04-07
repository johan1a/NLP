package nlp;

import java.util.LinkedList;

import sentenceProbability.Sentence;
import viterbi.ViterbiTagger;

public class NLP {
	private String trainingSet = "corpus/CoNLL2009-ST-English-train-pos.txt";
	private String developmentSet = "corpus/CoNLL2009-ST-English-development-pos.txt";
	private String testSet = "corpus/CoNLL2009-ST-test-words.txt";
	private String taggedTestSet = "corpus/taggedTestSet.txt";
	private String testCorpus = "corpus/testCorpus.txt";
	int n;

	public NLP(int k) {
		n = k;
	}

	public static void main(String[] args) {
		int maxSentenceLength = 1000000;

		boolean tag = false;
		boolean eval = false;
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				String cmd = args[i];
				if (cmd.equals("-tag")) {
					tag = true;
				} else if (cmd.equals("-eval")) {
					eval = true;
				} else if (cmd.equals("-n")) {
					maxSentenceLength = Integer.parseInt(args[i + 1]);
					i++;
				}
			}
		} else {
			System.out.println("Enter -tag to tag, -eval to evaluate.");
		}
		NLP nlp = new NLP(maxSentenceLength);
		if (tag) {
			nlp.tagCorpus(true);
		} else if (eval) {
			nlp.tagCorpus(false);
		}
	}

	/*
	 * Tags the test set and saves the output if testSetParsing is true. If not,
	 * the development set is tagged and the tagger is then evaluated without
	 * saving any output.
	 */
	public void tagCorpus(boolean testSetParsing) {
		CorpusParser parser = new CorpusParser();
		parser.parse(trainingSet, n);
		ViterbiTagger tagger = new ViterbiTagger(parser.getMostCommonPOSTags(),
				parser.getWordProbabilities(), parser.getPossiblePos(),
				parser.getBigramProbabilities());

		parser.setTestSetParsing(testSetParsing);
		if (testSetParsing) {
			parser.parse(testSet, n);
		} else {
			parser.parse(developmentSet, n);
		}
		LinkedList<Sentence> sentences = parser.getSentences();
		tagger.tagSentences(sentences);
		if (testSetParsing) {
			CorpusParser.saveOutput(taggedTestSet, sentences);
			System.out.println("Tagging done");
		} else {
			System.out.println("Prediction ratio: "
					+ Evaluator.evaluateSentences(sentences));
		}
	}
}
