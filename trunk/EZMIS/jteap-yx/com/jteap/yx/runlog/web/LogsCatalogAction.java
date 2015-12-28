/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.runlog.web;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.yx.runlog.manager.LogsCatalogManager;
import com.jteap.yx.runlog.model.LogsCatalog;

/**
 * 运行日志分类Action
 * @author caihuiwen
 */
@SuppressWarnings({ "unchecked", "serial" })
public class LogsCatalogAction extends AbstractTreeAction<LogsCatalog>{

	private LogsCatalogManager logsCatalogManager;
	
	public void setLogsCatalogManager(LogsCatalogManager logsCatalogManager) {
		this.logsCatalogManager = logsCatalogManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		LogsCatalog logsCatalog = (LogsCatalog)bean;
		return logsCatalog.getChildren();
	}
	
	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId=request.getParameter("parentId");
		return logsCatalogManager.findCatalogByParentId(parentId);
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortNo";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "catalogName";
	}

	@Override
	public HibernateEntityDao getManager() {
		return logsCatalogManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","catalogName"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","catalogName"
		};
	}
	
	/**
	 * 新建分类节点动作
	 * @return
	 * @throws Exception 
	 */
	public String saveOrUpdateCatalogAction() throws Exception{
		String catalogName=request.getParameter("nodeName");
		String parentId=request.getParameter("parentId");
		String id=request.getParameter("id");
		
		try{
			LogsCatalog catalog=null;
			
			if(StringUtils.isNotEmpty(id)){
				catalog=logsCatalogManager.get(id);
			}else{
				catalog=(LogsCatalog) this.creatBlankObject();
				if(StringUtils.isNotEmpty(parentId)){
					LogsCatalog parentCatalog=logsCatalogManager.get(parentId);
					catalog.setParent(parentCatalog);
				}
			}
			catalog.setCatalogName(catalogName);
			logsCatalogManager.save(catalog);
			outputJson("{success:true,id:'"+catalog.getId()+"'}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'"+ex.getMessage()+"'}");
		}
		return NONE;
	}
	
}
