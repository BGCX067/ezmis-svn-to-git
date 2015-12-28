package com.jteap.jhtj.sjxxxdy.model;
/**
 * 
 *描述：用于封装其他系统字段信息
 *时间：2010-4-12
 *作者：童贝
 *
 */
public class OtherSystem {
	private String systemName;
	private String server;
	private String dbName;
	private String userId;
	private String userPwd;
	
	private String vname;  //数据源名称
	private String cvname; //数据源中文名
	private String sqlstr; //sql语句
	private String fname;
	private String cfname;
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getCvname() {
		return cvname;
	}
	public void setCvname(String cvname) {
		this.cvname = cvname;
	}
	public String getSqlstr() {
		return sqlstr;
	}
	public void setSqlstr(String sqlstr) {
		this.sqlstr = sqlstr;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getCfname() {
		return cfname;
	}
	public void setCfname(String cfname) {
		this.cfname = cfname;
	}
	
}
