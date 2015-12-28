/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.tz.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.tz.manager.Cydtj600Manager;

/**
 * 厂用电统计(600MW)
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class Cydtj600Action extends AbstractAction {
	
	private Cydtj600Manager cydtj600Manager;

	public void setCydtj600Manager(Cydtj600Manager cydtj600Manager) {
		this.cydtj600Manager = cydtj600Manager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return cydtj600Manager;
	}
	
	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","tianXieRen","tianXieShiJian","zb","cbr","cbsj","fdjyg_3","gcb_3","fdjyg_4","gcb_4","lcb_3","lcb_4","qbb_2"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","tianXieRen","tianXieShiJian","zb","cbr","cbsj","fdjyg_3","gcb_3","fdjyg_4","gcb_4","lcb_3","lcb_4","qbb_2"
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
		
		HqlUtil.addOrder(hql, "obj.cbsj", "ASC");
	}
	
}
