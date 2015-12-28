package com.jteap.system.resource.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.person.manager.P2GManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.manager.OperationManager;
import com.jteap.system.resource.manager.ResourceManager;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.role.manager.RoleManager;


/**
 * 资源动作，资源包括模块和操作
 * @author tantyou
 * @date 2008-1-16
 */
@SuppressWarnings({ "unchecked", "serial"})
public class ResourceAction extends AbstractTreeAction<Resource>{

	//资源管理对象
	private ResourceManager resManager; 
	
	//角色管理对象
	private RoleManager roleManager;
	
	//操作管理对象
	private OperationManager operationManager;
	
	//当前用户所拥有的权限集合
	private Collection<Resource> permResources;
	
	private PersonManager personManager;
	
	private P2GManager p2gManager;
	
	
//	/**
//	 * 角色对应的指定资源显示，是一个带CHECKBOX的树，提供给用户选择
//	 * @author 唐剑钢
//	 * 2008-1-15
//	 */
//	@SuppressWarnings("unchecked")
//	public String showResoucesAction() throws Exception {
//       String roleId = this.request.getParameter("roleId");
//		if (StringUtils.isNotEmpty(roleId)) {
//			Role role = roleManager.get(roleId);
//			Collection<Resource> res=roleManager.findResourcesByRole(role);
//			JsonConfig jsonConfig=this.getTreeJsonConfig();		
//			TreeActionJsonBeanProcessor treeBeanProcessor=new TreeActionJsonBeanProcessor();
//		  	treeBeanProcessor.putCommAttrs("uiProvider", "col");
//			
//			jsonConfig.registerJsonBeanProcessor(Resource.class, treeBeanProcessor);
//			JSONArray result=JSONArray.fromObject(res,jsonConfig);
//			this.outputJson(result.toString());
//		}
//		return NONE;	
//	}


//   /**
//    * 角色对应的已指定的的资源，是一个不带CHECKBOX的树,显示给用户
//    * @author 唐剑钢
//    * 2008-1-19
//    */
//	public String showResoucesDetailAction() throws Exception {
//       String roleId = this.request.getParameter("roleId");
//		if (StringUtils.isNotEmpty(roleId)) {
//			Role role = roleManager.get(roleId);
//			Collection<Resource> res=roleManager.findResourcesByRole(role);
//			JsonConfig jsonConfig=this.getTreeJsonConfig();		
//			TreeActionJsonBeanProcessor treeBeanProcessor=new TreeActionJsonBeanProcessor();
//		    jsonConfig.registerJsonBeanProcessor(Resource.class, treeBeanProcessor);
//			JSONArray result=JSONArray.fromObject(res,jsonConfig);
//			this.outputJson(result.toString());
//		}
//		return NONE;	
//	}

//	
//	/**
//	 *  资源添加
//	 * @author Ugen
//	 * @return
//	 * @throws Exception
//	 * 2008-1-31
//	 */
//	public String addResourceAction() throws Exception{
//		
//		Resource res = new Resource();
//		Module mod = new Module();
//		Operation op = new Operation();
//		
//		String resName = StringUtil.isoToUTF8(request.getParameter("resName"));
//		String link = request.getParameter("link");
//		String icon = request.getParameter("icon");
//		String remark = request.getParameter("remark");
//		boolean visiabled = Boolean.getBoolean(request.getParameter("visiabled"));
//		
//		String ops = request.getParameter("opsJson");
//		System.out.println("=================>"+ops);
//		ops=java.net.URLDecoder.decode(ops, "UTF-8");
//		
//		res.setResName(resName);
//		res.setRemark(remark);
//		res.setVisiabled(visiabled);
//		
//		mod.setIcon(icon);
//		mod.setLink(link);
//		op.setAction(ops);
//		op.setBeforeScript(ops);
//		op.setStyle(ops);
//		
//		//resManager.save(res);
//		resManager.save(op);
//		return NONE;
//	}


	
	public P2GManager getP2gManager() {
		return p2gManager;
	}


