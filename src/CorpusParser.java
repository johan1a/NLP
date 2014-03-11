import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class CorpusParser {
	private String fileName = "CoNLL2009-ST-English-trial.txt";
	LinkedList<Word> allWords;
	private TreeMap<String, Integer> wordFrequencies, POSFrequencies;

	public void parse() {
		try {
			BufferedReader r = new BufferedReader(new FileReader(fileName));
			wordFrequencies = new TreeMap<String, Integer>();
			POSFrequencies = new TreeMap<String, Integer>();

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
				}
				line = r.readLine();
			}

			r.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void incrementPOSFrequency(Word word) {
		String POS = word.getPOS();
		Integer frequency = POSFrequencies.get(POS);
		if (frequency == null) {
			POSFrequencies.put(POS, 1);
		} else {
			POSFrequencies.put(POS, frequency + 1);
		}
	}

	private void incrementWordFrequency(Word word) {
		String form = word.getFORM();
		Integer frequency = wordFrequencies.get(form);
		if (frequency == null) {
			wordFrequencies.put(form, 1);
		} else {
			wordFrequencies.put(form, frequency + 1);
		}
	}

	public void printWords() {
		for (String s : wordFrequencies.keySet()) {
			System.out.println(s + " " + wordFrequencies.get(s));
		}
	}
}
