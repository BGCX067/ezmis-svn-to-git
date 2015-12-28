package com.jteap.jhtj.chart;
/**
 * 
 * 功能说明:
 * @author 童贝		
 * @version May 11, 2009
 */
public class MSLineStyle {
	private String name; 
	private String type="font";
	private String size="12";
	private String bold;
	private String color;
	private String param; 
	private String start;
	private String duration;
	
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
		
	@Override
	public String toString() {
		String result="<style ";
		if(this.name!=null&&!this.name.equals("")){
			result=result+"name=\""+this.getName()+"\" ";
		}
		if(this.size!=null&&!this.size.equals("")){
			result=result+"size=\""+this.getSize()+"\" ";
		}
		if(this.type!=null&&!this.type.equals("")){
			result=result+"type=\""+this.getType()+"\" ";
		}
		if(this.color!=null&&!this.color.equals("")){
			result=result+"color=\""+this.getColor()+"\" ";
		}
		if(this.bold!=null&&!this.bold.equals("")){
			result=result+"bold=\""+this.getBold()+"\" ";
		}
		if(this.duration!=null&&!this.duration.equals("")){
			result=result+"duration=\""+this.getDuration()+"\" ";
		}
		if(this.param!=null&&!this.param.equals("")){
			result=result+"param=\""+this.getParam()+"\" ";
		}
		if(this.start!=null&&!this.start.equals("")){
			result=result+"start=\""+this.getStart()+"\" ";
		}
		result=result+"/>";
		return result;
	}
	public String getBold() {
		return bold;
	}
	public void setBold(String bold) {
		this.bold = bold;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
