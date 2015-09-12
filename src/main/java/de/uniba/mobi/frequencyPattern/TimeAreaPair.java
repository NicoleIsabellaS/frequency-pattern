package de.uniba.mobi.frequencyPattern;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TimeAreaPair implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 212650605054811872L;
	private LocalDateTime timestamp;
	private Area area;

	public TimeAreaPair() {
		super();
	}

	public TimeAreaPair(LocalDateTime timestamp, Area area) {
		this.timestamp = timestamp;
		this.area = area;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "TimeAreaPair [timestamp=" + timestamp + ", area=" + area + "]";
	}
}
