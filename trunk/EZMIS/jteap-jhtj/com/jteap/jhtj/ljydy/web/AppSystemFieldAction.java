package com.jteap.jhtj.ljydy.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.ljydy.manager.AppSystemFieldManager;
import com.jteap.jhtj.ljydy.model.AppSystemField;
@SuppressWarnings({ "unchecked", "serial" })
public class AppSystemFieldAction extends AbstractAction {
	private AppSystemFieldManager appSystemFieldManager;
	@Override
	public HibernateEntityDao getManager() {
		return appSystemFieldManager;
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String sisid=StringUtils.defaultIfEmpty(request.getParameter("sisid"), "");
		String vname=StringUtils.defaultIfEmpty(request.getParameter("vname"), "");
		HqlUtil.addCondition(hql, "system.id",sisid);
		HqlUtil.addCondition(hql, "vname",vname);
		HqlUtil.addOrder(hql, "forder", "asc");
	}
	
	public String deleteNodeAction() throws Exception{
		String sisid=request.getParameter("sisid");
		String vname=request.getParameter("vname");
		if(StringUtils.isNotEmpty(sisid)&&StringUtils.isNotEmpty(vname)){
			this.appSystemFieldManager.removeBatchInHql(AppSystemField.class, "system.id='"+sisid+"' and vname='"+vname+"'");
			outputJson("{success:true}");
		}else{
			outputJson("{success:false}");
		}
		return NONE;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","sid","vname","fname","cvname","cfname","ftype","forder"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","sid","vname","fname","cvname","cfname","ftype","forder"};
	}

	public AppSystemFieldManager getAppSystemFieldManager() {
		return appSystemFieldManager;
	}

	public void setAppSystemFieldManager(AppSystemFieldManager appSystemFieldManager) {
		this.appSystemFieldManager = appSystemFieldManager;
	}
	
}
