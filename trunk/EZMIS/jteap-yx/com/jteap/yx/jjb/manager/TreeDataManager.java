package com.jteap.yx.jjb.manager;

import java.util.Collection;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.yx.jjb.model.TreeDataBean;
/**
 * 运行表单树数据处理
 * @author lvchao
 *
 */
@SuppressWarnings("unchecked")
public class TreeDataManager extends HibernateEntityDao<TreeDataBean>{

	public Collection<TreeDataBean> findTreeByParentId(String flag){
		
		StringBuffer hql=new StringBuffer("from TreeDataBean as s where ");
		Object args[]=null;
		hql.append(" s.flag=?");
		args=new String[]{flag};
		return find(hql.toString(),args);
	}
}
