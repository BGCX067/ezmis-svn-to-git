package com.jteap.wz.tjny.web;

import java.util.Collection;
import java.util.List;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.wz.tjny.manager.TjnyManager;
import com.jteap.wz.tjny.model.Tjny;

/**
 * 统计年月处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "unchecked", "serial" })
public class TjnyTreeAction extends AbstractTreeAction {

	private TjnyManager tjnyManager;
	
	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return null;
	}
	

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortno";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "ny";
	}

	@Override
	public HibernateEntityDao getManager() {
		return tjnyManager;
	}
	/**
	 * 删除指定年月的统计
	 * @return
	 * @throws Exception 
	 */
	public String removeTj() throws Exception{
	
		try {
			String nf  = request.getParameter("nf");
			String yf = request.getParameter("yf");
			String lb = request.getParameter("lb");
			String hql = "from Tjny t where t.ny = '"+nf+"年"+yf+"月"+"' and t.bblb = '"+lb+"'";
			List list = tjnyManager.find(hql);
			if(list.size()>0){
				tjnyManager.remove((Tjny)list.get(0));
				this.outputJson("{success:true}");
			}else{
				this.outputJson("{success:false,text:'请稍后再试'}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.outputJson("{success:false,text:'请稍后再试'}");
		}
		return NONE;
	}
	
	@Override
	public String[] listJsonProperties() {

		return new String[]{"id","ny","sortno"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"ny"};
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId = request.getParameter("node");
		String lb = request.getParameter("lb");
		if(parentId!=null && parentId.equals("rootNode"))
			parentId = null;
		StringBuffer hql = new StringBuffer("from Tjny t ");
		if(StringUtil.isNotEmpty(lb)){
			hql.append(" where t.bblb = "+lb);
		}
		hql.append("order by t.ny desc");
		return tjnyManager.find(hql.toString());
	}

	public TjnyManager getTjnyManager() {
		return tjnyManager;
	}

	public void setTjnyManager(TjnyManager tjnyManager) {
		this.tjnyManager = tjnyManager;
	}

}
