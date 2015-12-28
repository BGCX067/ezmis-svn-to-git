package com.jteap.wz.sfjctj.excelxml;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *描述：行
 *时间：2010-8-9
 *作者：童贝
 *
 */
public class Row {
	private List<Cell> cells=new ArrayList<Cell>();
	
	public String toString(){
		StringBuffer result=new StringBuffer();
		result.append("<Row>");
		for(Cell cell:cells){
			result.append(cell.toString());
		}
		result.append("</Row>");
		return result.toString();
	}
}
