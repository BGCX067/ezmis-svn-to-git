package com.jteap.system.jdbc.manager;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jteap.core.dao.JdbcEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.system.jdbc.model.FieldInfo;
import com.jteap.system.jdbc.model.TableInfo;

/**
 * 物理表管理器 
 * @author tanchang
 *
 */
public class JdbcManager extends JdbcEntityDao{
	
	/**
	 * 取得指定schemaName和tableName的表
	 * @param schemaName	
	 * @param tableName
	 * @return 返回符合条件的表集合
	 * @throws SQLException 
	 */
	public List<TableInfo> findTableList(String fullName) throws Exception{
		Connection conn=null;
		List<TableInfo> list=new ArrayList<TableInfo>();
		try{
			conn=dataSource.getConnection();
			DatabaseMetaData dmd = conn.getMetaData();
			String schema = dmd.getUserName();
			//查询所有非系统表
			StringBuffer sqlSb=new StringBuffer("SELECT OWNER,TABLE_NAME FROM DBA_TABLES WHERE OWNER NOT IN('MGMT_VIEW','SYS','SYSTEM','DBSNMP','SYSMAN','OUTLN','MDSYS','ORDSYS','EXFSYS','DMSYS','WMSYS','CTXSYS','ANONYMOUS','XDB','ORDPLUGINS','ORDPLUGINS','SI_INFORMTN_SCHEMA','OLAPSYS','SCOTT','TSMSYS','BI','PM','MDDATA','IX','SH','DIP','OE','HR') AND OWNER = '"+schema+"'");
			if(StringUtil.isNotEmpty(fullName)){
				sqlSb.append(" AND TABLE_NAME LIKE '%"+fullName+"%'");
			}
			sqlSb.append(" ORDER BY OWNER,TABLE_NAME");
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(sqlSb.toString());
			while(rs.next()){
				String owner=rs.getString("OWNER");
				String tbname=rs.getString("TABLE_NAME");
				TableInfo pt=new TableInfo();
				pt.setTableName(tbname);
				pt.setSchema(owner);
				list.add(pt);
			}
		}catch(Exception ex){
			throw ex;
		}finally{
			if (conn != null)
				conn.close();
		}
		return list;
	}
	
