package com.jteap.dgt.dzz.web;

import java.util.Collection;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.dgt.dzz.manager.DzzManager;
import com.jteap.dgt.dzz.model.Dzz;

/**
 * 党组织处理类
 * @author lvchao
 *
 */
@SuppressWarnings( { "unchecked", "serial" })
public class DzzTreeAction extends AbstractTreeAction {

	private DzzManager dzzManager;
	
	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return null;
	}
	
	public String getDangNameAction() throws Exception{
		String id=request.getParameter("id");
		//System.out.println(id);
		Dzz dzz=dzzManager.get(id);
		
		String json = JSONUtil.objectToJson(dzz, updateJsonProperties());
		outputJson("{success:true,data:[" + json + "]}");
		
		return null;
	}
	
	

	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId = request.getParameter("node");
		if(parentId!=null && parentId.equals("rootNode"))
			parentId = null;
		return dzzManager.getAll();
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortno";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "dangzu_name";
	}

	@Override
	public HibernateEntityDao getManager() {
		return dzzManager;
	}

	@Override
	public String[] listJsonProperties() {

		return new String[]{"id","dangzu_name","sortno"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"dangzu_name"};
	}

	public DzzManager getDzzManager() {
		return dzzManager;
	}

	public void setDzzManager(DzzManager dzzManager) {
		this.dzzManager = dzzManager;
	}


}
