package com.jteap.jx.dxxgl.manager;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jx.dxxgl.model.GyxtjxjhBj;

/**
 * 公用系统检修计划-标记manager
 * 
 * @author wangyun
 *
 */
public class GyxtjxjhBjManager extends HibernateEntityDao<GyxtjxjhBj>{

	/**
	 * 
	 * 描述 : 根据设备、月份、计划年份找到标记
	 * 作者 : wangyun
	 * 时间 : Aug 20, 2010
	 * 参数 : 
	 * 		sbid ： 设备ID
	 * 		monthValue ： 月份
	 * 		jhnf ： 计划年份
	 * 返回值 : 
	 * 异常 :
	 */
	public GyxtjxjhBj findByGyxtjxjh(String sbid, String month, String jhnf) {
		String hql = "from GyxtjxjhBj where sbid=? and bjyf=? and jhnf=?";
		Object obj = this.findUniqueByHql(hql, new Object[] {sbid, month, jhnf});
		GyxtjxjhBj gyxtjxjhBj = null;
		if (obj != null) {
			gyxtjxjhBj = (GyxtjxjhBj)obj;
		}
		return gyxtjxjhBj;
	}

}
