package com.jteap.form.dbdef.web;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.ArrayUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.form.dbdef.manager.DefColumnInfoManager;
import com.jteap.form.dbdef.manager.DefTableInfoManager;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.form.dbdef.model.DefColumnType;
import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.system.jdbc.model.TableInfo;

/**
 * 数据库物理表处理动作对象
 * @author tanchang
 *
 */
@SuppressWarnings({ "unchecked", "serial" })
public class PhysicTableAction extends AbstractTreeAction<TableInfo>{
	private PhysicTableManager physicTableManager;
	private DefTableInfoManager defTableInfoManager;
	private DefColumnInfoManager defColumnInfoManager;
	private EFormManager eformManager;

	public void setEformManager(EFormManager eformManager) {
		this.eformManager = eformManager;
	}

	public void setDefTableInfoManager(DefTableInfoManager defTableInfoManager) {
		this.defTableInfoManager = defTableInfoManager;
	}

	public void setDefColumnInfoManager(DefColumnInfoManager defColumnInfoManager) {
		this.defColumnInfoManager = defColumnInfoManager;
	}

	public void setPhysicTableManager(PhysicTableManager physicTableManager) {
		this.physicTableManager = physicTableManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		String key=request.getParameter("key");
		List list=physicTableManager.findTableList(key);
		return list;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "fullName";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "tableName";
	}

