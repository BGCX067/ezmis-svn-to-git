package com.jteap.jhtj.sjxxxdy.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;


import org.hibernate.Query;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.jhtj.sjflsz.model.TjItemKindKey;
import com.jteap.jhtj.sjxxxdy.model.TjInfoItem;
import com.jteap.jhtj.sjxxxdy.model.TjItemOpr;
import com.jteap.jhtj.sjxxxdy.model.TjItemUpdate;
@SuppressWarnings({ "unchecked", "serial" })
public class TjInfoItemManager extends HibernateEntityDao<TjInfoItem> {
	private DataSource dataSource;
	/**
	 * 
	 *描述：根据分类编码和信息项编码查找数据信息项定义对象
	 *时间：2010-4-9
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public TjInfoItem findTjInfoItemByKidAndItem(String kid,String item){
		TjInfoItem tjInfoItem=null;
		String hql="from TjInfoItem where kid='"+kid+"' and item='"+item+"'";
		List<TjInfoItem> itemList= this.find(hql);
		if(itemList.size()>0){
			tjInfoItem=itemList.get(0);
		}
		return tjInfoItem;
	}
	
	
	
	/**
	 * 
	 *描述：创建数据表
	 *时间：2010-4-10
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String createTable(String kid){
		String result="true";
		try{
			String hql="select distinct(tjitemOpr.tabname) from TjItemOpr tjitemOpr where tjitemOpr.tabname='"+kid+"'";
			List<String> listOpr=this.find(hql);
			//操作表的数据不为空
			if(listOpr.size()>0){
				Iterator<String> it=listOpr.iterator();
				while(it.hasNext()){
					String tabName=it.next();
					String tableName="DATA_"+tabName.toUpperCase();
					String tempName="TEMP_"+tabName.toUpperCase();
					//String sql="select * from all_tables where table_name='"+tableName+"' and owner='YYJHTJ'";
					String sql="select * from tab where tname='"+tableName+"'";
					List tableList=this.getSession().createSQLQuery(sql).list();
					//表存在
					if(tableList.size()>0){
						String tabContent="select * from "+tableName;
						List contentList=this.getSession().createSQLQuery(tabContent).list();
						//表数据不为空
						if(contentList.size()>0){
							String selItem="from TjItemOpr tjitemOpr where tjitemOpr.tabname='"+tabName+"'";
							List<TjItemOpr> listOprItem=this.find(selItem);
							Iterator<TjItemOpr> itOprItem=listOprItem.iterator();
							while(itOprItem.hasNext()){
								TjItemOpr tjItemOpr=itOprItem.next();
								//插入操作
								if(tjItemOpr.getOprflag().intValue()==1){
									this.alterAddTable(tableName, tjItemOpr);
									this.alterAddTable(tempName, tjItemOpr);
									//创建存储过程
								}else{
								//删除操作
									//this.alterDropTable(tableName, tjItemOpr);
									//删除存储过程
								}
								this.remove(tjItemOpr);
							}
						}else{
						//表数据为空
							this.dropTable(tableName);
							this.dropTable(tempName);
							String selItem="from TjInfoItem tjInfoItem where tjInfoItem.kid='"+kid+"'";
							List<TjInfoItem> listTjInfoItem=this.find(selItem);
							if(listTjInfoItem.size()>0){
								String keySql="from TjItemKindKey key where key.kid='"+kid+"'";
								List<TjItemKindKey> keyList= this.find(keySql);
								
								//创建关键字sql
								String resultKey=this.creatrPrimaryKey(keyList);
								//创建关键字
								String primaryKey=this.creatrPrimaryKeyKey(keyList);
								//创建普通字段
								String resultField=this.createField(listTjInfoItem);
								//创建表
								this.createTableSql(resultKey, resultField,primaryKey, tableName);
								//创建临时表
								this.createTableSql(resultKey, resultField,primaryKey, tempName);
								//创建存储过程
							}
							//删除记录
							this.dropTableData(kid);
						}
					}else{
					//表不存在
						String selItem="from TjInfoItem tjInfoItem where tjInfoItem.kid='"+kid+"'";
						List<TjInfoItem> listTjInfoItem=this.find(selItem);
						
						String keySql="from TjItemKindKey key where key.kid='"+kid+"'";
						List<TjItemKindKey> keyList= this.find(keySql);
						
						//创建关键字sql
						String resultKey=this.creatrPrimaryKey(keyList);
						//创建关键字
						String primaryKey=this.creatrPrimaryKeyKey(keyList);
						//创建普通字段
						String resultField=this.createField(listTjInfoItem);
						//创建表
						this.createTableSql(resultKey, resultField,primaryKey ,tableName);
						//创建临时表
						this.createTableSql(resultKey, resultField,primaryKey, tempName);
						//创建存储过程
						
						//删除记录
						this.dropTableData(kid);
					}
				}
			}
			//修改流程
			this.updateTable(kid);
		}catch(Exception e){
			result="false";
		}
		return result;
	}
	
	
	/**
	 * 
	 *描述：更新表结构
	 *时间：2010-4-10
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws SQLException 
	 */
	public void updateTable(String kid) throws SQLException{
		String hql="select distinct(tjItemUpdate.tabname) from TjItemUpdate tjItemUpdate where tjItemUpdate.tabname='"+kid+"'";
		List<String> listOpr=this.find(hql);
		//操作表的数据不为空
		if(listOpr.size()>0){
			Iterator<String> it=listOpr.iterator();
			while(it.hasNext()){
				String tabName=it.next();
				String tableName="DATA_"+tabName.toUpperCase();
				String tempName="TEMP_"+tabName.toUpperCase();
				//String sql="select * from all_tables where table_name='"+tableName+"' and owner='YYJHTJ'";
				String sql="select * from tab where tname='"+tableName+"'";
				List couList=this.getSession().createSQLQuery(sql).list();
				//表存在
				if(couList.size()>0){
					String tabContent="select * from "+tableName;
					List contentList=this.getSession().createSQLQuery(tabContent).list();
					//表数据不为空
					if(contentList.size()>0){
						String selItem="from TjItemUpdate tjItemUpdate where tjItemUpdate.tabname='"+tabName+"'";
						List<TjItemUpdate> listOprItem=this.find(selItem);
						Iterator<TjItemUpdate> itOprItem=listOprItem.iterator();
						while(itOprItem.hasNext()){
							TjItemUpdate tjItemUpdate=itOprItem.next();
							//修改字段
							this.alterModifyTable(tableName, tjItemUpdate);
							this.alterModifyTable(tempName, tjItemUpdate);
							//如果改成了计算类型就创建存储过程，如果先是计算改成了别的取数方式就删除存储过程
							//删除记录
							this.remove(tjItemUpdate);
						}
					}else{
					//表数据为空
						this.dropTable(tableName);
						this.dropTable(tempName);
						String selItem="from TjInfoItem tjInfoItem where tjInfoItem.kid='"+kid+"'";
						List<TjInfoItem> listTjInfoItem=this.find(selItem);
						
						String keySql="from TjItemKindKey key where key.kid='"+kid+"'";
						List<TjItemKindKey> keyList= this.find(keySql);
						
						//创建关键字sql
						String resultKey=this.creatrPrimaryKey(keyList);
						//创建关键字
						String primaryKey=this.creatrPrimaryKeyKey(keyList);
						//创建普通字段
						String resultField=this.createField(listTjInfoItem);
						//创建表
						this.createTableSql(resultKey, resultField,primaryKey, tableName);
						this.createTableSql(resultKey, resultField,primaryKey, tempName);
						//创建存储过程
						//删除修改记录
						this.dropTjUpdateTable(kid);
					}
				}else{
				//表不存在
					String selItem="from TjInfoItem tjInfoItem where tjInfoItem.kid='"+kid+"'";
					List<TjInfoItem> listTjInfoItem=this.find(selItem);
					
					String keySql="from TjItemKindKey key where key.kid='"+kid+"'";
					List<TjItemKindKey> keyList= this.find(keySql);
					
					//创建关键字sql
					String resultKey=this.creatrPrimaryKey(keyList);
					//创建关键字
					String primaryKey=this.creatrPrimaryKeyKey(keyList);
					//创建普通字段
					String resultField=this.createField(listTjInfoItem);
					//创建表
					this.createTableSql(resultKey, resultField,primaryKey, tableName);
					this.createTableSql(resultKey, resultField,primaryKey, tempName);
					//创建存储过程
					//删除修改记录
					this.dropTjUpdateTable(kid);
				}
			}
			//结束
			this.createProc(kid);
		}else{
			//结束
			this.createProc(kid);
		}
	}
	
	


