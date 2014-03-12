package sentenceProbability;
import java.util.TreeMap;

	public class SentenceElement{

		private String form;
		private TreeMap<String, Double> posList;
		
		public SentenceElement(String form, TreeMap<String, Double> treeMap) {
			this.posList = treeMap;
			this.form = form;
		}

		public String getForm() {
			return form;
		}

		public TreeMap<String, Double> getPostTags() {
			return posList;
		}
		
	}
