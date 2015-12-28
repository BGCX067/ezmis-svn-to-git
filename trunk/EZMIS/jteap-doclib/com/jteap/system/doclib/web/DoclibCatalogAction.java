package com.jteap.system.doclib.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.doclib.manager.DoclibCatalogFieldManager;
import com.jteap.system.doclib.manager.DoclibCatalogManager;
import com.jteap.system.doclib.manager.DoclibLevelManager;
import com.jteap.system.doclib.manager.DoclibManager;
import com.jteap.system.doclib.model.Doclib;
import com.jteap.system.doclib.model.DoclibCatalog;
import com.jteap.system.doclib.model.DoclibCatalogField;
import com.jteap.system.doclib.model.DoclibLevel;
import com.jteap.system.doclib.model.DoclibLevelRole;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.resource.manager.ResourceManager;
import com.jteap.system.role.model.Role;

/**
 * 文档中心分类操作对象
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "unchecked", "serial", "unused" })
public class DoclibCatalogAction extends AbstractTreeAction<DoclibCatalog> {

	private DoclibCatalogManager doclibCatalogManager;
	private DoclibCatalogFieldManager doclibCatalogFieldManager;
	private ResourceManager resourceManager;

	private DoclibManager doclibManager;
	private DoclibLevelManager doclibLevelManager ;
	
	private PersonManager personManager;
	
	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public DoclibManager getDoclibManager() {
		return doclibManager;
	}

	public void setDoclibManager(DoclibManager doclibManager) {
		this.doclibManager = doclibManager;
	}

	public DoclibCatalogManager getDoclibCatalogManager() {
		return doclibCatalogManager;
	}

	public void setDoclibCatalogManager(
			DoclibCatalogManager doclibCatalogManager) {
		this.doclibCatalogManager = doclibCatalogManager;
	}

	
	
	public DoclibLevelManager getDoclibLevelManager() {
		return doclibLevelManager;
	}

	public void setDoclibLevelManager(DoclibLevelManager doclibLevelManager) {
		this.doclibLevelManager = doclibLevelManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		DoclibCatalog catalog = (DoclibCatalog) bean;
		Set<DoclibCatalog> catalogSet=catalog.getChildren();
		Collection<DoclibCatalog> result=new ArrayList<DoclibCatalog>();
		if(!isCurrentRootUser()){
			this.filterCatalog(catalogSet, result);
		}else{
			result=catalogSet;
		}
		return result;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
	 
		return null;
	}
	
	@Override
	protected Collection getRootObjects() throws Exception {
		String catalogCode = request.getParameter("catalogCode");
		if (StringUtil.isNotEmpty(catalogCode)) {
			Collection<DoclibCatalog> catalogList = doclibCatalogManager.findCatalogByCode(catalogCode);
			Collection<DoclibCatalog> result=new ArrayList<DoclibCatalog>();
			if(!isCurrentRootUser()){
				this.filterCatalog(catalogList, result);
			}else{
				result=catalogList;
			}
			return result;
		} else {
			String parentId = request.getParameter("parentId");
			return doclibCatalogManager.findCatalogByParentId(parentId);
		}
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortno";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "title";
	}

	@Override
	public HibernateEntityDao getManager() {
		return doclibCatalogManager;
	}

	/**
	 * @Override public String[] listJsonProperties() { return new String[] {
	 *           "catalogTitle", "id" }; }
	 */

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "name", "type", "emunValue", "format","len",
				"sortno" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "title","catalogCode","catalogPerm","templateFile","sortno" };
	}

	@Override
	protected String beforeDeleteNode(Object node) throws Exception {
	 
		return null;
	}


	/**
	 * 新建分类节点动作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveOrUpdateCatalogAction() throws Exception {
		String catalogTitle = request.getParameter("nodeName");
		//System.out.println("----->catalogTitle<----" + catalogTitle);
		String parentId = request.getParameter("parentId");
		//System.out.println("----->parentId<----" + parentId);
		String id = request.getParameter("id");
		//System.out.println("----->id<----" + id);
		String catalogPerm = request.getParameter("levelId");
		String catalogCode = request.getParameter("catalogCode");
		String sortNo = request.getParameter("sortNo");

		try {
			DoclibCatalog catalog = null;
			DoclibLevel catalogLevel = null ;
			
			if(StringUtils.isNotEmpty(catalogPerm)){
				catalogLevel = doclibLevelManager.get(catalogPerm);
			}
			
			if (StringUtils.isNotEmpty(id)) {
				catalog = doclibCatalogManager.get(id);
			} else {
				catalog = (DoclibCatalog) this.creatBlankObject();
				if (StringUtils.isNotEmpty(parentId)) {
					DoclibCatalog parentCatalog = doclibCatalogManager.get(parentId);
					catalog.setParent(parentCatalog);
				}
			}
			Long sortno = (catalog.getSortno() == null) ? 0 : catalog.getSortno();
			//System.out.println("========>sortno<========" + sortno);
			catalog.setSortno(sortno);
			catalog.setTitle(catalogTitle);
			
			catalog.setCatalogPerm(catalogLevel);
			catalog.setCatalogCode(catalogCode);
			catalog.setSortno(Long.parseLong(sortNo));
			doclibCatalogManager.save(catalog);
			outputJson("{success:true,id:'" + catalog.getId() + "'}");
		} catch (Exception ex) {
			outputJson("{success:false,msg:'" + ex.getMessage() + "'}");
		}
		return NONE;
	}

	/**
	 * 显示更新的动作,取出需要更新的对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showUpdateAction() throws Exception {
		String id = request.getParameter("id");
		Object obj = getManager().get(id);
		String json = JSONUtil.objectToJson(obj, updateJsonProperties());
		boolean flag = findDoclibByDoclibCatalog((DoclibCatalog)obj);			//文档分类下面有文档
		boolean canModify = flag?false:true ;				//文档分类下面有文档时不能修改扩展字段名
		outputJson("{success:true,canModify:"+canModify+",data:[" + json + "]}");
		return NONE;
	}

	/**
	 * 显示需要更新的分类扩展字段集合
	 */
	public String showUpdataCatalogFiledAction() throws Exception {
		String id = request.getParameter("id");
		if(StringUtils.isNotEmpty(id)){
			DoclibCatalog catalog = (DoclibCatalog) getManager().get(id);
			Collection list = catalog.getFields();
			String json = JSONUtil.listToJson(list, listJsonProperties());
			//System.out.println("=========>json<===========1" + json);
			StringBuffer dataBlock = new StringBuffer();
			dataBlock.append("{success:true,totalCount:" + catalog.getFields().size()
					+ ",list:" + json + "}");
			outputJson(dataBlock.toString());
		}else{
			outputJson("{success:true,totalCount:0,list:[]}");
		}
		return NONE;
	}

	/**
	 * @author pengyonggui 2010-05-27
	 * 检查分类代码的唯一性，用来唯一标识一个分类
	 * @return
	 * @throws Exception
	 */
	public String checkCatalogCodeAction() throws Exception {
		boolean unique = false ;
		String catalogCode = request.getParameter("catalogCode");
		Collection<DoclibCatalog> data = doclibCatalogManager.findCatalogByCode(catalogCode);
		if(data!=null && data.size()>0){
			unique = false ;
		}else{
			unique = true ;
		}
		String json = "{flag:"+unique + "}" ;
		outputJson(json);
		return NONE;
	}
	/**
	 * @author pengyonggui 2010-04-01
	 * 查询分类和子类别下有没有文档
	 * @return
	 */
	private boolean findDoclibByDoclibCatalog(DoclibCatalog catalog){
		boolean flag = false ;
		Long count = doclibManager.countDoclibByCatalogId(catalog.getId());
		if(count!=null && count.longValue()>0){
			flag = true ;
		}else{
			Set<DoclibCatalog> catalogs = catalog.getChildren();
			for(DoclibCatalog item : catalogs){
				flag = findDoclibByDoclibCatalog(item);
				if(flag){
					break ;
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * 创建扩展字段
	 */
	public String saveUpdateAction() throws BusinessException {
		try {
			String catalogTitle = request.getParameter("title");
			String ops = request.getParameter("ops"); // 操作列表
			String id = request.getParameter("id"); // 编号用于判断是否修改
			String parentId = request.getParameter("parentId"); // 父亲模块
			String catalogPerm = request.getParameter("levelID");			//文档级别
			String catalogCode = request.getParameter("catalogCode");		//分类代码
			String template = request.getParameter("template");				//显示模板
			String sotr = request.getParameter("sortNo");
			DoclibLevel catalogLevel = null ;
			DoclibCatalog doclibCatalog = null;
			
			if(StringUtils.isNotEmpty(catalogPerm)){
				catalogLevel = doclibLevelManager.get(catalogPerm);
			}
			
			if (StringUtils.isNotEmpty(id)) {
				// 修改
				doclibCatalog = doclibCatalogManager.get(id);
				doclibCatalog.setTitle(catalogTitle);
				doclibCatalog.setSortno(Long.parseLong(sotr));
				doclibCatalog.setTemplateFile(template);
				doclibCatalog.setCatalogPerm(catalogLevel);
				//需要更新文档的
				if(StringUtils.isNotEmpty(catalogPerm)&&!doclibCatalog.getCatalogPerm().getId().equals(catalogPerm)){
					List<Doclib> doclibList=this.doclibManager.findDoclibListByCatalogId(id);
					for(Doclib doclib:doclibList){
						doclib.setDoclibLevel(catalogLevel);
						this.doclibManager.save(doclib);
					}
				}
				//System.out.println("ops:"+ops);
				List<Map<String, String>> opList = JSONUtil.parseList(ops);
				int sortNo = 0;
				for (Map opMap : opList) {
					sortNo++;
					String fieldId = (String) opMap.get("id");
					DoclibCatalogField field = null;
					if (StringUtils.isNotEmpty(fieldId)) {
						field = doclibCatalogFieldManager.get(fieldId);
						setFieldProperties(field, opMap);
					} else {
						field = new DoclibCatalogField();
						field.setSortno(sortNo);
						setFieldProperties(field, opMap);
						doclibCatalogFieldManager.save(field);
						doclibCatalogFieldManager.flush();
						//doclibCatalogFieldManager.clear();
						doclibCatalog.getFields().add(field);
						field.setDoclibCatalog(doclibCatalog);
					}
					
				}
			} else {
				// 新增
				doclibCatalog = new DoclibCatalog();
				// 创建的时候指定父亲资源，修改的时候不指定资源
				if (StringUtils.isNotEmpty(parentId)) {
					DoclibCatalog parent = doclibCatalogManager.get(parentId);
					doclibCatalog.setParent(parent);
				}
				doclibCatalog.setTitle(catalogTitle);
				doclibCatalog.setCatalogPerm(catalogLevel);
				doclibCatalog.setCatalogCode(catalogCode);
				doclibCatalog.setTemplateFile(template);
				doclibCatalog.setSortno(Long.parseLong(sotr));
				doclibCatalogManager.save(doclibCatalog);
				doclibCatalogManager.flush();
				// 新增操作
				if (StringUtils.isNotEmpty(ops)) {
					//System.out.println(ops);
					List<Map<String, String>> fieldList = JSONUtil.parseList(ops);
					int sortNo = 0;
					for (Map fieldMap : fieldList) {
						sortNo++;
						DoclibCatalogField field = new DoclibCatalogField();
						setFieldProperties(field, fieldMap);
						field.setSortno(sortNo);
						doclibCatalogFieldManager.save(field);
						doclibCatalogFieldManager.flush();
						field.setDoclibCatalog(doclibCatalog);
						//doclibCatalog.getFields().add(field);
						doclibCatalogFieldManager.save(field);
					}
				}
			}

			
			doclibCatalogManager.save(doclibCatalog);
			this.outputJson("{success:true}");
		} catch (Exception ex) {
			ex.printStackTrace();
			//throw new BusinessException(ex);
		}
		return NONE;
	}

	/**
	 * 删除扩展字段
	 * 
	 * @param field
	 * @param opMap
	 */
	private String deleteUpdateAction() throws Exception {

		return NONE;
	}

	private void setFieldProperties(DoclibCatalogField field, Map opMap) {
		// field.setId(opMap.get("id").toString());
		field.setName(opMap.get("name").toString());
		field.setFormat(opMap.get("format").toString());
		field.setEmunValue(opMap.get("emunValue").toString());
		field.setSortno(Integer.valueOf(opMap.get("sortno").toString()));
		field.setType(opMap.get("type").toString());
		field.setLen(Integer.valueOf(opMap.get("len").toString()));
	}
	
	
	@Override
	public String showTreeAction() throws Exception {
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				DoclibCatalog kind=(DoclibCatalog) obj;
				map.put("leaf", kind.getChildren().size()>0?false:true);
				map.put("expanded", true);
				map.put("sortNo", kind.getSortno());
			}
		});
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;
	}
	
	/**
	 * 
	 *描述：过滤分类
	 *时间：2010-6-24
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	private void filterCatalog(Collection<DoclibCatalog> catalogList,Collection<DoclibCatalog> result){
		Person person = personManager.getCurrentPerson(sessionAttrs);
		//获得用户的角色
		Set<P2Role> p2rSet=person.getRoles();
		//分类集合
		for(DoclibCatalog catalog:catalogList){
			//获得当前文档的级别
			DoclibLevel level=catalog.getCatalogPerm();
			//获得级别对应的角色
			Set<DoclibLevelRole> levelRoles=level.getLevelRoles();
			//循环中间对象
			for(P2Role p2r:p2rSet){
				//获取角色
				Role role=p2r.getRole();
				//角色id
				String roleid=role.getId().toString();
				for(DoclibLevelRole levelRole:levelRoles){
					//取出文档级别对应的角色Id
					String levelRoleId=levelRole.getRole().getId().toString();
					if(levelRoleId.equals(roleid)){
						boolean isAdd=true;
						for(DoclibCatalog dc:result){
							if(dc.getId().equals(catalog.getId())){
								isAdd=false;
							}
						}
						if(isAdd){
							result.add(catalog);
						}
					}
				}
			}
		}
	}

	public DoclibCatalogFieldManager getDoclibCatalogFieldManager() {
		return doclibCatalogFieldManager;
	}

	public void setDoclibCatalogFieldManager(
			DoclibCatalogFieldManager doclibCatalogFieldManager) {
		this.doclibCatalogFieldManager = doclibCatalogFieldManager;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}


};
