/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.system.dataperm.manager;

import java.io.Serializable;
import java.util.*;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.ArrayUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.system.dataperm.model.*;
import com.jteap.system.group.model.Group;
import com.jteap.system.person.model.P2G;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.model.Role;
@SuppressWarnings({ "serial", "unchecked", "unused" })
public class DatapermManager extends HibernateEntityDao<DataPerm>{ 
	public static String PERSON="person";
	public static String GROUP="group";
	public static String DISSOCIATION_YES="yes";
//	public Collection<DataPerm> findDatapermByParentId(String parentId){
//		
//		StringBuffer hql=new StringBuffer("from Dataperm as g where ");
//		Object args[]=null;
//		if(StringUtil.isEmpty(parentId)){
//			hql.append("g.parent is null");
//			
//		}else{
//			hql.append("g.parent.id=?");
//			args=new String[]{parentId};
//		}
//		return find(hql.toString(),args);
//	}
	
	/**
	 * 功能说明:英文名是否存在
	 * @author 童贝
	 * @version Dec 1, 2009
	 * @param name
	 * @return
	 */
	public boolean isExistName(String name){
		if(StringUtil.isEmpty(name)){
			return false;
		}
		boolean result=true;
		String hql="from DataPerm p where datapermname=?";
		List list=this.find(hql, new Object[]{name});
		if(list.size()>0){
			result=false;
		}
		return result;
	}

	/**
	 * 功能说明:关联用户和权限信息
	 * @author 童贝
	 * @version Dec 3, 2009
	 * @param personOrGruop 标识选择的是组织还是用户
	 * @param datapermids 权限列表
	 * @param users 用户或者组织
	 * @param dissociation 是否是游离(yes标识游离,no不是游离)
	 * @return
	 */
	public boolean datapermJoinPerson(String datapermids,String users){
		boolean result=false;
		//如果选的是人员
		String[] userArray=users.split(",");
		//支持批量设置
		for(String user:userArray){
			Person curPerson= this.get(Person.class, user);
			removeBatchInHql(Perm2Person.class, "obj.person.id='"+curPerson.getId()+"'");
			String[] dps=datapermids.split(",");
			for(String datapermid:dps){
				if(StringUtil.isNotEmpty(datapermid)){
					DataPerm dataperm=this.get(datapermid);
					Perm2Person p2p = new Perm2Person();
					p2p.setPerm(dataperm);
					p2p.setPerson(curPerson);
					this.save(p2p);
					result=true;
				}
			}
		}
		return result;
	}
	
	/**
	 * 功能说明:关联角色和权限
	 * @author 童贝
	 * @version Dec 4, 2009
	 * @param datapermids
	 * @param roleid
	 * @return
	 */
	public boolean datapermJoinRole(String datapermids,String roleid){
		boolean result=false;
		Role role = this.get(Role.class,roleid);
		removeBatchInHql(Perm2Role.class, "obj.role.id='"+role.getId()+"'");
		String[] dps=datapermids.split(",");
		for(String datapermid:dps){
			if(StringUtil.isNotEmpty(datapermid)){
				DataPerm dataperm=this.get(datapermid);
				Perm2Role p2r = new Perm2Role();
				p2r.setPerm(dataperm);
				p2r.setRole(role);
				this.save(p2r);
				result=true;
			}
		}
		return result;
	}
	
//	
	
	/**
	 * 根据人员编号查询该人员已被授予的数据权限
	 */
	public Map<String,DataPerm> getDatapermByPersonId(String personid){
		Map<String, DataPerm> resultMap=new HashMap<String, DataPerm>();
		String hql = "from Perm2Person p2p where p2p.person.id=?";
		List<Perm2Person> p2ps = this.find(hql, personid);
		for (Perm2Person p2p : p2ps) {
			resultMap.put(p2p.getPerm().getId().toString(), p2p.getPerm());
		}
		return resultMap;
	}
	
//
//
	/**
	 * 根据角色编号查询该角色所拥有的所有数据权限对象
	 */
	public Map<String, DataPerm> getDatapermByRoleId(String roleid){
		Map<String, DataPerm> resultMap=new HashMap<String, DataPerm>();
		String hql = "from Perm2Role pr where pr.role.id = ?";
		List<Perm2Role> p2rs = this.find(hql, roleid);
		for (Perm2Role p2r : p2rs) {
			resultMap.put(p2r.getPerm().getId().toString(), p2r.getPerm());
		}
		return resultMap;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	private Set<DataPerm> findPermByRoleId(String id) {
		String hql = "from DataPerm p where p";
		return null;
	}
	
	private Set<DataPerm> findPermByPersonId(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 查询排序号最大值
	 * @return
	 */
	public long findMaxOrder(){
		String hql = "select max(dorder) from DataPerm p";
		List<Long> resultList = this.find(hql);
		long max = 0;
		if(resultList.size()>0){
			Long max2 = resultList.get(0);
			if(max2!=null)
				max = max2;
		}
		return max;
	}
	
	
}
