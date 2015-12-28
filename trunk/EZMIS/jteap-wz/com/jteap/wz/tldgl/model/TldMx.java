package com.jteap.wz.tldgl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.wz.wzda.model.Wzda;

@Entity
@Table(name = "TB_WZ_YTLDMX")
public class TldMx {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name = "XH")
	private String xh;
	
	@ManyToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzda wzda;
	
	@ManyToOne()
	@JoinColumn(name="TLDBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Tldgl tldgl;
	
	@Column(name = "TLSL")
	private Double tlsl;
	
	@Column(name = "JLDW")
	private String jldw;

	@Transient
	private Double je;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public Wzda getWzda() {
		return wzda;
	}

	public void setWzda(Wzda wzda) {
		this.wzda = wzda;
	}

	public Tldgl getTldgl() {
		return tldgl;
	}

	public void setTldgl(Tldgl tldgl) {
		this.tldgl = tldgl;
	}

	public Double getTlsl() {
		return tlsl;
	}

	public void setTlsl(Double tlsl) {
		this.tlsl = tlsl;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public Double getJe() {
		try {
			je = this.wzda.getJhdj()*this.tlsl;
		} catch (Exception e) {
			je = 0d;
		}
		return je;
	}
	public void setJe(Double je) {
		this.je = je;
	}

}
