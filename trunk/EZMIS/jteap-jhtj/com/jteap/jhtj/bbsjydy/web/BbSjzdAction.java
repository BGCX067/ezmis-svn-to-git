package com.jteap.jhtj.bbsjydy.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.bbsjydy.manager.BbSjzdManager;
@SuppressWarnings({ "unchecked", "serial" })
public class BbSjzdAction extends AbstractAction {
	private BbSjzdManager bbSjzdManager;
	@Override
	public HibernateEntityDao getManager() {
		return bbSjzdManager;
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String bbioid=request.getParameter("bbioid");
		HqlUtil.addCondition(hql, "bbioid",bbioid);
		HqlUtil.addOrder(hql,"forder", "asc");
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","fname","cfname","ftype","forder"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","fname","cfname","ftype","forder"};
	}

	public BbSjzdManager getBbSjzdManager() {
		return bbSjzdManager;
	}

	public void setBbSjzdManager(BbSjzdManager bbSjzdManager) {
		this.bbSjzdManager = bbSjzdManager;
	}
}
