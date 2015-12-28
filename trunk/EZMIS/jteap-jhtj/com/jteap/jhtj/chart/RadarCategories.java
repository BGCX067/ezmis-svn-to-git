package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RadarCategories {
	private String font;
	private String fontSize;
	private List<RadarCategorie> radarCategories=new ArrayList<RadarCategorie>();
	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public String getFontSize() {
		return fontSize;
	}
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
	public List<RadarCategorie> getRadarCategories() {
		return radarCategories;
	}
	public void setRadarCategories(List<RadarCategorie> radarCategories) {
		this.radarCategories = radarCategories;
	}
	
	@Override
	public String toString(){
		String result="<categories ";
		if(this.font!=null&&!this.font.equals("")){
			result=result+"font=\""+this.font+"\" ";
		}
		if(this.fontSize!=null&&!this.fontSize.equals("")){
			result=result+"fontSize=\""+this.fontSize+"\" ";
		}
		result=result+">";
		Iterator<RadarCategorie> it=this.radarCategories.iterator();
		while(it.hasNext()){
			RadarCategorie radarCategorie=it.next();
			result=result+radarCategorie.toString();
		}
		result=result+"</categories>";
		return result;
	}
}
