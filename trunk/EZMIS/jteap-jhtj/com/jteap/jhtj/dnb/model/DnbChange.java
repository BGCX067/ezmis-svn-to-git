/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.jhtj.dnb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 电能表换表
 * @author caihuiwen
 */
@Entity
@Table(name = "ELEC.ELEC_CHANGE")
public class DnbChange{
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")	
	private String id;
	
	
	@Column(name="ELECID")
	private int elecId;
	
	@Column(name="TIMES")
	private String times;
	
	//换表人
	@Column(name="HBR")
	private String hbr;
	
	//新表CTS
	@Column(name="CT_N")
	private String ctN;
	//新表PTS
	@Column(name="PT_N")
	private String ptN;
	//旧表CTS
	@Column(name="CT_O")
	private String ctO;
	//旧表PTS
	@Column(name="PT_O")
	private String ptO;

	//新表表号
	@Column(name="ELECBH_N")
	private String elecbhN;
	//旧表表号
	@Column(name="ELECBH_O")
	private String elecbhO;
	
////////////新表 ////////////////////	
	//总正有功_新表
	@Column(name="PZ_N")
	private double pzN;
	//总负有功_新表
	@Column(name="PF_N")
	private double pfN;
	//总正无功_新表
	@Column(name="QZ_N")
	private double qzN;
	//总负无功_新表
	@Column(name="QF_N")
	private double qfN;
	
	//峰正有功_新表
	@Column(name="FPZ_N")
	private double fpzN;
	//峰负有功_新表
	@Column(name="FPF_N")
	private double fpfN;
	//峰正无功_新表
	@Column(name="FQZ_N")
	private double fqzN;
	//峰负无功_新表
	@Column(name="FQF_N")
	private double fqfN;
	
	//平正有功_新表
	@Column(name="PPZ_N")
	private double ppzN;
	//平负有功_新表
	@Column(name="PPF_N")
	private double ppfN;
	//平正无功_新表
	@Column(name="PQZ_N")
	private double pqzN;
	//平负无功_新表
	@Column(name="PQF_N")
	private double pqfN;
	
	//谷正有功_新表
	@Column(name="GPZ_N")
	private double gpzN;
	//谷负有功_新表
	@Column(name="GPF_N")
	private double gpfN;
	//谷正无功_新表
	@Column(name="GQZ_N")
	private double gqzN;
	//谷负无功_新表
	@Column(name="GQF_N")
	private double gqfN;
	
////////////// 旧表 ////////////////////
	//总正有功_旧表
	@Column(name="PZ_O")
	private double pzO;
	//总负有功_旧表
	@Column(name="PF_O")
	private double pfO;
	//总正无功_旧表
	@Column(name="QZ_O")
	private double qzO;
	//总负无功_旧表
	@Column(name="QF_O")
	private double qfO;
	
	//峰正有功_旧表
	@Column(name="FPZ_O")
	private double fpzO;
	//峰负有功_旧表
	@Column(name="FPF_O")
	private double fpfO;
	//峰正无功_旧表
	@Column(name="FQZ_O")
	private double fqzO;
	//峰负无功_旧表
	@Column(name="FQF_O")
	private double fqfO;
	
	//平正有功_旧表
	@Column(name="PPZ_O")
	private double ppzO;
	//平负有功_旧表
	@Column(name="PPF_O")
	private double ppfO;
	//平正无功_旧表
	@Column(name="PQZ_O")
	private double pqzO;
	//平负无功_旧表
	@Column(name="PQF_O")
	private double pqfO;
	
