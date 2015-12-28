package com.jteap.system.dataperm.web;

import java.util.Collection;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.dataperm.manager.DatapermManager;
import com.jteap.system.dataperm.model.DataPerm;
@SuppressWarnings({ "unchecked", "serial" })
public class DatapermCatalogAction extends AbstractTreeAction<DataPerm> {

	private DatapermManager datapermManager;
	
	public DatapermManager getDatapermManager() {
		return datapermManager;
	}

	public void setDatapermManager(DatapermManager datapermManager) {
		this.datapermManager = datapermManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		return datapermManager.getAll();
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "dorder";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "datapermcname";
	}

	@Override
	public HibernateEntityDao getManager() {
		return datapermManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
				"id",
				"datapermname",
				"datapermcname",
				"sql",
				"qualification",
				"tablename",
				"dorder",
			""};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
				"id",
				"datapermname",
				"datapermcname",
				"sql",
				"qualification",
				"tablename",
				"dorder",
			""};
	}

//	
//	@Override
//	protected TreeActionJsonBeanHandler injectJsonBeanHandler() {
//		return new TreeActionJsonBeanHandler(){
//
//			public void beanHandler(Object obj, Map map) {
//				DataPerm table=(DataPerm) obj;
//				map.put("checked",true);
//			}
//			
//		};
//	}
	
	/**
	 * 显示用户被选中的树
	 */
	public String showTreeAndCheckedAction() throws Exception{
		String personid=request.getParameter("personid");
		final Map<String, DataPerm> datapermMap=this.datapermManager.getDatapermByPersonId(personid);
		
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				//map.put("ccCheck", new Boolean(false));
				DataPerm dataperm=(DataPerm) obj;
				if(datapermMap.containsKey(dataperm.getId())){
				   map.put("checked", new Boolean(true));
				}else{
				   map.put("checked", new Boolean(false));
				}
			}
		});
		
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		
		Collection roots=getRootObjects();
		//开始json化
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		System.out.println(result);
		this.outputJson(result.toString());
		return NONE;
	}

	
	/**
	 * 显示角色被选中的树
	 */
	public String showTreeAndCheckedRoleAction() throws Exception{
		String roleid=request.getParameter("roleid");
		final Map<String, DataPerm> datapermMap=this.datapermManager.getDatapermByRoleId(roleid);
		
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				//map.put("ccCheck", new Boolean(false));
				DataPerm dataperm=(DataPerm) obj;
				if(datapermMap.containsKey(dataperm.getId())){
				   map.put("checked", new Boolean(true));
				}else{
				   map.put("checked", new Boolean(false));
				}
			}
		});
		
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		
		Collection roots=getRootObjects();
		//开始json化
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		System.out.println(result);
		this.outputJson(result.toString());
		return NONE;
	}
}
