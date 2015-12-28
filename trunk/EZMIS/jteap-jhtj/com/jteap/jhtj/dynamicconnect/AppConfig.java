package com.jteap.jhtj.dynamicconnect;


import com.jteap.core.support.SystemConfig;


public class AppConfig {
	private String conDriverName;
	private String conUrl;
	private String conUserName;
	private String conUserPwd;
	private String maxConnection="100";
	private static AppConfig instance = null;
	public static AppConfig getInstance() {
        if (instance == null) {
            instance =new AppConfig();
        }
        return (instance);
    }
	
	private AppConfig(){
		this.conDriverName=SystemConfig.getProperty("jdbc.driverClassName");
		this.conUrl=SystemConfig.getProperty("jdbc.url");
		this.conUserName=SystemConfig.getProperty("jdbc.username");
		this.conUserPwd=SystemConfig.getProperty("jdbc.password");
	}
	
	public String getConDriverName() {
		return conDriverName;
	}
	public void setConDriverName(String conDriverName) {
		this.conDriverName = conDriverName;
	}
	public String getConUrl() {
		return conUrl;
	}
	public void setConUrl(String conUrl) {
		this.conUrl = conUrl;
	}
	public String getConUserName() {
		return conUserName;
	}
	public void setConUserName(String conUserName) {
		this.conUserName = conUserName;
	}
	public String getConUserPwd() {
		return conUserPwd;
	}
	public void setConUserPwd(String conUserPwd) {
		this.conUserPwd = conUserPwd;
	}
	public String getMaxConnection() {
		return maxConnection;
	}
	public void setMaxConnection(String maxConnection) {
		this.maxConnection = maxConnection;
	}
	
}
