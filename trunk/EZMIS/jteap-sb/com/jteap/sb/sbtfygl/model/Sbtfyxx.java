package com.jteap.sb.sbtfygl.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


/**
 * 设备停复役信息
 * @author nicky
 *
 */
@Entity
@Table(name = "TB_SB_TFYGL_SBTFYXX")
public class Sbtfyxx {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Type(type = "string")
	private String id;
	
	@Column(name = "jz")
	private String jz;
	
	@Column(name = "jzzt")
	private String jzzt;
	
	@Column(name = "tfysj")
	private Date tfysj;
	
	@Column(name = "syr")
	private String syr;
	
	@Column(name = "syyj")
	private String syyj;
	
	@Column(name = "sysj")
	private Date sysj;
	
	@Column(name = "remark")
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJz() {
		return jz;
	}

	public void setJz(String jz) {
		this.jz = jz;
	}

	public String getJzzt() {
		return jzzt;
	}

	public void setJzzt(String jzzt) {
		this.jzzt = jzzt;
	}

	public Date getTfysj() {
		return tfysj;
	}

	public void setTfysj(Date tfysj) {
		this.tfysj = tfysj;
	}

	public String getSyr() {
		return syr;
	}

	public void setSyr(String syr) {
		this.syr = syr;
	}

	public String getSyyj() {
		return syyj;
	}

	public void setSyyj(String syyj) {
		this.syyj = syyj;
	}

	public Date getSysj() {
		return sysj;
	}

	public void setSysj(Date sysj) {
		this.sysj = sysj;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
