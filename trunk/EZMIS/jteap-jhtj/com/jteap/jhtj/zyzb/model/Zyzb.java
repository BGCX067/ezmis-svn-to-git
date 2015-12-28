package com.jteap.jhtj.zyzb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name="ZB_ZBJD")
public class Zyzb {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy="uuid")
	@Column(name="ID")
	@Type(type="string")
	private String id;
	@Column(name="zblx")
	private String zblx;   //指标类型      item
	@Column(name="name")   
	private String name;   //指标名称
	@Column(name="z_order")
	private String order;
	@Column(name="zbfl")
	private String zbfl;   //指标分类 1为电量 2为燃料 3为可靠性
	@Column(name="fxfs")
	private String fxfs;   //分析方式 0不做处理 1同期对比分析 2机组对比分析 3指标关联分析
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getZblx() {
		return zblx;
	}
	public void setZblx(String zblx) {
		this.zblx = zblx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getZbfl() {
		return zbfl;
	}
	public void setZbfl(String zbfl) {
		this.zbfl = zbfl;
	}
	public String getFxfs() {
		return fxfs;
	}
	public void setFxfs(String fxfs) {
		this.fxfs = fxfs;
	}
	
}
