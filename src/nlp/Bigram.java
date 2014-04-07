package nlp;
public class Bigram {

	private String lastPos, secondPos;

	public Bigram(String prevPos, String pos) {
		lastPos = prevPos;
		secondPos = pos;
	}

	public String getFirst() {
		return lastPos;
	}

	public String getSecond() {
		return secondPos;
	}

	@Override
	public String toString() {
		return lastPos + " " + secondPos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastPos == null) ? 0 : lastPos.hashCode());
		result = prime * result
				+ ((secondPos == null) ? 0 : secondPos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bigram other = (Bigram) obj;
		if (lastPos == null) {
			if (other.lastPos != null)
				return false;
		} else if (!lastPos.equals(other.lastPos))
			return false;
		if (secondPos == null) {
			if (other.secondPos != null)
				return false;
		} else if (!secondPos.equals(other.secondPos))
			return false;
		return true;
	}
	
	
}
