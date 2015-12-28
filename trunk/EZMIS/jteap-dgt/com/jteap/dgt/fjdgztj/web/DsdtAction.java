package com.jteap.dgt.fjdgztj.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.dgt.fjdgztj.manager.DsdtManager;
/**
 * 导师带徒处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "unchecked", "serial" })
public class DsdtAction extends AbstractAction {

	private DsdtManager dsdtManager;

	@Override
	public HibernateEntityDao getManager() {
		return dsdtManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "month", "result", "tudi", "shifu",
				"content" ,"tongji","gonghui","year","jidu"};
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		this.isUseQueryCache=false;
		
		String gonghui = request.getParameter("gonghui");
		String year = request.getParameter("year");
		String jidu = request.getParameter("jidu");
		String shifu = request.getParameter("shifu");
		String tudi = request.getParameter("tudi");
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
		if(StringUtil.isNotEmpty(shifu)){
			HqlUtil.addCondition(hql, "shifu",shifu,HqlUtil.LOGIC_AND,HqlUtil.TYPE_STRING_LIKE);
		}
		if(StringUtil.isNotEmpty(tudi)){
			HqlUtil.addCondition(hql, "tudi",tudi,HqlUtil.LOGIC_AND,HqlUtil.TYPE_STRING_LIKE);
		}
		if (StringUtil.isEmpty(orderBy)) {
			HqlUtil.addOrder(hql, "tongji.year", "desc");
		}
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public DsdtManager getDsdtManager() {
		return dsdtManager;
	}

	public void setDsdtManager(DsdtManager dsdtManager) {
		this.dsdtManager = dsdtManager;
	}

}
