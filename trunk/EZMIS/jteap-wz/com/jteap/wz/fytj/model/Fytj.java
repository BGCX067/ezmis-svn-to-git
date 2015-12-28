package com.jteap.wz.fytj.model;

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

import com.jteap.wz.ckgl.model.Ckgl;
import com.jteap.wz.gcxmgl.model.Proj;
import com.jteap.wz.tjny.model.Tjny;

/**
 * 物资费用统计报表实体类
 * @author lvchao
 *
 */
@Entity
@Table(name = "TB_WZ_TWZFYTJB")
public class Fytj {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	
	@Column(name="NF")
	private String nf;
	
	@Column(name="YF")
	private String yf;
	
	@ManyToOne()
	@JoinColumn(name="proj")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})

	private Proj proj;
	
	@Column(name="TJSJ")
	private String tjsj;
	
	@Column(name="BYJE")
	private Double byje;
	
	@Column(name="BNLJ")
	private Double bnlj;
	
	@ManyToOne()
	@JoinColumn(name="TJNY")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Tjny tjny;	//统计年月
	
	@ManyToOne()
	@JoinColumn(name="CKID")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Ckgl ckgl;	//仓库管理
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNf() {
		return nf;
	}
	public void setNf(String nf) {
		this.nf = nf;
	}
	public String getYf() {
		return yf;
	}
	public void setYf(String yf) {
		this.yf = yf;
	}
	 
	public String getTjsj() {
		return tjsj;
	}
	public void setTjsj(String tjsj) {
		this.tjsj = tjsj;
	}
	public Double getByje() {
		return byje;
	}
	public void setByje(Double byje) {
		this.byje = byje;
	}
	public Double getBnlj() {
		return bnlj;
	}
	public void setBnlj(Double bnlj) {
		this.bnlj = bnlj;
	}
	public Proj getProj() {
		return proj;
	}
	public void setProj(Proj proj) {
		this.proj = proj;
	}
	public Tjny getTjny() {
		return tjny;
	}
	public void setTjny(Tjny tjny) {
		this.tjny = tjny;
	}
	public Ckgl getCkgl() {
		return ckgl;
	}
	public void setCkgl(Ckgl ckgl) {
		this.ckgl = ckgl;
	}
	
}
