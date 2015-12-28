package com.jteap.wz.sfjctj.excelxml;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *描述：导出入口
 *时间：2010-8-9
 *作者：童贝
 *
 */
public class Workbook {
	private List<Worksheet> sheets=new ArrayList<Worksheet>();
	private String head="<?xml version='1.0'?><?mso-application progid='Excel.Sheet'?><Workbook xmlns='urn:schemas-microsoft-com:office:spreadsheet' xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns:ss='urn:schemas-microsoft-com:office:spreadsheet' xmlns:html='http://www.w3.org/TR/REC-html40'><DocumentProperties xmlns='urn:schemas-microsoft-com:office:office'><Created>1996-12-17T01:32:42Z</Created><LastSaved>2010-07-22T07:41:04Z</LastSaved><Version>11.5606</Version></DocumentProperties><OfficeDocumentSettings xmlns='urn:schemas-microsoft-com:office:office'><RemovePersonalInformation/></OfficeDocumentSettings><ExcelWorkbook xmlns='urn:schemas-microsoft-com:office:excel'><WindowHeight>4530</WindowHeight><WindowWidth>8505</WindowWidth><WindowTopX>480</WindowTopX><WindowTopY>120</WindowTopY><AcceptLabelsInFormulas/><ProtectStructure>False</ProtectStructure><ProtectWindows>False</ProtectWindows></ExcelWorkbook><Styles><Style ss:ID='Default' ss:Name='Normal'><Alignment ss:Vertical='Bottom'/><Borders/><Font ss:FontName='宋体' x:CharSet='134' ss:Size='12'/><Interior/><NumberFormat/><Protection/></Style><Style ss:ID='s21'><NumberFormat ss:Format='@'/></Style><Style ss:ID='s22'><NumberFormat ss:Format='yyyy/m/d\\ h:mm;@'/></Style></Styles>";
	
	public List<Worksheet> getSheets() {
		return sheets;
	}

	public void setSheets(List<Worksheet> sheets) {
		this.sheets = sheets;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String toString(){
		StringBuffer result=new StringBuffer();
		result.append(this.head);
		for(Worksheet sheet:sheets){
			result.append(sheet.toString());
		}
		result.append("</Workbook>");
		return null;
	}
}
