package au.gov.nla.forte.constant;

public class Nla {
	
	private final static String BASE_URL = "http://nla.gov.au/";
	private final static String THUMBNAIL = "-t";
	private final static String EXAMINATION_COPY = "-e";
	private final static String OAI_URL = "http://www.nla.gov.au/apps/oaicat/servlet/OAIHandler?verb=GetRecord&metadataPrefix=oai_dc&identifier=oai:nla.gov.au:";
	
	public static String getOaiUrl(String pid) {
		return OAI_URL + pid;
	}
	
	public static String getThumbnailUrl(String pid) {
		return BASE_URL + pid + THUMBNAIL;
	}
	
	public static String getImageUrl(String pid) {
		return BASE_URL + pid + EXAMINATION_COPY;
	}
	
	public static String getItemUrl(String pid) {
		return BASE_URL + pid;
	}

}