	public void setP2gManager(P2GManager manager) {
		p2gManager = manager;
	}


	public PersonManager getPersonManager() {
		return personManager;
	}


	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}


	/**
	 * 显示平台所有资源的树，供勾选
	 * @return
	 * @throws Exception
	 */
	public String showAllResourcesForCheck() throws Exception{
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			
			public void beanHandler(Object obj, Map map) {
				map.put("checked",false);
				map.put("ccCheck", true);
			}
		});
//		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
//		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		jsonConfig.registerJsonBeanProcessor(Resource.class, jsonBeanProcessor);
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		

		return NONE;
	}
	/**
	 * 补加方法
	 * 方法功能描述 :选中已经选中的CHECBOX，提供给用户修改资源
	 * @author 唐剑钢
	 * @return
	 * @throws Exception
	 * 2008-1-23
	 * 返回类型：String
	 */
	public String showAllResourcesIsForCheck() throws Exception{
		this.p2gManager.evitPerson();
		String presonid=request.getParameter("id");
		final HashMap<String,String> resourcemap=new HashMap<String,String>();
		if (presonid.indexOf(",") == -1) {
			Person person=this.personManager.get(presonid);
			// 找到人员的所有资源（包括角色资源和指定资源）
			Collection<Resource> ress = resManager.findResourceByPerson(person);
			for(Resource res:ress){
				resourcemap.put(res.getId().toString(), res.getId().toString());
			}
		}
	    
	    JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				//如果这个对象的ID和这个角色对的ID相等那么就过滤掉,设置真,否则设置为假
				map.put("ccCheck", new Boolean(true));
				
				Resource resource=(Resource) obj;
				
				map.put("leaf", resource.getChildRes().size()>0?false:true);
				map.put("expanded", false);
				if(resourcemap.containsKey(resource.getId().toString())){
				   map.put("checked", new Boolean(true));
				}else{
				   map.put("checked", new Boolean(false));
				}
				
			}
			
		});
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		System.out.println(result.toString());
		return NONE;
		
	}
	/**
	 * 取得儿子节点
	 * 需要根据当前用户是否具有该节点的权限进行处理
	 */
	@Override
	protected Collection getChildren(Object bean) {
		Resource res=(Resource) bean;
		Collection children=res.getChildRes();
		if(isCurrentRootUser()){
			return children;
		}else{
			Collection permChildren=new ArrayList();
			for (Object object : children) {
				if(permResources.contains(object)){
					permChildren.add(object);
				}
			}
			return permChildren;
		}
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "parentRes";
	}

	@Override
	protected Collection getRootObjects() throws Exception{
		if(this.isCurrentRootUser()){
			return resManager.findRootResource();	
		}else{
			permResources=resManager.findResourceByPerson(personManager.getCurrentPerson(sessionAttrs));
			Object[] resArray=permResources.toArray();
			Collection<Resource> results=new ArrayList<Resource>();
			for(int i=0;i<resArray.length;i++){
				Resource resI=(Resource) resArray[i];
				String path1=resI.getPathWithText();
				results.add(resI);
				for(int j=0;j<resArray.length;j++){
					if(i==j) continue;
					Resource resJ=(Resource) resArray[j];
					String path2=resJ.getPathWithText();
					if(path1.indexOf(path2)>=0){
						results.remove(resI);
					}
				}
			}
			return results;
		}
		
	}
	
	
	public OperationManager getOperationManager() {
		return operationManager;
	}

	public void setOperationManager(OperationManager operationManager) {
		this.operationManager = operationManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public ResourceManager getResManager() {
		return resManager;
	}

	public void setResManager(ResourceManager resManager) {
		this.resManager = resManager;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortNo";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "resName";
	}

	
	@Override
	public HibernateEntityDao getManager() {
		return resManager;
	}

	
	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","resName","childRes","type"};
	}

	@Override
	public String[] updateJsonProperties() {
		
		return null;
	}

}
