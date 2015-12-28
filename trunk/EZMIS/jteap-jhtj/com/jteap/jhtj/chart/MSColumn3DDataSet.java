package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MSColumn3DDataSet {
	private String seriesName;
	private String color;
	private String alpha;
	private String showValues;
	private String includeInLegend;
	
	private List<MSColumn3DSet> mSColumn3DSets=new ArrayList<MSColumn3DSet>();

	public List<MSColumn3DSet> getMSColumn3DSets() {
		return mSColumn3DSets;
	}

	public void setMSColumn3DSets(List<MSColumn3DSet> column3DSets) {
		mSColumn3DSets = column3DSets;
	}
	
	@Override
	public String toString() {
		String result="<dataset ";
		if(this.seriesName!=null&&!this.seriesName.equals("")){
			result=result+"seriesName=\""+this.getSeriesName()+"\" ";
		}
		if(this.color!=null&&!this.color.equals("")){
			result=result+"color=\""+this.color+"\" ";
		}
		if(this.showValues!=null&&!this.showValues.equals("")){
			result=result+"showValues=\""+this.showValues+"\" ";
		}
		if(this.alpha!=null&&!this.alpha.equals("")){
			result=result+"alpha=\""+this.alpha+"\" ";
		}
		if(this.includeInLegend!=null&&!this.includeInLegend.equals("")){
			result=result+"includeInLegend=\""+this.includeInLegend+"\" ";
		}
		result=result+">";
		
		Iterator<MSColumn3DSet> it=this.mSColumn3DSets.iterator();
		while(it.hasNext()){
			MSColumn3DSet msc=it.next();
			result=result+msc.toString();
		}
		result=result+"</dataset>";
		return result;
	}

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

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public String getShowValues() {
		return showValues;
	}

	public void setShowValues(String showValues) {
		this.showValues = showValues;
	}

	public String getIncludeInLegend() {
		return includeInLegend;
	}

	public void setIncludeInLegend(String includeInLegend) {
		this.includeInLegend = includeInLegend;
	}
}
