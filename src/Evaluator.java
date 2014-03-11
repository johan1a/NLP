import java.util.LinkedList;
import java.util.TreeMap;

public class Evaluator {

	private TreeMap<String, TreeMap<String, Integer>> confusionMatrix;
	private double PPOSMatchRatio;

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

	private void incrementConfusionMatrix(String pos, String pPos) {
		TreeMap<String, Integer> map = confusionMatrix.get(pos);
		if (map == null) {
			map = new TreeMap<String, Integer>();
			confusionMatrix.put(pos, map);
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
