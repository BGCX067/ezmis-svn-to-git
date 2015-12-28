/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.system.dataperm.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.system.dataperm.manager.*;
import com.jteap.system.dataperm.model.TableToClass;

import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;

@SuppressWarnings({ "unchecked", "serial" })
public class TableToClassAction extends AbstractAction{

	private TableToClassManager tableToClassManager;

	@Override
	public HibernateEntityDao getManager() {
		return tableToClassManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id",
			"tablename",
			"tablecname",
			"classname",
			"classcname",
			"classpath",
			"torder",
		""};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"tablename",
				"tablecname",
				"classname",
				"classcname",
				"classpath",
				"torder",
		""};
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		HqlUtil.addOrder(hql, "torder", "asc");
	}
	
	@Override
	protected void beforeSaveUpdate(HttpServletRequest request,
			HttpServletResponse response, Object obj, Object originalObject) {
		TableToClass tabletoclass=(TableToClass)obj;
		if(tabletoclass.getTorder()==0l){
			tabletoclass.setTorder(this.tableToClassManager.findMaxOrder()+1);
		}
	}

	public TableToClassManager getTableToClassManager() {
		return tableToClassManager;
	}

	public void setTableToClassManager(TableToClassManager tableToClassManager) {
		this.tableToClassManager = tableToClassManager;
	}
}

