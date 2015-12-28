/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.kcpd.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.kcpd.manager.PddMxManager;


@SuppressWarnings({ "unchecked", "serial" })
public class PddMxAction extends AbstractAction {
	
	private PddMxManager pddMxManager;

	public PddMxManager getPddMxManager() {
		return pddMxManager;
	}

	public void setPddMxManager(PddMxManager pddMxManager) {
		this.pddMxManager = pddMxManager;
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
		return pddMxManager;
	}


	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"xh",
			"wzda",
			"wzmc",
			"xhgg",
			"kw",
			"cwmc",
			"ck",
			"ckmc",
			"pdd",
			"bh",
			"czr",
			"pqje",
			"pjj",
			"pqsl",
			"pdsl",
			"slcy",
			"jecy",
			"cyyy",
			"zksj"
			};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"xh",
				"wzda",
				"wzmc",
				"xhgg",
				"kw",
				"cwmc",
				"ck",
				"ckmc",
				"pdd",
				"bh",
				"pqje",
				"pjj",
				"pqsl",
				"pdsl",
				"slcy",
				"jecy",
				"cyyy",
				"zksj"};
	}

}
