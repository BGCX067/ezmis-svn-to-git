package com.jteap.dgt.dyxxk.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.dgt.dyxxk.manager.DyxxkManager;

@SuppressWarnings( { "unchecked", "serial" })
public class DyxxkAction extends AbstractAction {

	private DyxxkManager dyxxkmanager;
	private SessionFactory sessionFactory;
	
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public HibernateEntityDao getManager() {
		return dyxxkmanager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "name", "sex", "minzu", "xueli",
				"birthday", "bumen", "dangzu", "dzz", "dangzu_name",
				"join_date", "time" };
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		//左边党组织树传来的Id
		String id = request.getParameter("id");
		String orderBy = request.getParameter("sort");
		this.isUseQueryCache=false;
		if (StringUtil.isNotEmpty(id)) {
			HqlUtil.addWholeCondition(hql, "dangzu=" + id);
		}
		String hqlWhere = WebUtils.getRequestParam(request, "queryParamsSql");
		//System.out.println("where:" + hqlWhere);
		if (StringUtils.isNotEmpty(hqlWhere)) {
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
	
		if (StringUtils.isEmpty(orderBy)) {
			HqlUtil.addOrder(hql, "sortno", "desc");
		}
		
		//System.out.println(hql.toString());

	}
	
	/**
	 * 删除党组织
	 * @return
	 * @throws Exception
	 */
	public String confirmDy() throws Exception{
		String ids = request.getParameter("ids");
		String hql="from Dyxxk as dy where dy.dangzu=?";
	 
		//System.out.println(dyxxkmanager.find(hql, ids).size());
		if(dyxxkmanager.find(hql, ids).size()==0){
			
			outputJson("{success:true}");
		}else{
			outputJson("{failure:true,msg:'有成员的组织无法删除!'}");
			
		}
		return null;
	}

	public DyxxkManager getDyxxkmanager() {
		return dyxxkmanager;
	}

	public void setDyxxkmanager(DyxxkManager dyxxkmanager) {
		this.dyxxkmanager = dyxxkmanager;
	}

}
