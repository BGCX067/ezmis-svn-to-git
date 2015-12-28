package com.jteap.dgt.tyxx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.dgt.tyxx.manager.TqtyManager;
/**
 * 团青推优处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "unchecked", "serial" })
public class TqtyAction extends AbstractAction {

	private TqtyManager tqtyManager;

	@Override
	public HibernateEntityDao getManager() {
		return tqtyManager;
	}

	@Override
	public String[] listJsonProperties() {
		
		return new String[] { "id","name", "birthday", "jiji_fenzi", "xueli", "sex",
				"minzu", "bumen", "tuanzu", "shengqing", "tuiyou_shijian","bumen_zhiwu","time" };
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		String hqlWhere = WebUtils.getRequestParam(request, "queryParamsSql");
		String orderBy = request.getParameter("sort");
		this.isUseQueryCache=false;
		if (StringUtils.isNotEmpty(hqlWhere)) {
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		if (StringUtils.isEmpty(orderBy)) {
			HqlUtil.addOrder(hql, "tuiyou_shijian", "desc");
		}
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public TqtyManager getTqtyManager() {
		return tqtyManager;
	}

	public void setTqtyManager(TqtyManager tqtyManager) {
		this.tqtyManager = tqtyManager;
	}

}
