package com.jteap.jhtj.chart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MSColumn3DCategories {
	private String fontSize;
	private List<MSColumn3DCategory> mSColumn3DCategorys=new ArrayList<MSColumn3DCategory>();

	public List<MSColumn3DCategory> getMSColumn3DCategorys() {
		return mSColumn3DCategorys;
	}

	public void setMSColumn3DCategorys(List<MSColumn3DCategory> column3DCategorys) {
		mSColumn3DCategorys = column3DCategorys;
	}
	
	@Override
	public String toString() {
		String result="<categories ";
		if(this.fontSize!=null&&!this.fontSize.equals("")){
			result=result+"fontSize=\""+this.fontSize+"\" ";
		}
		result=result+">";
		Iterator<MSColumn3DCategory> it=this.mSColumn3DCategorys.iterator();
		while(it.hasNext()){
			MSColumn3DCategory msc=it.next();
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
