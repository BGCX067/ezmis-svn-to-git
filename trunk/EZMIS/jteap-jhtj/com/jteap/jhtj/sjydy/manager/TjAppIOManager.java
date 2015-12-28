package com.jteap.jhtj.sjydy.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;


import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;


import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.DateUtils;
import com.jteap.jhtj.dynamicconnect.DynamicConnectionManager;
import com.jteap.jhtj.jkbldy.model.TjInterFace;
import com.jteap.jhtj.ljydy.model.AppSystemConn;
import com.jteap.jhtj.ljydy.model.AppSystemField;
import com.jteap.jhtj.sjflsz.model.TjItemKindKey;
import com.jteap.jhtj.sjxxxdy.model.TjInfoItem;
import com.jteap.jhtj.sjydy.model.TjAppIO;
import com.jteap.jhtj.sjydy.model.TjAppSjzd;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.dict.model.DictCatalog;
@SuppressWarnings({ "unchecked", "serial" })
public class TjAppIOManager extends HibernateEntityDao<TjAppIO> {
	private DataSource dataSource;
	/**
	 * 功能说明:根据系统ID查找系统数据接口集合
	 * @author 童贝
	 * @version Apr 13, 2009
	 * @param systemId
	 * @return
	 */
	public List<TjAppIO> findTjAppIOBySystemId(String systemId){
		List<TjAppIO> list=new ArrayList<TjAppIO>();
		if(!systemId.equals("")&&systemId!=null){
			String hql="from TjAppIO io where io.sid='"+systemId+"'";
			list=this.find(hql);
		}
		return list;
	}
	
	
	/**
	 * 功能说明:过滤重复的表
	 * @author 童贝
	 * @version Apr 11, 2009
	 * @param tables
	 * @return
	 */
	public List<AppSystemField> filterRepeatTablesByConn(AppSystemConn sysConn){
		Set<AppSystemField> fieldSet=sysConn.getAppSystemFields();
		ArrayList<AppSystemField> temList=new ArrayList<AppSystemField>();
		if(fieldSet.size()>0){
			boolean flag=false;
			for(AppSystemField table:fieldSet){
				if(temList.size()>0){
					for(AppSystemField tempField:temList){
						if(tempField.getVname().equals(table.getVname())){
							flag=false;
							break;
						}else{
							flag=true;
						}
					}
				}else{
					flag=true;
				}
				if(flag){
					temList.add(table);
				}
			}
		}
		return temList;
	}
	
