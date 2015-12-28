package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DualYMSColumn3DAndLineDataSet {
	private String seriesName;
	private String parentYAxis;
	private List<DualYMSColumn3DAndLineSet> dualYMSColumn3DAndLineSets=new ArrayList<DualYMSColumn3DAndLineSet>();

	public String getParentYAxis() {
		return parentYAxis;
	}

	public void setParentYAxis(String parentYAxis) {
		this.parentYAxis = parentYAxis;
	}

	public List<DualYMSColumn3DAndLineSet> getDualYMSColumn3DAndLineSets() {
		return dualYMSColumn3DAndLineSets;
	}

	public void setDualYMSColumn3DAndLineSets(
			List<DualYMSColumn3DAndLineSet> dualYMSColumn3DAndLineSets) {
		this.dualYMSColumn3DAndLineSets = dualYMSColumn3DAndLineSets;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	
	@Override
	public String toString() {
		String result="<dataset ";
		if(this.seriesName!=null&&!this.seriesName.equals("")){
			result=result+"seriesName=\""+this.getSeriesName()+"\" ";
		}
		if(this.parentYAxis!=null&&!this.parentYAxis.equals("")){
			result=result+"parentYAxis=\""+this.parentYAxis+"\" ";
		}
		result=result+">";
		
		Iterator<DualYMSColumn3DAndLineSet> it=this.dualYMSColumn3DAndLineSets.iterator();
		while(it.hasNext()){
			DualYMSColumn3DAndLineSet msc=it.next();
			result=result+msc.toString();
		}
		result=result+"</dataset>";
		return result;
	}
	
}
