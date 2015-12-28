package com.jteap.jhtj.bbzc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.bbzc.manager.BbIndexManager;
import com.jteap.jhtj.bbzc.model.BbIndex;
@SuppressWarnings({ "unchecked", "serial" })
public class BbIndexAction extends AbstractAction {
	private BbIndexManager bbIndexManager;
	@Override
	public HibernateEntityDao getManager() {
		return bbIndexManager;
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String catalogId=request.getParameter("catalogId");
		HqlUtil.addCondition(hql,"flid",catalogId);
		HqlUtil.addOrder(hql,"sortno", "asc");
	}
	
	/**
	 * 
	 *描述：验证连接名称是否唯一
	 *时间：2010-4-1
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String validateNameUniqueAction() throws Exception {
		String bbbm = request.getParameter("bbbm");//分类ID
		String flid = request.getParameter("flid");//分类ID
		BbIndex index=this.bbIndexManager.findBbIndexByBbbmAndFlid(bbbm, flid);
		if(index!=null){
			outputJson("{unique:false}");
		}else{
			outputJson("{unique:true}");
		}
		return NONE;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","bbbm","bbmc","sortno"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","bbbm","bbmc","sortno"};
	}

	public BbIndexManager getBbIndexManager() {
		return bbIndexManager;
	}

	public void setBbIndexManager(BbIndexManager bbIndexManager) {
		this.bbIndexManager = bbIndexManager;
	}

}
