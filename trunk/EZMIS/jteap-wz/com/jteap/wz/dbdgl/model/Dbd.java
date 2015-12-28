package com.jteap.wz.dbdgl.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;

@Entity
@Table(name = "TB_WZ_YDBD")
public class Dbd {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name = "BH")	
	private String bh;
	
	@Column(name = "DBSJ")
	private Date dbsj;
	
	@Column(name = "DBXZ")
	private String dbxz;
	
	@Column(name = "DBQDW")
	private String dbqdw;
	
	@Column(name = "DBHDW")
	private String dbhdw;
	
	@Column(name = "DBYY")
	private String dbyy;
	
	@Column(name = "BZ")
	private String bz;
	
	@Column(name = "CZR")
	private String czr;			//操作人
	
	@Column(name = "YSR")
	private String ysr;			//调入为验收人，调出为批准人
	
	@Column(name = "THR")
	private String thr;			//调入为提货人，调出为复核人
	
	@Column(name = "CWFZR")
	private String cwfzr;		//调入为财务负责人，调出为经办人
	
	@Column(name = "DBQF")
	private String dbqf;
	
	@Column(name = "ZT")
	private String zt;

	@OneToMany(mappedBy="dbd")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy("xh asc")
	private Set<DbdMx> dbdmxs;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public Date getDbsj() {
		return dbsj;
	}

	public void setDbsj(Date dbsj) {
		this.dbsj = dbsj;
	}

	public String getDbxz() {
		return dbxz;
	}

	public void setDbxz(String dbxz) {
		this.dbxz = dbxz;
	}

	public String getDbqdw() {
		return dbqdw;
	}

	public void setDbqdw(String dbqdw) {
		this.dbqdw = dbqdw;
	}

	public String getDbhdw() {
		return dbhdw;
	}

	public void setDbhdw(String dbhdw) {
		this.dbhdw = dbhdw;
	}

	public String getDbyy() {
		return dbyy;
	}

	public void setDbyy(String dbyy) {
		this.dbyy = dbyy;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getCzr() {
		return czr;
	}

	public void setCzr(String czr) {
		this.czr = czr;
	}

	public String getYsr() {
		return ysr;
	}

	public void setYsr(String ysr) {
		this.ysr = ysr;
	}

	public String getThr() {
		return thr;
	}

	public void setThr(String thr) {
		this.thr = thr;
	}

	public String getCwfzr() {
		return cwfzr;
	}

	public void setCwfzr(String cwfzr) {
		this.cwfzr = cwfzr;
	}

	public String getDbqf() {
		return dbqf;
	}

	public void setDbqf(String dbqf) {
		this.dbqf = dbqf;
	}
	
	public Person getPersonCzr(){
		if(this.czr==null || this.czr.equals("")){
			Person p = new Person();
			p.setId("");
			p.setUserName("");
			p.setUserLoginName("");
			Set<P2Role> s = new HashSet<P2Role>();
			p.setRoles(s);
			return p;
		}
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
//		return pm.findUniqueBy("userLoginName", this.czr);
		return pm.findUniqueBy("userName", this.czr);
	}
	
	public Person getPersonYsr(){
		if(this.ysr==null || this.ysr.equals("")){
			Person p = new Person();
			p.setId("");
			p.setUserName("");
			p.setUserLoginName("");
			Set<P2Role> s = new HashSet<P2Role>();
			p.setRoles(s);
			return p;
		}
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
//		return pm.findUniqueBy("userLoginName", this.ysr);
		return pm.findUniqueBy("userName", this.ysr);
	}
	
	public Person getPersonThr(){
		if(this.thr==null || this.thr.equals("")){
			Person p = new Person();
			p.setId("");
			p.setUserName("");
			p.setUserLoginName("");
			Set<P2Role> s = new HashSet<P2Role>();
			p.setRoles(s);
			return p;
		}
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
//		return pm.findUniqueBy("userLoginName", this.thr);
		return pm.findUniqueBy("userName", this.thr);
	}
	
	public Person getPersonCwfzr(){
		if(this.cwfzr==null || this.cwfzr.equals("")){
			Person p = new Person();
			p.setId("");
			p.setUserName("");
			p.setUserLoginName("");
			Set<P2Role> s = new HashSet<P2Role>();
			p.setRoles(s);
			return p;
		}
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
//		return pm.findUniqueBy("userLoginName", this.cwfzr);
		return pm.findUniqueBy("userName", this.cwfzr);
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public Set<DbdMx> getDbdmxs() {
		return dbdmxs;
	}

	public void setDbdmxs(Set<DbdMx> dbdmxs) {
		this.dbdmxs = dbdmxs;
	}
	
	

}
