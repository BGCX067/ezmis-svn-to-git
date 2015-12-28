package com.jteap.jx.dxxgl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * 
 * 公用系统检修计划表
 * 
 * @author wangyun
 * 
 */
@Entity
@Table(name = "TB_JX_DXXGL_GYXTJXJH")
public class Gyxtjxjh {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	@Type(type = "string")
	private String id;

	// 设备ID
	@Column(name = "SBID")
	private String sbid;

	// 设备名称
	@Column(name = "SBMC")
	private String sbmc;

	// 上次检修时间
	@Column(name = "SCJXSJ")
	private String scjxsj;

	// 1月份值
	@Column(name = "MONTH1_VALUE")
	private String month1;

	// 2月份值
	@Column(name = "MONTH2_VALUE")
	private String month2;

	// 3月份值
	@Column(name = "MONTH3_VALUE")
	private String month3;

	// 4月份值
	@Column(name = "MONTH4_VALUE")
	private String month4;

	// 5月份值
	@Column(name = "MONTH5_VALUE")
	private String month5;

	// 6月份值
	@Column(name = "MONTH6_VALUE")
	private String month6;

	// 7月份值
	@Column(name = "MONTH7_VALUE")
	private String month7;

	// 8月份值
	@Column(name = "MONTH8_VALUE")
	private String month8;

	// 9月份值
	@Column(name = "MONTH9_VALUE")
	private String month9;

	// 10月份值
	@Column(name = "MONTH10_VALUE")
	private String month10;

	// 11月份值
	@Column(name = "MONTH11_VALUE")
	private String month11;

	// 12月份值
	@Column(name = "MONTH12_VALUE")
	private String month12;

	// 计划年份
	@Column(name = "JHNF")
	private String jhnf;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSbid() {
		return sbid;
	}

	public void setSbid(String sbid) {
		this.sbid = sbid;
	}

	public String getSbmc() {
		return sbmc;
	}

	public void setSbmc(String sbmc) {
		this.sbmc = sbmc;
	}

	public String getMonth1() {
		return month1;
	}

	public void setMonth1(String month1) {
		this.month1 = month1;
	}

	public String getMonth2() {
		return month2;
	}

	public void setMonth2(String month2) {
		this.month2 = month2;
	}

	public String getMonth3() {
		return month3;
	}

	public void setMonth3(String month3) {
		this.month3 = month3;
	}

	public String getMonth4() {
		return month4;
	}

	public void setMonth4(String month4) {
		this.month4 = month4;
	}

	public String getMonth5() {
		return month5;
	}

	public void setMonth5(String month5) {
		this.month5 = month5;
	}

	public String getMonth6() {
		return month6;
	}

	public void setMonth6(String month6) {
		this.month6 = month6;
	}

	public String getMonth7() {
		return month7;
	}

	public void setMonth7(String month7) {
		this.month7 = month7;
	}

	public String getMonth8() {
		return month8;
	}

	public void setMonth8(String month8) {
		this.month8 = month8;
	}

	public String getMonth9() {
		return month9;
	}

	public void setMonth9(String month9) {
		this.month9 = month9;
	}

	public String getMonth10() {
		return month10;
	}

	public void setMonth10(String month10) {
		this.month10 = month10;
	}

	public String getMonth11() {
		return month11;
	}

	public void setMonth11(String month11) {
		this.month11 = month11;
	}

	public String getMonth12() {
		return month12;
	}

	public void setMonth12(String month12) {
		this.month12 = month12;
	}

	public String getJhnf() {
		return jhnf;
	}

	public void setJhnf(String jhnf) {
		this.jhnf = jhnf;
	}

	public String getScjxsj() {
		return scjxsj;
	}

	public void setScjxsj(String scjxsj) {
		this.scjxsj = scjxsj;
	}

}
