import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class CorpusParser {
	LinkedList<Word> allWords;
	private TreeMap<String, Integer> wordFrequencies, POSFrequencies;
	TreeMap<String, TreeMap<String, Integer>> wordPOSCount;
	TreeMap<String, String> mostCommonPos;

	public void parse(String set) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(set));
			wordFrequencies = new TreeMap<String, Integer>();
			POSFrequencies = new TreeMap<String, Integer>();
			wordPOSCount = new TreeMap<String, TreeMap<String, Integer>>();
			allWords = new LinkedList<Word>();
			String[] tags = {};
			String line = r.readLine();
			Word word;

			while (line != null) {
				tags = line.split("\\s+", 7);
				if (tags.length >= 6) {
					word = new Word(tags);
					allWords.add(word);

					incrementWordFrequency(word);
					incrementPOSFrequency(word);
					incrementWordPosCount(word);
				}
				line = r.readLine();
			}

			mostCommonPos = new TreeMap<String, String>();
			for (String form : wordPOSCount.keySet()) {
				mostCommonPos.put(form, getMostCommonWordPOS(form));
			}

			r.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void printWords() {
		for (String s : wordFrequencies.keySet()) {
			System.out.println(s + " " + wordFrequencies.get(s));
		}
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

	public TreeMap<String, String> getMostCommonPOSTags() {
		return mostCommonPos;
	}

	public LinkedList<Word> getWords() {
		return allWords;
	}

	/* Returns the most common POS tag of the word */
	private String getMostCommonWordPOS(String word) {
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

	/* Increments the amount of times the word has been tagged as pos */
	private void incrementWordPosCount(Word word) {
		String form = word.getFORM(), pos = word.getPOS();
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

	/* Increments the amount of times the tag POS has been used */
	private void incrementPOSFrequency(Word word) {
		String POS = word.getPOS();
		Integer frequency = POSFrequencies.get(POS);
		if (frequency == null) {
			POSFrequencies.put(POS, 1);
		} else {
			POSFrequencies.put(POS, frequency + 1);
		}
	}

	/* Increments the amount of times the word has been seen */
	private void incrementWordFrequency(Word word) {
		String form = word.getFORM();
		Integer frequency = wordFrequencies.get(form);
		if (frequency == null) {
			wordFrequencies.put(form, 1);
		} else {
			wordFrequencies.put(form, frequency + 1);
		}
	}

}
