package com.jteap.wz.sfjctj.excelxml;

import org.apache.commons.lang.StringUtils;

/**
 * 
 *描述：数据对象
 *时间：2010-8-9
 *作者：童贝
 *
 */
public class Data {
	//数据类型
	private String type;
	//值
	private String value;
	
	public String toString(){
		StringBuffer result=new StringBuffer();
		result.append("<Data ");
		if(StringUtils.isNotEmpty(this.type)){
			result.append("ss:Type='"+this.type+"' ");
		}
		result.append(">");
		if(StringUtils.isNotEmpty(this.value)){
			result.append(this.value);
		}
		result.append("</Data>");
		return result.toString();
	}
}
