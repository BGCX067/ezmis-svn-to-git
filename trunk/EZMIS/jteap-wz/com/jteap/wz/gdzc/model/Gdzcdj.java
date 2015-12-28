package com.jteap.wz.gdzc.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_WZ_GDZCDJ")
public class Gdzcdj {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name = "ZCBM")
	private String zcbm;
	
	@Column(name = "GMLYJXYPJ")
	private String gmlyjxypj;
	
	@Column(name = "SYBMJDYJ")
	private String sybmjdyj;
	
	@Column(name = "GR")
	private String gr;
	
	@Column(name = "JSRY")
	private String jsry;
	
	@Column(name = "FZR")
	private String fzr;
	
	@Column(name = "CWBMFZR")
	private String cwbmfzr;
	
	@Column(name = "CWBMJBR")
	private String cwbmjbr;
	
	@Column(name = "CWBMJBRQ")
	private Date cwbmjbrq;
	
	@Column(name = "JHJYBFZR")
	private String jhjybfzr;
	
	@Column(name = "JHJYBJBR")
	private String jhjybjbr;
	
	@Column(name = "JHJYBJBRQ")
	private Date jhjybjbrq;
	
	@Column(name = "SCJSBFZR")
	private String scjsbfzr;
	
	@Column(name = "SCJSBJBR")
	private String scjsbjbr;
	
	@Column(name = "SCJSBJBRQ")
	private Date scjsbjbrq;
	
	@Column(name = "ZG")
	private String zg;
	
	@Column(name = "FZJL")
	private String fzjl;
	
	@Column(name = "ZKJS")
	private String zkjs;
	
	@Column(name = "ZJL")
	private String zjl;
	
	@Column(name = "CZY")
	private String czy;
	
	@Column(name = "CZYXM")
	private String czyxm;
	
	@Column(name = "ZT")
	private String zt;
	
	@Column(name = "FLOW_STATUS")
	private String flow_status;
	
	@Column(name = "SQBM")
	private String sqbm;
	
	// 所有条目明细
	@OneToMany(mappedBy="gdzcdj")
	@Cascade(value={org.hibernate.annotations.CascadeType.SAVE_UPDATE,org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<GdzcdjMx> gdzcdjmxs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZcbm() {
		return zcbm;
	}

	public void setZcbm(String zcbm) {
		this.zcbm = zcbm;
	}

	public String getGmlyjxypj() {
		return gmlyjxypj;
	}

	public void setGmlyjxypj(String gmlyjxypj) {
		this.gmlyjxypj = gmlyjxypj;
	}

	public String getSybmjdyj() {
		return sybmjdyj;
	}

	public void setSybmjdyj(String sybmjdyj) {
		this.sybmjdyj = sybmjdyj;
	}

	public String getGr() {
		return gr;
	}

	public void setGr(String gr) {
		this.gr = gr;
	}

	public String getJsry() {
		return jsry;
	}

	public void setJsry(String jsry) {
		this.jsry = jsry;
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getCwbmfzr() {
		return cwbmfzr;
	}

	public void setCwbmfzr(String cwbmfzr) {
		this.cwbmfzr = cwbmfzr;
	}

	public String getCwbmjbr() {
		return cwbmjbr;
	}

	public void setCwbmjbr(String cwbmjbr) {
		this.cwbmjbr = cwbmjbr;
	}

	public Date getCwbmjbrq() {
		return cwbmjbrq;
	}

	public void setCwbmjbrq(Date cwbmjbrq) {
		this.cwbmjbrq = cwbmjbrq;
	}

	public String getJhjybfzr() {
		return jhjybfzr;
	}

	public void setJhjybfzr(String jhjybfzr) {
		this.jhjybfzr = jhjybfzr;
	}

	public String getJhjybjbr() {
		return jhjybjbr;
	}

	public void setJhjybjbr(String jhjybjbr) {
		this.jhjybjbr = jhjybjbr;
	}

	public Date getJhjybjbrq() {
		return jhjybjbrq;
	}

	public void setJhjybjbrq(Date jhjybjbrq) {
		this.jhjybjbrq = jhjybjbrq;
	}

	public String getScjsbfzr() {
		return scjsbfzr;
	}

	public void setScjsbfzr(String scjsbfzr) {
		this.scjsbfzr = scjsbfzr;
	}

	public String getScjsbjbr() {
		return scjsbjbr;
	}

	public void setScjsbjbr(String scjsbjbr) {
		this.scjsbjbr = scjsbjbr;
	}

	public Date getScjsbjbrq() {
		return scjsbjbrq;
	}

	public void setScjsbjbrq(Date scjsbjbrq) {
		this.scjsbjbrq = scjsbjbrq;
	}

	public String getZg() {
		return zg;
	}

	public void setZg(String zg) {
		this.zg = zg;
	}

	public String getFzjl() {
		return fzjl;
	}

	public void setFzjl(String fzjl) {
		this.fzjl = fzjl;
	}

	public String getZkjs() {
		return zkjs;
	}

	public void setZkjs(String zkjs) {
		this.zkjs = zkjs;
	}

	public String getZjl() {
		return zjl;
	}

	public void setZjl(String zjl) {
		this.zjl = zjl;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getFlow_status() {
		return flow_status;
	}

	public void setFlow_status(String flow_status) {
		this.flow_status = flow_status;
	}

	public Set<GdzcdjMx> getGdzcdjmxs() {
		return gdzcdjmxs;
	}

	public void setGdzcdjmxs(Set<GdzcdjMx> gdzcdjmxs) {
		this.gdzcdjmxs = gdzcdjmxs;
	}

	public String getCzy() {
		return czy;
	}

	public void setCzy(String czy) {
		this.czy = czy;
	}

	public String getCzyxm() {
		return czyxm;
	}

	public void setCzyxm(String czyxm) {
		this.czyxm = czyxm;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}


}
