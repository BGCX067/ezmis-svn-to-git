package com.jteap.wz.kcpd.model;

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

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.wz.ckgl.manager.CkglManager;
import com.jteap.wz.ckgl.model.Ckgl;

@Entity
@Table(name = "TB_WZ_YPDD")
@SuppressWarnings({ "unchecked", "serial", "unused" })
public class Pdd {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name = "BH")	
	private String bh;
	
	@Column(name = "PDSJ")
	private Date pdsj;
	
	@Column(name = "CZR")
	private String czr;
	
	@Column(name = "CKMC")
	private String ckmc;
	
	@Column(name = "SL")
	private String sl;
	
	@Column(name = "XDJ")
	private String xdj;
	
	@Column(name = "DDJ")
	private String ddj;
	
	@Column(name = "ZKSJ")
	private String zksj;
	
	@Column(name = "ZT")
	private String zt;

	@OneToMany(mappedBy="pdd")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy("xh asc")
	private Set<PddMx> pddmxs;
	
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

	public Date getPdsj() {
		return pdsj;
	}

	public void setPdsj(Date pdsj) {
		this.pdsj = pdsj;
	}

	public String getCzr() {
		return czr;
	}

	public void setCzr(String czr) {
		this.czr = czr;
	}

	 
	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
	}
	public String getSl() {
		return sl;
	}

	public void setSl(String sl) {
		this.sl = sl;
	}

	public String getXdj() {
		return xdj;
	}

	public void setXdj(String xdj) {
		this.xdj = xdj;
	}

	public String getDdj() {
		return ddj;
	}

	public void setDdj(String ddj) {
		this.ddj = ddj;
	}

	public String getZksj() {
		return zksj;
	}

	public void setZksj(String zksj) {
		this.zksj = zksj;
	}

	public String getCkmc() {
		if(StringUtils.isNotEmpty(this.ckmc)){
			CkglManager ckglManager = (CkglManager) SpringContextUtil.getBean("ckglManager");
			Ckgl ckgl = ckglManager.findUniqueBy("id", this.ckmc);
			return ckgl.getCkmc();
		}else{
			return ckmc;
		}
	}

	public Set<PddMx> getPddmxs() {
		return pddmxs;
	}

	public void setPddmxs(Set<PddMx> pddmxs) {
		this.pddmxs = pddmxs;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
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
}
