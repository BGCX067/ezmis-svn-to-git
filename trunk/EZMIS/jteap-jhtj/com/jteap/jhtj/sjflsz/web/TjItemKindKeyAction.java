package com.jteap.jhtj.sjflsz.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.jhtj.sjflsz.manager.TjItemKindKeyManager;
import com.jteap.jhtj.sjflsz.model.TjItemKindKey;
@SuppressWarnings({ "unchecked", "serial" })
public class TjItemKindKeyAction extends AbstractAction {
	private TjItemKindKeyManager tjItemKindKeyManager;
	@Override
	public HibernateEntityDao getManager() {
		return tjItemKindKeyManager;
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String kid=request.getParameter("kid");
		HqlUtil.addCondition(hql, "kid",kid);
		HqlUtil.addOrder(hql, "iorder", "asc");
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
		String icode = request.getParameter("name");//关键字编码
		String kid = request.getParameter("kid");//分类ID
		TjItemKindKey key=this.tjItemKindKeyManager.findTjItemKindKeyByKidAndIcode(kid, icode);
		if(key!=null){
			outputJson("{unique:false}");
		}else{
			outputJson("{unique:true}");
		}
		//outputJson("{unique:true}");
		return NONE;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","kid","icode","iname","lx","cd","iorder"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","kid","icode","iname","lx","cd","iorder"};
	}

	public TjItemKindKeyManager getTjItemKindKeyManager() {
		return tjItemKindKeyManager;
	}

	public void setTjItemKindKeyManager(TjItemKindKeyManager tjItemKindKeyManager) {
		this.tjItemKindKeyManager = tjItemKindKeyManager;
	}
}
