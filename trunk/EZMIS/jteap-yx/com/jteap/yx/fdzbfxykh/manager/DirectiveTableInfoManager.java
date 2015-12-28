/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.fdzbfxykh.manager;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.StringUtil;
import com.jteap.yx.fdzbfxykh.model.DirectiveColumnInfo;
import com.jteap.yx.fdzbfxykh.model.DirectiveTableInfo;

/**
 * 小指标表定义Manager
 * @author caihuiwen
 */
@SuppressWarnings("unchecked")
public class DirectiveTableInfoManager extends HibernateEntityDao<DirectiveTableInfo>{
	
	private DirectivePhysicManager directivePhysicManager;
	private DirectiveColumnInfoManager directiveColumnInfoManager;
	
	public void setDirectiveColumnInfoManager(
			DirectiveColumnInfoManager directiveColumnInfoManager) {
		this.directiveColumnInfoManager = directiveColumnInfoManager;
	}

	public void setDirectivePhysicManager(
			DirectivePhysicManager directivePhysicManager) {
		this.directivePhysicManager = directivePhysicManager;
	}
	
	/**
	 * 获取所有记录,根据排序号 排升序
	 * @return
	 */
	public List<DirectiveTableInfo> findSortnoAll(){
		String hql = "from DirectiveTableInfo t order by t.sortno asc";
		return this.find(hql);
	}
	
	/**
	 * 根据小指标表定义Id 删除小指标表定义、删除小指标的字段定义
	 * @param Id
	 * @return tableCode
	 */
	public String deleteDirectiveTableInfo(String id){
		String tableCode = "";
		
		String hqlQuery = "from DirectiveTableInfo t where t.id=?";
		List<DirectiveTableInfo> list = this.find(hqlQuery, id);
		if(list.size() > 0){
			DirectiveTableInfo directiveTableInfo = list.get(0);
			tableCode = directiveTableInfo.getTableCode();
			String hqldelete = "delete DirectiveTableInfo t where t.id=?";
			Query query = this.createQuery(hqldelete, directiveTableInfo.getId());
			query.executeUpdate();
			remove(directiveTableInfo);
			
			//删除相关字段信息
			String hqlQColumn = "from DirectiveColumnInfo t where t.directiveId=?";
			List<DirectiveColumnInfo> columnList = this.find(hqlQColumn, id);
			for (int i = 0; i < columnList.size(); i++) {
				DirectiveColumnInfo directiveColumnInfo = columnList.get(i);
				String hqlDColumn = "delete DirectiveColumnInfo t where t.id=?";
				Query queryColumn = this.createQuery(hqlDColumn, directiveColumnInfo.getId());
				queryColumn.executeUpdate();
				remove(directiveColumnInfo);
			}
		}
		return tableCode;
	}
	
	/**
	 * 表对应字段发生 增、删、该操作时,添加表修改记录.
	 * @author caihuiwen.
	 * @param directiveId    表定义Id	
	 * @param columnInfo  	 字段定义.
	 * @param modifyType	 修改类型 (add, alter, drop).
	 * @param beforColumnCode修改前的字段名.
	 * @param ids			 删除的字段ID数组.
	 */
	public void modifyRecByDefColumnInfoChange(String directiveId,DirectiveColumnInfo columnInfo, String modifyType, String beforColumnCode, String ids[]){
		DirectiveTableInfo tableInfo = this.get(directiveId);
		if(null == tableInfo){
			//表定义为空,返回
			return;
		}
		
		try {
			
			String schema = SystemConfig.getProperty("jdbc.schema");
			String tableCode = "TB_YX_DIRECTIVE_" + StringUtil.upperCase(tableInfo.getTableCode());
			
			if(!directivePhysicManager.isExist(schema, tableCode)){
				//不存在物理表,返回
				return;
			}
			
			StringBuffer modifyRec = new StringBuffer("ALTER TABLE " + schema + "." + tableCode + " ");
			if("add".equals(modifyType)){
				
				modifyRec.append("ADD " + columnInfo.getDirectiveCode() + " VARCHAR2(50)");
				
			}else if("alter".equals(modifyType) && null != beforColumnCode ){
				
				if(!beforColumnCode.equals(columnInfo.getDirectiveCode())){
					modifyRec.append("RENAME COLUMN " + beforColumnCode + " to " + columnInfo.getDirectiveCode() + " ;");
				}
				
			}else if("drop".equals(modifyType) && null != ids){
				
				modifyRec.append("DROP COLUMN ");
				for (int i = 0; i < ids.length; i++) {
					DirectiveColumnInfo dropColumnInfo = directiveColumnInfoManager.get(ids[i]);
					if(i == 0){
						modifyRec.append(dropColumnInfo.getDirectiveCode());
					}else {
						modifyRec.append(";ALTER TABLE " + schema + "." +tableCode + " ");
						modifyRec.append("DROP COLUMN ");
						modifyRec.append(dropColumnInfo.getDirectiveCode());
					}
				}
				
			}
			modifyRec.append(";");
			
			if(StringUtil.isNotEmpty(tableInfo.getModifyRec())){
				tableInfo.setModifyRec(tableInfo.getModifyRec() + modifyRec.toString());
			}else {
				tableInfo.setModifyRec(modifyRec.toString());
			}
			
			//将该表定义的同步状态设为 未同步  0
			tableInfo.setFinalManuscript(false);
			this.save(tableInfo);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
