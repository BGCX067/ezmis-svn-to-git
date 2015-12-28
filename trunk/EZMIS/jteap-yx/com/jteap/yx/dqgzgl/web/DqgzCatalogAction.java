/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.yx.dqgzgl.web;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.yx.dqgzgl.manager.DqgzCatalogManager;
import com.jteap.yx.dqgzgl.model.DqgzCatalog;

/**
 * 定期工作分类Action
 * @author caihuiwen
 */
@SuppressWarnings({ "unchecked", "serial" })
public class DqgzCatalogAction extends AbstractTreeAction<DqgzCatalog>{

	private DqgzCatalogManager dqgzCatalogManager;
	
	public void setDqgzCatalogManager(DqgzCatalogManager dqgzCatalogManager) {
		this.dqgzCatalogManager = dqgzCatalogManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		DqgzCatalog catalog=(DqgzCatalog) bean;
		return catalog.getChildren();
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId=request.getParameter("parentId");
		return dqgzCatalogManager.findCatalogByParentId(parentId);
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
		return dqgzCatalogManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","catalogName","sortno"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id","catalogName","sortno"
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
			DqgzCatalog catalog=null;
			
			if(StringUtils.isNotEmpty(id)){
				catalog=dqgzCatalogManager.get(id);
			}else{
				catalog=(DqgzCatalog) this.creatBlankObject();
				if(StringUtils.isNotEmpty(parentId)){
					DqgzCatalog parentCatalog=dqgzCatalogManager.get(parentId);
					catalog.setParent(parentCatalog);
				}
			}
			catalog.setCatalogName(catalogName);
			dqgzCatalogManager.save(catalog);
			outputJson("{success:true,id:'"+catalog.getId()+"'}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'"+ex.getMessage()+"'}");
		}
		return NONE;
	}
	
	/**
	 * 获取所有定期工作分类
	 * @return
	 */
	public String findAllCatalogAction(){
		
		String hql = "from DqgzCatalog t order by sortno desc";
		List<DqgzCatalog> list = dqgzCatalogManager.find(hql);
		String json = JSONUtil.listToJson(list, listJsonProperties());
		
		try {
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
	
}
