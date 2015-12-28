package com.jteap.system.role.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.ArrayUtils;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.manager.R2RManager;
import com.jteap.system.role.model.R2R;
import com.jteap.system.role.model.Role;

/**
 * 主要用戶顯示屬于特定角色的資源樹結構
 * 
 * @author tantyou
 * @date 2008-1-23
 */
@SuppressWarnings({ "unchecked", "serial" })
public class R2RAction extends AbstractTreeAction<R2R> {
	private R2RManager r2rManager;		//resource to role manager object

	/**
	 * 取得父親節點的屬性名稱
	 */
	@Override
	protected String getParentPropertyName(Class beanClass) {
		String result="parentRes";
		if(R2R.class.isAssignableFrom(beanClass)){
			result= "resource.parentRes";
		}
		if(Resource.class.isAssignableFrom(beanClass)){
			result="parentRes";
		}
		return result;
	}

	/**
	 * 取得根節點集合
	 */
	@Override
	protected Collection getRootObjects() {
		//String roleId="40288147179b76a001179b81d6650001";
		String roleId=request.getParameter("roleId");
		Role role=r2rManager.get(Role.class, roleId);
		return r2rManager.findResourcesByRole(role, null,null);
	}
	
	/**
	 * 取得特定節點的子節點結合
	 */
	@Override
	protected Collection getChildren(Object bean) {
		if(bean instanceof R2R){
			R2R r2r=(R2R) bean;
			return r2rManager.findResourcesByRole(r2r.getRole(), r2r.getResource(),null);
		}else{
			Resource r=(Resource) bean;
			return r.getChildRes();
		}
	}
	/**
	 * 排序屬性名稱
	 */
	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		String result="sortNo";
		if(R2R.class.isAssignableFrom(beanClass)){
			result= "resource.sortNo";
		}
		if(Resource.class.isAssignableFrom(beanClass)){
			result="sortNo";
		}
		return result;
	}

	/**
	 * 節點顯示的文本屬性
	 */
	@Override
	protected String getTextPropertyName(Class beanClass) {
		String result="resName";
		if(R2R.class.isAssignableFrom(beanClass)){
			result= "resource.resName";
		}
		if(Resource.class.isAssignableFrom(beanClass)){
			result="resName";
		}
		return result;
		
	}

	//管理對象
	@Override
	public HibernateEntityDao getManager() {
		return r2rManager;
	}

	//用戶json化的字段
	@Override
	public String[] listJsonProperties() {
		return new String[] {"resName", "id","inherit","communicable","resourceId"};
	}

	
	@Override
	public String[] updateJsonProperties() {
		return null;
	}


