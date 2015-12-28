package com.jteap.system.person.manager;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.model.Role;
/**
 * person to role 人员角色中间对象管理器
 * 
 * @author tantyou
 * @date 2008-1-30
 */
@SuppressWarnings({"unchecked"})
public class P2RoleManager extends HibernateEntityDao<P2Role> {

//	/**
//	 * 
//	 * 方法功能描述 :移除指定人员与角色的关联
//	 * @author 唐剑钢
//	 * @param personId
//	 * @param roleId
//	 * 2007-12-24
//	 * 返回类型：void
//	 */
//	public void deleteByPersonAndRole(String personId,String roleId)throws Exception{
//		String hql="delete P2Role as p2r where p2r.person.id='"+personId+"' and p2r.role.id='"+roleId+"'";
//		Query query=this.createQuery(hql);
//		query.executeUpdate();
//	}
//	/**
//	 * 
//	 * 方法功能描述 :移除指定人员与角色的关联
//	 * @author 唐剑钢
//	 * @param person
//	 * @param role
//	 * 2007-12-24
//	 * 返回类型：void
//	 */
//	public void deleteByPersonAndRole(Person person,Role role)throws Exception{
//		String hql="delete P2Role as p2r where p2r.person=? and p2r.role=?";
//		Query query=this.createQuery(hql,person,role);
//		query.executeUpdate();
//	}
	

	
	/**
	 * 
	 * 方法功能描述 :移除指定人员与角色之间的所有关联
	 * @author 唐剑钢
	 * @param p2r
	 * @throws Exception
	 * 2007-12-24
	 * 返回类型：void
	 */
	public void deleteByAllPersonAndRole(Set<P2Role> p2r)throws Exception{
		
		//用于取出所有的ID号,进行批量删除
		String p2rIds[]=new String[p2r.size()];
		Iterator<P2Role> it=p2r.iterator();
		int i=0;
		while(it.hasNext()){
			p2rIds[i]=it.next().getId();
			i++;
		}
		removeBatch(p2rIds);
	}
	/**
	 * 
	 * 方法功能描述 :指定角色的时候，判断是否已经指定了这个角色
	 * @author 唐剑钢
	 * @return
	 * 2008-2-19
	 * 返回类型：boolean
	 */
	public boolean isExist(Person person,Role role){
		String hql="from P2Role as p2r where p2r.person.id='"+person.getId()+"' and p2r.role.id='"+role.getId()+"'";
		return this.find(hql).size()>0;
	}
	
	/**
	 * 
	 * 描述 : 根据角色和人员得到p2role
	 * 作者 : wangyun
	 * 时间 : Aug 15, 2010
	 * 参数 : 
	 * 返回值 : 
	 * 异常 :
	 */
	public P2Role findByPersonAndRole(Person person,Role role){
		String hql="from P2Role as p2r where p2r.person.id='"+person.getId()+"' and p2r.role.id='"+role.getId()+"'";
		List<P2Role> lst = this.find(hql);
		if (lst.size() > 0) {
			return lst.get(0);
		}
		return null;
	}
	
}
