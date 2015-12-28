package com.jteap.wz.sfjctj.excelxml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 
 *描述：表格对象
 *时间：2010-8-9
 *作者：童贝
 *
 */
public class Table {
	//列数(必填)
	private String expandedColumnCount;
	//行数(必填)
	private String expandedRowCount;
	//填充列(选填)
	private String fullColumns="1";
	//填充行(选填)
	private String fullRows="1";
	//默认列宽(选填)
	private String defaultColumnWidth="54";
	//默认行高(选填)
	private String defaultRowHeight="14.25";
	
	private List<Column> cols=new ArrayList<Column>();
	
	private List<Row> rows=new ArrayList<Row>();
	
	public String toString(){
		StringBuffer result=new StringBuffer();
		result.append("<Table ");
		if(StringUtils.isNotEmpty(this.expandedColumnCount)){
			result.append("ss:ExpandedColumnCount='"+this.expandedColumnCount+"' ");
		}
		if(StringUtils.isNotEmpty(this.expandedRowCount)){
			result.append("ss:ExpandedRowCount='"+this.expandedRowCount+"' ");
		}
		if(StringUtils.isNotEmpty(this.fullColumns)){
			result.append("x:FullColumns='"+this.fullColumns+"' ");
		}
		if(StringUtils.isNotEmpty(this.fullRows)){
			result.append("x:FullRows='"+this.fullRows+"' ");
		}
		if(StringUtils.isNotEmpty(this.defaultColumnWidth)){
			result.append("ss:DefaultColumnWidth='"+this.defaultColumnWidth+"' ");
		}
		if(StringUtils.isNotEmpty(this.defaultRowHeight)){
			result.append("ss:DefaultRowHeight='"+this.defaultRowHeight+"' ");
		}
		result.append(">");
		for(Column column:cols){
			result.append(column.toString());
		}
		
		for(Row row:rows){
			result.append(row.toString());
		}
		result.append("</Table>");
		return result.toString();
	}
}
