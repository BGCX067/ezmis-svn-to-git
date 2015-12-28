package com.jteap.form.dbdef.web;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.LogMethod;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.support.LogMethod.LOGLEVEL;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.form.dbdef.manager.DefColumnInfoManager;
import com.jteap.form.dbdef.manager.DefTableInfoManager;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.form.dbdef.model.DefTableInfo;

/**
 * 数据字典的Action处理类
 * @author tanchang
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class DefTableInfoAction extends AbstractTreeAction<DefTableInfo>{

	private DefTableInfoManager defTableInfoManager;
	private DefColumnInfoManager defColumnInfoManager;
	private PhysicTableManager physicTableManager;

	public void setPhysicTableManager(PhysicTableManager physicTableManager) {
		this.physicTableManager = physicTableManager;
	}


	public DefTableInfoManager getDefTableInfoManager() {
		return defTableInfoManager;
	}


	public DefColumnInfoManager getDefColumnInfoManager() {
		return defColumnInfoManager;
	}


	public void setDefColumnInfoManager(DefColumnInfoManager defColumnInfoManager) {
		this.defColumnInfoManager = defColumnInfoManager;
	}


	public void setDefTableInfoManager(DefTableInfoManager defTableInfoManager) {
		this.defTableInfoManager = defTableInfoManager;
	}
	
	
	/**
	 * 将存在的所有表的Schema作为第一级节点展现
	 */
	public String showTreeSchemaAction() throws Exception{
		
		String dsName= request.getParameter("node");
		List list=defTableInfoManager.findAllSchemas();
		StringBuffer sb=new StringBuffer("[");
		for (Object obj : list) {
			sb.append("{\"id\":\""+obj+"\",\"type\":\"schema\",\"text\":\"(SCHEMA)"+obj+"\",\"datasource\":\""+dsName+"\",\"expanded\":false,\"loader\":tableLoader,\"icon\":\""+request.getContextPath()+"/resources/icon/icon_15.gif\"},");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		this.outputJson(sb.toString());
		return NONE;
	}

	
	@Override
	protected Collection getChildren(Object bean) {
		DefTableInfo defTableInfo = (DefTableInfo)bean;
		return defTableInfo.getChildren();
	}
	
	protected String getChildPropertyName(Class beanClass){
		return "childRes";
	}
	
	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "parentRes";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		String parentId=request.getParameter("parentId");
		return defTableInfoManager.findCatalogByParentId(parentId);
		
//		String schemaName= SystemConfig.getProperty("jdbc.schema");
//		if(schemaName.equals("rootNode"))
//			schemaName=null;
//		Collection<DefTableInfo> list = null;
//		if(StringUtil.isNotEmpty(schemaName)){
//			list=defTableInfoManager.findAllTableBySchema(schemaName);
//		}else{
//			list = defTableInfoManager.getAll();
//		}
//		return list;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "tableName";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "tableName";
	}


	/**
	 * 注入jsonBeanHandler，在序列化之前加入额外的信息，比如图 标
	 */
	@Override
	protected TreeActionJsonBeanHandler injectJsonBeanHandler() {
		return new TreeActionJsonBeanHandler(){

			public void beanHandler(Object obj, Map map) {
				DefTableInfo table=(DefTableInfo) obj;
				
				if(table.getTableCode() != null){
					//表定义
					map.put("icon",request.getContextPath()+"/resources/icon/grid.png");
					map.put("type","table");
					map.put("tableCode", table.getTableCode());
				}else{
					//分类
					map.put("type","catalog");
				}
				
			}
			
		};
	}


	@Override
	public String[] listJsonProperties() {
		return new String[]{"type","tableCode","tableName","repDsName"};
	}


	@Override
	public String[] updateJsonProperties() {
		return new String[]{"type","tableCode","tableName","schema","id","repDsName"};
	}


	@Override
	public HibernateEntityDao getManager() {
		return defTableInfoManager;
	}
	
	
	/**
	 * 删除指定表动作
	 * 先需要删除该表所有字段
	 * 再删除表本身
	 * @return
	 * @throws Exception 
	 */
	public String deleteTableAction() throws Exception{
		String id=request.getParameter("id");
		String type=request.getParameter("type"); //schema ? table ?
		
		try{
			//根据不同的方式删除表 table?   schema ?
			if(type.equals("table")){
				defTableInfoManager.deleteTableByTableId(id);
			}else if(type.equals("schema")){
				defTableInfoManager.deleteTableBySchema(id);
			}
			outputJson("{success:true}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'删除失败'}");
			throw ex;
		}
		return NONE;
	}
	
	/**
	 * 判断指定的表是否已经存在对应的定义表
	 * @return
	 * @throws Exception
	 */
	public String isExistDefTableAction() throws Exception{
		String tableids=request.getParameter("tableids");
		StringBuffer msg=new StringBuffer("");
		if(StringUtil.isNotEmpty(tableids)){
			String tableIdArray[]=tableids.split(",");
			for (String tableId : tableIdArray) {
				if(StringUtil.isNotEmpty(tableId)){
					int idx=tableId.indexOf(".");
					String schema=tableId.substring(0,idx);
					String tableName=tableId.substring(idx+1);
					if(defTableInfoManager.isExistDefTableInfo(schema, tableName)){
						msg.append(schema+"."+tableName+",");
					}
				}
			}
		}
		//去掉最后的逗号
		if(msg.length()>0)
			msg.deleteCharAt(msg.length()-1);
		outputJson("{success:true,msg:'"+msg.toString()+"'}");
		return NONE;
	}
	
	/**
	 * 保存表信息,返回表对象ID.
	 * @author caihuiwen.
	 */
	@LogMethod(name="保存数据",loglevel=LOGLEVEL.EXCEPTION)
	public String saveUpdateReturnIdAction() throws BusinessException {
		try{
			DefTableInfo table = new DefTableInfo();
			String tableCode = request.getParameter("tableCode");
			String tableName = request.getParameter("tableName");
			String schema = SystemConfig.getProperty("jdbc.schema","");
			table.setTableCode(tableCode);
			table.setTableName(tableName);
			table.setSchema(schema);
			getManager().save(table);
			defColumnInfoManager.addPrimaryKeyByTableId(table.getId());
			this.outputJson("{success:true,id:'"+table.getId()+"'}");
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;
	}
	
	/**
	 * 判断表定义对应的物理表 是否存在.
	 * @return
	 */
	public String isExistPhysicTableAction(){
		try {
			String tablecode = request.getParameter("tablecode");
			String schema = SystemConfig.getProperty("jdbc.schema");
			if(physicTableManager.isExist(schema, tablecode)){
				this.outputJson("{success:true,exist:true}");
			}else {
				this.outputJson("{success:true,exist:false}");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 删除物理表.
	 * @return
	 */
	public String deletePhysicTableAction(){
		try {
			String tablecode = request.getParameter("tablecode");
			String schema = SystemConfig.getProperty("jdbc.schema");
			physicTableManager.deletePhysicTable(tablecode, schema);
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/***
	 * 保存或修改.
	 */
	public String saveUpdateAction() throws BusinessException {
		try {
			DefTableInfo defTableInfo = new DefTableInfo();
			String id = request.getParameter("id");
			if(StringUtil.isNotEmpty(id)){
				defTableInfo.setId(id);
			}
			defTableInfo.setTableCode(request.getParameter("tableCode"));
			defTableInfo.setTableName(request.getParameter("tableName"));
			defTableInfo.setSchema(SystemConfig.getProperty("jdbc.schema"));
			defTableInfoManager.save(defTableInfo);
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 拖动更改分类 (拖拽节点时触发的动作)
	 * @return
	 * @throws Exception
	 */
	public String moveNodeAction() throws Exception{
		String nodeId=request.getParameter("nodeId");//操作的节点对象编号
		String newParentId=request.getParameter("newParentId");	//新的父亲编号
		
		//当前操作节点
		DefTableInfo currentTable = defTableInfoManager.get(nodeId);
		//移动至的父节点
		DefTableInfo parent = defTableInfoManager.get(newParentId);
		currentTable.setParent(parent);
		
		defTableInfoManager.save(currentTable);
		
		outputJson("{success:true}");
		return NONE;
	}
	
	/**
	 * 获取所有分类
	 * @return
	 * @throws Exception
	 */
	public String findDefTableCatalogAction() throws Exception{
		String hql = "from DefTableInfo t where t.tableCode is null";
		List<DefTableInfo> list = defTableInfoManager.find(hql);
		String json = JSONUtil.listToJson(list, new String[]{"id","tableName"});
		
		this.outputJson(json);
		return NONE;
	}
	
	/**
	 * 新建分类节点动作
	 * @return
	 * @throws Exception 
	 */
	public String saveOrUpdateCatalogAction() throws Exception{
		String catalogName=request.getParameter("nodeName");
		String parentId=request.getParameter("parentId");
		String id=request.getParameter("id");

		try{
			DefTableInfo catalog=null;
			
			if(StringUtils.isNotEmpty(id)){
				catalog=defTableInfoManager.get(id);
			}else{
				catalog=(DefTableInfo) this.creatBlankObject();
				if(StringUtils.isNotEmpty(parentId)){
					DefTableInfo parentCatalog=defTableInfoManager.get(parentId);
					catalog.setParent(parentCatalog);
				}
			}
			catalog.setTableName(catalogName);
			defTableInfoManager.save(catalog);
			outputJson("{success:true,id:'"+catalog.getId()+"'}");
		}catch(Exception ex){
			outputJson("{success:false,msg:'"+ex.getMessage()+"'}");
		}
		return NONE;
	}
	
	/**
	 * 显示修改信息
	 * @return
	 * @throws Exception 
	 */
	public String showTableUpdateAction() throws Exception{
		String id = request.getParameter("id");
		if(id != null && id != ""){
			DefTableInfo defTableInfo = defTableInfoManager.get(id);
			String catalogId = "";
			if(defTableInfo.getParent() != null){
				catalogId = defTableInfo.getParent().getId();
			}
			
			this.outputJson("{success:true,tableCode:'" + defTableInfo.getTableCode() + "',tableName:'" 
						+ defTableInfo.getTableName() + "',catalogId:'" + catalogId + "'}");
		}
		
		return NONE;
	}
	
	/**
	 * 保存或修改表信息
	 * @return
	 * @throws Exception 
	 */
	public String saveUpdateTableAction() throws Exception{
		String id = request.getParameter("id");
		
		DefTableInfo tableInfo = new DefTableInfo();
		if(StringUtil.isNotEmpty(id)){
			tableInfo = defTableInfoManager.get(id);
		}
		tableInfo.setTableCode(request.getParameter("tableCode"));
		tableInfo.setTableName(request.getParameter("tableName"));
		
		String catalogId = request.getParameter("catalogId");
		if(catalogId != null && catalogId != ""){
			DefTableInfo parent = defTableInfoManager.get(catalogId);	
			tableInfo.setParent(parent);
		}
		defTableInfoManager.save(tableInfo);
		
		this.outputJson("{success:true}");
		return NONE;
	}
	
}
