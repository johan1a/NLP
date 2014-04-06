package nlp;

import java.util.LinkedList;
import java.util.TreeMap;

import sentenceProbability.Sentence;
import sentenceProbability.SentenceElement;

public class Evaluator {

	private TreeMap<String, TreeMap<String, Integer>> confusionMatrix;
	private double PPOSMatchRatio;

	public static double evaluateSentences(LinkedList<Sentence> sentences) {
		int pOSMatchCount = 0, pOSCount = 0;
		System.out.println("Evaluating...");
		SentenceElement e;
		for (Sentence sentence : sentences) {
			if (sentence.wasTagged()) {
				/* Don't evaluate <bos> and <eos> */
				for (int i = 1; i < sentence.getSize() - 1; i++) {
					e = sentence.getElement(i);
					if (e.hasAlphabeticalForm()) {

						String pos = e.getManualPos(), pPos = e
								.getPredictedPos();
						pOSCount++;
						if (pos.equals(pPos)) {
							pOSMatchCount++;
						}
					}
				}
			}
		}

		double pPOSMatchRatio = pOSMatchCount / ((double) pOSCount);
		return pPOSMatchRatio;
	}

	public void evaluate(LinkedList<Word> words) {
		confusionMatrix = new TreeMap<String, TreeMap<String, Integer>>();
		int POSMatchCount = 0, POSCount = words.size();

		for (Word word : words) {
			String pos = word.getPOS(), pPos = word.getPPOS();

			if (pos.equals(pPos)) {
				POSMatchCount++;
			}
			incrementConfusionMatrix(pos, pPos);
		}
		PPOSMatchRatio = POSMatchCount / ((double) POSCount);
	}

	public double getMatchRatio() {
		return PPOSMatchRatio;
	}

	private void incrementConfusionMatrix(String pos, String pPos) {
		TreeMap<String, Integer> map = confusionMatrix.get(pos);
		if (map == null) {
			map = new TreeMap<String, Integer>();
			confusionMatrix.put(pos, map);
		}
		if (pPos == null) {
			pPos = "null";
		}
		Integer count = map.get(pPos);
		if (count == null) {
			count = 0;
		}
		map.put(pPos, count + 1);
	}

	public void printConfusionMatrix() {
		for (String pos : confusionMatrix.keySet()) {
			TreeMap<String, Integer> map = confusionMatrix.get(pos);
			System.out.print(pos + ": ");
			for (String s : map.keySet()) {
				System.out.print("(" + s + ", " + map.get(s) + ") ");
			}
			System.out.println();
		}
	}

}
