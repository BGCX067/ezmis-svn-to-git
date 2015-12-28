package com.jteap.jx.bpbjgl.manager;

import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.jx.bpbjgl.model.BpbjZyfl;

/**
 * 备品备件-专业分类manager
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "unchecked" })
public class BpbjZyflManager extends HibernateEntityDao<BpbjZyfl> {

	/**
	 * 
	 * 描述 : 根据父节点ID 获得所有根节点
	 * 作者 : wangyun
	 * 时间 : Aug 16, 2010
	 * 参数 : 
	 * 		parentId : 父节点ID
	 * 返回值 : List<BpbjZyfl>
	 * 
	 */
	public List<BpbjZyfl> findRoots(String parentId) {
		String hsql = "from BpbjZyfl t";
		Object[] args = null;
		if (StringUtil.isEmpty(parentId)) {
			hsql += " where t.parentZyfl=null";
		} else {
			hsql += " where t.parentZyfl.id=?";
			args = new String[] { parentId };
		}
		hsql += " order by t.sortNo";
		return this.find(hsql, args);
	}
}
