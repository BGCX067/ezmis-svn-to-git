package com.jteap.yx.jjb.web;

import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.yx.jjb.manager.TreeDataManager;
import com.jteap.yx.jjb.model.TreeDataBean;
/**
 * 运行表单树 数据
 * @author lvchao
 *
 */
@SuppressWarnings({ "unchecked", "serial", "static-access" })	
public class TreeDataAction extends AbstractTreeAction{
	//
	private TreeDataManager treeDataManager;
	
	/**
	 * 显示树形结构的动作
	 * @return
	 * @throws Exception
	 */
	public String showTreeAction() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonConfig.registerJsonBeanProcessor(TreeDataBean.class,jsonBeanProcessor);
//		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
//			public void beanHandler(Object obj, Map map) {
//			}
//		});
		
		//开始json化
		Collection roots=getChildren(null);
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;	
	}
	
	@Override
	protected Collection getChildren(Object bean) {
		//TreeDataBean dt = (TreeDataBean) bean;
		String flag = request.getParameter("flag");
		
		return treeDataManager.findTreeByParentId(flag);
	}

	@Override
	protected String getParentPropertyName(Class arg0) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		// TODO Auto-generated method stub
		String flag = request.getParameter("flag");
		//String parentId = request.getParameter("parentId");
		return treeDataManager.findTreeByParentId(flag);
	}

	@Override
	protected String getSortNoPropertyName(Class arg0) {
		// TODO Auto-generated method stub
		return "id";
	}

	@Override
	protected String getTextPropertyName(Class arg0) {
		// TODO Auto-generated method stub
		return "name";
	}

	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return treeDataManager;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return new String[] { "id", "name", "value","flag","parent" };
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return new String[] { "id", "name", "value","flag","parent" };
	}

	public TreeDataManager getTreeDataManager() {
		return treeDataManager;
	}

	public void setTreeDataManager(TreeDataManager treeDataManager) {
		this.treeDataManager = treeDataManager;
	}


}
