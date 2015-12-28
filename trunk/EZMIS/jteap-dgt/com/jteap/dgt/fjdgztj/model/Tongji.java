package com.jteap.dgt.fjdgztj.model;

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
/**
 * 工会分季度统计实体类
 * @author lvchao
 *
 */
@Entity  
@Table(name="TB_DGT_GHJDTJ")
@SuppressWarnings("unchecked")
public class Tongji {

	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="id")
	@Type(type="string")
	private String id;
	
	@Column(name="JIDU")
	private String jidu;
	
	@Column(name="YEAR")
	private String year;
	
	@Column(name="GONGHUI")
	private String gonghui;
	
	@OneToMany(mappedBy="tongji")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Dgt110gz> getDgt110gz;
	
	@OneToMany(mappedBy="tongji")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Dsdt> getDsdt;
	
	@OneToMany(mappedBy="tongji")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Wenyu> getWenyu;
	
	@OneToMany(mappedBy="tongji")
	@Cascade(value={org.hibernate.annotations.CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Ldjs> getLdjs;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJidu() {
		return jidu;
	}

	public void setJidu(String jidu) {
		this.jidu = jidu;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getGonghui() {
		return gonghui;
	}

	public void setGonghui(String gonghui) {
		this.gonghui = gonghui;
	}

	public Set<Dgt110gz> getGetDgt110gz() {
		return getDgt110gz;
	}

	public void setGetDgt110gz(Set<Dgt110gz> getDgt110gz) {
		this.getDgt110gz = getDgt110gz;
	}

	public Set<Dsdt> getGetDsdt() {
		return getDsdt;
	}

	public void setGetDsdt(Set<Dsdt> getDsdt) {
		this.getDsdt = getDsdt;
	}

	public Set<Wenyu> getGetWenyu() {
		return getWenyu;
	}

	public void setGetWenyu(Set<Wenyu> getWenyu) {
		this.getWenyu = getWenyu;
	}

	public Set<Ldjs> getGetLdjs() {
		return getLdjs;
	}

	public void setGetLdjs(Set<Ldjs> getLdjs) {
		this.getLdjs = getLdjs;
	}
	
}
