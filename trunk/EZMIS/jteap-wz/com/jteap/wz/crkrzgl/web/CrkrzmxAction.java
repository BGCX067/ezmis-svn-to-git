/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.crkrzgl.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.crkrzgl.manager.CrkrzmxManager;



@SuppressWarnings({ "unchecked", "serial" })
public class CrkrzmxAction extends AbstractAction {
	private CrkrzmxManager crkrzmxManager;
	public CrkrzmxManager getCrkrzmxManager() {
		return crkrzmxManager;
	}

	public void setCrkrzmxManager(CrkrzmxManager crkrzmxManager) {
		this.crkrzmxManager = crkrzmxManager;
	}
	
	
	@Override
	public HibernateEntityDao getManager() {
		return crkrzmxManager;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
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
	public String[] listJsonProperties() {
	
		return new String[]{
			"sysl",
			"syje",
			"wzda",
			"dqkc",
			"jhdj",
			"yfpsl",
			"crksl",
			"id",
			"crkjg",
			"crkdh",
			"xh",
			"crkrzgl",
			"crksj",
			"crklb",
			"ck",
			"ckmc",
			"time",
		""};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"wzda",
			"crksl",
			"id",
			"crkjg",
			"crkdh",
			"xh",
			"crkrzid",
		""};
	}
}
