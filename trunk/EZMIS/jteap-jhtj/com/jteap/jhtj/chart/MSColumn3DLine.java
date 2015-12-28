package com.jteap.jhtj.chart;

public class MSColumn3DLine {
	private String startValue="26000"; 
	private String color="91C728";
	private String displayValue="";
	private String showOnTop="1";
	public String getStartValue() {
		return startValue;
	}
	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDisplayValue() {
		return displayValue;
	}
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	public String getShowOnTop() {
		return showOnTop;
	}
	public void setShowOnTop(String showOnTop) {
		this.showOnTop = showOnTop;
	}
	@Override
	public String toString() {
		String result="";
		if(this.getStartValue()!=null&&!this.getStartValue().equals("")){
			result=result+" startValue=\""+this.getStartValue()+"\" ";
		}
		if(this.getColor()!=null&&!this.getColor().equals("")){
			result=result+" color=\""+this.getColor()+"\" ";
		}
		if(this.getDisplayValue()!=null&&!this.getDisplayValue().equals("")){
			result=result+" displayValue=\""+this.getDisplayValue()+"\" ";
		}
		if(this.getShowOnTop()!=null&&!this.getShowOnTop().equals("")){
			result=result+" showOnTop=\""+this.getShowOnTop()+"\" ";
		}
		result="<line"+result+"/>";
		return result;
	}
}
