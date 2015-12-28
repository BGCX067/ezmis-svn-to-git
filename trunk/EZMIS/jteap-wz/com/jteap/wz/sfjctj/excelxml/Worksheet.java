package com.jteap.wz.sfjctj.excelxml;

import org.apache.commons.lang.StringUtils;

/**
 * 
 *描述：excel页信息
 *时间：2010-8-9
 *作者：童贝
 *
 */
public class Worksheet {
	//页名
	private String name;
	private Table table=new Table();
	private WorksheetOptions worksheetOptions=new WorksheetOptions();
	
	public String toString(){
		StringBuffer result=new StringBuffer();
		result.append("<Worksheet ");
		if(StringUtils.isNotEmpty(name)){
			result.append("ss:Name='"+name+"'");
		}
		result.append(">");
		
		result.append(table.toString());
		result.append(worksheetOptions.toString());
		result.append("</Worksheet>");
		return result.toString();
	}
}
