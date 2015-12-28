package com.jteap.form.dbdef.manager;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.form.cform.util.CFormExp;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.system.jdbc.manager.JdbcManager;

/**
 * 物理表管理器 
 * @author tanchang
 *
 */
@SuppressWarnings("unused")
public class PhysicTableManager extends JdbcManager{
	private Log log = LogFactory.getLog(PhysicTableManager.class);
	
	/**
	 * 根据表名，取得所有存在的字段并构造成列定义对象返回
	 * @param schema
	 * @param tableName
	 * @return
	 * @throws SQLException 
	 */
	public List<DefColumnInfo> findDefColumnInfoListByTable(String schema,String tableName) throws Exception{
		Connection conn=dataSource.getConnection();
		List<DefColumnInfo> list=new ArrayList<DefColumnInfo>();
		try{
			DatabaseMetaData dbmeta=conn.getMetaData();
			ResultSet rs=dbmeta.getColumns(null, schema, tableName, null);
			int i=0;
			while(rs.next()){
				DefColumnInfo columnInfo=new DefColumnInfo();
				String columnName=rs.getString("COLUMN_NAME");
				columnInfo.setColumncode(columnName);
				columnInfo.setColumnname(columnName);
				columnInfo.setColumntype(rs.getString("TYPE_NAME"));
				columnInfo.setColumnlength(rs.getInt("COLUMN_SIZE"));
				columnInfo.setColumnprec(rs.getInt("DECIMAL_DIGITS"));
				columnInfo.setAllownull(rs.getBoolean("NULLABLE"));
				columnInfo.setDefaultvalue(rs.getString("COLUMN_DEF"));
				columnInfo.setColumnorder(i);
				
				if(columnInfo.getColumntype().indexOf("TIMESTAMP")>=0)
					columnInfo.setFormat("Y-m-d H:i:s");
				
				if(columnInfo.getColumntype().indexOf("DATE")>=0){
					columnInfo.setFormat("Y-m-d");
				}
				list.add(columnInfo);
				i++;
				
			}
			rs.close();
		}catch(Exception ex){
			throw ex;
		}finally{
			conn.close();
		}
		return list;
	}
	
	
	
	/**
	 * 
	 * 重建表
		CREATE TABLE SCHEMA.TABLENAME
		(
		  FIELD1    VARCHAR2(32) not null,
		  FIELD2  	DATE,  
		  FIELD3  	TIMESTAMP,    
		  FIELD4    NUMBER(6,2),  
		  FIELD5    VARCHAR2(100),
		  FIELD5    CHAR(100),  
		  FIELD6    VARCHAR2(50),
		  FIELD7    RAW(1),
		  FIELD8	LONG RAW,
		  FIELD9	LONG
		  
		)
	 * @param schema
	 * @param table
	 * @throws Exception 
	 */
	
	public void rebuildTable(String schema,String tableName,List<DefColumnInfo> cols) throws Exception{
		
		String dropSql="DROP TABLE "+schema+"."+tableName+"";
		StringBuffer createSql=new StringBuffer("CREATE TABLE "+schema+"."+tableName+"(\r\n");
		StringBuffer pkSb=new StringBuffer();
		int i=0;
		for (DefColumnInfo defColumnInfo : cols) {
			i++;
			createSql.append(defColumnInfo.getColumncode()+"\t");
			createSql.append(defColumnInfo.getColumntype());
			String type=defColumnInfo.getColumntype().toUpperCase();
			
			
			if(type.equals("DATE") ||
					type.equals("TIMESTAMP") ||
					type.equals("LONG") ||
					type.equals("CLOB") ||
					type.equals("BLOB") ||
					type.equals("LONG RAW")){
				
			}else{
				if(type.equals("VARCHAR2") || type.equals("RAW") || type.equals("CHAR")){
					createSql.append("("+defColumnInfo.getColumnlength()+")");
				}
				if(defColumnInfo.getColumntype().equals("NUMBER"))
					createSql.append("("+defColumnInfo.getColumnlength()+","+defColumnInfo.getColumnprec()+")");
				
			}
			
			if(defColumnInfo.isPk())
				pkSb.append(defColumnInfo.getColumncode()+",");
						
			if(i<cols.size())
				createSql.append(",");
			
			createSql.append("\r\n");
			
		}
		createSql.deleteCharAt(createSql.length()-1);
		createSql.append(")");
	
		Connection conn=dataSource.getConnection();
		conn.setAutoCommit(false);
		try{
			Statement st=conn.createStatement();
			try{
				log.info("删除表:"+dropSql);
				st.executeUpdate(dropSql);
			}catch(Exception ex){}
			String csql = createSql.toString();
			log.info("创建表:"+csql);
			st.executeUpdate(csql);
			if(pkSb.length()>0){
				pkSb.deleteCharAt(pkSb.length()-1);
				String pkSql="alter table "+schema+"."+tableName+" add constraint PK_"+pkSb.toString()+ UUIDGenerator.javaId() +" primary key ("+pkSb.toString()+")";
				log.info(pkSql);
				st.executeUpdate(pkSql);
			}
			st.close();
			conn.commit();
		}catch(Exception ex){
			conn.rollback();
			throw ex;
		}finally{
			conn.close();
		}
		
	}
	
	
	

