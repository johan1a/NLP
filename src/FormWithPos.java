public class FormWithPos {
	String POS, form;

	public FormWithPos(String pos, String f) {
		POS = pos;
		form = f;
	}

	public String getPOS() {
		return POS;
	}

	public String getForm() {
		return form;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((POS == null) ? 0 : POS.hashCode());
		result = prime * result + ((form == null) ? 0 : form.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		FormWithPos other = (FormWithPos) obj;
		return POS.equals(other.POS) && form.equals(other.form);
	}
}
