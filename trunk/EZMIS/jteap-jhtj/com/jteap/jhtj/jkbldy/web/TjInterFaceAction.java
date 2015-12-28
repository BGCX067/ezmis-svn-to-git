package com.jteap.jhtj.jkbldy.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.jkbldy.manager.TjInterFaceManager;
@SuppressWarnings({ "unchecked", "serial" })
public class TjInterFaceAction extends AbstractAction {

	private TjInterFaceManager tjInterFaceManager;
	@Override
	public HibernateEntityDao getManager() {
		return tjInterFaceManager;
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		HqlUtil.addOrder(hql, "vorder", "asc");
	}
	

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","vname","cvname","vtype","vorder"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","vname","cvname","vtype","vorder"};
	}

	public TjInterFaceManager getTjInterFaceManager() {
		return tjInterFaceManager;
	}

	public void setTjInterFaceManager(TjInterFaceManager tjInterFaceManager) {
		this.tjInterFaceManager = tjInterFaceManager;
	}

}
