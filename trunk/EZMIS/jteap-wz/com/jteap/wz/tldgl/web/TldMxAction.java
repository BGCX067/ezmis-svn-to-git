/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.tldgl.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.tldgl.manager.TldMxManager;


@SuppressWarnings({ "unchecked", "serial" })
public class TldMxAction extends AbstractAction {
	
	private TldMxManager tldMxManager;
	

	public TldMxManager getTldMxManager() {
		return tldMxManager;
	}

	public void setTldMxManager(TldMxManager tldMxManager) {
		this.tldMxManager = tldMxManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			this.isUseQueryCache = false;
			String zt = request.getParameter("zt");
			String hqlWhere = request.getParameter("queryParamsSql");
			if(StringUtils.isNotEmpty(zt)){
				HqlUtil.addCondition(hql, "tldgl.zt", zt);
			}
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
		return tldMxManager;
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
			"jhdj",
			"xhgg",
			"wzmc",
			"kw",
			"cwmc",
			"ck",
			"ckmc",
			"tlsl",
			"jldw",
			"tldgl",
			"bh",
			"tlsj",
			"time",
			"czr",
			"ysr",
			"tlr",
			"cwfzr",
			"personCzr",
			"personYsr",
			"personTlr",
			"personCwfzr",
			"tlbm",
			"gclb",
			"gcxm",
			"tlyy",
			"zt",
			"userName",
			"userLoginName",
			"tlsl",
			"jldw"
			};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"xh",
				"wzda",
				"xhgg",
				"dqkc",
				"yfpsl",
				"wzmc",
				"kw",
				"cwmc",
				"ck",
				"ckmc",
				"tldgl",
				"bh",
				"tlsl",
				"jldw"};
	}

}