	//谷正有功_旧表
	@Column(name="GPZ_O")
	private double gpzO;
	//谷负有功_旧表
	@Column(name="GPF_O")
	private double gpfO;
	//谷正无功_旧表
	@Column(name="GQZ_O")
	private double gqzO;
	//谷负无功_旧表
	@Column(name="GQF_O")
	private double gqfO;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getElecId() {
		return elecId;
	}
	public void setElecId(int elecId) {
		this.elecId = elecId;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getHbr() {
		return hbr;
	}
	public void setHbr(String hbr) {
		this.hbr = hbr;
	}
	public String getCtN() {
		return ctN;
	}
	public void setCtN(String ctN) {
		this.ctN = ctN;
	}
	public String getPtN() {
		return ptN;
	}
	public void setPtN(String ptN) {
		this.ptN = ptN;
	}
	public String getCtO() {
		return ctO;
	}
	public void setCtO(String ctO) {
		this.ctO = ctO;
	}
	public String getPtO() {
		return ptO;
	}
	public void setPtO(String ptO) {
		this.ptO = ptO;
	}
	public String getElecbhN() {
		return elecbhN;
	}
	public void setElecbhN(String elecbhN) {
		this.elecbhN = elecbhN;
	}
	public String getElecbhO() {
		return elecbhO;
	}
	public void setElecbhO(String elecbhO) {
		this.elecbhO = elecbhO;
	}
	public double getPzN() {
		return pzN;
	}
	public void setPzN(double pzN) {
		this.pzN = pzN;
	}
	public double getPfN() {
		return pfN;
	}
	public void setPfN(double pfN) {
		this.pfN = pfN;
	}
	public double getQzN() {
		return qzN;
	}
	public void setQzN(double qzN) {
		this.qzN = qzN;
	}
	public double getQfN() {
		return qfN;
	}
	public void setQfN(double qfN) {
		this.qfN = qfN;
	}
	public double getFpzN() {
		return fpzN;
	}
	public void setFpzN(double fpzN) {
		this.fpzN = fpzN;
	}
	public double getFpfN() {
		return fpfN;
	}
	public void setFpfN(double fpfN) {
		this.fpfN = fpfN;
	}
	public double getFqzN() {
		return fqzN;
	}
	public void setFqzN(double fqzN) {
		this.fqzN = fqzN;
	}
	public double getFqfN() {
		return fqfN;
	}
	public void setFqfN(double fqfN) {
		this.fqfN = fqfN;
	}
	public double getPpzN() {
		return ppzN;
	}
	public void setPpzN(double ppzN) {
		this.ppzN = ppzN;
	}
	public double getPpfN() {
		return ppfN;
	}
	public void setPpfN(double ppfN) {
		this.ppfN = ppfN;
	}
	public double getPqzN() {
		return pqzN;
	}
	public void setPqzN(double pqzN) {
		this.pqzN = pqzN;
	}
	public double getPqfN() {
		return pqfN;
	}
	public void setPqfN(double pqfN) {
		this.pqfN = pqfN;
	}
	public double getGpzN() {
		return gpzN;
	}
	public void setGpzN(double gpzN) {
		this.gpzN = gpzN;
	}
	public double getGpfN() {
		return gpfN;
	}
	public void setGpfN(double gpfN) {
		this.gpfN = gpfN;
	}
	public double getGqzN() {
		return gqzN;
	}
	public void setGqzN(double gqzN) {
		this.gqzN = gqzN;
	}
	public double getGqfN() {
		return gqfN;
	}
	public void setGqfN(double gqfN) {
		this.gqfN = gqfN;
	}
	public double getPzO() {
		return pzO;
	}
	public void setPzO(double pzO) {
		this.pzO = pzO;
	}
	public double getPfO() {
		return pfO;
	}
	public void setPfO(double pfO) {
		this.pfO = pfO;
	}
	public double getQzO() {
		return qzO;
	}
	public void setQzO(double qzO) {
		this.qzO = qzO;
	}
	public double getQfO() {
		return qfO;
	}
	public void setQfO(double qfO) {
		this.qfO = qfO;
	}
	public double getFpzO() {
		return fpzO;
	}
	public void setFpzO(double fpzO) {
		this.fpzO = fpzO;
	}
	public double getFpfO() {
		return fpfO;
	}
	public void setFpfO(double fpfO) {
		this.fpfO = fpfO;
	}
	public double getFqzO() {
		return fqzO;
	}
	public void setFqzO(double fqzO) {
		this.fqzO = fqzO;
	}
	public double getFqfO() {
		return fqfO;
	}
	public void setFqfO(double fqfO) {
		this.fqfO = fqfO;
	}
	public double getPpzO() {
		return ppzO;
	}
	public void setPpzO(double ppzO) {
		this.ppzO = ppzO;
	}
	public double getPpfO() {
		return ppfO;
	}
	public void setPpfO(double ppfO) {
		this.ppfO = ppfO;
	}
	public double getPqzO() {
		return pqzO;
	}
	public void setPqzO(double pqzO) {
		this.pqzO = pqzO;
	}
	public double getPqfO() {
		return pqfO;
	}
	public void setPqfO(double pqfO) {
		this.pqfO = pqfO;
	}
	public double getGpzO() {
		return gpzO;
	}
	public void setGpzO(double gpzO) {
		this.gpzO = gpzO;
	}
	public double getGpfO() {
		return gpfO;
	}
	public void setGpfO(double gpfO) {
		this.gpfO = gpfO;
	}
	public double getGqzO() {
		return gqzO;
	}
	public void setGqzO(double gqzO) {
		this.gqzO = gqzO;
	}
	public double getGqfO() {
		return gqfO;
	}
	public void setGqfO(double gqfO) {
		this.gqfO = gqfO;
	}
	
}
