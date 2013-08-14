package au.gov.nla.forte.model;

public class Page {
	
	private String id;
	private String number;
	private String score;
	private String identifier;
	
	
	public String getThumbnailUrl() {
		return "http://nla.gov.au/" + getIdentifier() + "-t";
	}
	
	public String getImageUrl() {
		return "http://nla.gov.au/" + getIdentifier() + "-e";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
