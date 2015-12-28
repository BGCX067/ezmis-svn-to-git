package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MSLineDataSet {
	String seriesName;
	String color;//="1D8BD1";
	String anchorBorderColor;//="1D8BD1";
	String anchorBgColor;//="1D8BD1";
	
	List<MSLineSet> mSLineSets=new ArrayList<MSLineSet>();

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAnchorBorderColor() {
		return anchorBorderColor;
	}

	public void setAnchorBorderColor(String anchorBorderColor) {
		this.anchorBorderColor = anchorBorderColor;
	}

	public String getAnchorBgColor() {
		return anchorBgColor;
	}

	public void setAnchorBgColor(String anchorBgColor) {
		this.anchorBgColor = anchorBgColor;
	}

	public List<MSLineSet> getMSLineSets() {
		return mSLineSets;
	}

	public void setMSLineSets(List<MSLineSet> lineSets) {
		mSLineSets = lineSets;
	}
	
	@Override
	public String toString() {
		String result="<dataset ";
		if(this.seriesName!=null&&!this.seriesName.equals("")){
			result=result+"seriesName=\""+this.getSeriesName()+"\" ";
		}
		if(this.color!=null&&!this.color.equals("")){
			result=result+"color=\""+this.getColor()+"\" ";
		}
		if(this.anchorBorderColor!=null&&!this.anchorBorderColor.equals("")){
			result=result+"anchorBorderColor=\""+this.getAnchorBorderColor()+"\" ";
		}
		if(this.anchorBgColor!=null&&!this.anchorBgColor.equals("")){
			result=result+"anchorBgColor=\""+this.getAnchorBgColor()+"\" ";
		}
		result=result+">";
		Iterator<MSLineSet> it=this.getMSLineSets().iterator();
		while(it.hasNext()){
			MSLineSet msc=it.next();
			result=result+msc.toString();
		}
		result=result+"</dataset>";
		return result;
	}
}
