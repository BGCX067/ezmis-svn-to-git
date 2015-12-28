/*
 * Copyright 2009 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */
 package com.jteap.wz.yhdgl.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.wz.yhdgl.manager.WzhtManager;

@SuppressWarnings({ "unchecked", "serial" })
public class WzhtAction extends AbstractAction {
	private WzhtManager wzhtManager;
	
	
	public WzhtManager getWzhtManager() {
		return wzhtManager;
	}

	public void setWzhtManager(WzhtManager wzhtManager) {
		this.wzhtManager = wzhtManager;
	}

	@Override
	public void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql){
		try{
			this.isUseQueryCache = false;
			String zt = request.getParameter("zt");
			String hqlWhere = request.getParameter("queryParamsSql");
			if(StringUtils.isNotEmpty(zt)){
				HqlUtil.addCondition(hql, "zt",zt);
			}
			if(StringUtils.isNotEmpty(hqlWhere)){
				String hqlWhereTemp = hqlWhere.replace("$", "%");
				HqlUtil.addWholeCondition(hql, hqlWhereTemp);
			}
		}catch(Exception ex){
			throw new BusinessException(ex);
		}
		if(!this.isHaveSortField()){
			HqlUtil.addOrder(hql, "htbh", "asc");
		}
	}
	
	@Override
	public HibernateEntityDao getManager() {
		return wzhtManager;
	}

	@Override
	public String[] listJsonProperties() {
	
		return new String[]{
				"id",
				"htbh",
				"htmc",
				"htlx",
				"status",
				"cjsj",
				"sqbm",
				"sqr",
				"jhsqdh",
				"gfdw",
				"qddd",
				"xfdw",
				"qtsm",
				"htje",
				"htjbryj",
				"htjbr",
				"htjbrqmsj",
				"jhjybfzryj",
				"jhjybfzr",
				"jhjybfzrqmsj",
				"jhjybzryj",
				"jhjybzr",
				"jhjybzrqmsj",
				"zgfzjlyj",
				"zgfzjl",
				"zgfzjlqmsj",
				"htqdr",
				"zjl",
				"zjlqmrq",
				"htxh",
				"htcjsj",
		"time"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"htbh",
				"htmc",
				"htlx",
				"status",
				"cjsj",
				"sqbm",
				"sqr",
				"jhsqdh",
				"gfdw",
				"qddd",
				"xfdw",
				"qtsm",
				"htje",
				"htjbryj",
				"htjbr",
				"htjbrqmsj",
				"jhjybfzryj",
				"jhjybfzr",
				"jhjybfzrqmsj",
				"jhjybzryj",
				"jhjybzr",
				"jhjybzrqmsj",
				"zgfzjlyj",
				"zgfzjl",
				"zgfzjlqmsj",
				"htqdr",
				"zjl",
				"zjlqmrq",
				"htxh",
				"htcjsj",
		"time"};
	}

}
