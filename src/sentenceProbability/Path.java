package sentenceProbability;

public class Path {
	double probability;
	String pathString;

	public Path(String string, double d) {
		this.pathString = string;
		probability = d;
	}

	public String getString() {
		return pathString;
	}

	public double getProbability() {
		return probability;
	}

	@Override
	public String toString() {
		return pathString + ": " + probability;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(probability);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((pathString == null) ? 0 : pathString.hashCode());
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
		Path other = (Path) obj;
		if (Double.doubleToLongBits(probability) != Double
				.doubleToLongBits(other.probability))
			return false;
		if (pathString == null) {
			if (other.pathString != null)
				return false;
		} else if (!pathString.equals(other.pathString))
			return false;
		return true;
	}

}
