package com.jteap.jhtj.ljydy.manager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;



import org.jdom.Element;


import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.StringUtil;
import com.jteap.jhtj.dynamicconnect.DynamicConnectionManager;
import com.jteap.jhtj.ljydy.model.AppSystemConn;
import com.jteap.jhtj.ljydy.model.AppSystemField;

public class AppSystemConnManager extends HibernateEntityDao<AppSystemConn> {
	/**
	 * 功能说明:得到指定连接的所有的表
	 * @author 童贝
	 * @version Apr 8, 2009
	 * @param system
	 * @return
	 * @throws Exception 
	 */
	public List<AppSystemField> findTablesByConn(AppSystemConn system,String tableName) throws Exception{
		List<AppSystemField> tables=new ArrayList<AppSystemField>();
		DynamicConnectionManager dcManager=DynamicConnectionManager.getInstance();
		Connection conn=null;
		ResultSet tableRs = null;
		try{

			conn=dcManager.getDBConnection(system.getClassName(), system.getUrl(), system.getUserId());
			if(!conn.isClosed()){
				DatabaseMetaData meta = conn.getMetaData();
				String schema = getSchema(system);
				/**
				 * 取得所有的表和视图
				 */
				String[] types=new String[]{"TABLE","VIEW"};
				if(tableName.equals("")){
					tableRs=meta.getTables(null, schema, "%", types);
				}else{
					tableRs=meta.getTables(null, schema, tableName, null);
				}
				
		
				while (tableRs.next()) {
					AppSystemField table = new AppSystemField();
					String tName=tableRs.getString(3);
					if(tName.indexOf("$")>-1||tName.indexOf("+")>-1||tName.indexOf("/")>-1||tName.indexOf("//")>-1||tName.indexOf("==")>-1){
						//一些系统表就不加入进来
					}else{
						//表英文名
						table.setVname(tName);
						table.setSystem(system);
						tables.add(table);
					}
				}
				//tableRs.close();
				//dcManager.freeDBConnection(conn,system.getUrl(),system.getUserId());
			}
		}catch(Exception e){
		}finally{
			dcManager.freeDBConnection(conn,system.getUrl(),system.getUserId());
			dcManager.realse();
			if(tableRs!=null){
				tableRs.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return tables;
	}
	
	
	/**
	 * 取Schema根据不同的数据库
	 * 
	 * @param systemConn
	 * @return
	 */
	private String getSchema(AppSystemConn systemConn) {
		String schema;
		/**
		 * 需要判断当前是什么类型的数据，应该用什么方式处理
		 */
		if (systemConn.getDbType().intValue()==1) {
			schema = systemConn.getUserId().toUpperCase();// SCHEMA均以大写形式存在
		} else if (systemConn.getDbType().intValue()==2) {
			schema = "dbo";
		} else if (systemConn.getDbType().intValue()==6) {
			schema = systemConn.getUserId();
		} else if (systemConn.getDbType().intValue()==3) {
			schema = "dbo";
		} else {
			schema = systemConn.getUserId();
		}
		return schema;
	}
	
	
	/**
	 * 功能说明:取得指定表的全部符合条件的字段信息
	 * @author 童贝
	 * @version Apr 8, 2009
	 * @param system
	 * @param tableName
	 * @return
	 * @throws Exception 
	 */
	public List<AppSystemField> getAllFieldInfoInTable(AppSystemConn system,
			String tableName,String sFieldName) throws Exception {
		List<AppSystemField> fields = new ArrayList<AppSystemField>();
		if (system == null) {
			return fields;
		}
		DynamicConnectionManager dcManager=DynamicConnectionManager.getInstance();
		Connection conn=null;
		ResultSet fieldRs=null;
		try {
			conn=dcManager.getDBConnection(system.getClassName(), system.getUrl(), system.getUserId());
			DatabaseMetaData meta = conn.getMetaData();
			String schema = getSchema(system);
			
			if(sFieldName.equals("")){
				fieldRs = meta.getColumns(null, schema, tableName, "%");
			}else{
				fieldRs = meta.getColumns(null, schema, tableName, sFieldName);
			}
			
			while (fieldRs.next()) {
				AppSystemField table = new AppSystemField();
				table.setVname(tableName);
				table.setFname(fieldRs.getString(4));
				table.setFtype(fieldRs.getString(6));
				table.setSystem(system);
				fields.add(table);
			}
		} catch (Exception e) {
		}finally{
			dcManager.freeDBConnection(conn,system.getUrl(),system.getUserId());
			dcManager.realse();
			if(fieldRs!=null){
				fieldRs.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return fields;
	}
	
	
	
	
	/**
	 * 
	 *描述：处理表数据信息
	 *时间：2010-4-2
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String processTableInfo(Element oTable) throws Exception {
		String sTableName = oTable.getAttribute("name").getValue();
		String sysid = oTable.getAttribute("sysid").getValue();
		String sTableCName = oTable.getAttribute("cname").getValue();
		AppSystemConn system = this.get(sysid);
		//判断选择的表是否在本系统中已经存在
		boolean isTable=this.findAppSystemFieldBySystemAndVname(system, sTableName);
		//表不存在才能存储
		if(isTable){
			//查找指定的表信息
			List<AppSystemField> list=this.findTablesByConn(system, sTableName);
			AppSystemField table=null;
			if(list.size()>0){
				table=list.get(0);
			}
			//设置中文名
			table.setCvname(sTableCName.equals("") ? sTableName : sTableCName);

			int size = oTable.getChildren().size();
			//存放所有被选中的字段
			List<Element> filerList=new ArrayList<Element>();
			//得到该表所有的字段
			List<AppSystemField>  List= (List<AppSystemField>)this.getAllFieldInfoInTable(system, table.getVname(),"");
			for (int i = 0; i < size; i++) {
				Element oField = (Element) oTable.getChildren().get(i);
				String checked = StringUtil.dealNull(oField.getAttribute("checked").getValue());
				if (checked.equalsIgnoreCase("true")) {
					filerList.add(oField);
				}
			}
			if(filerList.size()>0){
				for(int j=0;j<filerList.size();j++){
					Element oField=filerList.get(j);
					processFieldInfo(system,table, oField,List,j+1);
				}
			}
		}else{
		}
		return "ok";
	}
	
	
	
	/**
	 * 
	 *描述：查找从别的系统上取的表是否已经在本系统存过了
	 *时间：2010-4-2
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public boolean findAppSystemFieldBySystemAndVname(AppSystemConn appConn,String vname){
		Set<AppSystemField> fields=appConn.getAppSystemFields();
		for(AppSystemField af:fields){
			String tableName=af.getVname();
			if(vname.equalsIgnoreCase(tableName)){
				return false;
			}
		}
		return true;
	}
	
	
	
	/**
	 * 
	 *描述：处理字段数据信息
	 *时间：2010-4-2
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	private String processFieldInfo(AppSystemConn system,AppSystemField table, Element oField,List<AppSystemField> list,int forder) {
		String sFieldName = oField.getAttribute("name").getValue();
		String sFieldCName = oField.getAttribute("cname").getValue();
		String sType = oField.getAttribute("type").getValue();
		String checked = StringUtil.dealNull(oField.getAttribute("checked")
				.getValue());
		if (checked.equalsIgnoreCase("true")) {
			if(list.size()>0){
				//保存该字段的信息
				AppSystemField field=null;
				Iterator<AppSystemField> itFie=list.iterator();
				while(itFie.hasNext()){
					AppSystemField tempField=itFie.next();
					if(tempField.getFname().equalsIgnoreCase(sFieldName)){
						field=tempField;
					}
				}
				if(field!=null){
					field.setCfname(sFieldCName.equals("") ? sFieldName : sFieldCName);
					//最后保存的对象
					AppSystemField saveField=new AppSystemField();
					saveField.setVname(table.getVname());
					saveField.setSystem(system);
					saveField.setCvname(table.getCvname());
					
					saveField.setFname(field.getFname());
					saveField.setCfname(field.getCfname());
					saveField.setFtype(sType);
					
					saveField.setForder(new Long(forder));
					
					system.getAppSystemFields().add(saveField);
					this.save(system);
					this.save(saveField);
				}
			}
		}else if (checked.equalsIgnoreCase("false")){
		}
		return "ok";
	}
	
	/**
	 * 
	 * 描述:初始化已存在的链接
	 * 时间:2010 11 1
	 * 作者:童贝
	 * 参数:
	 * 返回值:void
	 * 抛出异常:
	 */
	public void initAppSystemConn() {
		//取得所有的连接
		List<AppSystemConn> list=this.getAll();
		DynamicConnectionManager dcManager=DynamicConnectionManager.getInstance();
		Iterator<AppSystemConn> it=list.iterator();
		//配置文件读取最大连接
		String maxConnection="100";
		while(it.hasNext()){
			AppSystemConn appConn=it.next();
			dcManager.addConnectionManager(appConn.getClassName(), appConn.getUrl(), appConn.getUserId(), appConn.getUserPwd(), new Integer(maxConnection));
		}
		//System.out.println("初始化连接成功....................................");
	}
}
