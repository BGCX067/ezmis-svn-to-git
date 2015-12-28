package com.jteap.system.person.manager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jteap.core.Constants;
import com.jteap.core.utils.MD5;
import com.jteap.core.utils.PropertiesUtil;
import com.jteap.system.person.model.Person;

/**
 * 平台管理员管理类
 * @author tantyou
 * @date 2007-12-10
 */
public class RootManager{
	private File configFile;
	
	RootManager(){
		URL url=this.getClass().getClassLoader().getResource(Constants.DEFAULT_CONFIG_FILE);
		String fileName=url.getFile();
		Pattern p = Pattern.compile("%20", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(fileName);
		fileName=m.replaceAll(" ");
		configFile=new File(fileName);
	}
	
	/**
	 * 验证平台管理员的密码是否正确
	 * @param password
	 * @return
	 * @throws IOException
	 */
	public Person validate(String password) throws IOException{
		MD5 md5=new MD5();
		PropertiesUtil props=new PropertiesUtil(configFile,"UTF-8");
		String md5Pwd=md5.getMD5ofStr(password);
		String pwd=props.getProperties("root.password");
		int idx=pwd.indexOf("{MD5}");
		if(idx>=0){
			pwd=pwd.substring(idx+5).trim();
		}
		if(md5Pwd.equals(pwd)){
			Person person=new Person();
			person.setId(Constants.ADMINISTRATOR_ID);
			person.setUserName(Constants.ADMINISTRATOR_NAME);
			person.setUserLoginName(Constants.ADMINISTRATOR_ACCOUNT);
			return person;
		}
		return null;
	}
	
	/**
	 * 验证平台管理员的密码(MD5格式)是否正确
	 * @param password
	 * @return
	 * @throws IOException
	 */
	public Person validateMd5(String password) throws IOException{
		PropertiesUtil props=new PropertiesUtil(configFile,"UTF-8");
		String pwd=props.getProperties("root.password");
		int idx=pwd.indexOf("{MD5}");
		if(idx>=0){
			pwd=pwd.substring(idx+5).trim();
		}
		if(password.equals(pwd)){
			Person person=new Person();
			person.setId(Constants.ADMINISTRATOR_ID);
			person.setUserName(Constants.ADMINISTRATOR_NAME);
			person.setUserLoginName(Constants.ADMINISTRATOR_ACCOUNT);
			return person;
		}
		return null;
	}

	
}