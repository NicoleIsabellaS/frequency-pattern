package de.uniba.mobi.frequencyPattern;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Node implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3723455680830001313L;
	private String hashmac;
	private Map<String, String> timeline = new HashMap<>();

	public Node() {
		super();
	}

	public Node(String hashmac) {
		this.hashmac = hashmac;
	}

	public String getHashmac() {
		return hashmac;
	}

	public void setHashmac(String hashmac) {
		this.hashmac = hashmac;
	}

	public Map<String, String> getTimeline() {
		return timeline;
	}

	public void setTimeline(Map<String, String> timeline) {
		this.timeline = timeline;
	}
}
