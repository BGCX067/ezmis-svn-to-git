package com.jteap.dgt.fjdgztj.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.dgt.fjdgztj.manager.Gz110Manager;
/**
 * 工作110
 * @author lvchao
 *
 */
@SuppressWarnings( { "unchecked", "serial" })
public class Gz110Action extends AbstractAction {

	private Gz110Manager gz110Manager;

	@Override
	public HibernateEntityDao getManager() {
		return gz110Manager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "zgname", "riqi", "gzlx", "time","tongji","gonghui","year","jidu" };
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public Gz110Manager getGz110Manager() {
		return gz110Manager;
	}

	public void setGz110Manager(Gz110Manager gz110Manager) {
		this.gz110Manager = gz110Manager;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		this.isUseQueryCache=false;
		
		String gonghui = request.getParameter("gonghui");
		String year = request.getParameter("year");
		String jidu = request.getParameter("jidu");	
		String zgname = request.getParameter("zgname");
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
		if(StringUtil.isNotEmpty(zgname)){
			HqlUtil.addCondition(hql, "zgname",zgname,HqlUtil.LOGIC_AND,HqlUtil.TYPE_STRING_LIKE);
		}
		if (StringUtil.isEmpty(orderBy)) {
			HqlUtil.addOrder(hql, "tongji.year", "desc");
		}
	}

}
