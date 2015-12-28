package com.jteap.form.dbdef.manager;


import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.StringUtil;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.form.dbdef.model.DefTableInfo;

/**
 * 定义表管理对象
 * @author tanchang
 *
 */
@SuppressWarnings("unchecked")
public class DefTableInfoManager extends HibernateEntityDao<DefTableInfo>{
	
	private PhysicTableManager physicTableManager;
	private DefColumnInfoManager defColumnInfoManager;
	

	public void setDefColumnInfoManager(DefColumnInfoManager defColumnInfoManager) {
		this.defColumnInfoManager = defColumnInfoManager;
	}

	public void setPhysicTableManager(PhysicTableManager physicTableManager) {
		this.physicTableManager = physicTableManager;
	}


	/**
	 * 查询存在的所有schema
	 * @return
	 */
	public List<String> findAllSchemas(){
		String hql="select distinct a.schema from DefTableInfo as a";
		List<String> list=this.find(hql);
		
		return list;
	}
	
	
	/**
	 * 根据schema名字取得所有该schema的表的定义对象
	 * @param schemaName
	 * @param dataSourceName
	 * @return
	 */

	public List<DefTableInfo> findAllTableBySchema(String schemaName){
		String hql="from DefTableInfo as a where a.schema=? order by a.tableName";
		List<DefTableInfo> list=this.find(hql,new Object[]{schemaName});
		return list;
	}
	
	
	/**
	 * 根据schema和tableName删除定义表
	 * 删除指定的表之前需要删除对应的字段
	 * @param schema
	 * @param tableName
	 * @param dsName 数据源名称
	 */

	public void deleteTableByTableName(String schema,String tableName){
		String hql="from DefTableInfo as a where a.tableCode=? and a.schema=?";
		List<DefTableInfo> list=this.find(hql,new Object[]{tableName,schema});
		DefTableInfo tableInfo=null;
		if (list.size()>0) {
			tableInfo=list.iterator().next();
			String delHql="delete DefColumnInfo as a where a.table.id=?";
			Query query=this.createQuery(delHql,new Object[]{tableInfo.getId()});
			query.executeUpdate();
			remove(tableInfo);
		}
	}
	
	/**
	 * 是否存在指定表名称的定义表
	 * @param schema
	 * @param tableName
	 * @param dsName
	 * @return
	 */

	public boolean isExistDefTableInfo(String schema,String tableName){
		String hql="from DefTableInfo as a where a.tableCode=? and a.schema=?";
		List<DefTableInfo> list=this.find(hql,new Object[]{tableName,schema});
		return list.size()>0;
	}
	

	/**
	 * 根据schema删除表定义
	 * 1.删除所有该schema的表字段
	 * 2.删除schema的表定义
	 * @param schemaName
	 * @return
	 */
	public int deleteTableBySchema(String schemaName){
		String hql="delete DefColumnInfo as a where a.table.id in (select b.id from DefTableInfo as b where b.schema='"+schemaName+"')";
		Query query=this.createQuery(hql);
		query.executeUpdate();
		hql="delete DefTableInfo as a where a.schema='"+schemaName+"'";
		query=this.createQuery(hql);
		return query.executeUpdate();
	}
	
	/**
	 * 根据tableid删除表定义
	 * 1.删除所有该table的表字段
	 * 2.删除schema的表定义
	 * @param schemaName
	 * @return
	 */
	public int deleteTableByTableId(String id){
		String hql="delete DefColumnInfo as a where a.table.id='"+id+"'";
		Query query=this.createQuery(hql);
		query.executeUpdate();
		hql="delete DefTableInfo as a where a.id='"+id+"'";
		query=this.createQuery(hql);
		return query.executeUpdate();
	}
	
