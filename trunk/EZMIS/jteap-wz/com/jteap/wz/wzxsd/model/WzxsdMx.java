package com.jteap.wz.wzxsd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

import com.jteap.wz.wzda.model.Wzda;

@Entity
@Table(name = "TB_WZ_YWZXSDMX")
public class WzxsdMx {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@ManyToOne()
	@JoinColumn(name="XSBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzxsd wzxsd;
	
	@ManyToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzda wzda;
	
	@Column(name = "KCDJ")
	private Double kcdj;
	
	@Column(name = "JLDW")
	private String jldw;
	
	@Column(name = "XSSL")
	private Double xssl;
	
	@Column(name = "ZZSL")
	private Double zzsl;
	
	@Column(name = "XSDJ")
	private Double xsdj;
	
	@Column(name = "GLF")
	private Double glf;
	
	@Column(name = "YSF")
	private Double ysf;
	
	@Column(name = "XSJE")
	private Double xsje;
	
	@Column(name = "JEHJ")
	private String jehj;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Wzxsd getWzxsd() {
		return wzxsd;
	}

	public void setWzxsd(Wzxsd wzxsd) {
		this.wzxsd = wzxsd;
	}

	public Wzda getWzda() {
		return wzda;
	}

	public void setWzda(Wzda wzda) {
		this.wzda = wzda;
	}

	public Double getKcdj() {
		return kcdj;
	}

	public void setKcdj(Double kcdj) {
		this.kcdj = kcdj;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public Double getXssl() {
		return xssl;
	}

	public void setXssl(Double xssl) {
		this.xssl = xssl;
	}

	public Double getZzsl() {
		return zzsl;
	}

	public void setZzsl(Double zzsl) {
		this.zzsl = zzsl;
	}

	public Double getXsdj() {
		return xsdj;
	}

	public void setXsdj(Double xsdj) {
		this.xsdj = xsdj;
	}

	public Double getGlf() {
		return glf;
	}

	public void setGlf(Double glf) {
		this.glf = glf;
	}

	public Double getYsf() {
		return ysf;
	}

	public void setYsf(Double ysf) {
		this.ysf = ysf;
	}

	public Double getXsje() {
		return xsje;
	}

	public void setXsje(Double xsje) {
		this.xsje = xsje;
	}

	public String getJehj() {
		return jehj;
	}

	public void setJehj(String jehj) {
		this.jehj = jehj;
	}

}
