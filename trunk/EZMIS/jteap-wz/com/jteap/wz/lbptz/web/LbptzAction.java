/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.lbptz.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.lbptz.manager.LbptzManager;




@SuppressWarnings({"serial","unchecked"})
public class LbptzAction extends AbstractAction {
	private LbptzManager lbptzManager;
	public LbptzManager getLbptzManager() {
		return lbptzManager;
	}

	public void setLbptzManager(LbptzManager lbptzManager) {
		this.lbptzManager = lbptzManager;
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
		return lbptzManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"bh",
			"bm",
			"zgbm",
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
			"llr",
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
				"zgbm",
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
				"llr",
				"czr",
				"grgyqf",
			"time"};
	}
}