	/**
	 * 表对应字段发生 增、删、该操作时,添加表修改记录.
	 * @author caihuiwen.
	 * @param defColumnInfo  字段定义.
	 * @param modifyType	 修改类型 (add, alter, drop).
	 * @param beforColumnCode修改前的字段名.
	 * @param ids			 删除的字段ID数组.
	 * @param unique		 是否 为一键 (t/f 是/否)
	 */
	public void modifyRecByDefColumnInfoChange(DefColumnInfo defColumnInfo, String modifyType, String beforColumnCode, String ids[],boolean unique){
		
		DefTableInfo defTableInfo = defColumnInfo.getTable();
		if(null == defTableInfo){
			//表定义为空,返回
			return;
		}
		
		try {
			
			String schema = SystemConfig.getProperty("jdbc.schema");
			if(!physicTableManager.isExist(schema, defTableInfo.getTableCode())){
				//不存在物理表,返回
				return;
			}
			
			StringBuffer modifyRec = new StringBuffer("ALTER TABLE " + schema + "." + defTableInfo.getTableCode() + " ");
			if("add".equals(modifyType)){
				
				modifyRec.append("ADD ");
				modifyRec.append(defColumnInfo.getColumncode() + " ");
				modifyRec.append(defColumnInfo.getColumntype());
				
				String type = defColumnInfo.getColumntype().toUpperCase();
				if("VARCHAR2".equals(type) || "RAW".equals(type) || "CHAR".equals(type)){
					modifyRec.append("(" + defColumnInfo.getColumnlength() + ")");
				}else if("NUMBER".equals(type) && defColumnInfo.getColumnprec() > 0){
					modifyRec.append("(" + defColumnInfo.getColumnlength() + "," + defColumnInfo.getColumnprec() + ")");
				}
				modifyRec.append(" ");
				
				if(defColumnInfo.isPk()){
					if(physicTableManager.findUniquePrimaryKeyName(schema, defTableInfo.getTableCode()) == null){
						modifyRec.append("PRIMARY KEY ");
					}else {
						//如果该表定义已存在主键  则不允许添加其他主键(因为物理表只允许存在一个主键)
						defColumnInfo.setPk(false);
					}
				}
				
				if(StringUtil.isNotEmpty(defColumnInfo.getDefaultvalue())){
					modifyRec.append("DEFAULT '" + defColumnInfo.getDefaultvalue() + "' ");
				}
				
				if(defColumnInfo.getAllownull()){
					if(physicTableManager.isHaveData(defTableInfo.getTableCode())){
						if(StringUtil.isEmpty(defColumnInfo.getDefaultvalue())){
							modifyRec.append("DEFAULT ' ' ");
						}
					}
					modifyRec.append("NOT NULL ");
				}
				
				//唯一键
				/*if(unique && !defColumnInfo.isPk()){
					modifyRec.append(";ALTER TABLE " + schema + "." + defTableInfo.getTableCode() + " ");
					modifyRec.append("ADD CONSTRAINT CONS_" + defColumnInfo.getColumncode() + " UNIQUE(" + defColumnInfo.getColumncode() + ") ");
				}*/
				
			}else if("alter".equals(modifyType) && null != beforColumnCode ){
				
				if(!beforColumnCode.equals(defColumnInfo.getColumncode())){
					modifyRec.append("RENAME COLUMN " + beforColumnCode + " to " + defColumnInfo.getColumncode() + " ;");
				}
				modifyRec.append("ALTER TABLE " + schema + "." + defTableInfo.getTableCode() + " ");
				modifyRec.append("MODIFY ");
				modifyRec.append(defColumnInfo.getColumncode() + " ");
				modifyRec.append(defColumnInfo.getColumntype());
				
				String type = defColumnInfo.getColumntype().toUpperCase();
				if("VARCHAR2".equals(type) || "RAW".equals(type) || "CHAR".equals(type)){
					modifyRec.append("(" + defColumnInfo.getColumnlength() + ")");
				}else if("NUMBER".equals(type) && defColumnInfo.getColumnprec() > 0){
					modifyRec.append("(" + defColumnInfo.getColumnlength() + "," + defColumnInfo.getColumnprec() + ")");
				}
				modifyRec.append(" ");
				
				if(defColumnInfo.isPk()){
					if(physicTableManager.findUniquePrimaryKeyName(schema, defTableInfo.getTableCode()) == null){
						modifyRec.append("PRIMARY KEY ");
					}else {
						//如果该表定义已存在主键  则不允许添加其他主键(因为物理表只允许存在一个主键)
						defColumnInfo.setPk(false);
					}
				}
				
				if(StringUtil.isNotEmpty(defColumnInfo.getDefaultvalue())){
					modifyRec.append("DEFAULT '" + defColumnInfo.getDefaultvalue() + "' ");
				}
				
				if(defColumnInfo.getAllownull()){
					if(physicTableManager.isHaveData(defTableInfo.getTableCode())){
						if(StringUtil.isEmpty(defColumnInfo.getDefaultvalue())){
							modifyRec.append("DEFAULT ' ' ");
						}
					}
					modifyRec.append("NOT NULL ");
				}
				
				//唯一键
				/*if(unique && !defColumnInfo.isPk()){
					modifyRec.append(";ALTER TABLE " + schema + "." + defTableInfo.getTableCode() + " ");
					modifyRec.append("ADD CONSTRAINT CONS_" + defColumnInfo.getColumncode() + " UNIQUE(" + defColumnInfo.getColumncode() + ") ");
				}else{
					modifyRec.append(";ALTER TABLE " + schema + "." + defTableInfo.getTableCode() + " ");
					modifyRec.append("DROP CONSTRAINT CONS_" + beforColumnCode + " ");
				}*/
				
			}else if("drop".equals(modifyType) && null != ids){
				
				modifyRec.append("DROP COLUMN ");
				for (int i = 0; i < ids.length; i++) {
					DefColumnInfo defColumnInfo2 = defColumnInfoManager.get(ids[i]);
					if(i == 0){
						modifyRec.append(defColumnInfo2.getColumncode());
					}else {
						modifyRec.append(";ALTER TABLE " + schema + "." + defTableInfo.getTableCode() + " ");
						modifyRec.append("DROP COLUMN ");
						modifyRec.append(defColumnInfo2.getColumncode());
					}
				}
				
			}
			modifyRec.append(";");
			
			if(StringUtil.isNotEmpty(defTableInfo.getModifyRec())){
				defTableInfo.setModifyRec(defTableInfo.getModifyRec() + modifyRec.toString());
			}else {
				defTableInfo.setModifyRec(modifyRec.toString());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据父亲节点编号查询该节点下的所有子节点
	 * @param parentId  
	 * @return  如果parentId为空 返回所有顶层节点
	 * 否则返回所有指定parentId下的所有子节点
	 */
	public Collection<DefTableInfo> findCatalogByParentId(String parentId){
		
		StringBuffer hql=new StringBuffer("from DefTableInfo as g where ");
		Object args[]=null;
		if(StringUtils.isEmpty(parentId)){
			hql.append("g.parent is null");
			
		}else{
			hql.append("g.parent.id=?");
			args=new String[]{parentId};
		}
		return find(hql.toString(),args);
	}
	
}
