package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * 功能说明:制作雷达图
 * @author 童贝		
 * @version Aug 11, 2009
 */
public class RadarChart {
	private String caption;
	private String bgColor;
	private String radarFillColor;
	private String plotFillAlpha;
	private String plotBorderThickness;//网格的数量
	private String anchorAlpha;
	private String numberPrefix;
	private String numDivLines;
	private String legendPosition;//数据的标题的位置
	private String showAboutMenuItem;//关于
	private String aboutMenuItemLabel;
	private String aboutMenuItemLink;
	private String baseFontSize;
	private String legendMarkerCircle;
	private String showLegend;
	private String showPlotBorder;//是否显示形成的图形
	private String showRadarBorder;//是否显示最外层边框
	private String radarBorderThickness;//最外层边框的厚度
	private String adjustDiv;
	private String yAxisValuesStep;
	private String legendAllowDrag;
	private String formatNumberScale;
	
	private RadarCategories radarCategories=new RadarCategories();
	
	private List<RadarDataSet> radarDataSets=new ArrayList<RadarDataSet>();
	
	private MSLineStyles mSLineStyles=new MSLineStyles();
	
	public MSLineStyles getMSLineStyles() {
		return mSLineStyles;
	}
	public void setMSLineStyles(MSLineStyles lineStyles) {
		mSLineStyles = lineStyles;
	}
	public List<RadarDataSet> getRadarDataSets() {
		return radarDataSets;
	}
	public void setRadarDataSets(List<RadarDataSet> radarDataSets) {
		this.radarDataSets = radarDataSets;
	}
	public RadarCategories getRadarCategories() {
		return radarCategories;
	}
	public void setRadarCategories(RadarCategories radarCategories) {
		this.radarCategories = radarCategories;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getBgColor() {
		return bgColor;
	}
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	public String getRadarFillColor() {
		return radarFillColor;
	}
	public void setRadarFillColor(String radarFillColor) {
		this.radarFillColor = radarFillColor;
	}
	public String getPlotFillAlpha() {
		return plotFillAlpha;
	}
	public void setPlotFillAlpha(String plotFillAlpha) {
		this.plotFillAlpha = plotFillAlpha;
	}
	public String getPlotBorderThickness() {
		return plotBorderThickness;
	}
	public void setPlotBorderThickness(String plotBorderThickness) {
		this.plotBorderThickness = plotBorderThickness;
	}
	public String getAnchorAlpha() {
		return anchorAlpha;
	}
	public void setAnchorAlpha(String anchorAlpha) {
		this.anchorAlpha = anchorAlpha;
	}
	public String getNumberPrefix() {
		return numberPrefix;
	}
	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}
	public String getNumDivLines() {
		return numDivLines;
	}
	public void setNumDivLines(String numDivLines) {
		this.numDivLines = numDivLines;
	}
	public String getLegendPosition() {
		return legendPosition;
	}
	public void setLegendPosition(String legendPosition) {
		this.legendPosition = legendPosition;
	}
	
