package com.jteap.wz.util;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;

@SuppressWarnings("unchecked")
public class ExcelUtil {
	
	
	public static void addRow(int rowNum,int rowHight,HSSFRow row,HSSFCell cell,HSSFSheet sheet,List<Map> cellList,boolean isNew,int cellStar){
		if(isNew){
			row = sheet.createRow(rowNum);
		}else{
			row = sheet.getRow(rowNum);
		}
		
		row.setHeight((short) rowHight);// 设定行的高度
//		System.out.println();
//		System.out.println("row:"+rowNum);
//		System.out.println("size:"+cellList.size());
		if(cellList!=null){
			ExcelUtil.addCell(row, cell, sheet, cellList,cellStar);
		}
//		cell = row.createCell(0);
//		cell.setCellValue("物资档案");
//		//cell.setCellStyle(titleStyle);// 设置单元格样式
//		sheet.addMergedRegion(address);
	}
	
	public static void addCell(HSSFRow row,HSSFCell cell,HSSFSheet sheet,List<Map> cellList,int cellStar){
		for(int i = 0;i<cellList.size();i++){
			Map map = cellList.get(i);
			cell = row.createCell((i+cellStar));
//			System.out.println("col:"+(i+cellStar));
//			System.out.println("value:"+map.get("value"));
			if(map.get("value")!=null){
				cell.setCellValue((String)map.get("value"));
			}
			if(map.get("address")!=null){
				sheet.addMergedRegion((CellRangeAddress)map.get("address"));
			}
			if(map.get("style")!=null){
				cell.setCellStyle((HSSFCellStyle)map.get("style"));
			}
		}
	}
}