	/**
	 * 保存或者更新指定表的指定记录 ，如果是新创建的记录，则返回对应的docid
	 * @param schema
	 * @param tableName
	 * @param docid allow null ,create new rec if docid is null,new rec's docid is uuid
	 * @param recordJson
	 * @param diXml 数据项映射xml，描述了每一个数据项的映射方式，以及计算公式公式
	 * @throws SQLException 
	 */
	@SuppressWarnings({ "unchecked" })
	public String saveOrUpdateRec(String schema,String tableName,String docid,String recordJson,String diXml,CFormExp exp) throws Exception{
		JSONObject json = JSONObject.fromObject(recordJson);
		String key = findUniquePrimaryKeyName(schema,tableName);
		String sql = buildUpdatableQuerySql(schema,tableName);
		boolean isNew = StringUtil.isEmpty(docid);	//如果文档编号为空，表示新创建记录，否则修改记录
		Object jsonIdObject = json.get(key);
		//如果ID字段值为空值，则为修改
		if(jsonIdObject == null || (jsonIdObject instanceof JSONObject && ((JSONObject)jsonIdObject).isNullObject()==true)){
			isNew = true;
		}else{
			isNew = false;
			docid = jsonIdObject.toString();
		}
		//解析数据项xml   <root><di></di><di></di>....</root>
		Document document = DocumentHelper.parseText(diXml);
		Map diMap = new HashMap();
		List<Element> diList = document.selectNodes("/root/di");
		
		for (Element diNode : diList) {
			String fd = diNode.attribute("fd").getValue().toUpperCase(); //以字段名大写作为map key
			diMap.put(fd, diNode);
		}
		
		if(!isNew){
			sql+=" WHERE "+key+" = '"+docid+"'";
		}
		Connection conn = dataSource.getConnection();
		try{
			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = st.executeQuery(sql);
			if(StringUtil.isNotEmpty(docid)){
				rs.next();
			}else{
				rs.moveToInsertRow();
			}
			
			ResultSetMetaData meta = rs.getMetaData();
			int fdCount = meta.getColumnCount();
			
			for (int i = 1; i <= fdCount; i++) {
				String fdName = meta.getColumnName(i);
				String fdType = meta.getColumnTypeName(i);
				Element diNode = (Element) diMap.get(fdName);
				String cf_sa = null;
				String cf_cr = null;
				if(diNode!=null){
					cf_sa = diNode.attributeValue("cf_sa");
					cf_cr = diNode.attributeValue("cf_cr");
				}
					
				if(fdName.equals(key))
					continue;
				Object fdValue = null;
				Object jsonObject = json.get(fdName);
				//如果当前字段为空值，则不予处理
				if(jsonObject == null || (jsonObject instanceof JSONObject && ((JSONObject)jsonObject).isNullObject()==true)){
					
				}else if(jsonObject != null && !(jsonObject instanceof JSONObject)){
					fdValue = jsonObject.toString();
				}else if(fdType.equals("DATE") || fdType.equals("TIMESTAMP")){
					long time = ((JSONObject)jsonObject).getLong("time");
					fdValue = time+"";
				}
				
				/**
				 * 创建时计算
				 */
				if(StringUtil.isNotEmpty(cf_cr) && isNew){
					System.out.println("创建时计算公式："+cf_cr);
					exp.setValue((String)fdValue);
					fdValue = exp.eval(cf_cr);
				}
				
				/**
				 * 保存时计算
				 */
				if(StringUtil.isNotEmpty(cf_sa)){
					System.out.println("保存时计算公式："+cf_sa);
					exp.setValue((String)fdValue);
					fdValue = exp.eval(cf_sa);
				}
				
				if(fdType.equals("DATE") || fdType.equals("TIMESTAMP")){
					if(fdValue!=null){
						Long time=0L;
						try{
							time = Long.parseLong((String)fdValue);
						}catch(Exception ex){}
						if(time>0)
							fdValue = new java.sql.Timestamp(time);
						else
							fdValue = null;
						
							
					}
				}
				
				rs.updateObject(i, fdValue);
				
			}
			
			if(isNew){
				
				String id = (String) json.get(key);	//前台有否指定编号,如果有，则使用前台的
				if(StringUtil.isNotEmpty(id)){
					docid = id;
				}else{
					docid=UUIDGenerator.hibernateUUID();
				}
				rs.updateObject(key,docid);
				rs.insertRow();
			}else{
				rs.updateRow();
			}
			rs.close();
				
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}finally{
			conn.commit();
			conn.close();
		}
		return docid;
	}


}
