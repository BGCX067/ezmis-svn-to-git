package com.jteap.index.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MSLineApplication {
	private List<MSLineApply> mSLineApplys=new ArrayList<MSLineApply>();

	public List<MSLineApply> getMSLineApplys() {
		return mSLineApplys;
	}

	public void setMSLineApplys(List<MSLineApply> lineApplys) {
		mSLineApplys = lineApplys;
	}
	
	@Override
	public String toString() {
		String result="<application>";
		Iterator<MSLineApply> it=this.mSLineApplys.iterator();
		while(it.hasNext()){
			MSLineApply msa=it.next();
			result=result+msa.toString();
		}
		result=result+"</application>";
		return result;
	}
	
}
