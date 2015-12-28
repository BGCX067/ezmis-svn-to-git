/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.gcht.htsp.web;

import java.util.Collection;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.gcht.htsp.manager.HeTongCatalogManager;
import com.jteap.gcht.htsp.model.HeTongCatalog;

/**
 * 合同分类Action
 * @author caihuiwen
 */
@SuppressWarnings({ "unchecked", "serial" })
public class HeTongCatalogAction extends AbstractTreeAction<HeTongCatalog>{

	private HeTongCatalogManager heTongCatalogManager;
	
	public void setHeTongCatalogManager(HeTongCatalogManager heTongCatalogManager) {
		this.heTongCatalogManager = heTongCatalogManager;
	}
	
	@Override
	protected Collection getChildren(Object bean) {
		HeTongCatalog catalog=(HeTongCatalog) bean;
		return catalog.getChildren();
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId=request.getParameter("parentId");
		String flbm = request.getParameter("flbm");
		if(StringUtil.isNotEmpty(flbm)){
			return heTongCatalogManager.findCatalogByParentId(parentId,flbm);
		}else{
			return heTongCatalogManager.findCatalogByParentId(parentId);
		}
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
		return heTongCatalogManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","catalogName","sortno","flbm"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id","catalogName","sortno","flbm"
		};
	}
	
	/**
	 * 显示树形结构的动作
	 * @return
	 * @throws Exception
	 */
	public String showTreeAction() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonConfig.registerJsonBeanProcessor(HeTongCatalog.class,jsonBeanProcessor);
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				map.put("flbm","WZ");
			}
		});
		
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;	
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
		String flbm = request.getParameter("flbm");
		
		try{
			HeTongCatalog catalog=null;
			
			if(StringUtils.isNotEmpty(id)){
				catalog=heTongCatalogManager.get(id);
			}else{
				catalog=(HeTongCatalog) this.creatBlankObject();
				if(StringUtils.isNotEmpty(parentId)){
					HeTongCatalog parentCatalog=heTongCatalogManager.get(parentId);
					catalog.setParent(parentCatalog);
				}
			}
			catalog.setCatalogName(catalogName);
			catalog.setFlbm(flbm);
			heTongCatalogManager.save(catalog);
			outputJson("{success:true,id:'"+catalog.getId()+"'}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'"+ex.getMessage()+"'}");
		}
		return NONE;
	}
	
}
