package com.jteap.jhtj.sjxxxdy.web;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.sjxxxdy.manager.ImportExcelManager;
import com.jteap.jhtj.sjxxxdy.manager.TjInfoItemManager;
import com.jteap.jhtj.sjxxxdy.model.ExcelModel;
@SuppressWarnings({ "unchecked", "serial" })
public class ImportExcelAction extends AbstractAction {
	private TjInfoItemManager tjInfoItemManager;
	private ImportExcelManager importExcelManager;
	private File[] uploads;
	private String[] uploadContentTypes;
	private String[] uploadFileNames;
	
	private String sheetNum;
	private String row;
	private String name;
	private String cname;
	private String dw;
	private String sjfz;
	private String cexp;
	private String corder;
	
	public String importAction() throws Exception{
		ExcelModel excel=new ExcelModel();
		String kid=request.getParameter("kid");
		if(StringUtils.isNotEmpty(kid)){
			excel.setKid(kid);
		}
		if(StringUtils.isNotEmpty(sheetNum)){
			excel.setSheetNum(new Integer(sheetNum));
		}
		if(StringUtils.isNotEmpty(row)){
			excel.setRow(new Integer(row));
		}
		if(StringUtils.isNotEmpty(name)){
			excel.setName(new Integer(name));
		}
		if(StringUtils.isNotEmpty(cname)){
			excel.setCname(new Integer(cname));
		}
		if(StringUtils.isNotEmpty(dw)){
			excel.setDw(new Integer(dw));
		}
		if(StringUtils.isNotEmpty(sjfz)){
			excel.setSjfz(new Integer(sjfz));
		}
		if(StringUtils.isNotEmpty(cexp)){
			excel.setCexp(new Integer(cexp));
		}
		if(StringUtils.isNotEmpty(corder)){
			excel.setCorder(new Integer(corder));
		}
		if(uploads.length>0){
			excel.setFile(uploads[0]);
		}
		this.importExcelManager.importExcel(excel);
		return NONE;
	}

	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	public File[] getUpload() {
		return uploads;
	}


	public void setUpload(File[] uploads) {
		this.uploads = uploads;
	}


	public String[] getUploadContentType() {
		return uploadContentTypes;
	}


	public void setUploadContentType(String[] uploadContentTypes) {
		this.uploadContentTypes = uploadContentTypes;
	}


	public String[] getUploadFileName() {
		return uploadFileNames;
	}


	public void setUploadFileName(String[] uploadFileNames) {
		this.uploadFileNames = uploadFileNames;
	}

	public TjInfoItemManager getTjInfoItemManager() {
		return tjInfoItemManager;
	}

	public void setTjInfoItemManager(TjInfoItemManager tjInfoItemManager) {
		this.tjInfoItemManager = tjInfoItemManager;
	}

	public String getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(String sheetNum) {
		this.sheetNum = sheetNum;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public String getSjfz() {
		return sjfz;
	}

	public void setSjfz(String sjfz) {
		this.sjfz = sjfz;
	}

	public String getCexp() {
		return cexp;
	}

	public void setCexp(String cexp) {
		this.cexp = cexp;
	}

	public String getCorder() {
		return corder;
	}

	public void setCorder(String corder) {
		this.corder = corder;
	}

	public ImportExcelManager getImportExcelManager() {
		return importExcelManager;
	}

	public void setImportExcelManager(ImportExcelManager importExcelManager) {
		this.importExcelManager = importExcelManager;
	}
}
