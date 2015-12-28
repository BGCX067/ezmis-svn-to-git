/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.gqjtz.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.gqjtz.manager.GqjtzManager;




@SuppressWarnings({"unchecked","serial"})
public class GqjtzAction extends AbstractAction {
	private GqjtzManager gqjtzManager;
	public GqjtzManager getGqjtzManager() {
		return gqjtzManager;
	}

	public void setGqjtzManager(GqjtzManager gqjtzManager) {
		this.gqjtzManager = gqjtzManager;
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
			HqlUtil.addOrder(hql, "ffsj", "desc");
		}
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return gqjtzManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"bh",
			"bm",
			"zgxm",
			"wzbm",
			"wzmc",
			"xhgg",
			"ffsj",
			"dqsj",
			"zyf",
			"ffsl",
			"dj",
			"zje",
			"zg",
			"bmzg",
			"sjc",
			"flr",
			"czr",
			"grgyqf",
		"time"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"bh",
				"bm",
				"zgxm",
				"wzbm",
				"wzmc",
				"xhgg",
				"ffsj",
				"dqsj",
				"zyf",
				"ffsl",
				"dj",
				"zje",
				"zg",
				"bmzg",
				"sjc",
				"flr",
				"czr",
				"grgyqf",
			"time"};
	}
}
