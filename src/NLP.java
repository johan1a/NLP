import java.util.LinkedList;

public class NLP {

	private String trainingSet = "corpus/CoNLL2009-ST-English-train-pos.txt";
	private String developmentSet = "corpus/CoNLL2009-ST-English-development-pos.txt";
	private String testSentence = "corpus/testCorpus.txt";

	public static void main(String[] args) {
		NLP nlp = new NLP();
		nlp.markovTask();
	}

	public void markovTask() {
		CorpusParser parser = new CorpusParser();
		parser.parse(trainingSet);

		BaselineTagger tagger = new BaselineTagger(
				parser.getMostCommonPOSTags(), parser.getWordProbabilities(),
				parser.getPossiblePos());

		parser.parse(testSentence);
		LinkedList<Word> words = parser.getWords();
		tagger.tagMarkov(words);
		tagger.printProbabilities();
		//tagger.printMarkovMatrix();
	}

	public void firstTask() {
		CorpusParser parser = new CorpusParser();
		parser.parse(trainingSet);

		BaselineTagger tagger = new BaselineTagger(
				parser.getMostCommonPOSTags(), parser.getWordProbabilities(),
				parser.getPossiblePos());

		parser.parse(developmentSet);
		LinkedList<Word> words = parser.getWords();
		tagger.tag(words);

		Evaluator evaluator = new Evaluator();
		evaluator.evaluate(words);

		System.out.println(evaluator.getMatchRatio());
	}

}
