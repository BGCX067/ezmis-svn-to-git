package com.jteap.system.resource.web;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.NumberUtils;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.index.manager.MyQuickManager;
import com.jteap.index.model.MyQuick;
import com.jteap.system.person.manager.P2ResManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Res;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.manager.ModuleManager;
import com.jteap.system.resource.manager.ResourceManager;
import com.jteap.system.resource.manager.ResourcesUsersManager;
import com.jteap.system.resource.model.Module;
import com.jteap.system.resource.model.Operation;
import com.jteap.system.resource.model.Resource;
import com.jteap.system.resource.model.ResourcesUsers;
import com.jteap.system.role.manager.R2RManager;
import com.jteap.system.role.manager.RoleManager;

/**
 * 模块动作
 * 
 * @author tantyou
 * @date 2008-1-17
 */ 

@SuppressWarnings( { "unchecked", "serial", "unused" })
public class ModuleAction extends AbstractTreeAction<Module> {

	// 模块管理对象
	private ModuleManager moduleManager;
	// 资源管理器
	private ResourceManager resManager;
	// 角色管理对象
	private RoleManager roleManager;
	// person to resource manager object
	private P2ResManager p2resManager;
	// role to resource manager object
	private R2RManager r2rManager;
	private ResourcesUsersManager resourcesUsersManager;
	private SessionFactory sessionFactory;
	private PersonManager personManager;
	private MyQuickManager myQuickManager;
	
	public void setMyQuickManager(MyQuickManager myQuickManager) {
		this.myQuickManager = myQuickManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	/**
	 * 获取指定模块的详细信息 Ugen 2008-1-18
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getModuleDetailAction() throws Exception {
		String resId = request.getParameter("id");
		if (StringUtils.isNotEmpty(resId)) {
			Module module = moduleManager.get(resId);
			String[] field1 = { "resName", "link", "adminOp", "parentRes",
					"showText", "remark", "visiabled", "icon", "tip",
					"childRes", "sn", "tip", "type" };

			String json = JSONUtil.objectToJson(module, field1);
			this.outputJson("{success:true,data:" + json + "}");
		}
		return NONE;
	}



	/**
	 * 当将模块节点从一个模块下拖拽到另外一个模块之后，需要修改两个parent的leaf属性
	 */
	@Override
	protected void dragMoveNodeProcess(Object obj, boolean parentChanged,
			String oldParentId, String newParentId) {
		this._clearL2Cache();
		
		Module m = (Module) obj;
		if(parentChanged){
			Module opm = null;
			Module npm = null;
			if(StringUtil.isNotEmpty(oldParentId)){
				opm = moduleManager.get(oldParentId);
			}
			if(StringUtil.isNotEmpty(newParentId)){
				npm = moduleManager.get(newParentId);
			}
			
			if(npm!=null) {
				npm.getChildRes().add(m);
				m.setParentRes(npm);
				if(npm.getLeafModule()!=null && npm.getLeafModule().equals(true)){
					npm.setLeafModule(false);
					moduleManager.save(npm);
				}
				
			}
			
			if(opm!=null){
				opm.getChildRes().remove(m);
				if(opm.getLeafModule() != null && opm.getLeafModule().equals(false)){
					boolean hasModule = false;
					for (Resource childRes : opm.getChildRes()) {
						if(childRes instanceof Module){
							hasModule = true;
							break;
						}
					}
					if(!hasModule){
						opm.setLeafModule(true);
					}
					moduleManager.save(opm);
				}
			}
			
			
			
			
			
			
			
		}
		
	}

	/**
	 * 显示树形结构的动作，模块管理用
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showTreeAction() throws Exception {
		JsonConfig jsonConfig = getTreeJsonConfig();// 配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor = new TreeActionJsonBeanProcessor();
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler() {
			public void beanHandler(Object obj, Map map) {
				Module module = (Module) obj;
				if (StringUtils.isNotEmpty(module.getIcon())) {
					map.put("icon", request.getContextPath() + "/"
							+ module.getIcon());
				}
				map.put("expanded", false);
				if(module.getLeafModule()!=null && module.getLeafModule()==true)
					map.put("leaf", true);
				else
					map.put("leaf", false);
			}
		});
		final Class cls = GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls, jsonBeanProcessor);

		// 开始json化
		Collection roots = getRootObjects();
		JSONArray result = JSONArray.fromObject(roots, jsonConfig);
		// 输出
		this.outputJson(result.toString());
		return NONE;
	}

	/**
	 * 显示功能树型，带有权限检查
	 * @return
	 * @throws Exception
	 */
	public String showFunctionTreeAction() throws Exception{
		try{
			JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
			TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
			jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
				//为节点添加自定义的属性
				public void beanHandler(Object obj, Map map) {
					Resource module=(Resource) obj;
					Object icon = BeanUtils.getProperty2(module, "icon");
					Object link = BeanUtils.getProperty2(module, "link");
					//如果有指定模块的图标，则将图标显示出来
					if(icon!=null){
						map.put("icon", request.getContextPath()+"/"+icon);
					}
					//如果模块未指定链接，就作为中间节点的样式显示
					if(module.getParentRes()==null){
						map.put("cls", "func-node");
					}else{
						map.put("link",link);
					}
					map.put("expanded",false);
					if(module.getLeafModule()!=null && module.getLeafModule().equals(true))
						map.put("leaf",true);
					else
						map.put("leaf",false);
					
				}});
			
//			//当前用户所拥有权限的资源
//			final Collection<Resource> permRes;
//			if(isCurrentRootUser()){
//				permRes=resManager.findAllVisibledResource();
//			}else{
//				permRes=resManager.findResourceByPerson(getCurrentPerson());
//			}
			
			String parentId = request.getParameter("node");
			if(parentId!=null && parentId.equals("rootNode")){
				parentId=null;
			}
			//根节点集合
			Collection roots=moduleManager.findModuleByParentWithPerm(personManager.getCurrentPerson(sessionAttrs),parentId);	
			
			//子节点怎么取
			jsonBeanProcessor.setCustomChildNodesHandler(new CustomChildNodesHandler(){
				public Collection getChildNodes(Object obj) {
					return null;
				}});
			jsonConfig.registerJsonBeanProcessor(Module.class,jsonBeanProcessor);
			jsonConfig.registerJsonBeanProcessor(Resource.class,jsonBeanProcessor);
			//开始json化
			JSONArray result=JSONArray.fromObject(roots,jsonConfig);
			//输出
			String json = result.toString();
			this.outputJson(json);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;	
	}
	
