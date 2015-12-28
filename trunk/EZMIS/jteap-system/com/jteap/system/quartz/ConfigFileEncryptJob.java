package com.jteap.system.quartz;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.MD5;
import com.jteap.core.utils.StringUtil;

/**
 * 定期查看配置文件中的密码是否修改，如果有修改，则将密码加密
 * @author tantyou
 * @date 2008-1-15
 */
public class ConfigFileEncryptJob {
	private Log log=LogFactory.getLog(ConfigFileEncryptJob.class);
	
	ConfigFileEncryptJob(){
	}
	
	/**
	 * 判断密码是否修改了，如果修改了，就重新进行加密
	 */
	public void encodePassword(){
		SystemConfig.reload();
		String pwd = SystemConfig.getProperty("root.password");
		if(StringUtil.isNotEmpty(pwd) && pwd.indexOf("{MD5}")<0){
			MD5 md5=new MD5();
			String md5Pwd="{MD5}"+md5.getMD5ofStr(pwd);
			SystemConfig.setProperty("root.password", md5Pwd);
			try {
				SystemConfig.save();
				log.info("平台管理员密码被修改，加密成功并重新启用");
				SystemConfig.reload();
				log.info("系统配置被修改，系统参数被重新加载 ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.gc();
	}
}