	/**
	 * 
	 *描述：根据表名得到该表下的所有字段
	 *时间：2010-4-6
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<AppSystemField> getFieldsByConnAndTableName(AppSystemConn sysConn,String tableName){
		ArrayList<AppSystemField> resultList=new ArrayList<AppSystemField>();
		Set<AppSystemField> fieldSet=sysConn.getAppSystemFields();
		for(AppSystemField af:fieldSet){
			String sTableName=af.getVname();
			if(sTableName.equalsIgnoreCase(tableName)){
				resultList.add(af);
			}
		}
		return resultList;
	}
	
	public String processTableInfo(Element oTable, ArrayList<String> fieldList,
			ArrayList<String> tableList, ArrayList<AppSystemField> addFieldList,String sysId) {
		String sTableName = oTable.getAttribute("name").getValue();
		// 下面这段将所有涉及的表名放进tableList
		tableList.add(sTableName);

		String sTableCName = oTable.getAttribute("cname").getValue();
		

		AppSystemField table =null;
		if (table == null) {
			table = new AppSystemField();
			table.setVname(sTableName);
			table.setSystem(null);
		}
		table.setCvname(sTableCName.equals("") ? sTableName : sTableCName);

		try {
			XPath fieldPath = XPath.newInstance("//field[@checked='true']");
			List fields = fieldPath.selectNodes(oTable);
			for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
				Element oField = (Element) iterator.next();
				processFieldInfo(table, oField, fieldList,
						sTableName, addFieldList,sysId);
			}
		} catch (JDOMException e) {
			//e.printStackTrace();
		}

		return "ok";
	}
	
	
	private String processFieldInfo(AppSystemField table,
			Element oField, ArrayList<String> fieldList, String sTableName,
			ArrayList<AppSystemField> addFieldList,String sysId) {
		String sFieldName = oField.getAttribute("name").getValue();
		// 下面这段组装所有表_字段然后放进fieldList；
		String strFieldName = sTableName + "." + sFieldName;
		while (fieldList.contains(strFieldName)) {
			strFieldName = strFieldName + "_1";
		}
		fieldList.add(strFieldName);

		String sFieldCName = oField.getAttribute("cname").getValue();
		String sType = oField.getAttribute("type").getValue();
		AppSystemField field = null;
		if (field == null) {
			field = new AppSystemField();
			field.setVname(table.getVname());
			field.setFname(sFieldName);
			if(StringUtils.isNotEmpty(sysId)){
				//field.setSystem(system)
			}
			field.setCvname(table.getCvname());
			field.setFtype(sType);
		}
		field.setCfname(sFieldCName.equals("") ? sFieldName : sFieldCName);
		
		addFieldList.add(field);

		return "ok";
	}
	
	
	/**
	 * 功能说明:检查sql语句是否合法
	 * @author 童贝
	 * @version Apr 11, 2009
	 * @param sql
	 * @param systemId
	 * @return
	 * @throws Exception 
	 */
	public boolean isSql(String sql,String systemId) throws Exception{
		boolean bool=true;
		DynamicConnectionManager dcManager=null;
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		AppSystemConn system=null;
		String type="";
		try{
			if(StringUtils.isNotEmpty(systemId)){
				type="1";//其他系统
				String res=this.replaceSql(sql);
				if(res.equals("false")){
					return false;
				}
				sql=res;
				String hql="from AppSystemConn ac where ac.id='"+systemId+"'";
				List<AppSystemConn> list= this.find(hql);
				if(list.size()>0){
					system=list.get(0);
					dcManager=DynamicConnectionManager.getInstance();
					conn=dcManager.getDBConnection(system.getClassName(), system.getUrl(), system.getUserId());
					if(conn!=null){
						state=conn.createStatement();
					    rs=state.executeQuery(sql);
					    return true;
					}else{
						return false;
					}
				}
			}else{
				type="2"; //本地系统
				conn=this.dataSource.getConnection();
				state=conn.createStatement();
			    rs=state.executeQuery(sql);
			    return true;
			}
		}catch(Exception ex){
			return false;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(state!=null){
				state.close();
			}
			if("1".equals(type)){
				dcManager.freeDBConnection(conn,system.getUrl(),system.getUserId());
				dcManager.realse();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return bool;
	}
	
	
	/**
	 * 
	 *描述：查找是否有接口，并替换成接口
	 *时间：2010-4-6
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String replaceSql(String sql){
		String newSql=new String(sql);
		boolean bool=true;
		int star=newSql.indexOf("[");
		int end=newSql.indexOf("]");
		//有接口开始替换
		if(star>0 && end>0){
			String hql="from TjInterFace";
			List list=this.find(hql);
			if(list.size()>0){
				while(bool){
					String temp= newSql.substring(star+1, end);
					String tj=this.findInterFace(list, temp);
					if(tj.equals("false")){
						return "false";
					}
					newSql=newSql.substring(0,star)+tj+newSql.substring(end+1,newSql.length());
					//flag=end;
					star=newSql.indexOf("[");
					end=newSql.indexOf("]");
					if(star<0 && end<0){
						bool=false;
					}
				}
			}
		}
		return newSql;
	}
	
	
	public String findInterFace(List<TjInterFace> list,String inter){
		String result="0";
		boolean bool=false;
		Iterator<TjInterFace> it=list.iterator();
		while(it.hasNext()){
			TjInterFace interF=it.next();
			if(interF.getVname().equals(inter)){
				bool=true;
				break;
			}
		}
		if(!bool){
			result="false";
		}
		return result;
	}
	
	/**
	 * 
	 *描述：根据sql语句，创建接口页面
	 *时间：2010-4-6
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String createInterfaceBySql(String sql){
		String newSql=new String(sql);
		String result="";
		int star=newSql.indexOf("[");
		int end=newSql.indexOf("]");
		//有接口开始替换
		if(star>0 && end>0){
			String hql="from TjInterFace";
			List list=this.find(hql);
			if(list.size()>0){
				if(newSql.indexOf("[NIAN]")>-1){
					String interInfo=getInterfaceInfo(list,"NIAN");
					if(!interInfo.equals("")){
						result=result+interInfo;
						Date myDate=new Date();
						String starNian=DateUtils.getDate(myDate, "yyyy");
						String endNian=new Integer(new Integer(starNian)-10).toString();
						result=result+","+endNian+","+starNian+","+starNian+"!";
					}
				}
				if(newSql.indexOf("[SNIAN]")>-1){
					String interInfo=getInterfaceInfo(list,"SNIAN");
					if(!interInfo.equals("")){
						result=result+interInfo;
						Date myDate=new Date();
						String starNian=DateUtils.getDate(myDate, "yyyy");
						String endNian=new Integer(new Integer(starNian)-10).toString();
						result=result+","+endNian+","+starNian+","+starNian+"!";
					}
				}
				if(newSql.indexOf("[ENIAN]")>-1){
					String interInfo=getInterfaceInfo(list,"ENIAN");
					if(!interInfo.equals("")){
						result=result+interInfo;
						Date myDate=new Date();
						String starNian=DateUtils.getDate(myDate, "yyyy");
						String endNian=new Integer(new Integer(starNian)-10).toString();
						result=result+","+endNian+","+starNian+","+starNian+"!";
					}
				}
				if(newSql.indexOf("[YUE]")>-1){
					String interInfo=getInterfaceInfo(list,"YUE");
					if(!interInfo.equals("")){
						result=result+interInfo;
						Date myDate=new Date();
						String starMonth=DateUtils.getDate(myDate, "MM");
						if(starMonth.charAt(0)=='0'){
							starMonth=starMonth.substring(1);
						}
						result=result+",1,12,"+starMonth+"!";
					}
				}
				if(newSql.indexOf("[SYUE]")>-1){
					String interInfo=getInterfaceInfo(list,"SYUE");
					if(!interInfo.equals("")){
						result=result+interInfo;
						Date myDate=new Date();
						String starMonth=DateUtils.getDate(myDate, "MM");
						if(starMonth.charAt(0)=='0'){
							starMonth=starMonth.substring(1);
						}
						result=result+",1,12,"+starMonth+"!";
					}
				}
				if(newSql.indexOf("[EYUE]")>-1){
					String interInfo=getInterfaceInfo(list,"EYUE");
					if(!interInfo.equals("")){
						result=result+interInfo;
						Date myDate=new Date();
						String starMonth=DateUtils.getDate(myDate, "MM");
						if(starMonth.charAt(0)=='0'){
							starMonth=starMonth.substring(1);
						}
						result=result+",1,12,"+starMonth+"!";
					}
				}
				if(newSql.indexOf("[RI]")>-1){
					String interInfo=getInterfaceInfo(list,"RI");
					if(!interInfo.equals("")){
						result=result+interInfo;
						String starDay=DateUtils.getDate(new Date(), "dd");
						if(starDay.charAt(0)=='0'){
							starDay=starDay.substring(1);
						}
						String endDay=DateUtils.getLastDate(new Date());
						result=result+",1,"+endDay.split("-")[2]+","+starDay+"!";
					}
				}
				if(newSql.indexOf("[SRQ]")>-1){
					String interInfo=getInterfaceInfo(list,"SRQ");
					if(!interInfo.equals("")){
						result=result+interInfo;
						String starDay=DateUtils.getDate(new Date(), "dd");
						if(starDay.charAt(0)=='0'){
							starDay=starDay.substring(1);
						}
						String endDay=DateUtils.getLastDate(new Date());
						result=result+",1,"+endDay+","+starDay+"!";
					}
				}
				if(newSql.indexOf("[ERQ]")>-1){
					String interInfo=getInterfaceInfo(list,"ERQ");
					if(!interInfo.equals("")){
						result=result+interInfo;
						String starDay=DateUtils.getDate(new Date(), "dd");
						if(starDay.charAt(0)=='0'){
							starDay=starDay.substring(1);
						}
						String endDay=DateUtils.getLastDate(new Date());
						result=result+",1,"+endDay+","+starDay+"!";
					}
				}
				if(newSql.indexOf("[JZ]")>-1){
					String interInfo=getInterfaceInfo(list,"JZ");
					if(!interInfo.equals("")){
						result=result+getInterfaceInfo(list,"JZ")+","+this.getTjSjzdByiCode();
					}
				}
				
				if(result.lastIndexOf("!")==result.length()-1){
					result=result.substring(0,result.length()-1);
				}
			}
		}
		return result;
	}
	
	
	/**
	 * 功能说明:得到具体接口的详细信息
	 * @author 童贝
	 * @version May 21, 2009
	 * @param list
	 * @param inter
	 * @return
	 */
	public String getInterfaceInfo(List<TjInterFace> list,String inter){
		String result="";
		Iterator<TjInterFace> it=list.iterator();
		while(it.hasNext()){
			TjInterFace interF=it.next();
			if(interF.getVname().equals(inter)){
				result=interF.getVname();//+","+interF.getCvname();
				break;
			}
		}
		return result;
	}
	
	
	/**
	 * 功能说明:得到机组的起始和终止值
	 * @author 童贝
	 * @version Sep 2, 2009
	 * @return
	 */
	public String getTjSjzdByiCode(){
		String result="";
		DictCatalog catalog=this.findUniqueBy(DictCatalog.class, "uniqueName", "JZMC");
		if(catalog!=null){
			Set<Dict> dictSet=catalog.getDicts();
			for(Dict dict:dictSet){
				//最小机组后面的数字
				result=result+dict.getValue()+"-";
			}
			if(!result.equals("")){
				result=result.substring(0,result.length()-1);
			}
		}
		return result;
	}
	
	
	/**
	 * 功能说明:把带接口的SQL语句替换成真实的SQL
	 * @author 童贝
	 * @version May 22, 2009
	 * @param sql
	 * @param values
	 * @return
	 */
	public String replaceInterfaceSql(String sql,String values){
		String newSql=new String(sql);
		boolean bool=true;
		int star=newSql.indexOf("[");
		int end=newSql.indexOf("]");
		//有接口开始替换
		if(star>0 && end>0){
			while(bool){
				String temp= newSql.substring(star+1, end);
				String tj=this.findInterfaceByItem(temp, values);
				if(tj.equals("false")){
					return "false";
				}
				//按字段的类型来替换值
				tj=this.getInterfaceValueByName(temp, tj);
				
				newSql=newSql.substring(0,star)+tj+newSql.substring(end+1,newSql.length());
				//flag=end;
				star=newSql.indexOf("[");
				end=newSql.indexOf("]");
				if(star<0 || end<0){
					bool=false;
				}
			}
		}
		return newSql;
	}
	
	public String getInterfaceValueByName(String name,String value){
		TjInterFace face=this.findUniqueBy(TjInterFace.class, "vname", name);
		String vtype=face.getVtype();
		if(vtype.equals("1")){
			value="'"+value+"'";
		}else if(vtype.equals("3")){
			value="to_date('"+value+"','YYYY-MM-DD')";
		}
		return value;
	}
	
	
	/**
	 * 功能说明:查找具体接口的值
	 * @author 童贝
	 * @version May 22, 2009
	 * @param item
	 * @param values
	 * @return
	 */
	public String findInterfaceByItem(String item,String values){
		String[] array=values.split("!");
		for(int i=0;i<array.length;i++){
			String[] items=array[i].split(",");
			if(items[0].equals(item)){
				return items[1];
			}
		}
		return "false";
	}
	
	/**
	 * 
	 *描述：根据系统ID和sql语句查询列信息
	 *时间：2010-4-7
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws SQLException 
	 * @throws Exception 
	 */
	public List<String> findDynaColumnsBySql(String sql,String sysid) throws Exception{
		List<String> resultList=new ArrayList<String>();
		DynamicConnectionManager dcManager=null;
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		AppSystemConn system=null;
		String type="";//用来标识是其他系统还是本系统,获取连接的形式不同
		try{
			if(StringUtils.isNotEmpty(sysid)){
				type="1";
				String hql="from AppSystemConn ac where ac.id='"+sysid+"'";
				List<AppSystemConn> list=this.find(hql);
				if(list.size()>0){
					system=list.get(0);
					dcManager=DynamicConnectionManager.getInstance();
					conn=dcManager.getDBConnection(system.getClassName(), system.getUrl(), system.getUserId());
				}
			}else{
				type="2";
				conn=this.dataSource.getConnection();
			}
			
			if(conn!=null){
				state=conn.createStatement();
			    rs=state.executeQuery(sql);
			    //列信息
			    ResultSetMetaData metaData=rs.getMetaData();
			    int count=metaData.getColumnCount();
			    for(int i=1;i<=count;i++){
			    	String name=metaData.getColumnName(i);
			    	resultList.add(name);
			    }
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(state!=null){
				state.close();
			}
			if("1".equals(type)){
				dcManager.freeDBConnection(conn,system.getUrl(),system.getUserId());
				dcManager.realse();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return resultList;
	}
	
	
	/**
	 * 功能说明:把sql查询出来的结果转换成list<map>的形式返回
	 * @author 童贝
	 * @version Nov 30, 2009
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List querySqlData(String sql,String sysid) throws Exception {
		DynamicConnectionManager dcManager=null;
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		AppSystemConn system=null;
		List list=new ArrayList();
		String type=""; //标识是其他系统还是本系统，获取连接的方式不同
		try{
			if(StringUtils.isNotEmpty(sysid)){
				type="1";
				String hql="from AppSystemConn ac where ac.id='"+sysid+"'";
				List<AppSystemConn> systemList=this.find(hql);
				if(systemList.size()>0){
					system=systemList.get(0);
					dcManager=DynamicConnectionManager.getInstance();
					conn=dcManager.getDBConnection(system.getClassName(), system.getUrl(), system.getUserId());
				}
			}else{
				type="2";
				conn=this.dataSource.getConnection();
			}
			if(conn!=null){
				st=conn.createStatement();
				rs=st.executeQuery(sql);
				ResultSetMetaData rsmeta=rs.getMetaData();
				while(rs.next()){
					Map map=new LinkedHashMap();
					for(int i=1;i<=rsmeta.getColumnCount();i++){
						Object obj=rs.getObject(i);
						//针对oracle timestamp日期单独处理，转换成date
						if(obj instanceof oracle.sql.TIMESTAMP){
							Date dt =((oracle.sql.TIMESTAMP)obj).dateValue();
							obj = DateUtils.formatDate(dt, "yyyy-MM-dd HH:mm:ss");
						}
						if(obj instanceof java.sql.Date){
							Date dt = (java.sql.Date) obj;
							obj = DateUtils.formatDate(dt, "yyyy-MM-dd");
						}
						if(obj instanceof oracle.sql.BLOB||obj instanceof oracle.sql.CLOB){
							//|| obj instanceof weblogic.jdbc.wrapper.Clob || obj instanceof weblogic.jdbc.wrapper.Blob
							obj="";
						}
						map.put(rsmeta.getColumnName(i), obj);
					}
					list.add(map);
				}
			}
		}catch(Exception ex){
			throw ex;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(st!=null){
				st.close();
			}
			if("1".equals(type)){
				dcManager.freeDBConnection(conn,system.getUrl(),system.getUserId());
				dcManager.realse();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return list;
	}
	
	/**
	 * 
	 *描述：删除数据字段
	 *时间：2010-4-7
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void deleteTjAppSjzdByVname(String sysid,String vname){
		String whereSql="sid='"+sysid+"' and vname='"+vname+"'";
		this.removeBatchInHql(TjAppSjzd.class, whereSql);
	}
	
	/**
	 * 
	 *描述：根据系统ID和数据源名称得到所有的数据源字段信息
	 *时间：2010-4-12
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<TjAppSjzd> findTjAppSjzdBySysidAndVname(String sysid,String vname){
		String hql="from TjAppSjzd where sid='"+sysid+"' and vname='"+vname+"'";
		List<TjAppSjzd> sjzdList=this.find(hql);
		return sjzdList;
	}
	
	
	/**
	 * 功能说明: 提取SQL语句中的字段集合，适合于多系统
	 * @author 童贝
	 * @version May 22, 2009
	 * @param sql
	 * @param systemId
	 * @return
	 * @throws Exception 
	 */
	public List<AppSystemField> getAppSystemFieldBySqlAndSystemId(String sql,String systemId) throws Exception{
		List<AppSystemField> resultList=new ArrayList<AppSystemField>();
		DynamicConnectionManager dcManager=null;
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		AppSystemConn system=null;
		String type="";
		try{
			if(StringUtils.isNotEmpty(systemId)){
				type="1";
				String hql="from AppSystemConn ac where ac.id='"+systemId+"'";
				List<AppSystemConn> list= this.find(hql);
				if(list.size()>0){
					system=list.get(0);
					dcManager=DynamicConnectionManager.getInstance();
					conn=dcManager.getDBConnection(system.getClassName(), system.getUrl(), system.getUserId());
				}
			}else{
				type="2";
				conn=this.dataSource.getConnection();
			}
			if(conn!=null){
				state=conn.createStatement();
				rs=state.executeQuery(sql);
				ResultSetMetaData metaData=rs.getMetaData();
				int columnCount=metaData.getColumnCount();
				for(int i=1;i<=columnCount;i++){
					AppSystemField df=new AppSystemField();
					String columnName=metaData.getColumnName(i);
					String columnType=metaData.getColumnTypeName(i);
					String tableName=metaData.getTableName(1);
					
					//字段名
					df.setFname(columnName);
					df.setVname(tableName);
					df.setFtype(columnType);
					String tempNewCFname=this.getNewColumnCName(columnName);
					if(tempNewCFname!= null){
						df.setCfname(tempNewCFname);
					}else{
						df.setCfname(columnName);
					}
					resultList.add(df);
				}
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(state!=null){
				state.close();
			}
			if("1".equals(type)){
				dcManager.freeDBConnection(conn,system.getUrl(),system.getUserId());
				dcManager.realse();
			}
			if(conn!=null){
				conn.close();
			}
		}
		
		return resultList;
	}
	
	
	/**
	 * 功能说明:得到字段中文名
	 * @author 童贝
	 * @version May 15, 2009
	 * @param fieldName
	 * @return
	 */
	public String getNewColumnCName(String fieldName){
		String hql="from TjInfoItem xm where xm.item='"+fieldName+"'";
		List<TjInfoItem> list=this.find(hql);
		if(list.size()>0){
			TjInfoItem xm=list.get(0);
			return xm.getIname();
		}else{
			String keyHql="from TjItemKindKey where icode='"+fieldName+"'";
			List<TjItemKindKey> keyList=this.find(keyHql);
			if(keyList.size()>0){
				TjItemKindKey key=keyList.get(0);
				return key.getIname();
			}
		}
		return fieldName;
	}
	
	
	/**
	 * 功能说明:查找数据源是否在数据项中使用
	 * @author 童贝
	 * @version Apr 27, 2009
	 * @param vname
	 * @param fname
	 * @return
	 */
	public boolean findTjInfoItemByVnameAndSysid(String vname,String sysid){
		boolean bool=false;
		String hql="from TjInfoItem info where info.vname='"+vname+"' and info.sid='"+sysid+"'";
		List<TjInfoItem> list=this.find(hql);
		if(list.size()>0){
			bool=false;
		}else{
			bool=true;
		}
		//boolean bool=true;
		return bool;
	}


	public DataSource getDataSource() {
		return dataSource;
	}


	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