	/**
	 * 根据业务数据记录编号查询该记录数据并以map的方式返回
	 * @param docid
	 * @param defTableId
	 * @return
	 * @throws SQLException   
	 */
	@SuppressWarnings("unchecked")
	public Map getRecById(Object docid,String schema,String tableName) throws SQLException{
		List keys = this.findPrimaryKeyColumnNameList(schema, tableName);
		if(keys.size()<=0)
			throw new BusinessException("数据表["+tableName+"]没有主键");
		String sql = "SELECT * FROM "+schema+"."+tableName+" WHERE "+keys.get(0)+"=?";
		Connection conn= null;
		Map result = new HashMap();
		try{
			conn=dataSource.getConnection();
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setObject(1,docid);
			ResultSet rs = pst.executeQuery();
			if(rs.next()){
				ResultSetMetaData meta = rs.getMetaData();
				int columnCount = meta.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					Object obj=rs.getObject(i);
					//由于json方法无法处理 java.sql.Date对象，所以转换为java.util.Date
					if(obj instanceof java.sql.Date){
						obj = new Date(((java.sql.Date)obj).getTime());
					}
					if(obj instanceof java.sql.Timestamp){
						obj = new Date(((java.sql.Timestamp)obj).getTime());
					}
					if(obj instanceof oracle.sql.TIMESTAMP){
						obj = new Date(((oracle.sql.TIMESTAMP)obj).dateValue().getTime());
					}
					//如果是数据库大字段
					obj = StringUtil.clobToStringByDB(obj);
					String columnName = meta.getColumnName(i);
					result.put(columnName,obj);
				}
			}
			rs.close();
			pst.close();
		}catch(Exception ex){
			throw new BusinessException("查询数据出现异常"+ex.toString());
		}finally{
			if (conn != null)
				conn.close();
		}
		return result;
	}
	
	
	/**
	 * 物理数据库中是否存在指定的表
	 * @param schema
	 * @param tableName
	 * @return
	 * @throws SQLException 
	 */
	public boolean isExist(String schema,String tableName) throws SQLException{
		Connection conn=null;
		boolean result=false;
		try{
			conn = dataSource.getConnection();
			DatabaseMetaData dbmeta=conn.getMetaData();
			ResultSet rs=dbmeta.getTables(null, schema, tableName, null);
			if(rs.next()){
				result=true;
			}
			rs.close();
			conn.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		return result;
	}

	/**
	 * 根据指定的表名和列名取得该列的注释
	 * @param tableName
	 * @param columnName
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	private String _getColumnComment(String tableName,String columnName,Connection conn){
		PreparedStatement ps = null;
		String comments = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT COMMENTS FROM user_col_comments WHERE table_name = ? and column_name = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, tableName);
			ps.setString(2,	columnName);
			rs = ps.executeQuery();
			if(rs.next()){
				comments = rs.getString(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return comments;
		
	}
	
	/**
	 * 根据SQL语句查询结果集中的字段信息
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<FieldInfo> findFieldInfoListBySql(String sql) throws Exception{
		Connection conn=null;
		List<FieldInfo> list=new ArrayList<FieldInfo>();
		try{
			conn = dataSource.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			for (int i = 1; i <=count; i++) {
				FieldInfo columnInfo=new FieldInfo();
				String columnName=rsmd.getColumnName(i);
				columnInfo.setColumncode(columnName);
				columnInfo.setColumnname(columnName);
				columnInfo.setColumntype(rsmd.getColumnTypeName(i));
				columnInfo.setColumnlength(rsmd.getColumnDisplaySize(i));
				columnInfo.setColumnprec(rsmd.getPrecision(i));
				columnInfo.setAllownull(rsmd.isNullable(i)==1);				
				columnInfo.setColumnorder(i);
				list.add(columnInfo);
				String tableName = rsmd.getTableName(i);
				String comm = _getColumnComment(tableName,columnInfo.getColumncode(),conn);
				if(StringUtil.isNotEmpty(comm))
					columnInfo.setColumnname(comm);
			}
			rs.close();
			st.close();
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}finally{
			if(conn!=null)
				conn.close();
		}
		return list;
	}
	

	/**
	 * 根据表名，取得所有存在的字段并构造成列定义对象返回
	 * @param schema
	 * @param tableName
	 * @return
	 * @throws SQLException 
	 */
	public List<FieldInfo> findDefColumnInfoListByTable(String tableName) throws Exception{
		Connection conn= null;
		List<FieldInfo> list=new ArrayList<FieldInfo>();
		try{
			conn = dataSource.getConnection();
			DatabaseMetaData dbmeta=conn.getMetaData();
			ResultSet rs=dbmeta.getColumns(null, null, tableName, null);
			int i=0;
			while(rs.next()){
				FieldInfo columnInfo=new FieldInfo();
				String columnName=rs.getString("COLUMN_NAME");
				columnInfo.setColumncode(columnName);
				columnInfo.setColumnname(columnName);
				
				columnInfo.setColumntype(rs.getString("TYPE_NAME"));
				
				columnInfo.setColumnlength(rs.getInt("COLUMN_SIZE"));
				columnInfo.setColumnprec(rs.getInt("DECIMAL_DIGITS"));
				columnInfo.setAllownull(rs.getBoolean("NULLABLE"));
				
				columnInfo.setDefaultvalue(rs.getString("COLUMN_DEF"));
				columnInfo.setColumnorder(i);
				list.add(columnInfo);
				i++;
			}
			rs.close();
		}catch(Exception ex){
			throw ex;
		}finally{
			if(conn != null)
				conn.close();
		}
		return list;
	}
	
	/**
	 * 根据表名，找出该表的主键，并以列表的形式返回
	 * @param schema
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public List<String> findPrimaryKeyColumnNameList(String schema,String tableName) throws SQLException{
		Connection conn=null;
		List<String> list=new ArrayList<String>();
		try{
			conn = dataSource.getConnection();
			DatabaseMetaData dbmeta=conn.getMetaData();
			ResultSet rs=dbmeta.getPrimaryKeys(null, schema, tableName);
			while(rs.next()){
				String columnName=rs.getString("COLUMN_NAME");
				list.add(columnName);
			}
			rs.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(conn!=null)
				conn.close();
			
		}
		return list;
	}
	/**
	 * 获得单一主键
	 * @param schema
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public String findUniquePrimaryKeyName(String schema,String tableName) throws SQLException{
		List<String> keys = findPrimaryKeyColumnNameList(schema,tableName);
		String key = null;
		if(keys.size()>0){
			key = keys.get(0);
		}
		return key;
	}
	
	/**
	 * 
	 *描述：批量执行SQL语句
	 *时间：2010-5-31
	 *作者：蔡慧文
	 *参数：sqlbatch 批量SQL语句 以;分隔
	 *返回值:
	 *抛出异常：
	 * @throws Exception 
	 */
	public void executeSqlBatch(String sqlbatch) throws Exception{
		String[] sqls = sqlbatch.split(";");
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.batchUpdate(sqls);
	}
	
	
	
	
	/**
	 * 根据主键编号删除对应的记录
	 * @param schema
	 * @param tableName
	 * @param docid
	 * @throws Exception 
	 */
	public void deleteRecById(String schema,String tableName,Object docid) throws Exception{
		String key = this.findUniquePrimaryKeyName(schema, tableName);
		String sql = "DELETE FROM "+schema+"."+tableName+" WHERE "+key+" = ?";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(sql, new Object[]{docid});
	}
	
	
	/**
	 * 
	 *描述：通过SQL语句查询符合条件的唯一数据
	 *时间：2010-5-27
	 *作者：谭畅
	 *参数：sql 查询语句
	 *返回值:查询成功返回该查询结果的第一条记录的第一个字段的对象，否则返回空
	 *抛出异常：
	 */
	public Object queryUniqueBySql(String sql){
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Object obj = null;
		try{
			obj = template.queryForObject(sql, Object.class);
		}catch(Exception e){
			//e.printStackTrace();
		}
		return obj;
	}
	
	
	

	/**
	 * 根据表名查询该物理表是否存在数据.
	 * @param tableName
	 * @throws SQLException 
	 */
	public boolean isHaveData(String tableName) throws SQLException{
		boolean isHaveData = false;
		String sql = "select 1 from " + tableName +" where rownum=1";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				isHaveData = true;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return isHaveData;
	}
	
	
	

	/**
	 * 
	 *描述： 根据指定的数据项定义和数据信息新建或者修改指定表中的一行记录
	 *时间：2010-5-14
	 *作者：谭畅
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String saveOrUpdateRec(String schema, String tableName,
			String docid, Map<String,Element> diMap, Map<String,Object> dataMap) throws Exception {
		
		boolean isNew = StringUtil.isEmpty(docid);	//如果文档编号为空，表示新创建记录，否则修改记录
		if(isNew)
			docid = UUIDGenerator.hibernateUUID();
		String sql = buildUpdatePreparedSql(schema,tableName,dataMap,isNew);
		Connection conn = null;
		try{
			conn = dataSource.getConnection();
			PreparedStatement pst = conn.prepareStatement(sql);
			int i=0;
			//设置每个字段值
			for(String fd:dataMap.keySet()){
				i++;
				Object value = dataMap.get(fd);
				pst.setObject(i, value);
			}
			//如果是修改状态
			pst.setObject(i+1,docid);
			pst.execute();
			pst.close();
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}finally{
			if(conn!=null){
				conn.close();
			}
		}
		
		return docid;
	}
	

	/**
	 * 根据表名删除物理表
	 * @param tableName
	 * @param schema
	 * @throws Exception 
	 */
	public void deletePhysicTable(String tableName, String schema) throws Exception{
		try {
			if(!isExist(schema, tableName)){
				//不存在物理表,返回
				return;
			}
			String sql="DROP TABLE "+schema+"."+tableName;
			Connection conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			try {
				Statement st=conn.createStatement();
				st.executeUpdate(sql);
				st.close();
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				conn.rollback();
			}finally{
				conn.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * 
	 *描述：根据指定的数据项构建插入和更新SQL语句
	 *时间：2010-5-28
	 *作者：谭畅
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws SQLException 
	 */
	protected String buildUpdatePreparedSql(String schema, String tableName,
			Map<String, Object> dataMap,boolean isNew) throws SQLException {
		String sql = null;
		if(isNew){
			StringBuffer colListSB = new StringBuffer();
			StringBuffer wh = new StringBuffer();
			for(String col : dataMap.keySet()){
				colListSB.append(col+",");
				wh.append("?,");
			}
			colListSB.append("ID");
			wh.append("?");
			sql = "INSERT INTO "+schema+"."+tableName+" ("+colListSB+") VALUES ("+wh+")";
		}else{
			StringBuffer colListSB = new StringBuffer();
			String key = findUniquePrimaryKeyName(schema,tableName);
			for(String col : dataMap.keySet()){
				colListSB.append(col+"=?,");
			}
			if(colListSB.length()>0){
				colListSB.deleteCharAt(colListSB.length()-1);
			}
			sql = "UPDATE "+schema+"."+tableName+" SET "+colListSB+" WHERE "+key+"=?";
		}
		return sql;
	}
	
	
	/**
	 * 由于在ORACLE采用SELECT * FROM 的方式查询出来的记录集不能进行更新
	 * 所以必须查询出所有字段
	 * @param schema
	 * @param tableName
	 * @return
	 * @throws Exception 
	 */
	protected String buildUpdatableQuerySql(String schema,String tableName) throws Exception{
		StringBuffer sql=new StringBuffer("SELECT ");
		Connection conn=null;
		try{
			conn=dataSource.getConnection();
			DatabaseMetaData dbmeta=conn.getMetaData();
			ResultSet rs=dbmeta.getColumns(null, schema, tableName, null);
			while(rs.next()){
				String columnName=rs.getString("COLUMN_NAME");
				sql.append(columnName + ",");
			}
			rs.close();
		}catch(Exception ex){
			throw ex;
		}finally{
			if (conn != null) {
				conn.close();
			}
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(" FROM "+schema+"."+tableName);
		return sql.toString();
	}
	
}
