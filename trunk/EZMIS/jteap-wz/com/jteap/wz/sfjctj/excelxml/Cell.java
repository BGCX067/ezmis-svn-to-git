package com.jteap.wz.sfjctj.excelxml;

import org.apache.commons.lang.StringUtils;

public class Cell {
	private String styleID="s21";
	private Data data=new Data();
	public String toString(){
		StringBuffer result=new StringBuffer();
		result.append("<Cell ");
		if(StringUtils.isNotEmpty(this.styleID)){
			result.append("ss:StyleID='"+this.styleID+"' ");
		}
		result.append(">");
		result.append(data.toString());
		result.append("</Cell>");
		return result.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(0%3);
	}
}
