package nlp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import sentenceProbability.Sentence;
import sentenceProbability.SentenceElement;

public class CorpusParser {
	private final static String BOS = "<bos>", EOS = "<eos>";
	private final static String[] BOS_TAGS = { "0", BOS, BOS, BOS, BOS, BOS };
	private final static String[] EOS_TAGS = { "-1", EOS, EOS, EOS, EOS, EOS };
	private static final int TAGS_PER_WORD = 6;
	
	private LinkedList<Word> allWords;
	private TreeMap<String, Integer> wordFrequencies, POSFrequencies;
	private TreeMap<String, TreeMap<String, Integer>> wordPOSCount;
	private TreeMap<String, String> mostCommonPos;
	private HashMap<Bigram, Integer> bigramFrequencies;
	private HashMap<FormWithPos, Integer> FormWithPosCount;
	private TreeMap<String, HashSet<String>> possibleWordPOS;
	private LinkedList<Sentence> sentenceList;

	private boolean testSetParsing = false;
	private int sentenceLength;
	private String prevPOS;
	private String[] tags;
	private Sentence currentSentence;

	public void parse(String fileName, int n) {
		initDataStructure();
		try {
			BufferedReader r = new BufferedReader(new FileReader(fileName));
			startNewSentence();
			prevPOS = EOS;
			tags = BOS_TAGS;
			collectWordData(tags);

			System.out.println("Parsing corpus: " + fileName);
			String line = r.readLine();
			while (line != null) {
				tags = line.toLowerCase().split("\\s+", TAGS_PER_WORD);
				if (!isEmpty(line)) {
					collectWordData(tags);
					line = r.readLine();
				} else {
					tags = EOS_TAGS;
					collectWordData(tags);

					/* length without BOS and EOS */
					if (sentenceLength - 2 < n) {
						sentenceList.add(currentSentence);

					}

					line = r.readLine();
					if (line != null) {
						startNewSentence();
						tags = BOS_TAGS;
						collectWordData(tags);
					}
				}
			}
			calculateMostCommonPos();
			r.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isEmpty(String line) {
		return line.isEmpty() || line.startsWith("\t");
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

	public TreeMap<String, HashSet<String>> getPossiblePos() {
		return possibleWordPOS;
	}

	/* p(bigram) = count(bigram)/count(t) */
	public HashMap<Bigram, Double> getBigramProbabilities() {
		HashMap<Bigram, Double> p = new HashMap<Bigram, Double>();

		for (Bigram b : bigramFrequencies.keySet()) {
			p.put(b,
					bigramFrequencies.get(b)
							/ ((double) POSFrequencies.get(b.getSecond())));
		}
		return p;
	}

	/*
	 * P(wi|ti) = count(wi|ti)/count(ti)
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

	public HashMap<Bigram, Integer> getBigramFrequencies() {
		return bigramFrequencies;
	}

	public LinkedList<Sentence> getSentences() {
		return sentenceList;
	}

	public static void saveOutput(String file, LinkedList<Sentence> sentences) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
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

	public boolean isTestSetParsing() {
		return testSetParsing;
	}

	public void setTestSetParsing(boolean testSetParsing) {
		this.testSetParsing = testSetParsing;
	}

	private void calculateMostCommonPos() {
		mostCommonPos = new TreeMap<String, String>();
		for (String form : wordPOSCount.keySet()) {
			mostCommonPos.put(form, getMostCommonWordPOS(form));
		}
	}

	private void startNewSentence() {
		currentSentence = new Sentence();
		sentenceLength = 0;
	}

	/* Collects data from a word using the given tags */
	private void collectWordData(String[] tags) {
		Word word = new Word(tags);
		String POS = word.getPOS();
		allWords.add(word);
		if (isTestSetParsing()) {
			currentSentence.add(new SentenceElement(word.getForm(), ""));
		} else {
			addPossiblePOS(word, POS);
			incrementWordFrequency(word);
			incrementWordPosCount(word);
			incrementFormWithPosCount(word);
			incrementBigramCount(prevPOS, POS);
			incrementPOSFrequency(POS);
			currentSentence.add(new SentenceElement(word.getForm(), POS));
		}
		prevPOS = word.getPOS();
		sentenceLength++;
	}

	private void initDataStructure() {
		wordFrequencies = new TreeMap<String, Integer>();
		POSFrequencies = new TreeMap<String, Integer>();
		wordPOSCount = new TreeMap<String, TreeMap<String, Integer>>();
		allWords = new LinkedList<Word>();
		bigramFrequencies = new HashMap<Bigram, Integer>();
		FormWithPosCount = new HashMap<FormWithPos, Integer>();
		possibleWordPOS = new TreeMap<String, HashSet<String>>();
		sentenceList = new LinkedList<Sentence>();
	}

	private void addPossiblePOS(Word word, String pOS) {
		String form = word.getForm();
		HashSet<String> set = possibleWordPOS.get(form);
		if (set == null) {
			set = new HashSet<String>();
		}
		set.add(pOS);
		possibleWordPOS.put(form, set);
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
	private void incrementPOSFrequency(String POS) {
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
