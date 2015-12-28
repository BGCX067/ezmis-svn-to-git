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
import com.jteap.yx.tz.manager.Cydtj300Manager;

/**
 * 厂用电统计(300MW)
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class Cydtj300Action extends AbstractAction {
	
	private Cydtj300Manager cydtj300Manager;
	
	public void setCydtj300Manager(Cydtj300Manager cydtj300Manager) {
		this.cydtj300Manager = cydtj300Manager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return cydtj300Manager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","tianXieRen","tianXieShiJian","zb","cbr","cbsj","gcb_1","gcb_2",
			"zbf_1","zbp_1","zbg_1","zbf_2","zbp_2","zbg_2","qbbf","qbbp","qbbg","wsfd_1","wsfd_2","jkqbb"
		};
	}
	
	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","tianXieRen","tianXieShiJian","zb","cbr","cbsj","gcb_1","gcb_2",
			"zbf_1","zbp_1","zbg_1","zbf_2","zbp_2","zbg_2","qbbf","qbbp","qbbg","wsfd_1","wsfd_2","jkqbb"
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
