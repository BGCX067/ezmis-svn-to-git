package com.jteap.jx.dxxgl.manager;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jx.dxxgl.model.Jzjxjh;
import com.jteap.jx.dxxgl.model.Jzjxxm;

/**
 * 机组检修项目manager
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"unchecked"})
public class JzjxxmManager extends HibernateEntityDao<Jzjxxm> {

	/**
	 * 
	 * 描述 : 根据所属专业和计划查找项目
	 * 作者 : wangyun
	 * 时间 : Aug 11, 2010
	 * 参数 : 
	 * 		value ： 所属专业
	 * 		jzjxjh ： 机组检修计划
	 * 返回值 : 
	 * 异常 :
	 */
	public List<Jzjxxm> findBySszyAndJh(String value, Jzjxjh jzjxjh) {
		String hql = "from Jzjxxm t where t.jzjxjh = ? and t.sszy=?";
		List<Jzjxxm> lst = this.find(hql, new Object[] {jzjxjh, value});
		return lst;
	}

}
