package com.jteap.jx.dxxgl.manager;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jx.dxxgl.model.GyxtjxjhJl;

/**
 * 公用系统检修-记录manager
 * 
 * @author wangyun
 *
 */
public class GyxtjxjhJlManager extends HibernateEntityDao<GyxtjxjhJl> {

	/**
	 * 
	 * 描述 : 根据设备、月份、计划年份找到记录
	 * 作者 : wangyun
	 * 时间 : Aug 20, 2010
	 * 参数 : 
	 * 		sbid ： 设备ID
	 * 		monthValue ： 月份
	 * 		jhnf ： 计划年份
	 * 返回值 : 
	 * 异常 :
	 */
	public GyxtjxjhJl findByGyxtjxjh(String sbid, String month, String jhnf) {
		String hql = "from GyxtjxjhJl where sbid=? and jlyf=? and jhnf=?";
		Object obj = this.findUniqueByHql(hql, new Object[] {sbid, month, jhnf});
		GyxtjxjhJl gyxtjxjhJl = null;
		if (obj != null) {
			gyxtjxjhJl = (GyxtjxjhJl)obj;
		}
		return gyxtjxjhJl;
	}

}
