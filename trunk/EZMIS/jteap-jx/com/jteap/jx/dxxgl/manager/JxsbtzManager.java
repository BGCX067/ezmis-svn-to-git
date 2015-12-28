package com.jteap.jx.dxxgl.manager;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jx.dxxgl.model.Jxsbtz;

/**
 * 检修设备台账manager
 * 
 * @author wangyun
 *
 */
@SuppressWarnings({"serial", "unchecked"})
public class JxsbtzManager extends HibernateEntityDao<Jxsbtz> {

	/**
	 * 
	 * 描述 : 找出所属专业最大sortNo
	 * 作者 : wangyun
	 * 时间 : Aug 17, 2010
	 * 参数 : 
	 * 		zyId : 所属专业
	 * 返回值 : 
	 * 异常 :
	 */
	public int findMaxSortNoBySszy(String zyId) {
		String hql = "select max(sortNo) from Jxsbtz where sszy=?";
		List<Jxsbtz> lst = this.find(hql, zyId);
		if (lst.get(0) != null) {
			return DataAccessUtils.intResult(lst);
		}
		return 0;
	}

	/**
	 * 
	 * 描述 : 根据所属专业获得设备台账信息
	 * 作者 : wangyun
	 * 时间 : Aug 18, 2010
	 * 参数 : 
	 * 		zyId ： 所属专业
	 * 返回值 : 
	 * 		List<Jxsbtz>
	 * 
	 */
	public List<Jxsbtz> findBySszy(String zyId) {
		String hql = "from Jxsbtz where sszy=? order by sortNo";
		List<Jxsbtz> lst = this.find(hql, zyId);
		return lst;
	}
	
}
