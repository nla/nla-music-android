package au.gov.nla.forte.constant;

import java.util.HashMap;

public class ScoreCount {
	
	private int TOTAL_SCORES = 12146;
	private final int MIN_SCORES_PER_DECADE = 200; 
	private final int MAX_SCORES_PER_DECADE = 1200;  
	
	private HashMap<Integer, Integer> map;
	
	public ScoreCount() {
		map = new HashMap<Integer, Integer>(18);
		map.put(1800, 46);
		map.put(1810, 4);
		map.put(1820, 34);
		map.put(1830, 26);
		map.put(1840, 167);
		map.put(1850, 402);
		map.put(1860, 394);
		map.put(1870, 421);
		map.put(1880, 609);
		map.put(1890, 1564);
		map.put(1900, 2049);
		map.put(1910, 2944);
		map.put(1920, 2045);
		map.put(1930, 1113);
		map.put(1940, 262);
		map.put(1950, 68);
		map.put(1960, 6);
		map.put(1970, 2);
	}
	
	public int calculateHeight(int year, int minHeight) {
		
        int numberInDecade = map.get(year);
        
        if (numberInDecade > MAX_SCORES_PER_DECADE) 
            numberInDecade = MAX_SCORES_PER_DECADE;
        else if (numberInDecade <MIN_SCORES_PER_DECADE)
            numberInDecade = MIN_SCORES_PER_DECADE;
    	
    	return Math.round((numberInDecade / MIN_SCORES_PER_DECADE) * minHeight);
    }
}
