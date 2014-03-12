import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import sentenceProbability.SearchTree;
import sentenceProbability.Sentence;

public class BaselineTagger {
	/* The most common POS tags of the words */
	TreeMap<String, String> mostCommonPos;
	private HashMap<FormWithPos, Double> wordProbabilities;
	private TreeMap<String, HashSet<String>> possiblePos;
	private TreeMap<String, TreeMap<String, Double>> matrix;

	public BaselineTagger(TreeMap<String, String> mostCommonPos,
			HashMap<FormWithPos, Double> hashMap,
			TreeMap<String, HashSet<String>> treeMap2) {
		this.mostCommonPos = mostCommonPos;
		wordProbabilities = hashMap;
		possiblePos = treeMap2;
	}

	public void tag(LinkedList<Word> words) {
		for (Word word : words) {
			word.setPPOS(mostCommonPos.get(word.getForm()));
		}
	}

	public void tagMarkov(LinkedList<Word> words) {
		System.out.println("Tagging...");
		matrix = new TreeMap<String, TreeMap<String, Double>>();

		for (Word w : words) {
			String form = w.getForm();

			if (possiblePos.get(form) != null) {

				for (String pos : possiblePos.get(form)) {

					FormWithPos fwp = new FormWithPos(pos, form);

					Double probability = wordProbabilities.get(fwp);

					if (probability == null) {
						probability = (double) 0;
					}

					TreeMap<String, Double> map = matrix.get(form);
					if (map == null) {
						map = new TreeMap<String, Double>();
						matrix.put(form, map);
					}

					map.put(pos, probability);
				}
			}
		}
	}

	public void printMarkovMatrix() {
		for (String form : matrix.keySet()) {
			TreeMap<String, Double> map = matrix.get(form);
			System.out.print(form + ": ");
			for (String pos : map.keySet()) {
				System.out.print("(" + pos + ": " + map.get(pos) + ")");
			}
			System.out.println();
		}
	}

	public void printProbabilities() {
		Sentence sentence = new Sentence(matrix);
		SearchTree tree = new SearchTree(sentence);
		printMarkovMatrix();
		System.out.println(tree.getBestPath());
		tree.printProbabilities();
	}

}
