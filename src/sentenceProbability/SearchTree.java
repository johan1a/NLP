package sentenceProbability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class SearchTree {
	private HashSet<Path> allPaths;
	Node root;

	public SearchTree(Sentence sentence) {
		root = new Node(0, sentence, 1);
		allPaths = new HashSet<Path>();
	}

	public Path getBestPathViterbi() {
		return null;
	}

	public Path getBestPath() {
		return root.getBestPath(new Path("", 1));
	}

	public void printProbabilities() {
		root.printProbability(new Path("", 1));
		System.out.println(allPaths.size());
	}

	class Node {
		String form;
		double probability;
		TreeMap<String, Node> children;
		HashMap<String, Double> leafNodeProbabilities;

		public Node(int index, Sentence sentence, double probability) {
			SentenceElement element = sentence.getElement(index);
			form = element.getForm();
			this.probability = probability;
			TreeMap<String, Double> posTags = element.getPosTags();

			if (index < sentence.getSize() - 1) {

				children = new TreeMap<String, Node>();
				for (String pos : posTags.keySet()) {
					children.put(pos,
							new Node(index + 1, sentence, posTags.get(pos)));
				}
			} else if (index == sentence.getSize() - 1) {
				leafNodeProbabilities = new HashMap<String, Double>();

				for (String pos : posTags.keySet()) {
					leafNodeProbabilities.put(pos, posTags.get(pos));
				}
			}
		}

		public Path getBestPath(Path path) {
			if (leafNodeProbabilities != null) {
				double max = 0;
				Path bestPath = null;
				for (String pos : leafNodeProbabilities.keySet()) {
					Path p = new Path(path.getString() + " " + pos,
							path.getProbability() * probability
									* leafNodeProbabilities.get(pos));
					if (p.getProbability() > max) {
						bestPath = p;
						max = p.getProbability();
					}
				}
				return bestPath;
			}
			double max = 0;
			Path bestPath = null;
			for (String pos : children.keySet()) {
				Node n = children.get(pos);
				Path p = n.getBestPath(new Path(path.getString() + " " + pos,
						path.getProbability() * probability));
				if (p.getProbability() > max) {
					bestPath = p;
					max = p.getProbability();
				}
			}
			return bestPath;
		}

		@SuppressWarnings("synthetic-access")
		public void printProbability(Path path) {
			if (leafNodeProbabilities != null) {
				for (String pos : leafNodeProbabilities.keySet()) {
					Path p = new Path(path.getString() + " " + pos,
							path.getProbability() * probability
									* leafNodeProbabilities.get(pos));
					allPaths.add(p);
				}
			} else {
				for (String pos : children.keySet()) {
					Node n = children.get(pos);
					n.printProbability(new Path(path.getString() + " " + pos,
							path.getProbability() * probability));
				}
			}
		}
	}

}
