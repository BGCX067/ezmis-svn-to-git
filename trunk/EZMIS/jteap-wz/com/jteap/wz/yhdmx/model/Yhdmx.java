/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 
package com.jteap.wz.yhdmx.model;

import java.math.BigDecimal;

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

import com.jteap.wz.cgjhmx.model.Cgjhmx;
import com.jteap.wz.wzda.model.Wzda;
import com.jteap.wz.yhdgl.model.Yhdgl;


@Entity
@Table(name = "TB_WZ_YYHDMX")
public class Yhdmx{

	//columns START
	@Column(name = "GHDW")	
	private java.lang.String ghdw;
	
	
	@Column(name = "XH")	
	private java.lang.String xh;
	
	//tax rate
	@Column(name = "SL")	
	private Double sl;
	
	
	@Column(name = "ZF")	
	private Double zf;
	
	
	@ManyToOne()
	@JoinColumn(name="WZBM")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Wzda wzdagl;  //物资档案
	
	
	@ManyToOne()
	@JoinColumn(name="YHDBH")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Yhdgl yhdgl;
	
	@Column(name = "TSSL")	
	private Double tssl;
	
	
	@Column(name = "FPBH")	
	private java.lang.String fpbh;
	
	
	@Column(name = "DHSL")	
	private Double dhsl;
	
	
	@Column(name = "CGJLDW")	
	private java.lang.String cgjldw;
	
	
	@Column(name = "SQDJ")	
	private Double sqdj;
	
	
	@Column(name = "YSSL")	
	private Double yssl;
	
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private java.lang.String id;
	
	
	@Column(name = "JHDJ")	
	private Double jhdj;
	
	
	@Column(name = "HSXS")	
	private Double hsxs;
	
	@Column(name = "ZT")	
	private java.lang.String zt;
	
	@Column(name = "RKSJ")	
	private java.util.Date rksj;
	
