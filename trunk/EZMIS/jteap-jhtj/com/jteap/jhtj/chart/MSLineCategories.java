package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MSLineCategories {
	List<MSLineCategory> categorys=new ArrayList<MSLineCategory>();

	public List<MSLineCategory> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<MSLineCategory> categorys) {
		this.categorys = categorys;
	}
	
	@Override
	public String toString() {
		String result="<categories>";
		Iterator<MSLineCategory> it=this.getCategorys().iterator();
		while(it.hasNext()){
			MSLineCategory msc=it.next();
			result=result+msc.toString();
		}
		result=result+"</categories>";
		return result;
	}
}
