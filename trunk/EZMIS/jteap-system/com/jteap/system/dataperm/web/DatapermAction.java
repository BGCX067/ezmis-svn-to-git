/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.system.dataperm.web;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.dataperm.manager.DatapermManager;
import com.jteap.system.dataperm.model.DataPerm;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.system.jdbc.model.FieldInfo;
import com.jteap.system.person.manager.P2GManager;
import com.jteap.system.role.manager.RoleManager;

@SuppressWarnings({ "unchecked", "serial" })
public class DatapermAction extends AbstractAction{

	private DatapermManager datapermManager;
	
	private JdbcManager jdbcManager;
	
	private P2GManager p2gManager;
	
	private RoleManager roleManager;
	

	public JdbcManager getJdbcManager() {
		return jdbcManager;
	}

	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public P2GManager getP2gManager() {
		return p2gManager;
	}

	public void setP2gManager(P2GManager manager) {
		p2gManager = manager;
	}

	public DatapermManager getDatapermManager() {
		return datapermManager;
	}

	public void setDatapermManager(DatapermManager datapermManager) {
		this.datapermManager = datapermManager;
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return datapermManager;
	}
	
	@Override
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		if(StringUtil.isNotEmpty(keys)){
			String ids[] = keys.split(",");
			for (String id : ids) {
				if(StringUtil.isEmpty(id))
					continue;
				this.getManager().removeById(id);
			}
		}
		outputJson("{success:true}");
		return NONE;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		// TODO Auto-generated method stub
//		super.beforeShowList(request, response, hql);
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "dorder", "asc");
		}
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id",
			"datapermname",
			"datapermcname",
			"sql",
			"qualification",
			"tablename",
			"dorder",
		""};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id",
			"datapermname",
			"datapermcname",
			"sql",
			"qualification",
			"tablename",
			"dorder",
		""};
	}
	
	/**
	 * 功能说明:动态查询列
	 * @author 童贝
	 * @version Nov 27, 2009
	 * @return
	 * @throws Exception
	 */
	public String findDynaColumns() throws Exception{
		StringBuffer result=new StringBuffer();
		String tablename=request.getParameter("tablename");
		//查询字段列表，并保存字段定义对象,存在大小写的问题
		
		List<FieldInfo> list=jdbcManager.findDefColumnInfoListByTable(tablename.toUpperCase());
		int i=0;
		//循环创建字段定义对象
		for (FieldInfo defColumnInfo : list) {
			result.append("{'"+defColumnInfo.getColumncode()+"':'"+defColumnInfo.getColumncode()+"'");
			if(i==list.size()-1){
				result.append("}");
			}else{
				result.append("},");
			}
			i++;
		}
		if(i==0){
			this.outputJson("{success:true,list:[]}");
		}else{
			//System.out.println(result);
			this.outputJson("{success:true,list:["+result+"]}");
		}
		return NONE;
	}
	
	/**
	 * 功能说明:动态查找数据
	 * @author 童贝
	 * @version Nov 30, 2009
	 * @return
	 * @throws Exception
	 */
	public String findDynaData() throws Exception{
		String sql = request.getParameter("sql");
		String where =request.getParameter("where");
		boolean isHaveWhere = false;
		if(StringUtil.isNotEmpty(where)){
			sql=sql+" where "+where;
			isHaveWhere =true;
		}
		sql = sql + (isHaveWhere?" and ":" where ") +" rownum<50";
		
		//System.out.println(sql);
		String json=null;
		try{
//			this.getPhysicTableManager(null);
			List list=jdbcManager.querySqlData(sql);
			json=JSONUtil.listToJson(list);
			json="{success:true,totalCount:'" + list.size() + "',list:"+ json + "}";
			//System.out.println(json);
			outputJson(json);
		}catch(Exception ex){
			//json="{success:false,totalCount:'0',list:[]}";
			outputJson("{success:false}");
			//throw ex;
		}
		return NONE;
	}
	
	
	/**
	 * 功能说明:保存之前的权限名验证
	 * @author 童贝
	 * @version Dec 1, 2009
	 * @return
	 * @throws Exception
	 */
	public String beforeSave() throws Exception {
		String datapermname=request.getParameter("datapermname");
		boolean result=this.datapermManager.isExistName(datapermname);
		outputJson("{success:"+result+"}");
		return NONE;
	}
	
	@Override
	protected void beforeSaveUpdate(HttpServletRequest request,
			HttpServletResponse response, Object obj, Object originalObject) {
		DataPerm dm=(DataPerm)obj;
		if(dm.getDorder()==0l){
			dm.setDorder(this.datapermManager.findMaxOrder()+1);
		}
	}
	
	/**
	 * 功能说明:保存用户与权限之间的关联关系
	 * @author 童贝
	 * @version Dec 3, 2009
	 * @return
	 * @throws Exception
	 */
	public String saveDatapermAndPersonAction() throws Exception{
		try{
			String datapermids=request.getParameter("datapermids");
			String users=request.getParameter("users");
			this.datapermManager.datapermJoinPerson(datapermids, users);
			outputJson("{success:true}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'关联出错!'}");
			throw ex;
		}
		return NONE;
	}
	
	/**
	 * 功能说明:保存权限和角色之间关联关系
	 * @author 童贝
	 * @version Dec 4, 2009
	 * @return
	 * @throws Exception
	 */
	public String saveDatapermAndRoleAction() throws Exception{
		try{
			String datapermids=request.getParameter("datapermids");
			String roleid=request.getParameter("roleid");
			this.datapermManager.datapermJoinRole(datapermids,roleid);
			outputJson("{success:true}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'关联出错!'}");
			throw ex;
		}
		return NONE;
	}
//	
//	/**
//	 * 根据人员ID删除和人员有关的数据权限
//	 * @author:童贝
//	 * @version Jan 7, 2010
//	 * @return
//	 */
//	public String deleteDatapermByPersonId() throws Exception{
//		String personid=request.getParameter("personid");
//		P2G p2g = this.p2gManager.get(personid);
//		Person person=p2g.getPerson();
//		person.getDataperms().clear();
//		this.p2gManager.save(person);
//		outputJson("{success:true}");
//		return NONE;
//	}
//	
//	/**
//	 * 根据角色ID删除和角色有关的数据权限
//	 * @author:童贝
//	 * @version Jan 7, 2010
//	 * @return
//	 */
//	public String deleteDatapermByRoleId() throws Exception{
//		String roleid=request.getParameter("roleid");
//		Role role=this.roleManager.get(roleid);
//		role.getDataperms().clear();
//		this.roleManager.save(role);
//		outputJson("{success:true}");
//		return NONE;
//	}
}

