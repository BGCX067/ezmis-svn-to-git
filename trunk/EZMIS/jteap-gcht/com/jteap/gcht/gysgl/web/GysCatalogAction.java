/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.gcht.gysgl.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.core.web.AbstractTreeAction.TreeActionJsonBeanHandler;
import com.jteap.core.web.AbstractTreeAction.TreeActionJsonBeanProcessor;
import com.jteap.gcht.gysgl.manager.GysCatalogManager;
import com.jteap.gcht.gysgl.model.GysCatalog;
import com.jteap.system.group.model.Group;

/**
 * 供应商分类Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class GysCatalogAction extends AbstractTreeAction<GysCatalog> {

	private GysCatalogManager gysCatalogManager;

	@Override
	public String showTreeAction() throws Exception {
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonConfig.registerJsonBeanProcessor(GysCatalog.class,jsonBeanProcessor);
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				GysCatalog gysCatalog =(GysCatalog) obj;
				//只展开第一层
				if(gysCatalog.getParentGys()==null)
					map.put("expanded", true);
				else
					map.put("expanded", false);
				map.put("flbm", gysCatalog.getFlbm());
			}
		});
		
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;
	}

	@Override
	protected String beforeDeleteNode(Object node) throws Exception {
		GysCatalog gysCatalog = (GysCatalog) node;
		int count = gysCatalogManager.findGysxxByGysCatalog(gysCatalog.getId());
		if (count >0) {
			return "该分类下有子分类或者供应商信息，无法删除！";
		}
		return super.beforeDeleteNode(node);
	}

	/**
	 * 
	 * 描述 : 增加供应商分类
	 * 作者 : wangyun
	 * 时间 : 2010-11-16
	 * 异常 : Exception
	 */
	public String addGysFlAction() {
		String id = request.getParameter("id");
		String parentId = request.getParameter("parentId");
		String gysflmc = request.getParameter("gysflmc");
		String preSortNo = request.getParameter("preSortNo");
		
		try {
			GysCatalog gysCatalog = null;
			if (StringUtil.isEmpty(id) || id.lastIndexOf("ynode") >= 0) {
				gysCatalog = new GysCatalog();
				// 获得分类编码
				if (StringUtils.isNotEmpty(parentId)) {
					GysCatalog parentCatalog = gysCatalogManager.get(parentId);
					//同级存在节点
					if(gysCatalogManager.getGysCatalogByHql(parentId) != null){
						gysCatalog.setFlbm(getRandom(true, gysCatalogManager.getGysCatalogByHql(parentId).getFlbm(), ""));
					}else{
						gysCatalog.setFlbm(getRandom(false, "", parentCatalog.getFlbm()));
					}
				}else{
					//同级存在节点
					if(gysCatalogManager.getGysCatalogByHql("") != null){
						gysCatalog.setFlbm(getRandom(true, gysCatalogManager.getGysCatalogByHql("").getFlbm(),""));
					}else{
						gysCatalog.setFlbm(getRandom(false,"",""));
					}
				}
			} else {
				gysCatalog = gysCatalogManager.get(id);
			}

			gysCatalog.setCatalogName(gysflmc);
			gysCatalog.setSortno(Integer.parseInt(preSortNo)+1000);
			if(StringUtils.isNotEmpty(parentId)){
				GysCatalog parentGys = gysCatalogManager.get(parentId);
				gysCatalog.setParentGys(parentGys);
			}
			
			gysCatalogManager.save(gysCatalog);
			outputJson("{success:true,id:'"+gysCatalog.getId()+"'}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 返回分类编码
	 * @param status(是否存在同级节点)
	 * @param lastBm(最大同级节点分类编码)
	 * @param parentBm(父节点分类编码)
	 * @return String
	 */
	@SuppressWarnings("static-access")
	private String getRandom(boolean status, String lastBm, String parentBm) {
		String flbm = "";
		if(status == false){
			if(parentBm != ""){
				flbm = parentBm + "01";
			}else{
				flbm = "01";
			}
		}else{
			flbm = String.valueOf(Integer.valueOf(lastBm) + 1);
			if(flbm.valueOf(0) != lastBm.valueOf(0)){
				flbm = "0" + flbm;
			}else{
				return flbm;
			}
		}
		return flbm;
	}

	public GysCatalogManager getGysCatalogManager() {
		return gysCatalogManager;
	}

	public void setGysCatalogManager(GysCatalogManager gysCatalogManager) {
		this.gysCatalogManager = gysCatalogManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return gysCatalogManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String [] { "flbm" };
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	@Override
	protected Collection getChildren(Object bean) {
		GysCatalog gysCatalog = (GysCatalog) bean;
		return gysCatalog.getChildGyss();
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "parentGys";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		// 根据父亲节点查询
		String parentId = request.getParameter("parentId");
		List<GysCatalog> lst = gysCatalogManager.findGysCatalogByParentId(parentId);
		return lst;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortno";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "catalogName";
	}
}
