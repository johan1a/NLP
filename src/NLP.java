import java.util.LinkedList;

public class NLP {

	private static String trainingSet = "corpus/CoNLL2009-ST-English-train-pos.txt";
	static String developmentSet = "corpus/CoNLL2009-ST-English-development-pos.txt";

	public static void main(String[] args) {
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
