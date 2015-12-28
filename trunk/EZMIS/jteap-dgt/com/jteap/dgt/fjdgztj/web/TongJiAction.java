package com.jteap.dgt.fjdgztj.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.dgt.fjdgztj.manager.TongjiManager;
import com.jteap.dgt.fjdgztj.model.Tongji;
import com.jteap.form.eform.manager.EFormManager;
/**
 * 工会分季度统计处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "unchecked", "serial" })
public class TongJiAction extends AbstractAction {

	private TongjiManager tongjiManager;
	private EFormManager eformManager;
	
	public EFormManager getEformManager() {
		return eformManager;
	}


	public void setEformManager(EFormManager eformManager) {
		this.eformManager = eformManager;
	}


	@Override
	public HibernateEntityDao getManager() {
		return tongjiManager;
	}

	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		
		this.isUseQueryCache=false;
		String gonghui = request.getParameter("gonghui");
		String year = request.getParameter("year");
		String jidu = request.getParameter("jidu");		
		String orderBy = request.getParameter("sort");
		if(StringUtil.isNotEmpty(gonghui)){
			HqlUtil.addCondition(hql, "gonghui",gonghui);
		}
		if(StringUtil.isNotEmpty(year)){
			HqlUtil.addCondition(hql, "year",year);
		}
		if(StringUtil.isNotEmpty(jidu)){
			HqlUtil.addCondition(hql, "jidu",jidu);
		}
		if (StringUtil.isEmpty(orderBy)) {
			HqlUtil.addOrder(hql, "year", "desc");
		}
	}

	/**
	 * 根据自定义表单formSn和记录ID删除自定义表单记录
	 * @return
	 * @throws Exception 
	 */
	public String delEFormRecAction() throws Exception{
		String ids = request.getParameter("ids");
		try{
			if(ids.indexOf(",")!=-1){
				String[] id = ids.split(",");
				for(int i=0;i<id.length;i++){
					Tongji tongji = tongjiManager.get(id[i]);
					tongjiManager.remove(tongji);
				}
			}else{
				Tongji tongji = tongjiManager.get(ids.substring(0, ids.length()-1));
				tongjiManager.remove(tongji);
			}
			this.outputJson("{success:true}");
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","gonghui","year","jidu"};
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}

	public TongjiManager getTongjiManager() {
		return tongjiManager;
	}

	public void setTongjiManager(TongjiManager tongjiManager) {
		this.tongjiManager = tongjiManager;
	}



}
