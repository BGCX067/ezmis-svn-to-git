package com.jteap.jhtj.chart;

public class MSLineApply {
	private String toObject="CAPTION";
	private String styles;
	public String getToObject() {
		return toObject;
	}
	public void setToObject(String toObject) {
		this.toObject = toObject;
	}
	public String getStyles() {
		return styles;
	}
	public void setStyles(String styles) {
		this.styles = styles;
	}
	
	@Override
	public String toString() {
		String result="<apply ";
		if(this.toObject!=null&&!this.toObject.equals("")){
			result=result+"toObject=\""+this.getToObject()+"\" ";
		}
		if(this.styles!=null&&!this.styles.equals("")){
			result=result+"styles=\""+this.getStyles()+"\" ";
		}
		result=result+"/>";
		return result;
	}
}
