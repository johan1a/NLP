import java.util.LinkedList;
import java.util.TreeMap;

public class BaselineTagger {
	/* The most common POS tags of the words */
	TreeMap<String, String> mostCommonPos;

	public BaselineTagger(TreeMap<String, String> mostCommonPos) {
		this.mostCommonPos = mostCommonPos;

	}

	public void tag(LinkedList<Word> words) {
		for(Word word : words){
			word.setPPOS(mostCommonPos.get(word.getFORM()));
		}
	}
}
