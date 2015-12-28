package com.jteap.form.dbdef.manager;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.form.dbdef.model.DefTableInfo;

public class DefColumnInfoManager extends HibernateEntityDao<DefColumnInfo>{
	
	/**
	 * 根据表编号删除字段
	 * @param tableid
	 */
	public void deleteByTableId(String tableid){
		String hql="delete DefColumnInfo as a where a.table.id='"+tableid+"'";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}
	
	
	/**
	 * 根据表名查询该表的所有列定义对象
	 * @param schema
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DefColumnInfo> findColumnInfoByTableName(String schema,String tableName){
		String hql="select a from DefColumnInfo as a ,DefTableInfo as b where a.table.id=b.id and b.schema='"+schema+"' and b.tableCode='"+tableName+"'  order by a.columnorder";
		List<DefColumnInfo> list = this.find(hql);
		return list;
	}
	
	/**
	 * 根据表名查询该表的所有列定义对象
	 * @param schema
	 * @param tableName
	 */
	@SuppressWarnings("unchecked")
	public List<DefColumnInfo> findColumnInfoByTableId(String tableid) throws Exception{
		try {
			String hql="from DefColumnInfo as a where a.table.id='"+tableid+"' order by a.columnname";
			List<DefColumnInfo> list = this.find(hql);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据表名查询该表的所有列定义对象,除了大字段之外
	 * @param schema
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DefColumnInfo> findColumnInfoByTableIdButLOB(String tableid){
		String hql="from DefColumnInfo as a where a.table.id='"+tableid+"' and a.columntype !='BLOB' and a.columntype!='CLOB' order by a.columnname";
		List<DefColumnInfo> list = this.find(hql);
		return list;
	}
	
	/**
	 * 根据表ID添加主键.
	 * @author caihuiwen.
	 */
	public void addPrimaryKeyByTableId(String tableId){
		DefTableInfo table = this.get(DefTableInfo.class,tableId);
		DefColumnInfo defColumnInfo = new DefColumnInfo();
		defColumnInfo.setTable(table);
		defColumnInfo.setColumncode("ID");
		defColumnInfo.setColumnname("编号");
		defColumnInfo.setColumntype("VARCHAR2");
		defColumnInfo.setColumnlength(32);
		defColumnInfo.setAllownull(false);
		defColumnInfo.setComm("自动编号");
		defColumnInfo.setColumnorder(0);
		defColumnInfo.setColumnprec(0);
		defColumnInfo.setPk(true);
		this.save(defColumnInfo);
	}
	
	
	/**
	 * 判断字段是否已经存在.
	 * @param columnId 字段名.
	 * @return t/f - 存在/不存在.
	 */
	@SuppressWarnings("unchecked")
	public boolean isExistColumn(String tableId, String columnCode){
		String hql = "from DefColumnInfo as a where a.table.id=? and a.columncode=?";
		List<DefColumnInfo> list = this.find(hql, new Object[]{tableId, columnCode});
		if(null != list && list.size() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据列名，找到指定表中的字段名
	 * @param tableId
	 * @param columnCode
	 * @return
	 */
	public DefColumnInfo findColumnByCode(String tableId,String columnCode){
		String hql = "from DefColumnInfo as a where a.table.id=? and a.columncode=?";
		DefColumnInfo columnInfo = (DefColumnInfo) this.findUniqueByHql(hql, tableId,columnCode);
		return columnInfo;
	}
}
