package com.jteap.index.chart;

public class MSLineCategory {
	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
	@Override
	public String toString() {
		String result="<category label=\""+this.getLabel()+"\" />";
		return result;
	}
}
