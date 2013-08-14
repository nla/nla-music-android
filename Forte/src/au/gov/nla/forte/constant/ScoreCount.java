package au.gov.nla.forte.constant;

import java.util.HashMap;

public class ScoreCount {
	
	private int totalCount = 12146;
	private HashMap<Integer, Integer> map;
	
	public ScoreCount() {
		map = new HashMap<Integer, Integer>();
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
	
	public int getTotal() {
		return totalCount;
	}
	
	public int getCountForYear(int year) {
		return map.get(year);
	}
}
