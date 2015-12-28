package com.jteap.jhtj.chart;

public class MSLineStyles {
	private MSLineDefinition mSLineDefinition=new MSLineDefinition();
	private MSLineApplication mSLineApplication=new MSLineApplication();

	public MSLineApplication getMSLineApplication() {
		return mSLineApplication;
	}

	public void setMSLineApplication(MSLineApplication lineApplication) {
		mSLineApplication = lineApplication;
	}

	public MSLineDefinition getMSLineDefinition() {
		return mSLineDefinition;
	}

	public void setMSLineDefinition(MSLineDefinition lineDefinition) {
		mSLineDefinition = lineDefinition;
	}
	
	
	@Override
	public String toString() {
		String result="<styles>";
		result=result+this.mSLineDefinition.toString();
		
		result=result+this.mSLineApplication.toString();
		
		result=result+"</styles>";
		return result;
	}
}
