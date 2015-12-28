package com.jteap.wz.wzxsd.model;

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
@Table(name = "TB_WZ_YWZXSD")
public class Wzxsd {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name = "BH")	
	private String bh;
	
	@Column(name = "XSRQ")
	private Date xsrq;
	
	@Column(name = "GHDW")
	private String ghdw;
	
	@Column(name = "BZ")
	private String bz;
	
	@Column(name = "ZT")
	private String zt;

	@OneToMany(mappedBy="wzxsd")
	@Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<WzxsdMx> wzxdsmxs;

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

	public Date getXsrq() {
		return xsrq;
	}

	public void setXsrq(Date xsrq) {
		this.xsrq = xsrq;
	}

	public String getGhdw() {
		return ghdw;
	}

	public void setGhdw(String ghdw) {
		this.ghdw = ghdw;
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

	public Set<WzxsdMx> getWzxdsmxs() {
		return wzxdsmxs;
	}

	public void setWzxdsmxs(Set<WzxsdMx> wzxdsmxs) {
		this.wzxdsmxs = wzxdsmxs;
	}
	

}
