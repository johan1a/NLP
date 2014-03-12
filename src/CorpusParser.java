import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;


public class CorpusParser {
	LinkedList<Word> allWords;
	private TreeMap<String, Integer> wordFrequencies, POSFrequencies;
	TreeMap<String, TreeMap<String, Integer>> wordPOSCount;
	TreeMap<String, String> mostCommonPos;
	HashMap<Bigram, Integer> bigramFrequencies;
	private HashMap<FormWithPos, Integer> FormWithPosCount;
	TreeMap<String, HashSet<String>> possibleWordPOS;

	public void parse(String set) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(set));
			wordFrequencies = new TreeMap<String, Integer>();
			POSFrequencies = new TreeMap<String, Integer>();
			wordPOSCount = new TreeMap<String, TreeMap<String, Integer>>();
			allWords = new LinkedList<Word>();
			bigramFrequencies = new HashMap<Bigram, Integer>();
			FormWithPosCount = new HashMap<FormWithPos, Integer>();
			possibleWordPOS = new TreeMap<String, HashSet<String>>();
			String[] tags = {};
			String line = r.readLine();
			Word word;
			String lastPOS = "<BOS>", POS;
			
			System.out.println("Parsing corpus...");
			while (line != null) {
				tags = line.toLowerCase().split("\\s+", 7);
				if (tags.length >= 6) {
					word = new Word(tags);
					allWords.add(word);
					incrementWordFrequency(word);
					incrementPOSFrequency(word);
					incrementWordPosCount(word);
					incrementFormWithPosCount(word);

					POS = word.getPOS();
					incrementBigramCount(lastPOS, POS);
					lastPOS = POS;

					addPossiblePOS(word.getForm(), POS);
				} else {
					incrementBigramCount(lastPOS, "<EOS>");
					lastPOS = "<BOS>";
				}

				line = r.readLine();
			}

			incrementBigramCount(lastPOS, "<EOS>");

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

	TreeMap<String, HashSet<String>> getPossiblePos() {
		return possibleWordPOS;
	}

	private void addPossiblePOS(String form, String pOS) {
		HashSet<String> set = possibleWordPOS.get(form);

		if (set == null) {
			set = new HashSet<String>();
		}
		set.add(pOS);
		possibleWordPOS.put(form, set);
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

	/* p(bigram) = count(bigram)/count(t)??? */
	public TreeMap<Bigram, Double> getBigramProbabilities() {
		TreeMap<Bigram, Double> p = new TreeMap<Bigram, Double>();

		for (Bigram b : bigramFrequencies.keySet()) {
			p.put(b,
					bigramFrequencies.get(b)
							/ ((double) POSFrequencies.get(b.getSecond())));
		}
		return p;
	}

	/*
	 * P(wi|ti) = count(wi|ti)/count(ti) ??? w är form, t är pos ???
	 */
	public HashMap<FormWithPos, Double> getWordProbabilities() {
		HashMap<FormWithPos, Double> p = new HashMap<FormWithPos, Double>();
		for (FormWithPos fwp : FormWithPosCount.keySet()) {
			p.put(fwp,
					FormWithPosCount.get(fwp)
							/ ((double) POSFrequencies.get(fwp.getPOS())));
		}
		return p;
	}

	public TreeMap<String, String> getMostCommonPOSTags() {
		return mostCommonPos;
	}

	public LinkedList<Word> getWords() {
		return allWords;
	}

	public Set<String> getAllPOSTags() {
		return POSFrequencies.keySet();
	}

	private void incrementFormWithPosCount(Word word) {
		String form = word.getForm(), pos = word.getPOS();

		FormWithPos p = new FormWithPos(pos, form);
		Integer i = FormWithPosCount.get(p);
		if (i == null) {
			FormWithPosCount.put(p, 1);
		} else {
			FormWithPosCount.put(p, i + 1);
		}
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

	/* Increments the number of times the bigram was seen */
	private void incrementBigramCount(String lastPOS, String pOS) {
		Bigram b = new Bigram(lastPOS, pOS);
		Integer i = bigramFrequencies.get(b);
		if (i == null) {
			bigramFrequencies.put(b, 1);
		} else {
			bigramFrequencies.put(b, i + 1);
		}
	}

	/* Increments the amount of times the word has been tagged as pos */
	private void incrementWordPosCount(Word word) {
		String form = word.getForm(), pos = word.getPOS();
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
		String form = word.getForm();
		Integer frequency = wordFrequencies.get(form);
		if (frequency == null) {
			wordFrequencies.put(form, 1);
		} else {
			wordFrequencies.put(form, frequency + 1);
		}
	}
}
