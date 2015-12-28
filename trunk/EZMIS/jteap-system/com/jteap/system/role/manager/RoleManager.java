package com.jteap.system.role.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.model.Role;

public class RoleManager extends HibernateEntityDao<Role> {
	/**
	 * 取得当前角色树
	 * @author 唐剑钢
	 * 2008-1-15
	 */
	@SuppressWarnings("unchecked")
	public Collection<Role> findRootRoles(){
		String hql="from Role as z where z.parentRole=null order by z.sortNo";
		return  this.find(hql);
	}
	/**
	 * 清除集合缓存，主要在删除角色是使用
	 */
	public void evitRoles() {
		this.getSessionFactory().evict(Role.class);
		this.getSessionFactory().evictCollection("com.jteap.system.person.model.Person.roles");
		this.getSessionFactory().evictCollection("com.jteap.system.role.model.Role.childRoles");
		this.getSessionFactory().evictQueries();
	}

	
//	/**
//	 *  取得指定角色的资源集合
//	 * @author 唐剑钢
//	 * 2008-1-15
//	 */
//	public Collection<Resource> findResourcesByRole(Role role){
//		ArrayList<Resource> resources=new ArrayList<Resource>();
//		for (R2R r2r  : role.getResources()) {
//			resources.add(r2r.getResource());
//		}
//		return resources;
//	}
	

	
	
	
	
	/**
	 * 取得指定人员创建的根角色
	 * @param person
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<Role> findRootRolesOfThePerson(Person person){
		String hql="from Role as role where role.creator=? and role.parentRole=null order by role.sortNo";
		return this.find(hql, person.getUserLoginName());
	}

	/**
	 * 方法功能描述 :
	 * @author 唐剑钢
	 * @param person
	 * @return
	 * 2008-1-27
	 * 返回类型：Collection<Role>
	 */
	public 	Collection<Role> findRoleByPerson(Person person){
		   ArrayList<Role> al=new ArrayList<Role>();
		   Set<P2Role> p2r=person.getRoles();
		    for(P2Role pr:p2r){
		    	Role role=pr.getRole();
		    	al.add(role);
		    }
		   return al;
	}
	
	/**
	 * 获得指定角色的对象
	 */
	public Role findRoleByRoleName(String roleName){
		return this.findUniqueBy("rolename", roleName);
	}
	
	
}
