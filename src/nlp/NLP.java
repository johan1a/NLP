package nlp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import sentenceProbability.Sentence;
import sentenceProbability.SentenceElement;

public class NLP {

	private String trainingSet = "corpus/CoNLL2009-ST-English-train-pos.txt";
	private String developmentSet = "corpus/CoNLL2009-ST-English-development-pos.txt";
	private String testSet = "corpus/CoNLL2009-ST-test-words.txt";
	private String taggedTestSet = "corpus/taggedTestSet.txt";
	private String testSentence = "corpus/testCorpus.txt";

	public static void main(String[] args) {
		NLP nlp = new NLP();
		nlp.tagTestSet();
		nlp.evaluateTagger();
	}

	public void tagTestSet() {
		CorpusParser parser = new CorpusParser();
		int n = 7;
		parser.parse(trainingSet, n);

		BaselineTagger tagger = new BaselineTagger(
				parser.getMostCommonPOSTags(), parser.getWordProbabilities(),
				parser.getPossiblePos(), parser.getBigramProbabilities());

		parser.testSetParsing = true;
		parser.parse(testSet, n);
		LinkedList<Sentence> sentences = parser.getSentences();
		tagger.tagViterbiSentences(sentences);

		saveOutput(sentences);
		System.out.println("Tagging done");
	}

	public void evaluateTagger() {
		CorpusParser parser = new CorpusParser();
		int n = 7;
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

	public void markovTask() {
		CorpusParser parser = new CorpusParser();
		parser.parse(trainingSet, 0);

		BaselineTagger tagger = new BaselineTagger(
				parser.getMostCommonPOSTags(), parser.getWordProbabilities(),
				parser.getPossiblePos(), parser.getBigramProbabilities());

		parser.parse(testSentence, 0);
		LinkedList<Word> words = parser.getWords();
		tagger.tagMarkov(words);
	}

	public void firstTask() {
		CorpusParser parser = new CorpusParser();
		parser.parse(trainingSet, 0);

		BaselineTagger tagger = new BaselineTagger(
				parser.getMostCommonPOSTags(), parser.getWordProbabilities(),
				parser.getPossiblePos(), parser.getBigramProbabilities());

		parser.parse(developmentSet, 0);
		LinkedList<Word> words = parser.getWords();
		tagger.tag(words);

		Evaluator evaluator = new Evaluator();
		evaluator.evaluate(words);

		System.out.println(evaluator.getMatchRatio());
	}

	private void saveOutput(LinkedList<Sentence> sentences) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					taggedTestSet));
			for (Sentence sentence : sentences) {
				int index = 1;
				if (sentence.wasTagged()) {
					for (SentenceElement e : sentence.getElements()) {
						if (e.getForm().equals("<bos>")) {
							continue;
						}
						writer.write(index + " " + e.getForm() + " "
								+ e.getPredictedPos());
						index++;
						writer.write("\n");
					}
					writer.write("\n");
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
