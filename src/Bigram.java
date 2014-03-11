public class Bigram {

	private String firstPos, secondPos;

	public Bigram(String f, String s) {
		firstPos = f;
		secondPos = s;
	}

	public String getFirst() {
		return firstPos;
	}

	public String getSecond() {
		return secondPos;
	}
}
