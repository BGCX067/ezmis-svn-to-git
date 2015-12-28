package com.jteap.wfengine.workflow.web;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.wfengine.workflow.manager.FlowCatalogManager;
import com.jteap.wfengine.workflow.model.FlowCatalog;

@SuppressWarnings({ "unchecked", "serial" })
public class FlowCatalogAction extends AbstractTreeAction {
	
	private FlowCatalogManager flowCatalogManager;
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
			FlowCatalog catalog=null;
			
			if(StringUtils.isNotEmpty(id)){
				catalog=flowCatalogManager.get(id);
			}else{
				catalog=new FlowCatalog();
				if(StringUtils.isNotEmpty(parentId)){
					FlowCatalog parentCatalog=flowCatalogManager.get(parentId);
					catalog.setParent(parentCatalog);
				}
			}
			catalog.setCatalogName(catalogName);
			flowCatalogManager.save(catalog);
			outputJson("{success:true,id:'"+catalog.getId()+"'}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'"+ex.getMessage()+"'}");
		}
		return NONE;
	}

	@Override
	protected Collection getChildren(Object bean) {
		FlowCatalog catalog=(FlowCatalog) bean;
		return catalog.getChildren();
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return null;
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId=request.getParameter("parentId");
		return flowCatalogManager.findCatalogByParentId(parentId);
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
		return flowCatalogManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"catalogName","id"};
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public FlowCatalogManager getFlowCatalogManager() {
		return flowCatalogManager;
	}

	public void setFlowCatalogManager(FlowCatalogManager flowCatalogManager) {
		this.flowCatalogManager = flowCatalogManager;
	}

}
