package com.jteap.dgt.tyxx.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.dgt.tyxx.manager.TyxxManager;
import com.jteap.dgt.tyxx.model.Tyxxk;

/**
 * 团员信息处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "unchecked", "serial" })
public class TyxxAction extends AbstractAction {

	private TyxxManager tyxxManager;

	@Override
	public HibernateEntityDao getManager() {
		return tyxxManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "zhengzhi", "status","tuanzu", "tuan_zhiwu",
				"birthday", "sex", "bumen", "name", "time","status" };
	}

	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		this.isUseQueryCache=false;
		String hqlWhere = WebUtils.getRequestParam(request, "queryParamsSql");
		String lt = WebUtils.getRequestParam(request, "lt");
		String zt = request.getParameter("zt");
		String orderBy = request.getParameter("sort");
		if (StringUtils.isNotEmpty(hqlWhere)) {
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		if (StringUtils.isEmpty(orderBy)) {
			HqlUtil.addOrder(hql, "sortno", "desc");
		}
		if(StringUtils.isNotEmpty(lt)){
			String hqlWhereTemp = "trunc((to_char(sysdate,'yyyyMMdd')-to_char(obj.birthday,'yyyyMMdd'))/10000)>=28";
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
		if(StringUtils.isNotEmpty(zt)){
			HqlUtil.addCondition(hql, "status", "0");
		}
	}
	/**
	 * 修改团员状态为离团
	 * @return
	 * @throws Exception 
	 */
	public String updateTyxxAction() throws Exception{
		String ids = request.getParameter("ids");
		try{
			String[] tmpIds = ids.split(",");
			for (String id : tmpIds) {
				Tyxxk ty= tyxxManager.get(id);
				ty.setStatus("1");
				ty.setZhengzhi("002");
				tyxxManager.save(ty);
			}
			this.outputJson("{success:true}");
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public TyxxManager getTyxxManager() {
		return tyxxManager;
	}

	public void setTyxxManager(TyxxManager tyxxManager) {
		this.tyxxManager = tyxxManager;
	}

}
