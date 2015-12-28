package com.jteap.dgt.fjdgztj.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.dgt.fjdgztj.manager.WyhdManager;
/**
 * 文娱活动处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "unchecked", "serial" })
public class WyhdAction extends AbstractAction {

	private WyhdManager wyhdManager;

	@Override
	public HibernateEntityDao getManager() {
		return wyhdManager;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		this.isUseQueryCache=false;
		String gonghui = request.getParameter("gonghui");
		String year = request.getParameter("year");
		String jidu = request.getParameter("jidu");
		String id	= request.getParameter("id");
		String pcount = request.getParameter("count");
		String orderBy = request.getParameter("sort");
		
		if (StringUtil.isNotEmpty(gonghui)) {
			HqlUtil.addCondition(hql, "tongji.gonghui", gonghui);
		}
		if (StringUtil.isNotEmpty(year)) {
			HqlUtil.addCondition(hql, "tongji.year", year);
		}
		if (StringUtil.isNotEmpty(jidu)) {
			HqlUtil.addCondition(hql, "tongji.jidu", jidu);
		}
		if (StringUtil.isNotEmpty(id)) {
			HqlUtil.addCondition(hql, "id", id);
		}
		if(StringUtil.isNotEmpty(pcount)){
			HqlUtil.addCondition(hql,"pepole_count",pcount);
		}
		if (StringUtil.isEmpty(orderBy)) {
			HqlUtil.addOrder(hql, "tongji.year", "desc");
		}
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "riqi", "pepole_count", "content", "time","dzz","id","tongji","gonghui","year","jidu"};
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public WyhdManager getWyhdManager() {
		return wyhdManager;
	}

	public void setWyhdManager(WyhdManager wyhdManager) {
		this.wyhdManager = wyhdManager;
	}

}
