/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.bfdgl.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.bfdgl.manager.WzbfdMxManager;

@SuppressWarnings({ "unchecked", "serial" })
public class WzbfdMxAction extends AbstractAction {
	
	private WzbfdMxManager wzbfdMxManager;

	public WzbfdMxManager getWzbfdMxManager() {
		return wzbfdMxManager;
	}

	public void setWzbfdMxManager(WzbfdMxManager wzbfdMxManager) {
		this.wzbfdMxManager = wzbfdMxManager;
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
		return wzbfdMxManager;
	}


	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
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
			"wzbfd",
			"bh",
			"jldw",
			"kcj",
			"bfsl",
			"clj",
			"zzsl",
			"glf",
			"ddyzf",
			"clje",
			"bfje"
			};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
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
				"wzbfd",
				"bh",
				"jldw",
				"kcj",
				"bfsl",
				"clj",
				"zzsl",
				"glf",
				"ddyzf",
				"clje",
				"bfje"};
	}

}
