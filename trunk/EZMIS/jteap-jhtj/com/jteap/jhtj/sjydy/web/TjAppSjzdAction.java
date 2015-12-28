package com.jteap.jhtj.sjydy.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.sjydy.manager.TjAppIOManager;
import com.jteap.jhtj.sjydy.manager.TjAppSjzdManager;
import com.jteap.jhtj.sjydy.model.TjAppIO;
@SuppressWarnings({ "unchecked", "serial" })
public class TjAppSjzdAction extends AbstractAction {
	private TjAppSjzdManager tjAppSjzdManager;
	private TjAppIOManager tjAppIOManager;
	@Override
	public HibernateEntityDao getManager() {
		return tjAppSjzdManager;
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String sysid=request.getParameter("sysid");
		HqlUtil.addCondition(hql,"sid",sysid);
		//tjappio的ID
		String appioid=request.getParameter("appioid");
		String vname="";
		if(StringUtils.isNotEmpty(appioid)){
			TjAppIO io=this.tjAppIOManager.get(appioid);
			vname=io.getVname();
		}	
		HqlUtil.addCondition(hql,"vname",vname);
		HqlUtil.addOrder(hql, "forder", "asc");
	}
	
	
	@Override
	public String showUpdateAction() throws Exception {
	
		return super.showUpdateAction();
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","vname","fname","sid","tname","cfname","ftype","forder"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","vname","fname","sid","tname","cfname","ftype","forder"};
	}

	public TjAppSjzdManager getTjAppSjzdManager() {
		return tjAppSjzdManager;
	}

	public void setTjAppSjzdManager(TjAppSjzdManager tjAppSjzdManager) {
		this.tjAppSjzdManager = tjAppSjzdManager;
	}

	public TjAppIOManager getTjAppIOManager() {
		return tjAppIOManager;
	}

	public void setTjAppIOManager(TjAppIOManager tjAppIOManager) {
		this.tjAppIOManager = tjAppIOManager;
	}
	
}
