import java.util.LinkedList;
import java.util.TreeMap;

public class BaselineTagger {
	/* The count of different parts of speech of the word */
	TreeMap<String, TreeMap<String, Integer>> wordPOSCount;

	public BaselineTagger() {
		wordPOSCount = new TreeMap<String, TreeMap<String, Integer>>();
	}

	public void tag(LinkedList<Word> words) {
		for (Word word : words) {
			incrementWordPosCount(word.getFORM(), word.getPOS());
		}
	}

	public String getMostCommonWordPOS(String word) {
		TreeMap<String, Integer> map = wordPOSCount.get(word);
		String mostCommonPos = "";
		int max = 0;
		int count;
		if (map != null) {
			for (String pos : map.keySet()) {
				count = map.get(pos);
				if (count > max) {
					max = count;
					mostCommonPos = pos;
				}
			}
		}
		return mostCommonPos;
	}

	private void incrementWordPosCount(String form, String pos) {
		TreeMap<String, Integer> map = wordPOSCount.get(form);
		if (map == null) {
			map = new TreeMap<String, Integer>();
			wordPOSCount.put(form, map);
		}
		Integer count = map.get(pos);
		if (count == null) {
			count = 0;
		}
		map.put(pos, count + 1);
	}

	public void printWordPosCount() {
		for (String form : wordPOSCount.keySet()) {
			TreeMap<String, Integer> map = wordPOSCount.get(form);
			System.out.print(form + ": ");
			for (String pos : map.keySet()) {
				System.out.print("(" + pos + ", " + map.get(pos) + ") ");
			}
			System.out.println();
		}
	}
}