	@Override
	public String toString() {
		String result="<chart ";
		if(this.caption!=null&&!this.caption.equals("")){
			result=result+"caption=\""+this.getCaption()+"\" ";
		}
		if(this.anchorAlpha!=null&&!this.anchorAlpha.equals("")){
			result=result+"anchorAlpha=\""+this.anchorAlpha+"\" ";
		}
		if(this.bgColor!=null&&!this.bgColor.equals("")){
			result=result+"bgColor=\""+this.bgColor+"\" ";
		}
		if(this.legendPosition!=null&&!this.legendPosition.equals("")){
			result=result+"legendPosition=\""+this.legendPosition+"\" ";
		}
		if(this.numberPrefix!=null&&!this.numberPrefix.equals("")){
			result=result+"numberPrefix=\""+this.numberPrefix+"\" ";
		}
		if(this.numDivLines!=null&&!this.numDivLines.equals("")){
			result=result+"numDivLines=\""+this.numDivLines+"\" ";
		}
		if(this.plotBorderThickness!=null&&!this.plotBorderThickness.equals("")){
			result=result+"plotBorderThickness=\""+this.plotBorderThickness+"\" ";
		}
		if(this.plotFillAlpha!=null&&!this.plotFillAlpha.equals("")){
			result=result+"plotFillAlpha=\""+this.plotFillAlpha+"\" ";
		}
		if(this.radarFillColor!=null&&!this.radarFillColor.equals("")){
			result=result+"radarFillColor=\""+this.radarFillColor+"\" ";
		}
		if(this.showAboutMenuItem!=null&&!this.showAboutMenuItem.equals("")){
			result=result+"showAboutMenuItem=\""+this.showAboutMenuItem+"\" ";
		}
		if(this.aboutMenuItemLabel!=null&&!this.aboutMenuItemLabel.equals("")){
			result=result+"aboutMenuItemLabel=\""+this.aboutMenuItemLabel+"\" ";
		}
		if(this.aboutMenuItemLink!=null&&!this.aboutMenuItemLink.equals("")){
			result=result+"aboutMenuItemLink=\""+this.aboutMenuItemLink+"\" ";
		}
		if(this.baseFontSize!=null&&!this.baseFontSize.equals("")){
			result=result+"baseFontSize=\""+this.baseFontSize+"\" ";
		}
		if(this.legendMarkerCircle!=null&&!this.legendMarkerCircle.equals("")){
			result=result+"legendMarkerCircle=\""+this.legendMarkerCircle+"\" ";
		}
		if(this.showLegend!=null&&!this.showLegend.equals("")){
			result=result+"showLegend=\""+this.showLegend+"\" ";
		}
		if(this.showPlotBorder!=null&&!this.showPlotBorder.equals("")){
			result=result+"showPlotBorder=\""+this.showPlotBorder+"\" ";
		}
		if(this.showRadarBorder!=null&&!this.showRadarBorder.equals("")){
			result=result+"showRadarBorder=\""+this.showRadarBorder+"\" ";
		}
		if(this.radarBorderThickness!=null&&!this.radarBorderThickness.equals("")){
			result=result+"radarBorderThickness=\""+this.radarBorderThickness+"\" ";
		}
		if(this.adjustDiv!=null&&!this.adjustDiv.equals("")){
			result=result+"adjustDiv=\""+this.adjustDiv+"\" ";
		}
		if(this.yAxisValuesStep!=null&&!this.yAxisValuesStep.equals("")){
			result=result+"yAxisValuesStep=\""+this.yAxisValuesStep+"\" ";
		}
		if(this.legendAllowDrag!=null&&!this.legendAllowDrag.equals("")){
			result=result+"legendAllowDrag=\""+this.legendAllowDrag+"\" ";
		}
		if(this.formatNumberScale!=null&&!this.formatNumberScale.equals("")){
			result=result+"formatNumberScale=\""+this.formatNumberScale+"\" ";
		}
		result=result+">";
		
		result=result+this.radarCategories.toString();
		
		Iterator<RadarDataSet> it=this.radarDataSets.iterator();
		while(it.hasNext()){
			RadarDataSet radarDataSet=it.next();
			result=result+radarDataSet.toString();
		}
		
		result=result+this.mSLineStyles.toString();
		
		result=result+"</chart>";
		return result;
	}
	public String getShowAboutMenuItem() {
		return showAboutMenuItem;
	}
	public void setShowAboutMenuItem(String showAboutMenuItem) {
		this.showAboutMenuItem = showAboutMenuItem;
	}
	public String getAboutMenuItemLabel() {
		return aboutMenuItemLabel;
	}
	public void setAboutMenuItemLabel(String aboutMenuItemLabel) {
		this.aboutMenuItemLabel = aboutMenuItemLabel;
	}
	public String getAboutMenuItemLink() {
		return aboutMenuItemLink;
	}
	public void setAboutMenuItemLink(String aboutMenuItemLink) {
		this.aboutMenuItemLink = aboutMenuItemLink;
	}
	public String getBaseFontSize() {
		return baseFontSize;
	}
	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}
	public String getLegendMarkerCircle() {
		return legendMarkerCircle;
	}
	public void setLegendMarkerCircle(String legendMarkerCircle) {
		this.legendMarkerCircle = legendMarkerCircle;
	}
	public String getShowLegend() {
		return showLegend;
	}
	public void setShowLegend(String showLegend) {
		this.showLegend = showLegend;
	}
	public String getShowPlotBorder() {
		return showPlotBorder;
	}
	public void setShowPlotBorder(String showPlotBorder) {
		this.showPlotBorder = showPlotBorder;
	}
	public String getShowRadarBorder() {
		return showRadarBorder;
	}
	public void setShowRadarBorder(String showRadarBorder) {
		this.showRadarBorder = showRadarBorder;
	}
	public String getRadarBorderThickness() {
		return radarBorderThickness;
	}
	public void setRadarBorderThickness(String radarBorderThickness) {
		this.radarBorderThickness = radarBorderThickness;
	}
	public String getAdjustDiv() {
		return adjustDiv;
	}
	public void setAdjustDiv(String adjustDiv) {
		this.adjustDiv = adjustDiv;
	}
	public String getYAxisValuesStep() {
		return yAxisValuesStep;
	}
	public void setYAxisValuesStep(String axisValuesStep) {
		yAxisValuesStep = axisValuesStep;
	}
	public String getLegendAllowDrag() {
		return legendAllowDrag;
	}
	public void setLegendAllowDrag(String legendAllowDrag) {
		this.legendAllowDrag = legendAllowDrag;
	}
	public String getFormatNumberScale() {
		return formatNumberScale;
	}
	public void setFormatNumberScale(String formatNumberScale) {
		this.formatNumberScale = formatNumberScale;
	}
}
