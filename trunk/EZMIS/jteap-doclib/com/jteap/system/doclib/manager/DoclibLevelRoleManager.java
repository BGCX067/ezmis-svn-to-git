package com.jteap.system.doclib.manager;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.system.doclib.model.DoclibCatalog;
import com.jteap.system.doclib.model.DoclibLevel;
import com.jteap.system.doclib.model.DoclibLevelRole;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.model.R2R;
import com.jteap.system.role.model.Role;

/**
 * 文档级别角色管理对象
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "unchecked", "unused" })
public class DoclibLevelRoleManager extends HibernateEntityDao<DoclibLevelRole> {

	
	public Collection findDoclibLevelRoleByDoclibId(String doclibLevelId){
		
		StringBuffer hql = new StringBuffer("from DoclibLevelRole as d where d.doclibLevel.id=?");
		Object args[] = null;
		args[0] = doclibLevelId;
		return find(hql.toString(), args);
		
	}
	
	
	/**
	 * 方法功能描述 :移除文档级别与角色之间的所有关联
	 * @param p2r
	 * @throws Exception
	 */
	public void deleteByAllLevelAndRole(Set<DoclibLevelRole> l2r)throws Exception{
		
		//用于取出所有的ID号,进行批量删除
		String l2rIds[]=new String[l2r.size()];
		Iterator<DoclibLevelRole> it=l2r.iterator();
		int i=0;
		while(it.hasNext()){
			l2rIds[i]=it.next().getId();
			i++;
		}
		removeBatch(l2rIds);
	}	
	
	/**
	 * 
	 * @param roleSet
	 * @return
	 */
	public Collection<DoclibLevelRole> findDoclibLevelByRoleList(Set<P2Role> roleSet){
		
		StringBuffer inWhere = new StringBuffer();
		//Set<P2Role> roleSet = person.getRoles();
		// 找到角色集合
		int intCount = 1;
		for (P2Role p2r : roleSet) {
			Role role = p2r.getRole();
			inWhere.append("'");
			inWhere.append(role.getId());
			inWhere.append("'");
			if(intCount < roleSet.size()){
				inWhere.append(",");
			}
			intCount++ ;
		}
		String strIn = inWhere.toString();
		if(strIn==null || strIn.equals("")){
			strIn = "''";
		}
		StringBuffer hql = new StringBuffer("from DoclibLevelRole as obj ");
		HqlUtil.addCondition(hql,"role.id",strIn,HqlUtil.LOGIC_AND,HqlUtil.TYPE_IN );
		Object args[] = null;
		return find(hql.toString(), args);
		
	}
	
	
}
