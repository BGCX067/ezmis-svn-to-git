/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.component.livegrid.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.component.livegrid.manager.BigDtManager;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractAction;




@SuppressWarnings("serial")
public class BigDtAction extends AbstractAction {
	private BigDtManager bigDtManager;
	public BigDtManager getBigDtManager() {
		return bigDtManager;
	}

	public void setBigDtManager(BigDtManager bigDtManager) {
		this.bigDtManager = bigDtManager;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return bigDtManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"fd1",
			"fd2",
			"fd3",
			"fd4",
			"fd5",
			"fd6",
			"fd7",
			"fd8",
			"fd9",
			"fd0",
		""};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id",
			"fd1",
			"fd2",
			"fd3",
			"fd4",
			"fd5",
			"fd6",
			"fd7",
			"fd8",
			"fd9",
			"fd0",
		""};
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
//		HqlUtil.addCondition(hql,"fd1","AD1",HqlUtil.LOGIC_AND,HqlUtil.TYPE_STRING_LIKE);
//		HqlUtil.addCondition(hql,"fd1","AD2",HqlUtil.LOGIC_OR,HqlUtil.TYPE_STRING_LIKE);
	}
	
	
	

}
