package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RadarDataSet {
	private String seriesname;
	private String color;
	private String anchorSides;
	private String anchorRadius;
	private String anchorBorderColor;
	private String anchorBgAlpha;
	private String showValues;
	
	private List<RadarSet> radarSets=new ArrayList<RadarSet>();
	public List<RadarSet> getRadarSets() {
		return radarSets;
	}
	public void setRadarSets(List<RadarSet> radarSets) {
		this.radarSets = radarSets;
	}
	public String getSeriesname() {
		return seriesname;
	}
	public void setSeriesname(String seriesname) {
		this.seriesname = seriesname;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getAnchorSides() {
		return anchorSides;
	}
	public void setAnchorSides(String anchorSides) {
		this.anchorSides = anchorSides;
	}
	public String getAnchorRadius() {
		return anchorRadius;
	}
	public void setAnchorRadius(String anchorRadius) {
		this.anchorRadius = anchorRadius;
	}
	public String getAnchorBorderColor() {
		return anchorBorderColor;
	}
	public void setAnchorBorderColor(String anchorBorderColor) {
		this.anchorBorderColor = anchorBorderColor;
	}
	public String getAnchorBgAlpha() {
		return anchorBgAlpha;
	}
	public void setAnchorBgAlpha(String anchorBgAlpha) {
		this.anchorBgAlpha = anchorBgAlpha;
	}
	
	@Override
	public String toString() {
		String result="<dataset ";
		if(this.seriesname!=null&&!this.seriesname.equals("")){
			result=result+"seriesname=\""+this.getSeriesname()+"\" ";
		}
		if(this.color!=null&&!this.color.equals("")){
			result=result+"color=\""+this.color+"\" ";
		}
		if(this.anchorBgAlpha!=null&&!this.anchorBgAlpha.equals("")){
			result=result+"anchorBgAlpha=\""+this.anchorBgAlpha+"\" ";
		}
		if(this.anchorBorderColor!=null&&!this.anchorBorderColor.equals("")){
			result=result+"anchorBorderColor=\""+this.anchorBorderColor+"\" ";
		}
		if(this.anchorRadius!=null&&!this.anchorRadius.equals("")){
			result=result+"anchorRadius=\""+this.anchorRadius+"\" ";
		}
		if(this.anchorSides!=null&&!this.anchorSides.equals("")){
			result=result+"anchorSides=\""+this.anchorSides+"\" ";
		}
		if(this.showValues!=null&&!this.showValues.equals("")){
			result=result+"showValues=\""+this.showValues+"\" ";
		}
		result=result+">";
		
		Iterator<RadarSet> it=this.radarSets.iterator();
		while(it.hasNext()){
			RadarSet msc=it.next();
			result=result+msc.toString();
		}
		result=result+"</dataset>";
		return result;
	}
	public String getShowValues() {
		return showValues;
	}
	public void setShowValues(String showValues) {
		this.showValues = showValues;
	}
}
