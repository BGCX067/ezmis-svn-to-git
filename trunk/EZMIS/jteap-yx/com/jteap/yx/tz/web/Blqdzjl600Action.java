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
import com.jteap.yx.tz.manager.Blqdzjl600Manager;

/**
 *避雷器动作记录(600MW)Action
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class Blqdzjl600Action extends AbstractAction {
	
	private Blqdzjl600Manager blqdzjl600Manager;
	
	public void setBlqdzjl600Manager(Blqdzjl600Manager blqdzjl600Manager) {
		this.blqdzjl600Manager = blqdzjl600Manager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return blqdzjl600Manager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","tianXieRen","tianXieShiJian","cbr","cbsj",
			"zbgycA3","zbgycB3","zbgycC3","zbgyczx3","zbgycA4","zbgycB4","zbgycC4","zbgyczx4",
			"qbbgycA02","qbbgycB02","qbbgycC02","mxA4","mxB4","mxC4","mxA5","mxB5","mxC5",
			"egxA","egxB","egxC","efxA","efxB","efxC","tsyhA","tsyhB","tsyhC","tsehA","tsehB","tsehC"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","tianXieRen","tianXieShiJian","cbr","cbsj",
			"zbgycA3","zbgycB3","zbgycC3","zbgyczx3","zbgycA4","zbgycB4","zbgycC4","zbgyczx4",
			"qbbgycA02","qbbgycB02","qbbgycC02","mxA4","mxB4","mxC4","mxA5","mxB5","mxC5",
			"egxA","egxB","egxC","efxA","efxB","efxC","tsyhA","tsyhB","tsyhC","tsehA","tsehB","tsehC"
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
		
		HqlUtil.addOrder(hql, "obj.cbsj", "DESC");
	}
	
}
