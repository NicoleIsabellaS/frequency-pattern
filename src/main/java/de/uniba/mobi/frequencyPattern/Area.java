package de.uniba.mobi.frequencyPattern;

public class Area {

	private String beamzone;

	public Area() {
		super();
	}

	public Area(String beamzone) {
		this.beamzone = beamzone;
	}

	public String getBeamzone() {
		return beamzone;
	}

	public void setBeamzone(String beamzone) {
		this.beamzone = beamzone;
	}

	@Override
	public String toString() {
		return "Area [beamzone=" + beamzone + "]";
	}
}
