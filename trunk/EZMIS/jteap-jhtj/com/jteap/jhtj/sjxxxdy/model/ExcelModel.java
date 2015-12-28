package com.jteap.jhtj.sjxxxdy.model;

import java.io.File;

public class ExcelModel {
	private String kid;//分类编码
	private Integer sheetNum;
	private Integer row;//从第几行开始读取数据
	private Integer name;//数据项编码
	private Integer cname;//数据项名称
	private Integer dw;//单位
	private Integer sjfz;//数据分组
	private File file;//绝对路径
	private Integer cexp;//计算公式号
	private Integer corder;//计算顺序号
	public Integer getCexp() {
		return cexp;
	}
	public void setCexp(Integer cexp) {
		this.cexp = cexp;
	}
	public Integer getCorder() {
		return corder;
	}
	public void setCorder(Integer corder) {
		this.corder = corder;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Integer getName() {
		return name;
	}
	public void setName(Integer name) {
		this.name = name;
	}
	public Integer getCname() {
		return cname;
	}
	public void setCname(Integer cname) {
		this.cname = cname;
	}
	public Integer getDw() {
		return dw;
	}
	public void setDw(Integer dw) {
		this.dw = dw;
	}
	public Integer getSjfz() {
		return sjfz;
	}
	public void setSjfz(Integer sjfz) {
		this.sjfz = sjfz;
	}
	public Integer getSheetNum() {
		return sheetNum;
	}
	public void setSheetNum(Integer sheetNum) {
		this.sheetNum = sheetNum;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
}
