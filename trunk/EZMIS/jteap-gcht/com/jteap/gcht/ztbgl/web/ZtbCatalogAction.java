/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
package com.jteap.gcht.ztbgl.web;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.gcht.ztbgl.manager.ZtbCatalogManager;
import com.jteap.gcht.ztbgl.model.ZtbCatalog;
import com.jteap.jx.bpbjgl.model.BpbjZyfl;

/**
 * 招标分类Action
 * 
 * @author wangyun
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class ZtbCatalogAction extends AbstractTreeAction<ZtbCatalog> {

	private ZtbCatalogManager ztbCatalogManager;

	@Override
	protected String beforeDeleteNode(Object node) throws Exception {
		ZtbCatalog ztbCatalog = (ZtbCatalog) node;
		int count = ztbCatalogManager.findZtbxxByZtbCatalog(ztbCatalog.getId());
		if (count >0) {
			return "该分类下有招投标信息，无法删除！";
		}
		return super.beforeDeleteNode(node);
	}

	/**
	 * 
	 * 描述 : 增加招标分类
	 * 作者 : wangyun
	 * 时间 : 2010-11-16
	 * 异常 : Exception
	 */
	public String addZtbFlAction() {
		String id = request.getParameter("id");
		String parentId = request.getParameter("parentId");
		String zbflmc = request.getParameter("zbflmc");
		String preSortNo = request.getParameter("preSortNo");
		
		try {
			ZtbCatalog ztbCatalog = null;
			if (StringUtil.isEmpty(id) || id.lastIndexOf("ynode") >= 0) {
				ztbCatalog = new ZtbCatalog();
			} else {
				ztbCatalog = ztbCatalogManager.get(id);
			}
			ztbCatalog.setCatalogName(zbflmc);
			ztbCatalog.setSortno(Integer.parseInt(preSortNo)+1000);
			if(StringUtils.isNotEmpty(parentId)){
				ZtbCatalog parentZtb = ztbCatalogManager.get(parentId);
				ztbCatalog.setParentZtb(parentZtb);
			}
			
			ztbCatalogManager.save(ztbCatalog);
			outputJson("{success:true,id:'"+ztbCatalog.getId()+"'}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	public ZtbCatalogManager getZtbCatalogManager() {
		return ztbCatalogManager;
	}

	public void setZtbCatalogManager(ZtbCatalogManager ztbCatalogManager) {
		this.ztbCatalogManager = ztbCatalogManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return ztbCatalogManager;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "parentZtb";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		// 根据父亲节点查询
		String parentId = request.getParameter("parentId");
		List<ZtbCatalog> lst = ztbCatalogManager.findZtbCatalogByParentId(parentId);
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
