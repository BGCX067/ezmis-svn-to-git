package com.jteap.form.eform.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.system.jdbc.manager.JdbcManager;
/**
 * EFormExp Velocity 模板公式计算
 * @author 谭畅
 *
 */
@SuppressWarnings("unchecked")
public class EFormExpVelocityCF {
	
	
	private Map contextMap;
	
	public void setContextMap(Map contextMap) {
		this.contextMap = contextMap;
	}
	
	/**
	 * sql计算公式
	 * @param sql
	 * @return
	 */
	public String sql(String sql){
		JdbcManager jdbcManager = (JdbcManager) SpringContextUtil.getBean("jdbcManager");
		Object obj = jdbcManager.queryUniqueBySql(sql);
		return obj!=null?obj.toString():"";
	}
	
	/**
	 * 取得环境变量公式
	 * @param key
	 * @return
	 */
	public String context(String key){
		Object obj = contextMap.get(key);
		return obj!=null?obj.toString():"";
	}
	
	/**
	 * 系统日期格式化输出公式
	 * @param fm
	 * @return
	 */
	public String sysdt(String fm){
		return DateUtils.getDate(fm);
	}
	
	/**
	 * 描述 : 生成uuid公式
	 * 作者 : caofei
	 * 时间 : Oct 22, 2010
	 * 参数 : 
	 * 返回值 : uuid
	 * 异常 :
	 */
	public String uuid(){
		return UUIDGenerator.hibernateUUID();
	}

	/**
	 * 
	 * 描述 : 带偏移量的系统日期格式化输出公式
	 * 作者 : wangyun
	 * 时间 : 2010-10-11
	 * 参数 : 
	 * 		fm ： 格式化格式
	 * 		offset ： 偏移量
	 * 		unit ： 单位 （y：年， M：月， d：日， H：时 m：分 s：秒）
	 * 返回值 : 
	 * 异常 :
	 */
	public String sysdt(String fm,int offset,String unit){
		Calendar cc = Calendar.getInstance();
		if ("y".equals(unit)) {
			cc.add(Calendar.YEAR, offset);
		} else if ("M".equals(unit)) {
			cc.add(Calendar.MONTH, offset);
		} else if ("d".equals(unit)) {
			cc.add(Calendar.DAY_OF_YEAR, offset);
		} else if ("H".equals(unit)) {
			cc.add(Calendar.HOUR_OF_DAY, offset);
		} else if ("m".equals(unit)) {
			cc.add(Calendar.MINUTE, offset);
		} else if ("s".equals(unit)) {
			cc.add(Calendar.SECOND, offset);
		}
		if (StringUtil.isEmpty(fm)) {
			fm = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(fm);
		String timestr = sdf.format(cc.getTime());
		return timestr;
	}
}