	/**
	 * 显示功能树所有顶级父节点(动态加载菜单)
	 */
	public String showTreeParentNodeAction() throws Exception {
		Collection<Module> roots = moduleManager.findModule(null);
		String result = JSONUtil.listToJson(roots, new String[]{"id","resName","link","icon"});
		this.outputJson(result);
		return NONE;
	}
	

	/**
	 * 创建/保存模块
	 * 
	 * @return
	 */
	public String saveUpdateAction() throws BusinessException {
		try {
			String resName = request.getParameter("resName"); // 模块名称
			String icon = request.getParameter("icon"); // 图标
			String link = request.getParameter("link"); // 链接
			String remark = request.getParameter("remark"); // 备注
			String visiabled = request.getParameter("visiabled"); // 可见性
			String ops = request.getParameter("ops"); // 操作列表
			String id = request.getParameter("id"); // 编号用于判断是否修改
			String parentId = request.getParameter("parentId"); // 父亲模块

			Module module;
			if (StringUtils.isNotEmpty(id)) {
				// 修改
				module = moduleManager.get(id);
				if (StringUtils.isNotEmpty(ops)) {
					List<Map<String, String>> opList = JSONUtil.parseList(ops);
					for (Map opMap : opList) {
						String opName = (String) opMap.get("name");
						Operation op = moduleManager.findOperationByShortName(module, opName);
						if (op == null) {
							op = new Operation();
							module.getChildRes().add(op);
							op.setParentRes(module);
						}
						setOperationProperties(op, opMap);
					}
				}
			} else {
				// 新增
				module = new Module();
				module.setType(Resource.RES_TYPE_MODULE);
				module.setSortNo((int) NumberUtils.randomNumber(1000));
				// 创建的时候指定父亲资源，修改的时候不指定资源
				if (StringUtils.isNotEmpty(parentId)) {
					Module parentModule = moduleManager.get(parentId);
					parentModule.getChildRes().add(module);
					module.setParentRes(parentModule);
					if(parentModule.getLeafModule()!=null && parentModule.getLeafModule().equals(true)){
						parentModule.setLeafModule(false);
						moduleManager.save(parentModule);
					}
				}
				module.setLeafModule(true);
				// 新增操作
				if (StringUtils.isNotEmpty(ops)) {
					List<Map<String, String>> opList = JSONUtil.parseList(ops);
					int sortNo = 0;
					for (Map opMap : opList) {
						sortNo++;
						Operation op = new Operation();
						setOperationProperties(op, opMap);
						op.setSortNo(sortNo);

						module.getChildRes().add(op);
						op.setParentRes(module);
					}
				}
			}
			// 设定各种属性
			module.setResName(resName);
			module.setIcon(icon);
			module.setLink(link);
			module.setRemark(remark);
			module.setResStyle('0');
			module.setVisiabled(StringUtils.isNotEmpty(visiabled)
					&& visiabled.equals("on") ? true : false);
			// 保存
			moduleManager.save(module);
			this.outputJson("{success:true}");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		
		return NONE;
	}

	/**
	 * 自定义模块创建/保存
	 */
	public String saveUpdateIndividualAction() {
		try {
			String resName = request.getParameter("resName"); // 模块名称
			String link = request.getParameter("link"); // 链接
			String remark = request.getParameter("remark"); // 备注
			String resStyle = request.getParameter("resStyle"); // 模块种类
			String id = request.getParameter("resourceId"); // 编号用于判断是否修改
			String parentId = request.getParameter("parentId"); // 父亲模块

			Module module;
			if (StringUtils.isNotEmpty(id)) {
				// 修改
				module = moduleManager.get(id);
			} else {
				// 新增
				module = new Module();
				module.setType(Resource.RES_TYPE_MODULE);
				module.setSortNo((int) NumberUtils.randomNumber(1000));
				// 创建的时候指定父亲资源，修改的时候不指定资源
				if (StringUtil.isNotEmpty(parentId)) {
					Module parentModule = moduleManager.get(parentId);
					module.setParentRes(parentModule);
					if(parentModule.getLeafModule()!=null && parentModule.getLeafModule().equals(true)){
						parentModule.setLeafModule(false);
						moduleManager.save(parentModule);
					}
				}
				module.setLeafModule(true);
				module.setResStyle('1');
				Set<P2Res> p2ress = new HashSet<P2Res>();
				P2Res p2r = new P2Res();
				p2r.setIndicator(personManager.getCurrentPerson(sessionAttrs).getUserLoginName());
				p2r.setPerson(personManager.getCurrentPerson(sessionAttrs));
				p2r.setResource(module);
				p2ress.add(p2r);
				module.setP2ress(p2ress);
			}
			// 设定各种属性
			module.setResName(resName);
			module.setLink(link);
			module.setRemark(remark);
			module.setVisiabled(true);

			// 保存
			moduleManager.save(module);
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException();
		}
		return NONE;
	}

	/**
	 * 删除节点
	 */
	@Override
	public String deleteNodeAction() {
		try {
			
			String nodeId = request.getParameter("nodeId");
			Module module = moduleManager.get(nodeId);
			moduleManager.deleteModule(module);
			_clearL2Cache();
			outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException();
		}
		return NONE;
	};
	
	private void _clearL2Cache(){
		sessionFactory.evict(Resource.class);
		sessionFactory.evict(Module.class);
		sessionFactory.evict(Operation.class);
		sessionFactory.evictCollection("com.jteap.system.resource.model.Resource.childRes");
		sessionFactory.evictQueries();
	}
	/**
	 * 得到快速链接模块
	 */
	public String getQuickLinkAction() {
		try {
			List<ResourcesUsers> list = resourcesUsersManager.findQuickLink(personManager.getCurrentPerson(sessionAttrs));

			StringBuffer json = new StringBuffer();
			json.append("{success:true,data:[");
			for (ResourcesUsers resUser : list) {
				Module module = moduleManager
						.get(resUser.getResource().getId());
				json.append("{'id':'");
				json.append(module.getId());
				json.append("','text':'");
				if (StringUtil.isNotEmpty(resUser.getNewName())) {
					json.append(resUser.getNewName());
				} else {
					json.append(module.getResName());
				}
				json.append("','link':'");
				json.append(module.getLink());
				json.append("','resStyle':'");
				json.append(module.getResStyle());
				json.append("','icon':'");
				json.append(request.getContextPath() + "/" + module.getIcon());
				json.append("'},");
			}

			String result = json.toString();
			if (list.size() > 0) {
				int index = result.lastIndexOf(",");
				result = result.substring(0, index);
			}
			result += "]}";
			this.outputJson(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	/**
	 * 显示二级菜单，带有权限检查
	 * @return
	 * @throws Exception
	 */
	public String showFunctionMenuTreeAction() throws Exception{
		String moduleId = request.getParameter("moduleId");
		
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				Resource module=(Resource) obj;
				Object icon = BeanUtils.getProperty2(module, "icon");
				Object link = BeanUtils.getProperty2(module, "link");
				//如果有指定模块的图标，则将图标显示出来
				if(icon!=null){
					map.put("icon", request.getContextPath()+"/"+icon);
				}
				//如果模块未指定链接，就作为中间节点的样式显示
				if(module.getParentRes()==null){
					map.put("cls", "func-node");
				}else{
					map.put("link",link);
				}
				map.put("expanded",false);
				if(module.getLeafModule()!=null && module.getLeafModule().equals(true)){
					map.put("leaf",true);
					
				}else{
					map.put("leaf",false);
				}
				
			}
			
		});
		
		String parentId = moduleId;
		if(parentId.equals("rootNode")){
			parentId=null;
		}
		//根节点集合
		Collection roots=moduleManager.findModuleByParentWithPerm(personManager.getCurrentPerson(sessionAttrs),parentId);	
		
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);

		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		
		return NONE;	
	}
	
//
//	/**
//	 * 处理个性化节点
//	 * 
//	 * @param jsonConfig
//	 * @param jsonBeanProcessor
//	 * @throws Exception
//	 */
//	private JSONArray individualNode(JsonConfig jsonConfig,
//			TreeActionJsonBeanProcessor jsonBeanProcessor,
//			final List<ResourcesUsers> resUsers) throws Exception {
//		// 当前用户所拥有权限的资源
//		final Collection<Resource> permRes;
//		if (isCurrentRootUser()) {
//			permRes = resManager.findAllVisibledResource();
//			Collection<Resource> add = p2resManager
//					.findAllDirResourceOfThePerson(getCurrentPerson());
//			permRes.addAll(add);
//		} else {
//			permRes = resManager.findResourceByPerson(getCurrentPerson());
//		}
//
//		// 根节点集合
//		Collection<Module> roots = moduleManager.findModuleWithVisible(null,
//				permRes);
//		// 个性化命名
//		roots = individualResName(resUsers, roots);
//
//		// 子节点怎么取
//		jsonBeanProcessor
//				.setCustomChildNodesHandler(new CustomChildNodesHandler() {
//
//					public Collection getChildNodes(Object obj) {
//						Collection coll = moduleManager.findModuleWithVisible(
//								(Module) obj, permRes);
//						// 个性化命名
//						coll = individualResName(resUsers, coll);
//						return coll;
//					}
//				});
//		jsonConfig.registerJsonBeanProcessor(Module.class, jsonBeanProcessor);
//
//		// 开始json化
//		JSONArray result = JSONArray.fromObject(roots, jsonConfig);
//		return result;
//	}

