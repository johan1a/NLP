package nlp;

import java.util.LinkedList;

import sentenceProbability.Sentence;

public class NLP {
	private String trainingSet = "corpus/CoNLL2009-ST-English-train-pos.txt";
	private String developmentSet = "corpus/CoNLL2009-ST-English-development-pos.txt";
	private String testSet = "corpus/CoNLL2009-ST-test-words.txt";
	private String taggedTestSet = "corpus/taggedTestSet.txt";
	int n;

	public NLP(int k) {
		n = k;
	}

	public static void main(String[] args) {
		NLP nlp = new NLP(10);

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
					nlp = new NLP(Integer.parseInt(args[i + 1]));
					i++;
				}
			}
		} else {
			System.out.println("Enter -tag to tag, -eval to evaluate.");
		}

		if (tag) {
			nlp.tagTestSet();
		} else if (eval) {
			nlp.evaluateTagger();
		}
	}

	public void tagTestSet() {
		CorpusParser parser = new CorpusParser();

		parser.parse(trainingSet, n);

		BaselineTagger tagger = new BaselineTagger(
				parser.getMostCommonPOSTags(), parser.getWordProbabilities(),
				parser.getPossiblePos(), parser.getBigramProbabilities());

		parser.testSetParsing = true;
		parser.parse(testSet, n);
		LinkedList<Sentence> sentences = parser.getSentences();
		tagger.tagViterbiSentences(sentences);

		CorpusParser.saveOutput(taggedTestSet, sentences);
		System.out.println("Tagging done");
	}

	public void evaluateTagger() {
		CorpusParser parser = new CorpusParser();

		parser.parse(trainingSet, n);

		BaselineTagger tagger = new BaselineTagger(
				parser.getMostCommonPOSTags(), parser.getWordProbabilities(),
				parser.getPossiblePos(), parser.getBigramProbabilities());

		parser.parse(developmentSet, n);
		LinkedList<Sentence> sentences = parser.getSentences();

		tagger.tagViterbiSentences(sentences);

		System.out.println("Prediction ratio: "
				+ Evaluator.evaluateSentences(sentences));
	}

}
