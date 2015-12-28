package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * 
 * 功能说明:categories
 * @author 童贝		
 * @version Jul 8, 2009
 */
public class DualYMSColumn3DAndLineCategories {
	private String fontSize;
	private List<DualYMSColumn3DAndLineCategorie> dualYMSColumn3DAndLineCategories=new ArrayList<DualYMSColumn3DAndLineCategorie>();
	
	public List<DualYMSColumn3DAndLineCategorie> getDualYMSColumn3DAndLineCategories() {
		return dualYMSColumn3DAndLineCategories;
	}

	public void setDualYMSColumn3DAndLineCategories(
			List<DualYMSColumn3DAndLineCategorie> dualYMSColumn3DAndLineCategories) {
		this.dualYMSColumn3DAndLineCategories = dualYMSColumn3DAndLineCategories;
	}

	@Override
	public String toString() {
		String result="<categories ";
		if(this.fontSize!=null&&!this.fontSize.equals("")){
			result=result+" fontSize=\""+this.fontSize+"\" ";
		}
		result=result+">";
		Iterator<DualYMSColumn3DAndLineCategorie> it=this.dualYMSColumn3DAndLineCategories.iterator();
		while(it.hasNext()){
			DualYMSColumn3DAndLineCategorie msc=it.next();
			result=result+msc.toString();
		}
		result=result+"</categories>";
		return result;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
}
