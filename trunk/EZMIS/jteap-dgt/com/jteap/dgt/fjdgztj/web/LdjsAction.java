package com.jteap.dgt.fjdgztj.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.dgt.fjdgztj.manager.LdjsManager;
import com.jteap.dgt.fjdgztj.manager.TongjiManager;
/**
 * 劳动竞赛处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "unchecked", "serial" })
public class LdjsAction extends AbstractAction {

	private LdjsManager ldjsManager;
	private TongjiManager tongjiManager;
	
	@Override
	public HibernateEntityDao getManager() {
		return ldjsManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","pepole_count","riqi","content","time","tongji","gonghui","year","jidu"};
	}

	
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		this.isUseQueryCache=false;
		String gonghui = request.getParameter("gonghui");
		String year = request.getParameter("year");
		String jidu = request.getParameter("jidu");		
		String pcount = request.getParameter("count");
		String orderBy = request.getParameter("sort");
		
		if(StringUtil.isNotEmpty(gonghui)){
			HqlUtil.addCondition(hql, "tongji.gonghui",gonghui);
		}
		
		if(StringUtil.isNotEmpty(year)){
			HqlUtil.addCondition(hql, "tongji.year",year);
		}
		
		if(StringUtil.isNotEmpty(jidu)){
			HqlUtil.addCondition(hql, "tongji.jidu",jidu);
		}
		if(StringUtil.isNotEmpty(pcount)){
			HqlUtil.addCondition(hql,"pepole_count",pcount);
		}
		if (StringUtil.isEmpty(orderBy)) {
			HqlUtil.addOrder(hql, "tongji.year", "desc");
		}

	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public LdjsManager getLdjsManager() {
		return ldjsManager;
	}

	public void setLdjsManager(LdjsManager ldjsManager) {
		this.ldjsManager = ldjsManager;
	}

	public TongjiManager getTongjiManager() {
		return tongjiManager;
	}

	public void setTongjiManager(TongjiManager tongjiManager) {
		this.tongjiManager = tongjiManager;
	}



}
