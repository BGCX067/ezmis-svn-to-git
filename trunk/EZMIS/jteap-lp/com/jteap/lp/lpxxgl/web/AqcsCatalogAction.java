/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.lp.lpxxgl.web;

import java.util.Collection;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.lp.lpxxgl.manager.AqcsCatalogManager;
import com.jteap.lp.lpxxgl.model.AqcsCatalog;

/**
 * 安全措施分类树Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "unused", "unchecked", "serial" })
public class AqcsCatalogAction extends AbstractTreeAction<AqcsCatalog> {

	private AqcsCatalogManager aqcsCatalogManager;

	/**
	 * 显示树形结构的动作
	 * @return
	 * @throws Exception
	 */
	public String showTreeAction() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonConfig.registerJsonBeanProcessor(AqcsCatalog.class,jsonBeanProcessor);
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				AqcsCatalog aqcsCatalog=(AqcsCatalog) obj;
				map.put("leaf", aqcsCatalog.getChildren().size()>0?false:true);
				map.put("sortNo", aqcsCatalog.getSortNo());
				//只展开第一层
				if(aqcsCatalog.getParent()==null)
					map.put("expanded", true);
				else
					map.put("expanded", false);
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
	 * 简单创建安措分类,只需要提供父亲安措编号,安措名称，就可以创建一个安措分类
	 * 
	 * @return
	 * @throws Exception
	 */
	public String simpleCreateCatalogAction() throws Exception {
		String parentId = request.getParameter("parentId");
		String nodeName = request.getParameter("nodeName");
		String preSortNo = request.getParameter("preSortNo");
		String id = request.getParameter("id");
		if ("".equals(preSortNo)) {
			preSortNo = "0";
		}
		AqcsCatalog aqcsCatalog = null;
		if (StringUtil.isNotEmpty(id)) {
			aqcsCatalog = aqcsCatalogManager.get(id);
		} else {
			aqcsCatalog =  new AqcsCatalog();
		}
		aqcsCatalog.setFlmc(nodeName);
		aqcsCatalog.setSortNo(Integer.parseInt(preSortNo) + 1000);
		if (StringUtils.isNotEmpty(parentId)) {
			AqcsCatalog parentCatalog = aqcsCatalogManager.get(parentId);
			aqcsCatalog.setParent(parentCatalog);
		}

		aqcsCatalogManager.save(aqcsCatalog);
		outputJson("{success:true,id:'" + aqcsCatalog.getId() + "'}");
		return NONE;
	}

	/**
	 * 删除安措分类树节点前判断
	 */
	@Override
	protected String beforeDeleteNode(Object node) throws Exception {
		AqcsCatalog aqcsCatalog = (AqcsCatalog) node;
		if (aqcsCatalog.getAqcss().size() >0) {
			return "此分类下有安全措施，不能删除！";
		}
		return null;
	}

	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "parent";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		// 根据父亲节点查询
		String parentId = request.getParameter("parentId");
		if (parentId == null) {
			parentId = request.getParameter("node");
		}
		if (StringUtils.isNotEmpty(parentId) && !parentId.equals("rootNode")) {
			return aqcsCatalogManager.findAqcsCatalogByParentId(parentId);
		}

		return aqcsCatalogManager.findAqcsCatalogByParentId(null);
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortNo";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "flmc";
	}

	@Override
	public HibernateEntityDao getManager() {
		return aqcsCatalogManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "parentid", "flmc" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "parentid", "flmc" };
	}

	public AqcsCatalogManager getAqcsCatalogManager() {
		return aqcsCatalogManager;
	}

	public void setAqcsCatalogManager(AqcsCatalogManager aqcsCatalogManager) {
		this.aqcsCatalogManager = aqcsCatalogManager;
	}

}