	@Override
	public HibernateEntityDao getManager() {
	
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * 重建物理表结构
	 * @return
	 * @throws Exception
	 */
	public String rebuildPhysicTableAction() throws Exception{
		String id=request.getParameter("tableid");
		DefTableInfo tableInfo=defTableInfoManager.get(id);
		if(StringUtil.isNotEmpty(id)){
			String schema=tableInfo.getSchema();
			//schema="ezmis";
			String tableName=tableInfo.getTableCode();
			List list=defColumnInfoManager.findColumnInfoByTableName(schema, tableName);
			try {
				physicTableManager.rebuildTable(schema, tableName, list);
				
				//清空表定义中的修改记录
				tableInfo.setModifyRec("");
				defTableInfoManager.save(tableInfo);
				
				//将关联到此数据定义的 表单定稿设为[未定稿]
				EForm eForm = eformManager.findUniqueBy(EForm.class,"defTable", tableInfo);
				if(null != eForm){
					eForm.setFinalManuscript(true);
					eformManager.save(eForm);
				}
				
				outputJson("{success:true}");
			} catch (SQLException e) {
				outputJson("{success:false,msg:'"+e.getMessage().trim()+"'}");
				e.printStackTrace();
			}
		}
		
		return NONE;
	}
	
	/**
	 * 导入物理表
	 * 1.根据指定的表名称进行导入  tableids:SCHEMA.TABLENAME,SCHEMA.TABLENAME,
	 * 2.该表如果已经存在定义表，先删除
	 * 3.先创建定义表，再创建字段，需要设置字段与表的关联以及是否主键
	 * @return
	 * @throws Exception 
	 */
	public String importPhysicTableAction() throws Exception{
		String tableids=request.getParameter("tableids");
		int count=0;
		StringBuffer msg=new StringBuffer("");
		if(StringUtil.isNotEmpty(tableids)){
			String tableIdArray[]=tableids.split(",");
			for (String tableId : tableIdArray) {
				if(StringUtil.isNotEmpty(tableId)){
					int idx=tableId.indexOf(".");
					String schema=tableId.substring(0,idx);
					String tableName=tableId.substring(idx+1);
					System.out.println("开始导入表【"+schema+"."+tableName+"】");
					//在数据库中是否存在相应的表
					if(physicTableManager.isExist(schema, tableName)){
						//新建之前需要删除已经存在的表定义对象
						defTableInfoManager.deleteTableByTableName(schema, tableName);
						
						//新建表定义对象
						DefTableInfo tableInfo=new DefTableInfo();
						tableInfo.setSchema(schema);
						tableInfo.setTableCode(tableName);
						tableInfo.setTableName(tableName);
						defTableInfoManager.save(tableInfo);
						
						//查询字段列表，并保存字段定义对象
						List<DefColumnInfo> list=physicTableManager.findDefColumnInfoListByTable(schema, tableName);
						Object pks[]= physicTableManager.findPrimaryKeyColumnNameList(schema, tableName).toArray();
						//循环创建字段定义对象
						for (DefColumnInfo defColumnInfo : list) {
							//设置关联
							defColumnInfo.setTable(tableInfo);
							tableInfo.getColumns().add(defColumnInfo);
							//是否主键
							if(ArrayUtils.isExist(pks, defColumnInfo.getColumncode())>=0){
								defColumnInfo.setPk(true);
							}
							
							defColumnInfoManager.save(defColumnInfo);
						}
						count++;
					}else{
						msg.append("物理表【"+schema+"."+tableName+"】不存在，或者没有权限");
					}
				}
			}
			
		}
		msg.append("成功导入"+count+"个表");
		outputJson("{success:true,msg:'"+msg.toString()+"'}");
		return 	NONE;
	}
	
	/**
	 * 根据传入的Sql语句，组织该SQL语句的结果集，并组织称GRID的数据格式返回
	 * @return
	 * @throws Exception 
	 */
	public String showTableDataBySqlAction() throws Exception{
		String sql = request.getParameter("sql");
		String json=null;
		try{
			List list= physicTableManager.querySqlData(sql);
			System.out.println(list);
			json=JSONUtil.listToJson(list);
			json="{success:true,totalCount:'" + list.size() + "',list:"+ json + "}";
			System.out.println(json);
			outputJson(json);
		}catch(Exception ex){
			json="{success:false,msg:'"+ex.getMessage()+"'}";
			outputJson(json);
			throw ex;
		}
		return NONE;
	}
	
	
	
	/**
	 * 显示指定表数据
	 * @return
	 * @throws Exception 
	 */
	public String showTableDataAction() throws Exception{
		
		
		String tableid = request.getParameter("tableid");
		
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT","25");
		
		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		
		int iStart=Integer.parseInt(start);
		int iLimit=Integer.parseInt(limit);
		try{
		
			DefTableInfo tableInfo=defTableInfoManager.get(tableid);
			Collection<DefColumnInfo> cols=tableInfo.getColumns();
			StringBuffer selectColSb=new StringBuffer();
			for (DefColumnInfo defColumnInfo : cols) {
				if(defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_BLOB) ||defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_CLOB)){
					continue;
				}
				if(defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_DATE)){
					selectColSb.append("to_char("+defColumnInfo.getColumncode()+",'YYYY-MM-DD') as "+defColumnInfo.getColumncode()+",");
				}else if(defColumnInfo.getColumntype().indexOf(DefColumnType.CTYPE_TIMESTAMP)>=0){
					selectColSb.append("to_char("+defColumnInfo.getColumncode()+",'YYYY-MM-DD hh24:mi:ss') as "+defColumnInfo.getColumncode()+",");
				}else{
					selectColSb.append(defColumnInfo.getColumncode()+",");
				}
			}
			if(selectColSb.length()>0)
				selectColSb.deleteCharAt(selectColSb.length()-1);
			
			
			String orderBy = request.getParameter("sort");
			String tmpOrder="";
			if (StringUtils.isNotEmpty(orderBy)) {
				String dir = request.getParameter("dir");
				if (StringUtils.isEmpty(dir))
					dir = "asc";
				tmpOrder="ORDER BY "+orderBy+" "+dir+"";
			}
			
			String sql="SELECT "+selectColSb.toString()+" FROM "+tableInfo.getSchema()+"."+tableInfo.getTableCode()+" "+ tmpOrder;
			Page page =physicTableManager.pagedQueryTableData(sql, iStart, iLimit);
			
			
			String json=JSONUtil.listToJson((List)page.getResult());
			
			json="{totalCount:'" + page.getTotalCount() + "',list:"+ json + "}";
			this.outputJson(json);
		
		}catch(Exception ex){
			this.outputJson("{success:false}");
			ex.printStackTrace();
//			throw ex;
		}
		return NONE;
	}
}
