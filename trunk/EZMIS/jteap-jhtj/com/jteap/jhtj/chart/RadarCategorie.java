package com.jteap.jhtj.chart;

public class RadarCategorie {
	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		String result="<category ";
		if(null!=this.getLabel()&&!this.getLabel().equals("")){
			result=result+"label=\""+this.getLabel()+"\"";
		}
		result=result+" />";
		return result;
	}
}
