package sentenceProbability;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import nlp.Bigram;
import nlp.FormWithPos;

public class SearchTreeViterbi {
	private HashSet<Path> allPaths;
	Node root;
	private HashMap<FormWithPos, Double> wordProbabilites;
	private HashMap<Bigram, Double> bigramProbabilities;

	public SearchTreeViterbi(Sentence sentence,
			HashMap<FormWithPos, Double> wordProbabilities,
			HashMap<Bigram, Double> bigramProbabilities2) {
		root = new Node(0, sentence, 1);
		allPaths = new HashSet<Path>();
		this.wordProbabilites = wordProbabilities;
		this.bigramProbabilities = bigramProbabilities2;
		// for(SentenceElement e: sentence.getElements()){
		// System.out.println(e);
		// }
	}

	public Path getBestPathViterbi() {
		return root.getBestPathViterbi(new Path("", 1), "");
	}

	public void printProbabilities() {
		System.out.println(getBestPathViterbi());
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

		@SuppressWarnings("synthetic-access")
		public Path getBestPathViterbi(Path path, String lastPos) {

			if (this.equals(root)) {
				return children.get("<bos>").getBestPathViterbi(
						new Path("<bos> ", 1), "<bos>");
			}
			double max = -1;
			Double bigramProbability = 0.0, wordProbability = 0.0;
			// String form = "";
			String bestPos = "";
			Path bestPath = null;

			/* If we are at a leaf node */
			if (leafNodeProbabilities != null) {
				for (String pos : leafNodeProbabilities.keySet()) {

					double p;
					// form = children.get(pos).getForm(); // same for every
					// child

					bigramProbability = bigramProbabilities.get(new Bigram(
							lastPos, pos));
					if (bigramProbability == null) {
						bigramProbability = 0.0;

					}

					wordProbability = wordProbabilites.get(new FormWithPos(pos,
							form));
					if (wordProbability == null) {
						wordProbability = 0.0;
					}

					p = bigramProbability * wordProbability;
					if (p > max) {
						max = p;
						bestPos = pos;
						bestPath = new Path(path.getString() + bestPos + " ",
								path.getProbability() * max);
					}
				}
				return new Path(path.getString() + bestPos + " ",
						path.getProbability() * max);
			}

			/* Otherwise */
			for (String pos : children.keySet()) {

				double p;
				// form = children.get(pos).getForm(); // same for every child

				bigramProbability = bigramProbabilities.get(new Bigram(lastPos,
						pos));
				if (bigramProbability == null) {
					bigramProbability = 0.0;
					// System.out.println(form + " " + children.get(pos).form);
				}

				wordProbability = wordProbabilites.get(new FormWithPos(pos,
						form));
				if (wordProbability == null) {
					wordProbability = 0.0;

				}
				//System.out.println(form + " " + lastPos + " " + pos + " " + bigramProbability + " " +wordProbability);
				p = bigramProbability * wordProbability;
				if (p > max) {
					max = p;
					bestPos = pos;
					bestPath = new Path(path.getString() + bestPos + " ",
							path.getProbability() * max);
				}
			}
			// System.out.println(max + " " + lastPos + " " + bestPos);
			// System.out.println(children.keySet());
			return children.get(bestPos).getBestPathViterbi(bestPath, bestPos);
		}

		@SuppressWarnings("synthetic-access")
		public void printProbability(Path path) {
			if (leafNodeProbabilities != null) {
				for (String pos : leafNodeProbabilities.keySet()) {
					Path p = new Path(path.getString() + " " + pos,
							path.getProbability() * probability
									* leafNodeProbabilities.get(pos));
					allPaths.add(p);
					// System.out.println(p);
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
