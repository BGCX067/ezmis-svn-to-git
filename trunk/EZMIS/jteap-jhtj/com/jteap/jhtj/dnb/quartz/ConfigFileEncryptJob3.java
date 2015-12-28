package com.jteap.jhtj.dnb.quartz;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jteap.core.utils.DateUtils;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.jhtj.dnb.manager.DnbBbManager;

/**
 * @author caihuiwen
 */
public class ConfigFileEncryptJob3 {
	private Log log=LogFactory.getLog(ConfigFileEncryptJob3.class);
	
	ConfigFileEncryptJob3(){
	}
	
	/**
	 * 定期保存电能表每天24点的有功增量总
	 * @throws SQLException 
	 */
	public void saveDnbYgzlz() throws SQLException{
		DnbBbManager dnbBbManager = (DnbBbManager)SpringContextUtil.getBean("dnbBbManager");
		if(dnbBbManager != null){
			dnbBbManager.saveDnbDateYgzlz(DateUtils.getDate("yyyy-MM-dd"));
			log.info("保存电能表每天24点的有功增量总");
		}
	}
}
