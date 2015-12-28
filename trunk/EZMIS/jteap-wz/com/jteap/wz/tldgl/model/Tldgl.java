package com.jteap.wz.tldgl.model;

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
@Table(name = "TB_WZ_YTLD")
public class Tldgl {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name = "BH")	
	private String bh;
	
	@Column(name = "TLSJ")
	private Date tlsj;
	
	@Column(name = "TLBM")
	private String tlbm;
	
	@Column(name = "GCLB")
	private String gclb;
	
	@Column(name = "GCXM")
	private String gcxm;
	
	@Column(name = "TLYY")
	private String tlyy;
	
	@Column(name = "CZR")
	private String czr;
	
	@Column(name = "YSR")
	private String ysr;
	
	@Column(name = "TLR")
	private String tlr;
	
	@Column(name = "CWFZR")
	private String cwfzr;
	
	@Column(name = "ZT")
	private String zt;

	@OneToMany(mappedBy="tldgl")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy("xh asc")
	private Set<TldMx> tldmxs;
	
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

	public Date getTlsj() {
		return tlsj;
	}

	public void setTlsj(Date tlsj) {
		this.tlsj = tlsj;
	}

	public String getTlbm() {
		return tlbm;
	}

	public void setTlbm(String tlbm) {
		this.tlbm = tlbm;
	}

	public String getGclb() {
		return gclb;
	}

	public void setGclb(String gclb) {
		this.gclb = gclb;
	}

	public String getGcxm() {
		return gcxm;
	}

	public void setGcxm(String gcxm) {
		this.gcxm = gcxm;
	}

	public String getTlyy() {
		return tlyy;
	}

	public void setTlyy(String tlyy) {
		this.tlyy = tlyy;
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

	public String getTlr() {
		return tlr;
	}

	public void setTlr(String tlr) {
		this.tlr = tlr;
	}

	public String getCwfzr() {
		return cwfzr;
	}

	public void setCwfzr(String cwfzr) {
		this.cwfzr = cwfzr;
	}

	public Set<TldMx> getTldmxs() {
		return tldmxs;
	}

	public void setTldmxs(Set<TldMx> tldmxs) {
		this.tldmxs = tldmxs;
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
	
	public Person getPersonTlr(){
		if(this.tlr==null || this.tlr.equals("")){
			Person p = new Person();
			p.setId("");
			p.setUserName("");
			p.setUserLoginName("");
			Set<P2Role> s = new HashSet<P2Role>();
			p.setRoles(s);
			return p;
		}
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
//		return pm.findUniqueBy("userLoginName", this.tlr);
		return pm.findUniqueBy("userName", this.tlr);
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

}
