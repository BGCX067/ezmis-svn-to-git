package com.jteap.gcht.htsp.model;
/**
 * 合同基类
 * @author 童贝
 * @date Feb 16, 2011
 */
public abstract class Ht {
	private String id;
	private String parentid;
	private String tableName;
	private String htbh;
	private String htxh;
	public final static String FLBM_WZ="WZ";
	public final static String FLBM_RL="RL";
	public final static String FLBM_GC="GC";
	public final static String FLBM_CW="CW";
	public final static String CWHT_TABLE="TB_HT_CWHT";
	public final static String WZHT_TABLE="TB_HT_WZHT";
	public final static String RLHT_TABLE="TB_HT_RLHT";
	public final static String GCHT_TABLE="TB_HT_GCHT";
	public String getHtbh() {
		return htbh;
	}
	public void setHtbh(String htbh) {
		this.htbh = htbh;
	}
	public String getHtxh() {
		return htxh;
	}
	public void setHtxh(String htxh) {
		this.htxh = htxh;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
