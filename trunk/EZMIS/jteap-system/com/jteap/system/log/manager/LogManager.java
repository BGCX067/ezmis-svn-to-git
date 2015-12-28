package com.jteap.system.log.manager;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.log.model.Log;


/**
 * 日志的管理类
 * @author 朱启亮
 * @date 2008-1-15
 */
public class LogManager extends HibernateEntityDao<Log> {
	/**
	 * 删除某一类型的日志数据
	 * @param type
	 * @author 朱启亮
	 * @date 2008-1-15
	 */
	public void removeLogsByType(String type){
		String hql = "delete Log as l where l.type=?";
		this.createQuery(hql, type).executeUpdate();
	}
}
