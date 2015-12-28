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
import com.jteap.yx.tz.manager.Blqdzjl300Manager;

/**
 *避雷器动作记录(300MW)Action
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class Blqdzjl300Action extends AbstractAction {
	
	private Blqdzjl300Manager blqdzjl300Manager;
	
	public void setBlqdzjl300Manager(Blqdzjl300Manager blqdzjl300Manager) {
		this.blqdzjl300Manager = blqdzjl300Manager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return blqdzjl300Manager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","tianXieRen","tianXieShiJian","cbr","cbsj","zbgyc_a1","zbgyc_b1","zbgyc_c1","zbgyczxd_1","zbgyc_a2",
			"zbgyc_b2","zbgyc_c2","zbgyczxd_2","qbbgyc_a01","qbbgyc_b01","qbbgyc_c01","mx_a1","mx_b1","mx_c1","mx_a2","mx_b2","mx_c2"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","tianXieRen","tianXieShiJian","cbr","cbsj","zbgyc_a1","zbgyc_b1","zbgyc_c1","zbgyczxd_1","zbgyc_a2",
			"zbgyc_b2","zbgyc_c2","zbgyczxd_2","qbbgyc_a01","qbbgyc_b01","qbbgyc_c01","mx_a1","mx_b1","mx_c1","mx_a2","mx_b2","mx_c2"
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
