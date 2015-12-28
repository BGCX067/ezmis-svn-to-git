package com.jteap.system.doclib.web;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
//import org.apache.derby.iapi.services.io.ArrayInputStream;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.core.web.AbstractTreeAction.TreeActionJsonBeanHandler;
import com.jteap.core.web.AbstractTreeAction.TreeActionJsonBeanProcessor;
import com.jteap.system.doclib.manager.DoclibCatalogManager;
import com.jteap.system.doclib.manager.DoclibFvManager;
import com.jteap.system.doclib.manager.DoclibLevelManager;
import com.jteap.system.doclib.manager.DoclibLevelRoleManager;
import com.jteap.system.doclib.manager.DoclibManager;
import com.jteap.system.doclib.model.Doclib;
import com.jteap.system.doclib.model.DoclibAttach;
import com.jteap.system.doclib.model.DoclibCatalog;
import com.jteap.system.doclib.model.DoclibCatalogField;
import com.jteap.system.doclib.model.DoclibFv;
import com.jteap.system.doclib.model.DoclibLevel;
import com.jteap.system.doclib.model.DoclibLevelRole;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.manager.RoleManager;
import com.jteap.system.role.model.Role;

/**
 * 文档级别操作对象
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DoclibLevelAction extends  AbstractAction {

	private DoclibManager doclibManager;
	private DoclibCatalogManager doclibCatalogManager;
	private DoclibLevelManager doclibLevelManager ;
	private DoclibLevelRoleManager doclibLevelRoleManager ; 
	private RoleManager roleManager;
	
	public DoclibCatalogManager getDoclibCatalogManager() {
		return doclibCatalogManager;
	}

	public void setDoclibCatalogManager(
			DoclibCatalogManager doclibCatalogManager) {
		this.doclibCatalogManager = doclibCatalogManager;
	}

	public DoclibManager getDoclibManager() {
		return doclibManager;
	}

	public void setDoclibManager(DoclibManager doclibManager) {
		this.doclibManager = doclibManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return doclibLevelManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "levelName","sortNo","levelDesc","levelRoles","doclibLevelRole"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "levelName", "sortNo","levelDesc"};
	}

	/**
	 * 列表显示之前针对条件和排序进行处理
	 */
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {

		
		// 添加查询分类条件
		//String catalogId = request.getParameter("catalogId");
		
		/*
		if (StringUtils.isNotEmpty(catalogId)) {
			HqlUtil.addCondition(hql, "doclibCatalog.id", catalogId);
		}*/
		
		// 默认按照创建时间倒序排序
	//	if (!this.isHaveSortField()) {
			HqlUtil.addOrder(hql, "sortNo", "desc");
	//	}
		
	}
	
	/**
	 * 显示要修改的文档级别信息
	 * @return
	 * @throws Exception
	 */
	public String showmodifyDoclibInfoAction() throws Exception {
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		DoclibLevel doclibLevel  ;
		try{
			if(StringUtils.isNotEmpty(id)){
				doclibLevel = (DoclibLevel)doclibLevelManager.get(id);
			}else{
				doclibLevel = new DoclibLevel();	
			}
			String json =  JSONUtil.objectToJson(doclibLevel, listJsonProperties());
			outputJson("{success:true,fdlist:" + json + "}");
		}catch (Exception ex) {
			outputJson("{success:false,msg:'" + ex.getMessage() + "'}");
		}
		return NONE;
	}
	


	/**
	 * 添加修改文档级别信息 2009-08-10
	 */
	public String saveUpdateAction() throws BusinessException {
		String id = request.getParameter("id");
		String levelName = request.getParameter("levelName");
		String strNo = request.getParameter("sortNo")==null?"0":request.getParameter("sortNo");
		int sortNo = Integer.parseInt(strNo);
		String levelDesc = request.getParameter("levelDesc");
		String roleId = request.getParameter("roleId");
		
		DoclibLevel doclibLevel  ;
		
		try{
			if(StringUtils.isNotEmpty(id)){
				doclibLevel = (DoclibLevel)doclibLevelManager.get(id);
				String[] roleIds = roleId.split(",");
				createL2R(roleIds,doclibLevel,true);
			}else{
				doclibLevel = new DoclibLevel();
				String[] roleIds = roleId.split(",");
				java.util.HashSet<DoclibLevelRole> levelRoles = new java.util.HashSet<DoclibLevelRole>();
				for(String sRoleId:roleIds){
					if(StringUtils.isNotEmpty(sRoleId)){
						DoclibLevelRole levelRole = new DoclibLevelRole();
						Role role=this.roleManager.get(sRoleId);
						levelRole.setRole(role);
						levelRole.setDoclibLevel(doclibLevel);
						levelRoles.add(levelRole);	
					}
				}
				doclibLevel.setLevelRoles(levelRoles);
			}
			doclibLevel.setLevelName(levelName);
			doclibLevel.setLevelDesc(levelDesc);
			doclibLevel.setSortNo(sortNo);
			doclibLevelManager.save(doclibLevel);
			outputJson("{success:true,id:'" +doclibLevel.getId() + "'}");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;
	}

	/**
	 * 方法功能描述 创建文档级别和角色集合的关联关系
	 * @param roleIds
	 * @param doclibLevel
	 * @param bClearOld
	 * @throws Exception
	 */
	private void createL2R(String roleIds[],DoclibLevel doclibLevel,boolean bClearOld)throws Exception{
		if(bClearOld){
			Set<DoclibLevelRole>  l2rset =doclibLevel.getLevelRoles();
			if(l2rset.size()>0){
				doclibLevelRoleManager.deleteByAllLevelAndRole(l2rset);
			}
		}
		for(String id:roleIds){
			if(StringUtils.isNotEmpty(id)){
				DoclibLevelRole levelRole = new DoclibLevelRole();
				Role role=this.roleManager.get(id);
				levelRole.setRole(role);
				levelRole.setDoclibLevel(doclibLevel);
				doclibLevelRoleManager.save(levelRole);
			}
		}
	}
	
	
	/**
	 * 修改文档及相关信息 2009-08-20
	 */
	public String updateDoclibAction() throws Exception {
		
	
		return NONE;
		
	}
	
	/**
	 * 删除文档级别。只有该文档级别下没有文档才可以删除。级联删除文档级别和角色的关联
	 */
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		String msg= null ;
		boolean success = false ;
		try {
			if (keys != null) {
				String[] ids = keys.split(",");
				
				for(String id : ids){
					Long count = 0L ;
					count = doclibManager.countDoclibByLevelId(id);
					if(count>0){
						 msg = "请先删除文档级别下的文档再删除文档级别！" ;
						 break ;
					}
				}
				if(msg==null){
					DoclibLevel doclibLevel = new DoclibLevel();
					this.getDoclibLevelManager().remove(doclibLevel, ids);
					success = true ;
				}
				outputJson("{success:"+success+",msg:'"+msg+"'}");
			}
		} catch (Exception ex) {
			 msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}

	public DoclibLevelManager getDoclibLevelManager() {
		return doclibLevelManager;
	}

	public void setDoclibLevelManager(DoclibLevelManager doclibLevelManager) {
		this.doclibLevelManager = doclibLevelManager;
	}

	public DoclibLevelRoleManager getDoclibLevelRoleManager() {
		return doclibLevelRoleManager;
	}

	public void setDoclibLevelRoleManager(
			DoclibLevelRoleManager doclibLevelRoleManager) {
		this.doclibLevelRoleManager = doclibLevelRoleManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}



}
