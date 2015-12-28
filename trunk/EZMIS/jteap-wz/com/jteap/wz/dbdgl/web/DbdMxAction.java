/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.dbdgl.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.dbdgl.manager.DbdMxManager;


@SuppressWarnings({ "unchecked", "serial" })
public class DbdMxAction extends AbstractAction {
	
	private DbdMxManager dbdMxManager;
	
	public DbdMxManager getDbdMxManager() {
		return dbdMxManager;
	}

	public void setDbdMxManager(DbdMxManager dbdMxManager) {
		this.dbdMxManager = dbdMxManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			this.isUseQueryCache = false;
			String hqlWhere = request.getParameter("queryParamsSql");
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "xh", "asc");
		}
	}
	
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return dbdMxManager;
	}


	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"je",
			"id",
			"xh",
			"wzda",
			"dqkc",
			"yfpsl",
			"wzmc",
			"xhgg",
			"kw",
			"cwmc",
			"ck",
			"ckmc",
			"dbd",
			"bh",
			"dbsj",
			"dbsl",
			"jldw",
			"dbdj",
			"time"
			};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"xh",
				"wzda",
				"xhgg",
				"wzmc",
				"dqkc",
				"yfpsl",
				"kw",
				"cwmc",
				"ck",
				"ckmc",
				"dbd",
				"bh",
				"dbsl",
				"jldw",
				"dbdj",
				"id"};
	}

}
