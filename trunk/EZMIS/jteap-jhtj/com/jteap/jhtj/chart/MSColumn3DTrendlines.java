package com.jteap.jhtj.chart;

public class MSColumn3DTrendlines {
	private MSColumn3DLine mSColumn3DLine=new MSColumn3DLine();

	public MSColumn3DLine getMSColumn3DLine() {
		return mSColumn3DLine;
	}

	public void setMSColumn3DLine(MSColumn3DLine column3DLine) {
		mSColumn3DLine = column3DLine;
	}
	@Override
	public String toString() {
		String result="<trendlines>"+this.mSColumn3DLine.toString()+"</trendlines>";
		return result;
	}
}
