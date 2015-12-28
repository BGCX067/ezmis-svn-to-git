/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.index.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.index.manager.XiTongTongZhiManager;

/**
 * 系统通知Action
 * @author caihuiwen
 */
@SuppressWarnings({ "unchecked", "serial" })
public class XiTongTongZhiAction extends AbstractAction{
	
	private XiTongTongZhiManager xiTongTongZhiManager;
	
	public void setXiTongTongZhiManager(XiTongTongZhiManager xiTongTongZhiManager) {
		this.xiTongTongZhiManager = xiTongTongZhiManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return xiTongTongZhiManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","xh","nr","fcr","sj"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","xh","nr","fcr","sj"
		};
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String hqlWhere = request.getParameter("queryParamsSql");
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", " ");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		HqlUtil.addOrder(hql, "obj.xh", "ASC");
	}
	
	
	
}