	/**
	 * 如果有个性化设置，取出个性化命名
	 * 
	 * @param resUsers
	 * 
	 * @param roots
	 * @return
	 */
	private Collection<Module> individualResName(List<ResourcesUsers> resUsers,
			Collection<Module> roots) {
		Collection<Module> result = new ArrayList<Module>();
		Iterator<Module> it = roots.iterator();
		while (it.hasNext()) {
			Module mo = (Module) it.next();
			Module newRes = mo;
			// 遍历个性化资源
			for (ResourcesUsers resUser : resUsers) {
				// 如果有个性化资源
				if (resUser.getResource().getId().equals(mo.getId())
						&& StringUtil.isNotEmpty(resUser.getNewName())) {
					try {
						// 新建一个资源
						newRes = new Module();
						// 将资源中所有属性copy到新资源实例中
						BeanUtils.copyProperties(newRes, mo);
						newRes.setResName(resUser.getNewName());
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			result.add(newRes);
		}
		return result;
	}

	/**
	 * 设定操作的属性
	 * 
	 * @param op
	 * @param map
	 */
	private void setOperationProperties(Operation op, Map opMap) {
		String opResName = (String) opMap.get("text");
		String opName = (String) opMap.get("name");
		String opIcon = (String) opMap.get("icon");
		String opTip = (String) opMap.get("tip");
		Boolean opShowText = (Boolean) opMap.get("showText");
		Boolean adminOp = (Boolean) opMap.get("adminOp");

		op.setResName(opResName);
		op.setIcon(opIcon);
		op.setTip(opTip);

		op.setSn(opName);
		op.setVisiabled(true);
		op.setShowText(opShowText);
		op.setAdminOp(adminOp);
		op.setType(Resource.RES_TYPE_OPERATION);
		op.setResStyle('0');

	}

	/**
	 * 删除模块动作之前，需要确定该模块没有子模块了
	 */
	@Override
	protected String beforeDeleteNode(Object node) throws Exception {
		Module module = (Module) node;
		for (Resource childRes : module.getChildRes()) {
			if (childRes instanceof Module) {
				return "请先删除该模块的子模块";
			}
		}
//		List<ResourcesUsers> list = resourcesUsersManager.findBy("resource.id",
//				module.getId());
//		if (list != null && list.size() > 0) {
//			return "请先删除该模块对应的个性化资源";
//		}
		return null;
	}

	/**
	 * 显示我的快捷功能树型,带有权限检查
	 * @return
	 * @throws Exception
	 */
	public String showMyQuickTreeAction() throws Exception{
		try{
			JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
			TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
			jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
				//为节点添加自定义的属性
				public void beanHandler(Object obj, Map map) {
					Resource module=(Resource) obj;
					Object icon = BeanUtils.getProperty2(module, "icon");
					Object link = BeanUtils.getProperty2(module, "link");
					//如果有指定模块的图标，则将图标显示出来
					if(icon!=null){
						map.put("icon", request.getContextPath()+"/"+icon);
					}
					//如果模块未指定链接，就作为中间节点的样式显示
					if(module.getParentRes()==null){
						map.put("cls", "func-node");
					}
					map.put("link",link);
					map.put("expanded",true);
					if(module.getLeafModule()!=null && module.getLeafModule().equals(true))
						map.put("leaf",true);
					else
						map.put("leaf",false);
					
				}});
			
			String parentId = request.getParameter("node");
			if(parentId!=null && parentId.equals("rootNode")){
				parentId=null;
			}
			//根节点集合
			Collection roots=moduleManager.findModuleByParentWithPerm(personManager.getCurrentPerson(sessionAttrs),parentId);	
			
			//获取当前当前登录人的 快捷设置
			Person currentPerson = personManager.getCurrentPerson(sessionAttrs);
			MyQuick myQuick = myQuickManager.findUniqueBy("personId", currentPerson.getId());
			if(myQuick != null && myQuick.getModuleIds() != null){
				String[] array_moduleIds = myQuick.getModuleIds().split(",");
				
				List list = (List)roots;
				for(int i=0; i<list.size(); i++){
					Module module = (Module) list.get(i);
					boolean isShow = false;
					
					for (int j = 0; j < array_moduleIds.length; j++) {
						if(module.getId().equals(array_moduleIds[j])){
							isShow = true;
							break;
						}
					}
					
					//如果快捷设置中没有该模块Id,则从 collection集合中移除
					if(!isShow){
						roots.remove(module);
						i--;
					}
				}
			}
			
			//子节点怎么取
			jsonBeanProcessor.setCustomChildNodesHandler(new CustomChildNodesHandler(){
				public Collection getChildNodes(Object obj) {
					return null;
				}});
			jsonConfig.registerJsonBeanProcessor(Module.class,jsonBeanProcessor);
			jsonConfig.registerJsonBeanProcessor(Resource.class,jsonBeanProcessor);
			//开始json化
			JSONArray result=JSONArray.fromObject(roots,jsonConfig);
			//输出
			String json = result.toString();
			this.outputJson(json);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;	
	}
	
	/**
	 * 显示我的快捷功能带checkbox树型,带有权限检查
	 * @return
	 * @throws Exception
	 */
	public String showCkMyQuickTreeAction() throws Exception{
		try{
			JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
			TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
			jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
				//为节点添加自定义的属性
				public void beanHandler(Object obj, Map map) {
					Resource module=(Resource) obj;
					Object icon = BeanUtils.getProperty2(module, "icon");
					Object link = BeanUtils.getProperty2(module, "link");
					//如果有指定模块的图标，则将图标显示出来
					if(icon!=null){
						map.put("icon", request.getContextPath()+"/"+icon);
					}
					//如果模块未指定链接，就作为中间节点的样式显示
					if(module.getParentRes()==null){
						map.put("cls", "func-node");
					}
					map.put("link",link);
					map.put("expanded",true);
					map.put("ccCheck", new Boolean(true));
					
					//获取当前登录人的 快捷设置
					Person currentPerson = personManager.getCurrentPerson(sessionAttrs);
					MyQuick myQuick = myQuickManager.findUniqueBy("personId",currentPerson.getId());
					if(myQuick != null){
						String moduleIds  = myQuick.getModuleIds();
						if(moduleIds != null){
							String[] array_moduleIds = moduleIds.split(",");
							boolean isChecked = false;
							for (int i = 0; i < array_moduleIds.length; i++) {
								if(map.get("id").toString().equals(array_moduleIds[i])){
									isChecked = true;
									break;
								}
							}
							map.put("checked", new Boolean(isChecked));
						}
					}else {
						map.put("checked", new Boolean(false));
					}
					
					if(module.getLeafModule()!=null && module.getLeafModule().equals(true))
						map.put("leaf",true);
					else
						map.put("leaf",false);
					
				}});
			
			String parentId = request.getParameter("node");
			if(parentId!=null && parentId.equals("rootNode")){
				parentId=null;
			}
			//根节点集合
			Collection roots=moduleManager.findModuleByParentWithPerm(personManager.getCurrentPerson(sessionAttrs),parentId);
			
			//子节点怎么取
			jsonBeanProcessor.setCustomChildNodesHandler(new CustomChildNodesHandler(){
				public Collection getChildNodes(Object obj) {
					return null;
					
				}});
			jsonConfig.registerJsonBeanProcessor(Module.class,jsonBeanProcessor);
			jsonConfig.registerJsonBeanProcessor(Resource.class,jsonBeanProcessor);
			//开始json化
			JSONArray result=JSONArray.fromObject(roots,jsonConfig);
			//输出
			String json = result.toString();
			this.outputJson(json);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;	
	}
	
	@Override
	protected Collection getChildren(Object bean) {
//		return moduleManager.findModule((Module) bean);
		return null;
	}
	protected String getChildPropertyName(Class beanClass){
		return "childRes";
	}
	
	
	@Override
	protected String getParentPropertyName(Class beanClass) {

		return "parentRes";
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	@Override
	protected Collection getRootObjects() {
		String parentId = request.getParameter("node");
		if(parentId!=null && parentId.equals("rootNode"))
			parentId = null;
		return moduleManager.findModule(parentId);
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
		return moduleManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "resName", "id", "childRes", "link", "sn",
				"resStyle", "remark" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "resName", "link", "adminOp", "showText", "icon",
				"remark", "name", "visiabled", "childRes", "sn", "tip", "type",
				"resStyle" };
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public void setModuleManager(ModuleManager moduleManager) {
		this.moduleManager = moduleManager;
	}

	public ResourceManager getResManager() {
		return resManager;
	}

	public void setResManager(ResourceManager resManager) {
		this.resManager = resManager;
	}

	public P2ResManager getP2resManager() {
		return p2resManager;
	}

	public void setP2resManager(P2ResManager manager) {
		p2resManager = manager;
	}

	public R2RManager getR2rManager() {
		return r2rManager;
	}

	public void setR2rManager(R2RManager manager) {
		r2rManager = manager;
	}

	public ResourcesUsersManager getResourcesUsersManager() {
		return resourcesUsersManager;
	}

	public void setResourcesUsersManager(
			ResourcesUsersManager resourcesUsersManager) {
		this.resourcesUsersManager = resourcesUsersManager;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
