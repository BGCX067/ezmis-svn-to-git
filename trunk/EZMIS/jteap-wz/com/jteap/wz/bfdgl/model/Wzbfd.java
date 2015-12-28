package com.jteap.wz.bfdgl.model;

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
@Table(name = "TB_WZ_YKCBFD")
public class Wzbfd {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name = "BH")	
	private String bh;
	
	@Column(name = "BFRQ")
	private Date bfrq;
	
	@Column(name = "HSRY")
	private String hsry;
	
	@Column(name = "CLRY")
	private String clry;
	
	@Column(name = "ZG")
	private String zg;
	
	@Column(name = "CLD")
	private String cld;

	@Column(name = "BZ")
	private String bz;
	
	@Column(name = "ZT")
	private String zt;

	@OneToMany(mappedBy="wzbfd")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	@OrderBy("xh asc")
	private Set<WzbfdMx> wzbfdmxs;
	
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

	public Date getBfrq() {
		return bfrq;
	}

	public void setBfrq(Date bfrq) {
		this.bfrq = bfrq;
	}

	public String getHsry() {
		return hsry;
	}

	public void setHsry(String hsry) {
		this.hsry = hsry;
	}

	public String getClry() {
		return clry;
	}

	public void setClry(String clry) {
		this.clry = clry;
	}

	public String getZg() {
		return zg;
	}

	public void setZg(String zg) {
		this.zg = zg;
	}

	public String getCld() {
		return cld;
	}

	public void setCld(String cld) {
		this.cld = cld;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
	
	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public Set<WzbfdMx> getWzbfdmxs() {
		return wzbfdmxs;
	}

	public void setWzbfdmxs(Set<WzbfdMx> wzbfdmxs) {
		this.wzbfdmxs = wzbfdmxs;
	}
	
	public Person getPersonHsry(){
		if(this.hsry==null || this.hsry.equals("")){
			Person p = new Person();
			p.setId("");
			p.setUserName("");
			p.setUserLoginName("");
			Set<P2Role> s = new HashSet<P2Role>();
			p.setRoles(s);
			return p;
		}
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
//		return pm.findUniqueBy("userLoginName", this.hsry);
		return pm.findUniqueBy("userName", this.hsry);
	}
	
	public Person getPersonClry(){
		if(this.clry==null || this.clry.equals("")){
			Person p = new Person();
			p.setId("");
			p.setUserName("");
			p.setUserLoginName("");
			Set<P2Role> s = new HashSet<P2Role>();
			p.setRoles(s);
			return p;
		}
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
//		return pm.findUniqueBy("userLoginName", this.clry);
		return pm.findUniqueBy("userName", this.clry);
	}
	
	public Person getPersonZg(){
		if(this.zg==null || this.zg.equals("")){
			Person p = new Person();
			p.setId("");
			p.setUserName("");
			p.setUserLoginName("");
			Set<P2Role> s = new HashSet<P2Role>();
			p.setRoles(s);
			return p;
		}
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
//		return pm.findUniqueBy("userLoginName", this.zg);
		return pm.findUniqueBy("userName", this.zg);
	}
	
	public Person getPersonCld(){
		if(this.cld==null || this.cld.equals("")){
			Person p = new Person();
			p.setId("");
			p.setUserName("");
			p.setUserLoginName("");
			Set<P2Role> s = new HashSet<P2Role>();
			p.setRoles(s);
			return p;
		}
		PersonManager pm = (PersonManager)SpringContextUtil.getBean("personManager");
//		return pm.findUniqueBy("userLoginName", this.cld);
		return pm.findUniqueBy("userName", this.cld);
	}

}
