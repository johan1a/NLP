import java.util.LinkedList;

public class NLP {

	private String trainingSet = "corpus/CoNLL2009-ST-English-train-pos.txt";
	private String developmentSet = "corpus/CoNLL2009-ST-English-development-pos.txt";
	private String testSentence = "corpus/testCorpus.txt";
	
	public static void main(String[] args) {
		NLP nlp = new NLP();
		nlp.testBigrams();
	}

	private void testBigrams() {
		CorpusParser parser = new CorpusParser();
		parser.parse(testSentence);
		parser.printBigrams();
	}

	public void firstTask() {
		CorpusParser parser = new CorpusParser();
		parser.parse(trainingSet);

		BaselineTagger tagger = new BaselineTagger(
				parser.getMostCommonPOSTags());

		parser.parse(developmentSet);
		LinkedList<Word> words = parser.getWords();
		tagger.tag(words);

		Evaluator evaluator = new Evaluator();
		evaluator.evaluate(words);

		System.out.println(evaluator.getMatchRatio());
	}

}
