package com.jteap.wz.dbdgl.model;

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
@Table(name = "TB_WZ_YDBDMX")
public class DbdMx {
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
	@JoinColumn(name="DBDBH")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Dbd dbd;
	
	@Column(name = "DBSL")
	private Double dbsl;
	
	@Column(name = "JLDW")
	private String jldw;
	
	@Column(name = "DBDJ")
	private Double dbdj;

	@Transient
	private Double je;
	
	public Double getJe() {
		try {
			this.je = this.dbsl * this.dbdj;
		} catch (Exception e) {
			this.je = 0d;
		}
		return je;
	}

	public void setJe(Double je) {
		this.je = je;
	}

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

	public Dbd getDbd() {
		return dbd;
	}

	public void setDbd(Dbd dbd) {
		this.dbd = dbd;
	}

	public Double getDbsl() {
		return dbsl;
	}

	public void setDbsl(Double dbsl) {
		this.dbsl = dbsl;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public Double getDbdj() {
		return dbdj;
	}

	public void setDbdj(Double dbdj) {
		this.dbdj = dbdj;
	}

}