	@ManyToOne()
	@JoinColumn(name="CGDMX")
	@LazyToOne(LazyToOneOption.PROXY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	private Cgjhmx cgjhmx;

	@SuppressWarnings("unused")
	@Transient
	private Double jhje;
	
	@Transient
	private Double sjhj;
	
	@Column(name = "BL_ZT")
	private java.lang.String blzt; 
	
	@Column(name = "JL_ZT")
	private java.lang.String jlzt;
	
	@Column(name = "REMARK")
	private java.lang.String remark;
	
	@Column(name = "SYSL")
	private Double sysl;

	public Double getSysl() {
		return sysl;
	}

	public void setSysl(Double sysl) {
		this.sysl = sysl;
	}

	public java.lang.String getBlzt() {
		return blzt;
	}

	public void setBlzt(java.lang.String blzt) {
		this.blzt = blzt;
	}

	public java.lang.String getJlzt() {
		return jlzt;
	}

	public void setJlzt(java.lang.String jlzt) {
		this.jlzt = jlzt;
	}

	public Double getJhje() {
		try {
			jhje = this.jhdj*this.yssl*this.hsxs;
		} catch (Exception e) {
			jhje = 0d;
		}
		return jhje;
	}

	public void setJhje(Double jhje) {
		this.jhje = jhje;
	}

	public Cgjhmx getCgjhmx() {
		return cgjhmx;
	}

	public void setCgjhmx(Cgjhmx cgjhmx) {
		this.cgjhmx = cgjhmx;
	}

	public java.lang.String getGhdw() {
		return this.ghdw;
	}
	
	public void setGhdw(java.lang.String value) {
		this.ghdw = value;
	}
	public java.lang.String getXh() {
		return this.xh;
	}
	
	public void setXh(java.lang.String value) {
		this.xh = value;
	}
	public Double getSl() {
		return this.sl;
	}
	
	public void setSl(Double value) {
		this.sl = value;
	}
	public Double getZf() {
		return this.zf;
	}
	
	public void setZf(Double value) {
		this.zf = value;
	}

	public Wzda getWzdagl() {
		return wzdagl;
	}

	public void setWzdagl(Wzda wzdagl) {
		this.wzdagl = wzdagl;
	}

	public Yhdgl getYhdgl() {
		return yhdgl;
	}

	public void setYhdgl(Yhdgl yhdgl) {
		this.yhdgl = yhdgl;
	}

	public Double getTssl() {
		return this.tssl;
	}
	
	public void setTssl(Double value) {
		this.tssl = value;
	}
	public java.lang.String getFpbh() {
		return this.fpbh;
	}
	
	public void setFpbh(java.lang.String value) {
		this.fpbh = value;
	}
	public Double getDhsl() {
		return this.dhsl;
	}
	
	public void setDhsl(Double value) {
		this.dhsl = value;
	}
	public java.lang.String getCgjldw() {
		return this.cgjldw;
	}
	
	public void setCgjldw(java.lang.String value) {
		this.cgjldw = value;
	}
	public Double getSqdj() {
		return this.sqdj;
	}
	
	public void setSqdj(Double value) {
		this.sqdj = value;
	}
	public Double getYssl() {
		return this.yssl;
	}
	
	public void setYssl(Double value) {
		this.yssl = value;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public Double getJhdj() {
		return this.jhdj;
	}
	
	public void setJhdj(Double value) {
		this.jhdj = value;
	}
	public Double getHsxs() {
		return this.hsxs;
	}
	
	public void setHsxs(Double value) {
		this.hsxs = value;
	}

	public java.lang.String getZt() {
		return zt;
	}

	public void setZt(java.lang.String zt) {
		this.zt = zt;
	}
    @Transient
	public Double getSjhj() {
		try{
//			DecimalFormat decimalFormat = new DecimalFormat("###.00");
//			//入库金额
//			double rkje= Double.valueOf(decimalFormat.format(yssl*hsxs*sqdj));
//			System.out.println("格式化之前："+yssl*hsxs*sqdj);
//			System.out.println("格式化之后:"+rkje);
//			//税额
//			double se = sl*rkje;
//			System.out.println("税额："+se);
//			BigDecimal b = new BigDecimal(se);
//			System.out.println("税额格式化之后"+b.setScale(2, BigDecimal.ROUND_HALF_UP));
//			this.sjhj = rkje+b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
			
//			BigDecimal b = new BigDecimal(yssl*hsxs*sqdj);
			
//	        BigDecimal one = new BigDecimal("1");
//	        double rkje= b.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
	        
	        
//	        double se = c.divide(one,2,BigDecimal.ROUND_HALF_UP).doubleValue();
//			double rkje= Double.valueOf(decimalFormat.format(yssl*hsxs*sqdj));
//			double se = Double.valueOf(decimalFormat.format(sl*rkje));;
//			double rkje = yssl*hsxs*sqdj;
//			double se = sl*rkje;
			//double类型在进行浮点数运算的时候会丢失精度，改为BigDecimal乘法运算
	    	BigDecimal yssl_decimal=new BigDecimal(Double.toString(yssl));
			BigDecimal hsxs_decimal=new BigDecimal(Double.toString(hsxs));
			BigDecimal sqdj_decimal=new BigDecimal(Double.toString(sqdj));
			BigDecimal sl_decimal  =new BigDecimal(Double.toString(sl));
			BigDecimal rk_price = yssl_decimal.multiply(hsxs_decimal).multiply(sqdj_decimal);
			BigDecimal tax = rk_price.multiply(sl_decimal);
	        this.sjhj = rk_price.setScale(2, BigDecimal.ROUND_HALF_UP).add(tax.setScale(2, BigDecimal.ROUND_HALF_UP)).doubleValue();
		}catch (Exception e) {
			this.sjhj = 0d;
		}
		return sjhj;
	}

	public void setSjhj(Double sjhj) {
		this.sjhj = sjhj;
	}

	public java.util.Date getRksj() {
		return rksj;
	}

	public void setRksj(java.util.Date rksj) {
		this.rksj = rksj;
	}

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	
}

