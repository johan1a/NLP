import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CorpusParser {
	private String fileName = "CoNLL2009-ST-English-trial.txt";
	private HashMap<String, Integer> wordFrequencies, POSFrequencies;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void parse() {
		try {
			BufferedReader r = new BufferedReader(new FileReader(fileName));
			wordFrequencies = new HashMap<String, Integer>();
			POSFrequencies = new HashMap<String, Integer>();
			String[] line = {};
			Word word;
			while (line != null) {
				line = r.readLine().split(" ", 6);
				word = new Word(line);
				incrementWordFrequency(word);
				incrementPOSFrequency(word);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void incrementPOSFrequency(Word word) {
		String POS = word.getPOS();
		Integer frequency = wordFrequencies.get(POS);
		if (frequency == null) {
			wordFrequencies.put(POS, 1);
		} else {
			wordFrequencies.put(POS, frequency + 1);
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

}
