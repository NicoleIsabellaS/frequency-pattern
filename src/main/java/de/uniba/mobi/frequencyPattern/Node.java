package de.uniba.mobi.frequencyPattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3723455680830001313L;
	private String hashmac;
	private List<TimeAreaPair> timeline = new ArrayList<>();

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

	public List<TimeAreaPair> getTimeline() {
		return timeline;
	}

	public void setTimeline(List<TimeAreaPair> timeline) {
		this.timeline = timeline;
	}
}