	/**
	 * 功能说明:修改表结构-增加字段
	 * @author 童贝
	 * @version Apr 22, 2009
	 * @param tableName
	 * @param tjItemOpr
	 * @throws SQLException 
	 */
	public void alterAddTable(String tableName,TjItemOpr tjItemOpr) throws SQLException{
		String tempType=tjItemOpr.getItype();
		String type="";
		if(tempType.equals("NUMBER")){
			type="NUMBER(18,"+tjItemOpr.getXsw()+")";
		}else if(tempType.equals("VARCHAR2")){
			type="VARCHAR2("+tjItemOpr.getCd()+")";
		}
		
		String sql="ALTER TABLE "+tableName+" ADD ("+tjItemOpr.getItem()+" "+type+")";
	
		Connection conn=null;
		Statement statement=null;
		try {
			conn=this.dataSource.getConnection();
			statement=conn.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("修改表结构-增加字段出错");
		}finally{
			if(statement!=null){
				statement.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
	}
	
	
	/**
	 * 功能说明:删除表
	 * @author 童贝
	 * @version Apr 22, 2009
	 * @param tableName
	 * @throws SQLException 
	 */
	public void dropTable(String tableName) throws SQLException{
		String sql="DROP TABLE "+tableName;
		this.operateDataBase(sql);
	}
	
	/**
	 * 功能说明:根据SQL语句对数据库的操作
	 * @author 童贝
	 * @version Apr 22, 2009
	 * @param sql
	 * @throws SQLException 
	 */
	public void operateDataBase(String sql) throws SQLException{
		Connection conn=null;
		Statement statement=null;
		try {
			conn=this.dataSource.getConnection();
			statement=conn.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("操作表结构出错");
		}finally{
			if(statement!=null){
				statement.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
	}
	
	
	/**
	 * 功能说明:创建关键字sql
	 * @author 童贝
	 * @version Apr 22, 2009
	 * @param keyList
	 * @return
	 */
	public String creatrPrimaryKey(List<TjItemKindKey> keyList){
		String sql="";
		Iterator<TjItemKindKey> it=keyList.iterator();
		while(it.hasNext()){
			TjItemKindKey key=it.next();
			String type="";
			//1－整数型、2－字符型，3－日期型；
			if(key.getLx().intValue()==1){
				type="NUMBER("+key.getCd()+")";
			}else if(key.getLx().intValue()==2){
				type="VARCHAR2("+key.getCd()+")";
			}else if(key.getLx().intValue()==3){
				type="DATE";
			}
			sql=sql+key.getIcode()+" "+type+",";
		}
		return sql;
	}
	
	
	public String creatrPrimaryKeyKey(List<TjItemKindKey> keyList){
		String sql="PRIMARY KEY(";
		Iterator<TjItemKindKey> it=keyList.iterator();
		while(it.hasNext()){
			TjItemKindKey key=it.next();
			sql=sql+key.getIcode()+",";
		}
		if(sql.equals("PRIMARY KEY(")){
			sql="";
		}else{
			sql=sql.substring(0,sql.length()-1)+")";
		}
		return sql;
	}
	
	/**
	 * 功能说明:创建字段sql
	 * @author 童贝
	 * @version Apr 22, 2009
	 * @param listOprItem
	 * @return
	 */
	public String createField(List<TjInfoItem> tjInfoItemList){
		String sql="";
		Iterator<TjInfoItem> it=tjInfoItemList.iterator();
		while(it.hasNext()){
			TjInfoItem tjInfoItem=it.next();
			String type="";
			String tempType=tjInfoItem.getItype();
			if(tempType.equals("INT")){
				type="NUMBER(6,0)";
			}else if(tempType.equals("FLOAT")){
				type="NUMBER(18,"+tjInfoItem.getXsw()+")";
			}else if(tempType.equals("VARCHAR")){
				type="VARCHAR2("+tjInfoItem.getCd()+")";
			}
			sql=sql+tjInfoItem.getItem()+" "+type+",";
		}
		if(!sql.equals("")){
			sql=sql.substring(0,sql.length()-1);
		}
		return sql;
	}
	
	
	/**
	 * 功能说明:创建表
	 * @author 童贝
	 * @version Apr 22, 2009
	 * @param keySql
	 * @param fieldSql
	 * @param tableName
	 * @throws SQLException 
	 */
	public void createTableSql(String keySql,String fieldSql,String primaryKey,String tableName) throws SQLException{
		String result=null;
		if(primaryKey.equals("")){
			result="CREATE TABLE "+tableName+"("+keySql+fieldSql+")";
		}else{
			result="CREATE TABLE "+tableName+"("+keySql+fieldSql+","+primaryKey+")";
		}
		this.operateDataBase(result);
	}
	
	/**
	 * 功能说明:删除操作表的记录
	 * @author 童贝
	 * @version Apr 24, 2009
	 * @param tableName
	 */
	public void dropTableData(String tableName){
		String hql="delete from TjItemOpr io where io.tabname=?";
		Query query=this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		query.setString(0, tableName);
		query.executeUpdate();
	}
	
	/**
	 * 
	 *描述：修改表结构
	 *时间：2010-4-10
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws SQLException 
	 */
	public void alterModifyTable(String tableName,TjItemUpdate tjItemUpdate) throws SQLException{
		if(!tjItemUpdate.getNitem().equals(tjItemUpdate.getOitem())){
			String rename="ALTER TABLE "+tableName+" RENAME COLUMN "+tjItemUpdate.getOitem()+" TO "+tjItemUpdate.getNitem();
			this.operateDataBase(rename);
		}
		String type="";
		if(tjItemUpdate.getItype().equalsIgnoreCase("NUMBER")){
			type="NUMBER("+tjItemUpdate.getCd()+","+tjItemUpdate.getXsw()+")";
		}else{
			type=tjItemUpdate.getItype()+"("+tjItemUpdate.getCd()+")";
		}
		String sql="ALTER TABLE "+tableName+" MODIFY("+tjItemUpdate.getNitem()+" "+type+")";
		this.operateDataBase(sql);
	}
	
	
	/**
	 * 功能说明:删除修改表的记录
	 * @author 童贝
	 * @version Apr 24, 2009
	 * @param tabname
	 */
	public void dropTjUpdateTable(String tabname){
		String hql="delete from TjItemUpdate io where io.tabname=?";
		Query query=this.getSession().createQuery(hql);
		query.setString(0, tabname);
		query.executeUpdate();
	}
	
	
	/**
	 * 功能说明:创建存储过程
	 * @author 童贝
	 * @version Apr 24, 2009
	 * @param kid
	 * @throws SQLException 
	 */
	public void createProc(String kid) throws SQLException{
		String curKid=kid;
		//存储过程名字
		String proName=curKid.toUpperCase()+"_PROCEDURE";
		String tempName="TEMP_"+curKid.toUpperCase();
		String curHql="from TjInfoItem info where info.kid='"+kid+"' and info.qsfs="+new Long(3)+" order by info.corder";
		List<TjInfoItem> itemList=this.find(curHql);
		if(itemList.size()>0){
			Iterator<TjInfoItem> itemIt=itemList.iterator();
			String result="CREATE OR REPLACE PROCEDURE "+proName+" IS BEGIN ";
			String sql="";
			Long jb=new Long(0);
			while(itemIt.hasNext()){
				TjInfoItem item=itemIt.next();
				if(sql.equals("")){
					String temp="UPDATE "+tempName+" set "+item.getItem()+"="+item.getCexp();
					sql=sql+temp;
					jb=item.getCorder();
				}else{
					//相等就是同一计算级别
					if(item.getCorder().intValue()==jb.intValue()){
						String temp=item.getItem()+"="+item.getCexp();
						sql=sql+","+temp;
					}else{
						String temp="UPDATE "+tempName+" set "+item.getItem()+"="+item.getCexp();
						sql=sql+";"+temp;
						jb=item.getCorder();
					}
				}
			}
			String last=sql.substring(sql.length());
			if(!last.equals(";")){
				sql=sql+";";
			}
			result=result+sql+"END "+proName+";";
			//只有该表有计算字段的时候才创建
			if(itemList.size()>0){
				this.operateDataBase(result);
			}else{
				
			}
		}else{
			//判断存储过程是否存在
			//删除存储过程
			String delete="DROP PROCEDURE "+kid+"_PROCEDURE";
			this.operateDataBase(delete);
		}
	}
	
	/**
	 * 功能说明:测试计算公式是够合法
	 * @author 童贝
	 * @version Jun 11, 2009
	 * @param kid
	 * @param cexp
	 * @return
	 */
	public boolean testCexp(String kid,String cexp){
		boolean bool=true;
		String sql="select "+cexp+" from DATA_"+kid;
		try{
			this.getSession().createSQLQuery(sql).list();
		}catch(Exception ex){
			return false;
		}
		return bool;
	}
	
	/**
	 * 
	 *描述：根据kid获得表的全部数据
	 *时间：2010-6-1
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List findData_Table(String kid){
		String sql="select * from DATA_"+kid;
		return this.getSession().createSQLQuery(sql).list();
	}

	public DataSource getDataSource() {
		return dataSource;
	}



	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
}
