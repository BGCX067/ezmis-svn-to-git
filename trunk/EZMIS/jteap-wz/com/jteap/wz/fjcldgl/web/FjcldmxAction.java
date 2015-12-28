/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.fjcldgl.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.fjcldgl.manager.FjcldmxManager;




@SuppressWarnings({"serial","unchecked"})
public class FjcldmxAction extends AbstractAction {
	private FjcldmxManager fjcldmxManager;
	
	public FjcldmxManager getFjcldmxManager() {
		return fjcldmxManager;
	}

	public void setFjcldmxManager(FjcldmxManager fjcldmxManager) {
		this.fjcldmxManager = fjcldmxManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			String hqlWhere = StringUtil.isoToUTF8(request.getParameter("queryParamsSql"));
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
		return fjcldmxManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
			"id",
			"fjcld",
			"bh",
			"xh",
			"wzlb",
			"wzmc",
			"dw",
			"sl",
			"cldj"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"fjcld",
				"bh",
				"xh",
				"wzlb",
				"wzmc",
				"dw",
				"sl",
				"cldj"};
	}
}
