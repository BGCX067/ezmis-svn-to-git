/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.wzxsd.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.wzxsd.manager.WzxsdMxManager;


@SuppressWarnings({ "unchecked", "serial" })
public class WzxsdMxAction extends AbstractAction {
	
	private WzxsdMxManager wzxsdMxManager;

	public WzxsdMxManager getWzxsdMxManager() {
		return wzxsdMxManager;
	}

	public void setWzxsdMxManager(WzxsdMxManager wzxsdMxManager) {
		this.wzxsdMxManager = wzxsdMxManager;
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
			//HqlUtil.addOrder(hql, "xh", "asc");
		}
	}
	
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return wzxsdMxManager;
	}


	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"wzda",
			"dqkc",
			"yfpsl",
			"wzmc",
			"jhdj",
			"xhgg",
			"kw",
			"cwmc",
			"ck",
			"ckmc",
			"wzxsd",
			"bh",
			"wzxsd",
			"kcdj",
			"jldw",
			"xssl",
			"zzsl",
			"xsdj",
			"glf",
			"ysf",
			"xsje",
			"jehj",
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
