package com.jteap.wz.xqjh.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Type;

/**
 * 描述 : 需求计划明细实体
 * 作者 : caofei
 * 时间 : Oct 21, 2010
 * 参数 : 
 * 返回值 : 
 * 异常 : 
 */
@Entity
@Table(name = "TB_WZ_XQJH_DETAIL")
public class XqjhDetail {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;          //编号
	
	@ManyToOne()
	@JoinColumn(name = "XQJHBH")
	@LazyToOne(LazyToOneOption.PROXY)
	private Xqjh xqjh;                    //需求计划
	
	@Column(name="WZBM")
	private String wzbm;                  //物资编码
	
	@Column(name="XH")
	private String xh;                    //序号
	
	@Column(name="PZSL")
	private double pzsl;                  //批准数量
	
	@Column(name="JLDW")
	private String jldw;                  //计量单位
	
	@Column(name="JHDJ")
	private double jhdj;                  //计划单价
	
	@Column(name="XYSJ")
	private Date xysj;                    //需用时间
	 
	@Column(name="GHS")
	private String ghs;                   //供货商
	
	@Column(name="FREE")
	private double free;                  //自由库存数量
	
	@Column(name="CGSL")
	private double cgsl;                  //采购数量
	
	@Column(name="DHSL")
	private double dhsl;                  //到货数量
	
	@Column(name="LYSL")
	private double lysl;                  //领用数量
	
	@Column(name="SLSL")
	private double slsl;                  //申领数量
	
	@Column(name="STATUS")
	private String status;                //状态

	@Column(name="DYSZT")
	private String dyszt;
	
	@Column(name="DYSCZR")
	private String dysczr;
	
	public java.lang.String getId() {
		return id;
	}

	public void setId(java.lang.String id) {
		this.id = id;
	}

	public String getWzbm() {
		return wzbm;
	}

	public void setWzbm(String wzbm) {
		this.wzbm = wzbm;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public double getJhdj() {
		return jhdj;
	}

	public void setJhdj(double jhdj) {
		this.jhdj = jhdj;
	}

	public Date getXysj() {
		return xysj;
	}

	public void setXysj(Date xysj) {
		this.xysj = xysj;
	}

	public String getGhs() {
		return ghs;
	}

	public void setGhs(String ghs) {
		this.ghs = ghs;
	}

	public double getPzsl() {
		return pzsl;
	}

	public void setPzsl(double pzsl) {
		this.pzsl = pzsl;
	}

	public double getFree() {
		return free;
	}

	public void setFree(double free) {
		this.free = free;
	}

	public double getCgsl() {
		return cgsl;
	}

	public void setCgsl(double cgsl) {
		this.cgsl = cgsl;
	}

	public double getDhsl() {
		return dhsl;
	}

	public void setDhsl(double dhsl) {
		this.dhsl = dhsl;
	}

	public double getLysl() {
		return lysl;
	}

	public void setLysl(double lysl) {
		this.lysl = lysl;
	}

	public double getSlsl() {
		return slsl;
	}

	public void setSlsl(double slsl) {
		this.slsl = slsl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Xqjh getXqjh() {
		return xqjh;
	}

	public void setXqjh(Xqjh xqjh) {
		this.xqjh = xqjh;
	}

	public String getDyszt() {
		return dyszt;
	}

	public void setDyszt(String dyszt) {
		this.dyszt = dyszt;
	}

	public String getDysczr() {
		return dysczr;
	}

	public void setDysczr(String dysczr) {
		this.dysczr = dysczr;
	}
	
}
