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
import com.jteap.wz.crkrzgl.manager.CrkrzglManager;




@SuppressWarnings({"serial","unchecked"})
public class CrkrzglAction extends AbstractAction {
	private CrkrzglManager crkrzglManager;
	public CrkrzglManager getCrkrzglManager() {
		return crkrzglManager;
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
			HqlUtil.addOrder(hql, "crkdh", "asc");
		}
	}

	public void setCrkrzglManager(CrkrzglManager crkrzglManager) {
		this.crkrzglManager = crkrzglManager;
	}
	
	
	@Override
	public HibernateEntityDao getManager() {
		return crkrzglManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"czr",
			"czrxm",
			"person",
			"userLoginName",
			"userName",
			"crksj",
			"crkqf",
			"crkdh",
			"ckbh",
			"ck",
			"ckmc",
			"crklb",
			"xgdjbh",
			"time"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id",
			"czr",
			"czrxm",
			"person",
			"userLoginName",
			"userName",
			"crksj",
			"crkqf",
			"crkdh",
			"ckbh",
			"crklb",
			"xgdjbh",
			"time"};
	}
}
