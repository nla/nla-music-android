package au.gov.nla.forte.util;

public enum PreferencesEnum {
	
	FORTE_DATABASE_COPIED("forteDBCopied");

	private String value;

	private PreferencesEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
