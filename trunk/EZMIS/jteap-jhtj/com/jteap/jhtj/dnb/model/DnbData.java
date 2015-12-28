package com.jteap.jhtj.dnb.model;

import java.util.Date;

/**
 * 
 * 描述:电能表数据用来封装结果值
 * 时间:2010 11 9
 * 作者:tngbei
 * 参数:
 * 返回值:
 * 抛出异常:
 */
public class DnbData {
	private Integer id;
	private Double pz;   //正向有功
	private Double pf;	 //反向有功
	private Double qz;	 //正向无功
	private Double qf;	 //反向无功
	
	private Double brpz; //本日正向有功
	private Double brpf; //本日反向有功
	private Double brqz; //本日正向无功
	private Double brqf; //本日反向无功
	
	private Double bypz; //本月正向有功
	private Double bypf; //本月反向有功
	private Double byqz; //本月正向无功
	private Double byqf; //本月反向无功
	
	//所有的电能表的ID
	public static final String DNB_CODES="19,25,20,26,15,30,16,28,17,29,18,21,22,23,24,31,3,4,5,6,11,12,7,8,9,10,13,14,1,2";
	public static final int[] DNB_ROWS={5,9,13,17,21,25,29,33,37,41,45,49,53,57,61,65,73,77,81,85,89,93,97,101,105,109,113,117,121,125};
	
	private Date times;  //时间
	public Date getTimes() {
		return times;
	}
	public void setTimes(Date times) {
		this.times = times;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getPz() {
		return pz;
	}
	public void setPz(Double pz) {
		this.pz = pz;
	}
	public Double getPf() {
		return pf;
	}
	public void setPf(Double pf) {
		this.pf = pf;
	}
	public Double getQz() {
		return qz;
	}
	public void setQz(Double qz) {
		this.qz = qz;
	}
	public Double getQf() {
		return qf;
	}
	public void setQf(Double qf) {
		this.qf = qf;
	}
	public Double getBrpz() {
		return brpz;
	}
	public void setBrpz(Double brpz) {
		this.brpz = brpz;
	}
	public Double getBrpf() {
		return brpf;
	}
	public void setBrpf(Double brpf) {
		this.brpf = brpf;
	}
	public Double getBrqz() {
		return brqz;
	}
	public void setBrqz(Double brqz) {
		this.brqz = brqz;
	}
	public Double getBrqf() {
		return brqf;
	}
	public void setBrqf(Double brqf) {
		this.brqf = brqf;
	}
	public Double getBypz() {
		return bypz;
	}
	public void setBypz(Double bypz) {
		this.bypz = bypz;
	}
	public Double getBypf() {
		return bypf;
	}
	public void setBypf(Double bypf) {
		this.bypf = bypf;
	}
	public Double getByqz() {
		return byqz;
	}
	public void setByqz(Double byqz) {
		this.byqz = byqz;
	}
	public Double getByqf() {
		return byqf;
	}
	public void setByqf(Double byqf) {
		this.byqf = byqf;
	}
	
}