//////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 顯示角色所擁有的資源樹
	 * @return
	 * @throws Exception
	 */
	public String showRoleResourceAction() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		//设定自定义参数
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				if(obj instanceof R2R){
					R2R r2r=(R2R) obj;
					map.put("resourceId", r2r.getResource().getId());
					map.put("inherit", r2r.isInherit());// 當前資源是否為繼承的資源
					map.put("communicable", r2r.isCommunicable());//添加擴展屬性 當前資源是否為繼承的資源
				}
				map.put("uiProvider", "col");
			}
		});
		//注册json化的java bean 处理器
		jsonConfig.registerJsonBeanProcessor(R2R.class,jsonBeanProcessor);
		jsonConfig.registerJsonBeanProcessor(Resource.class, jsonBeanProcessor);
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		
		return NONE;
	}
	
	
	/**
	 * 顯示角色所擁有的可传播的資源樹
	 * @return
	 * @throws Exception
	 */
	public String showRoleCommunicableResourceAction() throws Exception{
		String roleId=request.getParameter("roleId");
		Role role=r2rManager.get(Role.class, roleId);
		
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		//添加个性化属性
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				if(obj instanceof R2R){
					R2R r2r=(R2R) obj;
					map.put("resourceId", r2r.getResource().getId());
					map.put("inherit", r2r.isInherit());// 當前資源是否為繼承的資源
					map.put("communicable", r2r.isCommunicable());//添加擴展屬性 當前資源是否為繼承的資源
					map.put("checked",false);
					map.put("ccCheck", true);
					map.put("id", r2r.getResource().getId());
				}
			}
		});
		//设定自定义子节点处理方法，子显示可传播的子节点
		jsonBeanProcessor.setCustomChildNodesHandler(new CustomChildNodesHandler(){

			public Collection getChildNodes(Object bean) {
				if(bean instanceof R2R){
					R2R r2r=(R2R) bean;
					return r2rManager.findResourcesByRole(r2r.getRole(), r2r.getResource(),true);
				}else{
					Resource r=(Resource) bean;
					return r.getChildRes();
				}
			}
			
		});
		jsonConfig.registerJsonBeanProcessor(R2R.class,jsonBeanProcessor);
		jsonConfig.registerJsonBeanProcessor(Resource.class, jsonBeanProcessor);
		//开始json化
		Collection roots=r2rManager.findResourcesByRole(role, null,true);
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		
		return NONE;
	}
	/**
	 * 为角色指定资源的动作
	 * @return
	 * @throws Exception
	 */
	public String indicatResourceForRoleAction() throws Exception{
		String roleId=request.getParameter("roleId");
		String originalValue[]=request.getParameter("original").split(",");//原资源
		String nowValue[]=request.getParameter("now").split(",");//目前选中的
		List<String> subed=ArrayUtils.difference(originalValue, nowValue);
		List<String> added=ArrayUtils.difference(nowValue,originalValue);
		Role role=r2rManager.get(Role.class,roleId);
		//消除已经删除的资源
		for (String id : subed) {
			if(!id.equals("")){
				R2R r2r=r2rManager.findR2R(roleId, id);
				//清除的时候不能清除从上级继承的关联，只清除指定的资源
				if(!r2r.isInherit()){
					r2rManager.cascadeRemoveResourceFromRole(role, id);	
				}
			}	
		}
		//添加新加的资源
		for (String id : added) {
			if(!id.equals("")){
				boolean isExist=r2rManager.isExist(roleId, id);
				if(!isExist){//已经存在的r2r表明存在继承的资源，继承的资源不用理会
					r2rManager.indicatResourceToRole(roleId, id.toString());
				}
				
			}
				
		}
		String result="{success:true}";
		this.outputJson(result);
		
		return NONE;
	}
	/**
	 * 为角色指定继承资源的动作
	 * @return
	 * @throws Exception
	 */
	public String extendResourceForRoleAction() throws Exception{
		String roleId=request.getParameter("roleId");
		String originalValue[]=request.getParameter("original").split(",");//原资源
		String nowValue[]=request.getParameter("now").split(",");//目前选中的
		List<String> subed=ArrayUtils.difference(originalValue, nowValue);
		List<String> added=ArrayUtils.difference(nowValue,originalValue);
		Role role=r2rManager.get(Role.class,roleId);
		//消除已经删除的资源
		for (String id : subed) {
			if(!id.equals("")){
				R2R r2r=r2rManager.findR2R(roleId, id);
				if(r2r.isInherit()){
					r2rManager.cascadeRemoveResourceFromRole(role, id);	
//					System.out.println("删除了:"+id);
				}
			}
		}
		//添加新加的资源
		for (String id : added) {
			if(!id.equals("")){
				R2R r2r=r2rManager.findR2R(roleId, id);
				if(r2r!=null){
					r2r.setInherit(true);
					r2rManager.save(r2r);
				}else{
					r2rManager.cascadeSaveResource4Role(role,id);
//					System.out.println("添加了:"+id);
//					r2rManager.cascadeExtendResouce(roleId, id);
 
				}
			}
		}
		String result="{success:true}";
		this.outputJson(result);
		
		return NONE;
	}
	
	/**
	 * 批量设置指定R2R的可传播状态
	 * @return
	 * @throws Exception
	 */
	public String setR2RCommunicableAction() throws Exception{
		String ids=request.getParameter("ids");//修改状态的r2r的id的逗号分隔字符串
		String sCommunicable=request.getParameter("communicable");
		r2rManager.batchUpdateR2RCommunicable(ids.split(","),Boolean.parseBoolean(sCommunicable));
		this.outputJson("{success:true}");
		return NONE;
	}
	/////////////////////////////////////////////////////////////////////////
	public R2RManager getR2rManager() {
		return r2rManager;
	}

	public void setR2rManager(R2RManager manager) {
		r2rManager = manager;
	}

	
    
}
