package au.gov.nla.forte.model;

public class Favourite {
	
	private String id;
	private String score;
	private String identifier;
	private ScoreMetadata scoreMetadata;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public ScoreMetadata getScoreMetadata() {
		return scoreMetadata;
	}
	public void setScoreMetadata(ScoreMetadata scoreMetadata) {
		this.scoreMetadata = scoreMetadata;
	}
}
