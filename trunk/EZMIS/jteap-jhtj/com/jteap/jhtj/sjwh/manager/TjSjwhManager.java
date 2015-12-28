package com.jteap.jhtj.sjwh.manager;

import java.io.File;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;


import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.exceptions.NativeException;


import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.jhtj.jkbldy.model.TjInterFace;
import com.jteap.jhtj.ljydy.model.AppSystemConn;
import com.jteap.jhtj.sjflsz.manager.TjItemKindManager;
import com.jteap.jhtj.sjflsz.model.TjItemKind;
import com.jteap.jhtj.sjflsz.model.TjItemKindKey;
import com.jteap.jhtj.sjflsz.model.TjKndJZ;
import com.jteap.jhtj.sjwh.model.KeyModel;
import com.jteap.jhtj.sjxxxdy.model.TjInfoItem;
import com.jteap.jhtj.sjydy.manager.TjAppIOManager;
import com.jteap.jhtj.sjydy.model.TjAppIO;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.dict.model.DictCatalog;
@SuppressWarnings({ "unchecked", "serial" })
public class TjSjwhManager extends HibernateEntityDao<TjInfoItem> {
	private DataSource dataSource;
	private DataSource sisdataSource;
	private TjItemKindManager tjItemKindManager;
	private TjAppIOManager tjAppIOManager;
	/**
	 * 
	 *描述：查找关键字列表
	 *时间：2010-4-14
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<TjItemKindKey> findTjItemKeyByKid(String kid){
		List<TjItemKindKey> list=new ArrayList<TjItemKindKey>();
		String hql="from TjItemKindKey key where key.kid='"+kid+"' order by key.iorder";
		list=this.find(hql);
		return list;
	}
	
	
	/**
	 * 
	 *描述：查找普通字段列表
	 *时间：2010-4-14
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<TjInfoItem> findTjInfoItemByKid(String kid){
		List<TjInfoItem> list=new ArrayList<TjInfoItem>();
		String hql="from TjInfoItem info where info.kid='"+kid+"' and info.isvisible=1 order by info.iorder";
		list=this.find(hql);
		return list;
	}
	
	/**
	 * 
	 *描述：查找关键字对象
	 *时间：2010-4-19
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public TjItemKindKey findTjItemKindKeyByKidAndIcode(String kid,String icode){
		String hql="from TjItemKindKey where kid='"+kid+"' and icode='"+icode+"'";
		TjItemKindKey tjItemKindKey=(TjItemKindKey)this.findUniqueByHql(hql);
		return tjItemKindKey;
	}
	
	/**
	 * 
	 *描述：查找机组的ICODE
	 *时间：2010-4-19
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getTjKndJZNameById(String id){
		String name="";
		if(StringUtils.isNotEmpty(id)){
			TjKndJZ tjKndJZ=this.findUniqueBy(TjKndJZ.class,"id", id);
			if(tjKndJZ!=null){
				name=tjKndJZ.getJz();
			}
		}
		return name;
	}
	
	/**
	 * 
	 *描述：根据KID和机组返回最近的一条数据(2010-5-31修改，支持分页显示)
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public Page findDynaDataBykid(String kid,String jz,String[] keys,int start, int limit) throws SQLException{
		Page page=new Page();
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		//List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		String sql=null;
		try{
			StringBuffer where=new StringBuffer();
			for(String key:keys){
				String[] nameAndValue=key.split(",");
				if(nameAndValue.length==2){
					String name=nameAndValue[0];
					String value=nameAndValue[1];
					TjItemKindKey tjItemKindKey=this.findTjItemKindKeyByKidAndIcode(kid, name);
					int lx=tjItemKindKey.getLx().intValue();
					if(lx==1){
						where.append(name+"="+value+" and ");
					}else if(lx==2){
						where.append(name+"='"+value+"' and ");
					}else if(lx==3){
						where.append(name+"=to_date("+value+",'YYYY-MM-DD') and ");
					}
				}
			}
			if(!where.toString().equals("")){
				where.delete(where.lastIndexOf("and"),where.length()-1);
			}
			if(StringUtils.isEmpty(jz)){
				sql="select * from DATA_"+kid+" "+(where.toString().equals("")?"":"where "+where.toString()+" ")+" order by ";
			}else{
				sql="select * from DATA_"+kid+" where "+(where.toString().equals("")?"":where.toString()+" and ")+" jz='"+jz+"' order by ";
			}
			String order="";
			List<TjItemKindKey> keyList=this.findTjItemKeyByKid(kid);
			for(TjItemKindKey key:keyList){
				if(!key.getIcode().equalsIgnoreCase(TjInterFace.JZ)){
					//因为数组为空 经过split长度也会为1
					//参数格式NIAN,2011!YUE,08 经过split后 长度为2 所以这里判断就为>1 来判断是否查询显示
					if(keys.length>1){
						if(key.getIcode().equals("RI")){
							order=order+key.getIcode()+" asc,";
						}else{
							order=order+key.getIcode()+" desc,";
						}
					}else{
						order=order+key.getIcode()+" desc,";
					}
					
				}
			}
			if(!order.equals("")){
				order=order.substring(0,order.length()-1);
				sql=sql+order;
				//System.out.println(sql);
				page=this.pagedQueryTableData(sql, start, limit);
//				conn=this.dataSource.getConnection();
//				state=conn.createStatement();
//				rs=state.executeQuery(sql);
//				ResultSetMetaData rsmeta=rs.getMetaData();
//				while(rs.next()){
//					Map<String,String> map=new LinkedHashMap<String,String>();
//					for(int i=1;i<=rsmeta.getColumnCount();i++){
//						Object obj=rs.getObject(i);
//						map.put(rsmeta.getColumnName(i).toString(), (obj==null?"":obj.toString()));
//					}
//					result.add(map);
//					if(where.toString().equals("")){
//						break;
//					}
//				}
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
			if(conn!=null){
				conn.close();
			}
		}
		return page;
	}
	
	/**
	 * 
	 *描述：data表中是否存在数据
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public boolean isExistData(String kid,String flid){
		boolean res=false;
		String sql="";
		if(StringUtils.isNotEmpty(flid)){
			String jzVal=this.getTjKndJZNameById(flid);
			if(StringUtils.isNotEmpty(jzVal)){
				sql="select count(*) from " +"DATA_"+kid+" where jz='"+jzVal+"'";
			}else{
				sql="select count(*) from " +"DATA_"+kid;
			}
			List list=this.getSession().createSQLQuery(sql).list();
			if(list.size()>0){
				BigDecimal total=(BigDecimal)list.get(0);
				if(total.intValue()>0){
					res=true;
				}
			}
		}
		return res;
	}
	
	/**
	 * 
	 *描述：创建选择的条件字段
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String createSelectKey(List<String> keyList){
		String result="";
		for(String key:keyList){
			if(key.equalsIgnoreCase("NIAN")){
				Date myDate=new Date();
				String starNian=DateUtils.getDate(myDate, "yyyy");
				Date strdate=DateUtils.getPreDate(new Date());
				String starNian1=DateUtils.getDate(strdate, "yyyy");
				String endNian=new Integer(new Integer(starNian)-10).toString();
				result=result+"NIAN,"+endNian+","+starNian+","+starNian1+"!";
			}else if(key.equalsIgnoreCase("YUE")){
				Date strdate=DateUtils.getPreDate(new Date());
				String starMonth=DateUtils.getDate(strdate, "MM");
				if(starMonth.charAt(0)=='0'){
					starMonth=starMonth.substring(1);
				}
				result=result+"YUE,1,12,"+starMonth+"!";
			}else if(key.equalsIgnoreCase("RI")){
				Date strdate=DateUtils.getPreDate(new Date());
				String starDay=DateUtils.getDate(strdate, "dd");
				if(starDay.charAt(0)=='0'){
					starDay=starDay.substring(1);
				}
				String endDay=DateUtils.getLastDate(strdate);
				result=result+"RI,1,"+endDay.split("-")[2]+","+starDay+"!";
			}
		}
		if(result.lastIndexOf("!")==result.length()-1){
			result=result.substring(0,result.length()-1);
		}
		return result;
	}
	
	/**
	 * 
	 *描述：得到默认的日期时间
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getDefaultKeyData(Map<String,String> valueMap,List<String> keyList){
		StringBuffer result=new StringBuffer();
		String nian=null;
		String yue=null;
		String ri=null;
		for(String key:keyList){
			Object value=valueMap.get(key);
			if("NIAN".equalsIgnoreCase(key)){
				nian=value.toString();
			}else if("YUE".equalsIgnoreCase(key)){
				yue=value.toString();
			}else if("RI".equalsIgnoreCase(key)){
				ri=value.toString();
			}
		}
		if(StringUtils.isNotEmpty(nian)&&StringUtils.isNotEmpty(yue)&&StringUtils.isNotEmpty(ri)){
			String date=nian+"-"+yue+"-"+ri;
			Date curDate=DateUtils.StrToDate(date, "");
			String nextDateString=DateUtils.endDate(curDate);
			Date nextDate=DateUtils.StrToDate(nextDateString, "");
			result.append("NIAN,"+DateUtils.getDate(nextDate, "yyyy")+"!");
			yue=DateUtils.getDate(nextDate, "MM");
			result.append("YUE,"+(yue.charAt(0)=='0'?yue.substring(1):yue)+"!");
			ri=DateUtils.getDate(nextDate, "dd");
			result.append("RI,"+(ri.charAt(0)=='0'?ri.substring(1):ri));
		}else if(StringUtils.isNotEmpty(nian)&&StringUtils.isNotEmpty(yue)){
			String date=nian+"-"+yue+"-1";
			Date curDate=DateUtils.StrToDate(date, "");
			String nextDateString=DateUtils.getNextDate(curDate,1,"");
			Date nextDate=DateUtils.StrToDate(nextDateString, "");
			result.append("NIAN,"+DateUtils.getDate(nextDate, "yyyy")+"!");
			yue=DateUtils.getDate(nextDate, "MM");
			result.append("YUE,"+(yue.charAt(0)=='0'?yue.substring(1):yue));
		}else if(StringUtils.isNotEmpty(nian)){
			result.append("NIAN,"+(new Integer(nian)+1));
		}
		return result.toString();
	}
	
	/**
	 * 
	 *描述：得到年列表
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<KeyModel> getYearList(){
		List<KeyModel> yearList=new ArrayList<KeyModel>();
		String year=DateUtils.getTime("yyyy");
		Integer iyear=new Integer(year);
		for(int i=iyear;i>=iyear-10;i--){
			KeyModel key=new KeyModel();
			key.setDisplayValue(""+i);
			key.setValue(""+i);
			yearList.add(key);
		}
		return yearList;
	}
	
	/**
	 * 
	 *描述：得到月列表 
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<KeyModel> getMonthList(){
		List<KeyModel> monthList=new ArrayList<KeyModel>();
		for(int i=1;i<13;i++){
			KeyModel key=new KeyModel();
			key.setDisplayValue(""+i);
			key.setValue(""+i);
			monthList.add(key);
		}
		return monthList;
	}
	
	/**
	 * 
	 *描述：根据关键字信息得到日列表
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<KeyModel> getDayList(String keydata){
		List<KeyModel> dayList=new ArrayList<KeyModel>();
		String nian="";
		String yue="";
		String[] datas=keydata.split("!");
		for(int i=0;i<datas.length;i++){
			String[] keyValue=datas[i].split(",");
			if(keyValue[0].equalsIgnoreCase("NIAN")){
				nian=keyValue[1];
			}else if(keyValue[0].equalsIgnoreCase("YUE")){
				yue=keyValue[1];
			}
		}
		String lastDateString=DateUtils.getLastDate(nian+"-"+yue+"-1");
		Date lastDate=DateUtils.StrToDate(lastDateString, "");
		String lastri=DateUtils.getDate(lastDate,"dd");
		Integer ri=new Integer(lastri);
		for(int i=1;i<=ri;i++){
			KeyModel key=new KeyModel();
			key.setDisplayValue(i+"");
			key.setValue(i+"");
			dayList.add(key);
		}
		return dayList;
	}
	
	/**
	 * 
	 *描述：根据分类ID得到机组列表
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：kid 分类ID
	 *返回值:
	 *抛出异常：
	 */
	public List<KeyModel> getJzList(String kid){
		List<KeyModel> jzList=new ArrayList<KeyModel>();
		List<TjKndJZ> list=this.tjItemKindManager.getTjKndJZByKid(kid);
		for(TjKndJZ jz:list){
			KeyModel key=new KeyModel();
			key.setDisplayValue(jz.getJzname());
			key.setValue(jz.getJz());
			jzList.add(key);
		}
		return jzList;
	}
	
	/**
	 * 
	 *描述：初始化普通字段
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：kid 分类编码
	 *返回值:
	 *抛出异常：
	 */
	public String initFieldData(String kid){
		//存放结果
		StringBuffer result=new StringBuffer();
		//按业务类型查找
		DictCatalog catalog=this.findUniqueBy(DictCatalog.class,"uniqueName", "YWLX");
		if(catalog!=null){
			String hql="from Dict where catalog.id='"+catalog.getId()+"'";
			List<Dict> sjlxList=this.find(hql);
			if(sjlxList.size()>0){
				for(Dict dict:sjlxList){
					String sjlx=dict.getValue();
					String curHql="from TjInfoItem info where info.kid='"+kid+"' and info.sjlx='"+sjlx+"' and info.isvisible=1 order by info.iorder";
					List<TjInfoItem> itemList=this.find(curHql);
					if(itemList.size()>0){
						String sjlxCn=dict.getKey();
						String json=JSONUtil.listToJson(itemList,new String[]{"item","iname","dw","qsfs"});
						result.append("{sjlx:\""+sjlx+"\",sjlxCn:\""+sjlxCn+"\",data:["+json+"]},");
					}
				}
				if(!result.toString().equals("")){
					result.deleteCharAt(result.length()-1);
				}
			}
		}
		return result.toString();
	}
	
	
	/**
	 * 功能说明:删除临时表记录
	 * @author 童贝
	 * @version Apr 30, 2009
	 * @param kid
	 * @throws SQLException 
	 */
	public boolean deleteTempTable(String kid) throws SQLException{
		String sql="delete from TEMP_"+kid;
		this.operateDataBase(sql);
		return true;
	}
	
	
	/**
	 * 
	 *描述：把关键字信息加入到临时表中
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public boolean addTempTable(String kid,String... values) throws SQLException{
		String sql="insert into TEMP_"+kid;//+"(jz,nian,yue,ri) values('"+jz+"',"+nian+","+yue+","+ri+")";
		StringBuffer field=new StringBuffer();
		StringBuffer value=new StringBuffer();
		
		for(int i=0;i<values.length;i++){
			String[] item=values[i].split(",");
			String hql="from TjItemKindKey where kid='"+kid+"' and icode='"+item[0]+"'";
			TjItemKindKey key=(TjItemKindKey)this.findUniqueByHql(hql);
			field.append(item[0]+",");
			int lx=key.getLx().intValue();
			if(lx==1){
				value.append(item[1]+",");
			}else if(lx==2){
				value.append("'"+item[1]+"',");
			}else if(lx==3){
				value.append("to_date("+item[1]+",'yyyy-MM-dd'),");
			}
		}
		if(!field.toString().equals("")&&!value.toString().equals("")){
			field.deleteCharAt(field.length()-1);
			value.deleteCharAt(value.length()-1);
			sql=sql+"("+field.toString()+") values("+value.toString()+")";
			this.operateDataBase(sql);
			return true;
		}
		return false;
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
			System.out.println("操作表结构出错,出错sql:"+sql);
			this.logger.error("操作表结构出错,出错sql:"+sql);
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
	 * 
	 *描述：取数以前先把页面上面的值存储一遍
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws SQLException 
	 */
	public boolean beforeGetDataSave(String kid,String[] keyList,String[] fieldList,String tablePrx) throws SQLException{
		boolean result=true;
		//拼装条件字段
		StringBuffer keyWhere=new StringBuffer();
		for(String key:keyList){
			String[] nameAndValue=key.split(",");
			if(nameAndValue.length==2){
				String name=nameAndValue[0];
				String value=nameAndValue[1];
				TjItemKindKey tjItemKindKey=this.findTjItemKindKeyByKidAndIcode(kid, name);
				int lx=tjItemKindKey.getLx().intValue();
				if(lx==1){
					keyWhere.append(name+"="+value+" and ");
				}else if(lx==2){
					keyWhere.append(name+"='"+value+"' and ");
				}else if(lx==3){
					keyWhere.append(name+"="+"to_date("+value+",'yyyy-MM-dd') and ");
				}
			}
		}
		//拼装普通字段
		StringBuffer fields=new StringBuffer();
		for(String field:fieldList){
			String[] nameAndValue=field.split(",");
			if(nameAndValue.length==2){
				String name=nameAndValue[0];
				String value=nameAndValue[1];
				
				String hql="from TjInfoItem where kid='"+kid+"' and item='"+name+"'";
				TjInfoItem tjInfoItem=(TjInfoItem)this.findUniqueByHql(hql);
				String itype=tjInfoItem.getItype();
				if(TjInfoItem.ITYPE_VARCHAR.equals(itype)){
					fields.append(name+"='"+value+"',");
				}else{
					fields.append(name+"="+value+",");
				}
			}
		}
		
		if(!keyWhere.toString().equals("")&&!fields.toString().equals("")){
			keyWhere.delete(keyWhere.lastIndexOf("and"), keyWhere.length());
			fields.deleteCharAt(fields.length()-1);
			String sql="update "+tablePrx+kid+" set "+fields.toString()+" where "+keyWhere.toString();
			this.operateDataBase(sql);
		}else{
			result=false;
		}
		return result;
	}
	
	
	/**
	 * 
	 *描述：从其他系统中取数
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws Exception 
	 */
	public String getOtherSystem(String kid,String[] keyList) throws Exception{
		StringBuffer fields=new StringBuffer();
		StringBuffer result=new StringBuffer();
		String hql="select distinct tjInfoItem.sid,tjInfoItem.vname from TjInfoItem tjInfoItem where tjInfoItem.kid='"+kid+"' and tjInfoItem.isvisible=1 and tjInfoItem.qsfs="+new Long(2);
		List<Object[]> list=this.find(hql);
		//获取连接池
		//DynamicConnectionManager manager=DynamicConnectionManager.getInstance();
		for(Object[] sidAndVname:list){
			AppSystemConn appSystem=null;
			Connection conn=null;
			Statement statem=null;
			ResultSet rs=null;
			try{
				//sid,vname
				String sid=sidAndVname[0].toString();
				String vname=sidAndVname[1].toString();
				//根据系统和视图取出APPIO中的SQL语句
				String appIoHql="from TjAppIO io where io.sid='"+sid+"' and io.vname='"+vname+"'";
				TjAppIO appio=(TjAppIO)this.findUniqueByHql(appIoHql);
				//取出SQL语句
				String sql=appio.getSqlstr();
				//替换过接口的sql语句
				sql=this.replaceSql(sql, keyList);
				
				//取出该系统
				appSystem=this.get(AppSystemConn.class, sid);
				
				//根据sql查询
				
				//conn=manager.getDBConnection(appSystem.getClassName(), appSystem.getUrl(), appSystem.getUserId());
				conn=this.getCurrentConnection(appSystem.getClassName(), appSystem.getUrl(), appSystem.getUserId());
				statem=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs=statem.executeQuery(sql);
				
				//对查询结果进行处理
				String tjInfoHql="from TjInfoItem tjInfoItem where tjInfoItem.kid='"+kid+"' and tjInfoItem.isvisible=1 and tjInfoItem.qsfs="+new Long(2)+" and tjInfoItem.sid='"+sid+"' and tjInfoItem.vname='"+vname+"'";
				List<TjInfoItem> tjInfoList=this.find(tjInfoHql);
				for(TjInfoItem tjInfoItem:tjInfoList){
					if(!rs.isBeforeFirst()){
						rs.beforeFirst();
					}
					String value="";
					String itype=tjInfoItem.getItype();
					if(rs.next()){
						if(TjInfoItem.ITYPE_INT.equals(itype)){
							Integer tempRs1=rs.getInt(tjInfoItem.getFname());
							value=tempRs1.toString();
						}else if(TjInfoItem.ITYPE_FLOAT.equals(itype)){
							Double tempRs2=rs.getDouble(tjInfoItem.getFname());
							value=tempRs2.toString();
						}else if(TjInfoItem.ITYPE_VARCHAR.equals(itype)){
							String tempRs3=rs.getString(tjInfoItem.getFname());
							value=tempRs3;
						}else{
							Object tempRs4=rs.getObject(tjInfoItem.getFname());
							value=tempRs4.toString();
						}
						if(value.equals("")){
							value="0";
						}
						result=result.append("{"+tjInfoItem.getItem()+":'"+value+"'},");
						fields=fields.append(tjInfoItem.getItem()+",");
						//更新临时表
						this.updateTempField(itype, kid,tjInfoItem.getItem(), value);
					}
				}
				if(!fields.toString().equals("")){
					fields.deleteCharAt(fields.length()-1);
				}
				if(!result.toString().equals("")){
					result.deleteCharAt(result.length()-1);
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(rs!=null){
					rs.close();
				}
				if(statem!=null){
					statem.close();
				}
				//manager.freeDBConnection(conn,appSystem.getUrl(),appSystem.getUserId());
				//manager.realse();
				if(conn!=null){
					conn.close();
				}
			}
		}
		
		return "{fields:'"+fields.toString()+"',data:["+result.toString()+"]}";
	}
	
	/**
	 * 
	 *描述：把sql语句中的接口进行替换
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String replaceSql(String sql,String[] values){
		String newSql=new String(sql);
		boolean bool=true;
		int star=newSql.indexOf("[");
		int end=newSql.indexOf("]");
		//有接口开始替换
		if(star>0 && end>0){
			while(bool){
				String name= newSql.substring(star+1, end);
				String value=this.findInterfaceValueByItem(name, values);
				if(value.equals("")){
					return "";
				}
				//按字段的类型来替换值
				value=this.changeInterfaceValueByName(name, value);
				
				newSql=newSql.substring(0,star)+value+newSql.substring(end+1,newSql.length());
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
	
	/**
	 * 
	 *描述：按类型替换接口的值
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String changeInterfaceValueByName(String name,String value){
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
	public String findInterfaceValueByItem(String item,String[] values){
		for(int i=0;i<values.length;i++){
			String[] items=values[i].split(",");
			if(items[0].equals(item)){
				return items[1];
			}
		}
		return "false";
	}
	
	
	/**
	 * 功能说明:更新表字段
	 * @author 童贝
	 * @version Apr 30, 2009
	 * @param type 字段类型
	 * @param kid 分类ID
	 * @param content 内容
	 * @param field 字段名
	 * @throws SQLException 
	 */
	public void updateTempField(String itype,String kid,String field,String content) throws SQLException{
		String sql="";
		if(TjInfoItem.ITYPE_VARCHAR.equals(itype)){
			sql="update TEMP_"+kid+" set "+field+"='"+content+"'";
		}else{
			sql="update TEMP_"+kid+" set "+field+"="+content;
		}
		this.operateDataBase(sql);
	}
	
	
	
	/**
	 * 
	 *描述：从DLL取数
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public boolean executeDllData(String kid,String excelPath,String[] values) throws NativeException{
		Map<String,String> map=this.keyStringToMap(values);
		JNative jnative=null;
		String tName="";
		try {
			String hql="from TjInfoItem tjInfoItem where tjInfoItem.qsfs="+new Long(4)+"and tjInfoItem.isvisible=1 and tjInfoItem.kid='"+kid+"' order by tjInfoItem.dfunId";
			List<TjInfoItem> list=this.find(hql);
			if(list.size()>0){
				JNative.setLoggingEnabled(true);
				TjInfoItem tjInfoItem=list.get(0);
				if(JNative.isWindows()){
					//从资源文件里面读数据
					String username=SystemConfig.getProperty("dll.username");
					String password=SystemConfig.getProperty("dll.password");
					String dataSource=SystemConfig.getProperty("dll.DataSource");
					//String dllpath=SystemConfig.getProperty("dll.dllpath");
					
					//调用内存中的DLL
					tName=tjInfoItem.getDname();
					jnative=new JNative(excelPath+File.separatorChar+"LIB"+File.separatorChar+tjInfoItem.getDname()+".dll","ExecDLL");
					tName=excelPath+File.separatorChar+"LIB"+File.separatorChar+tjInfoItem.getDname()+".dll";					
					jnative.setParameter(0,org.xvolks.jnative.Type.STRING,kid);
					
					String jz=map.get("JZ");
					if(jz==null||jz.equals("")){
						jz="U0";
					}
					String nian=map.get("NIAN");
					if(nian==null||nian.equals("")){
						nian="0";
					}
					
					String yue=map.get("YUE");
					if(yue==null||yue.equals("")){
						yue="0";
					}
					
					String ri=map.get("RI");
					if(ri==null||ri.equals("")){
						ri="0";
					}

					jnative.setParameter(1,org.xvolks.jnative.Type.STRING,excelPath);
					jnative.setParameter(2, org.xvolks.jnative.Type.STRING,"Provider=MSDAORA.1;Password="+password+";User ID="+username+";Data Source="+dataSource+";Persist Security Info=True");
					jnative.setParameter(3,org.xvolks.jnative.Type.STRING,jz);
					jnative.setParameter(4,org.xvolks.jnative.Type.STRING, "0");
					jnative.setParameter(5,org.xvolks.jnative.Type.STRING,nian);
					jnative.setParameter(6,org.xvolks.jnative.Type.STRING, yue);
					jnative.setParameter(7,org.xvolks.jnative.Type.STRING, ri);
					//1,ri,2,yue,3,nian
					String flag=this.findFLTypeByKid(kid);
					jnative.setParameter(8,org.xvolks.jnative.Type.STRING, flag);
					jnative.invoke();
				}else if(JNative.isLinux()){
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JNative.unLoadLibrary(tName);
        }
		return true;
	}
	
	
	public Map<String, String> keyStringToMap(String[] keyList){
		Map<String,String> map=new HashMap<String, String>();
		for(int i=0;i<keyList.length;i++){
			String[] item=keyList[i].split(",");
			map.put(item[0], item[1]);
		}
		return map;
	}
	
	/**
	 * 功能说明:查看该分类编码是日数据还是月数据还是年数据
	 * @author 童贝
	 * @version Jun 12, 2009
	 * @param kid
	 * @return
	 */
	public String findFLTypeByKid(String kid){
		String result="";
		String hql="from TjItemKind itemkind where itemkind.kid='"+kid+"'";
		List<TjItemKind> list=this.find(hql);
		if(list.size()>0){
			TjItemKind itemKind=list.get(0);
			Long flag=itemKind.getFlflag();
			result=new Integer(flag.intValue()).toString();
		}
		return result;
	}
	
	/**
	 * 功能说明:以计算的方式取数(调用存储过程)
	 * @author 童贝
	 * @version Apr 30, 2009
	 * @param kid
	 * @throws SQLException 
	 */
	public boolean executeProcData(String kid) throws SQLException{
		String procName=kid+"_PROCEDURE";
		return this.operateProcDataBase("{call "+procName+"()}");
	}
	
	
	/**
	 * 功能说明:调用不带参数的存储过程
	 * @author 童贝
	 * @version Apr 30, 2009
	 * @param sql
	 * @throws SQLException 
	 */
	public boolean operateProcDataBase(String sql) throws SQLException{
		boolean bool=true;
		Connection conn=null;
		CallableStatement statement=null;
		try {
			conn=this.dataSource.getConnection();
			statement=conn.prepareCall(sql);
			statement.executeUpdate();
		} catch (SQLException e) {
			bool=false;
		}finally{
			if(statement!=null){
				statement.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return bool;
	}
	
	/**
	 * 
	 *描述：取得临时表的记录,只取fields里的字段
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws SQLException 
	 */
	public String findTempTableData(String kid,String[] fields) throws SQLException{
		StringBuffer dataList=new StringBuffer();
		StringBuffer keyList=new StringBuffer();
		//要取值的字段
		for(String field:fields){
			String[] nameAndValue=field.split(",");
			String name=nameAndValue[0];
			keyList.append(name+",");
		}
		if(!keyList.toString().equals("")){
			keyList.deleteCharAt(keyList.length()-1);
		}
		String sql="select "+keyList.toString()+" from TEMP_"+kid;
		Connection conn=null;
		Statement statem=null;
		ResultSet rs=null;
		try{
			conn=this.dataSource.getConnection();
			statem=conn.createStatement();
			rs=statem.executeQuery(sql);
			if(rs.next()){
				String[] keys=keyList.toString().split(",");
				for(String key:keys){
					Object value=rs.getObject(key);
					dataList.append("{"+key+":'"+(value==null?"":value)+"'},");
				}
			}
		}catch(Exception e){
			
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(statem!=null){
				statem.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		if(!dataList.toString().equals("")){
			dataList.deleteCharAt(dataList.length()-1);
		}
		return "{success:true,fields:'"+keyList.toString()+"',list:[["+dataList.toString()+"]]}";
	}
	
	/**
	 * 
	 *描述：保存或更新数据
	 *时间：2010-4-17
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public boolean saveOrUpdateData(String kid,String[] keyList,String[] fieldList) throws SQLException{
		boolean result=true;
		StringBuffer names=new StringBuffer();
		StringBuffer values=new StringBuffer();
		StringBuffer where=new StringBuffer();
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		try{
			//组装关键字
			for(String key:keyList){
				String[] nameAndValue=key.split(",");
				if(nameAndValue.length==2){
					String name=nameAndValue[0];
					String value=nameAndValue[1];
					names.append(name+",");
					TjItemKindKey tjItemKindKey=this.findTjItemKindKeyByKidAndIcode(kid, name);
					int lx=tjItemKindKey.getLx().intValue();
					if(lx==1){
						values.append(value+",");
						where.append(name+"="+value+" and ");
					}else if(lx==2){
						values.append("'"+value+"',");
						where.append(name+"='"+value+"' and ");
					}else if(lx==3){
						values.append("to_date("+value+",'YYYY-MM-DD'),");
						where.append(name+"=to_date("+value+",'YYYY-MM-DD') and ");
					}
				}
			}
			//判断查询条件
			if(!where.toString().equals("")){
				where.delete(where.lastIndexOf("and"),where.length()-1);
			}
			//查看是否有数据,有数据就是更新,没数据就增加
			String sql="select * from DATA_"+kid+" where "+where.toString();
			conn=this.dataSource.getConnection();
			state=conn.createStatement();
			rs=state.executeQuery(sql);
			if(!rs.next()){
				for(String field:fieldList){
					String[] nameAndValue=field.split(",");
					if(nameAndValue.length==2){
						String name=nameAndValue[0];
						String value=nameAndValue[1];
						names.append(name+",");
						String hql="from TjInfoItem where kid='"+kid+"' and item='"+name+"'";
						TjInfoItem tjInfoItem=(TjInfoItem)this.findUniqueByHql(hql);
						String itype=tjInfoItem.getItype();
						if(TjInfoItem.ITYPE_VARCHAR.equals(itype)){
							values.append("'"+value+"',");
						}else{
							values.append(value+",");
						}
					}
				}
				
				if(!names.toString().equals("")){
					names.deleteCharAt(names.length()-1);
				}
				if(!values.toString().equals("")){
					values.deleteCharAt(values.length()-1);
				}
				
				String insert="insert into DATA_"+kid+"("+names.toString()+") values("+values.toString()+")";
				this.operateDataBase(insert);
			}else{
				result = this.beforeGetDataSave(kid, keyList, fieldList,"DATA_");
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
			if(conn!=null){
				conn.close();
			}
		}
		return result;
	}
	
	
	/**
	 * 
	 *描述：根据关键字查找数据
	 *时间：2010-4-19
	 *作者：童贝
	 *参数：
	 *返回值:keyList的每一项的值的格式为(数据项编码,值)
	 *抛出异常：
	 */
	public String findDynaDataBykidAndKeys(String kid,String[] keyList) throws SQLException{
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		Map<String,String> keyMap=new HashMap<String, String>();
		StringBuffer result=new StringBuffer();
		StringBuffer where=new StringBuffer();
		try{
			for(String key:keyList){
				String[] nameAndValue=key.split(",");
				if(nameAndValue.length==2){
					String name=nameAndValue[0];
					String value=nameAndValue[1];
					keyMap.put(name, value);
					TjItemKindKey tjItemKindKey=this.findTjItemKindKeyByKidAndIcode(kid, name);
					int lx=tjItemKindKey.getLx().intValue();
					if(lx==1){
						where.append(name+"="+value+" and ");
					}else if(lx==2){
						where.append(name+"='"+value+"' and ");
					}else if(lx==3){
						where.append(name+"=to_date("+value+",'YYYY-MM-DD') and ");
					}
				}
			}
			//判断查询条件
			if(!where.toString().equals("")){
				where.delete(where.lastIndexOf("and"),where.length()-1);
			
				String sql="select * from DATA_"+kid+" where "+where.toString();
				conn=this.dataSource.getConnection();
				state=conn.createStatement();
				rs=state.executeQuery(sql);
				ResultSetMetaData rsmeta=rs.getMetaData();
				if(rs.next()){
					//Map<String,String> map=new LinkedHashMap<String,String>();
					for(int i=1;i<=rsmeta.getColumnCount();i++){
						Object obj=rs.getObject(i);
						String columnName=rsmeta.getColumnName(i).toString();
						if(keyMap.get(columnName)==null){
							result.append("{\""+columnName+"\":\""+(obj==null?"":obj.toString())+"\"},");
						}
					}
				}
			}
			if(!result.toString().equals("")){
				result.deleteCharAt(result.length()-1);
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
			if(conn!=null){
				conn.close();
			}
		}
		return result.toString();
	}
	
	
	/**
	 * 分页查询指定sql数据
	 * @param sql
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Page pagedQueryTableData(String sql, int start, int limit) throws Exception {
		
		String countSql = " select count (*) " + removeSelect(removeOrders(sql));
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		try{
			conn=this.dataSource.getConnection();
			st=conn.createStatement();
			rs=st.executeQuery(countSql);
			int count=0;
			if(rs.next()){
				count=rs.getInt(1);
			}
			rs.close();
			
			if(count<1)
				return new Page();

			sql="SELECT * FROM(SELECT A.*, ROWNUM RN FROM ("+sql+") A WHERE ROWNUM <= "+(start+limit)+") WHERE RN >= "+(start+1);
			
			rs=st.executeQuery(sql);
			List list=new ArrayList();
			ResultSetMetaData rsmeta=rs.getMetaData();
			
			while(rs.next()){
				Map map=new HashMap();  
				for(int i=1;i<=rsmeta.getColumnCount();i++){
					Object obj=rs.getObject(i);
					//针对oracle timestamp日期单独处理，转换成date
					if(obj instanceof oracle.sql.TIMESTAMP){
						obj=((oracle.sql.TIMESTAMP)obj).dateValue().getTime();
					}
					
					map.put(rsmeta.getColumnName(i), obj);
				}
				list.add(map);
			}
			rs.close();
			return new Page(start, count, limit, list);
		}catch(Exception ex){
			throw ex;
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(st!=null){
				st.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		
	}
	
	
	/**
	 * 去除hql的select 子句，未考虑union的情况,用于pagedQuery.
	 *
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	private static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除hql的orderby 子句，用于pagedQuery.
	 *
	 * @see #pagedQuery(String,int,int,Object[])
	 */
	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * 获取当前链接
	 * @param className
	 * @param url
	 * @param userId
	 * @return
	 * @throws SQLException 
	 * @throws SQLException
	 */
	private Connection getCurrentConnection(String className,String url,String userId) throws SQLException{
		//System.out.println("生产链接开始");
		Connection conn=null;
		Connection sisConn=null;
		int flag=0;
		try {
			conn=this.dataSource.getConnection();
			DatabaseMetaData dataSet=conn.getMetaData();
			//System.out.println(dataSet.getURL());
			//System.out.println(dataSet.getUserName());
			if(url.equalsIgnoreCase(dataSet.getURL())&&userId.equalsIgnoreCase(dataSet.getUserName())){
				flag=1;
				return conn;
			}else{
				sisConn=this.sisdataSource.getConnection();
				DatabaseMetaData sisdataSet=sisConn.getMetaData();
				//System.out.println(sisdataSet.getURL());
				//System.out.println(sisdataSet.getUserName());
				if(url.equalsIgnoreCase(sisdataSet.getURL())&&userId.equalsIgnoreCase(sisdataSet.getUserName())){
					flag=2;
					return sisConn;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(flag==0){
				if(conn!=null){
					conn.close();
				}
				if(sisConn!=null){
					sisConn.close();
				}
			}else if(flag==1){
				if(sisConn!=null){
					sisConn.close();
				}
			}else if(flag==2){
				if(conn!=null){
					conn.close();
				}
			}
		}
		//System.out.println("生产链接结束");
		return null;
	}


	public DataSource getDataSource() {
		return dataSource;
	}


	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public TjItemKindManager getTjItemKindManager() {
		return tjItemKindManager;
	}


	public void setTjItemKindManager(TjItemKindManager tjItemKindManager) {
		this.tjItemKindManager = tjItemKindManager;
	}


	public TjAppIOManager getTjAppIOManager() {
		return tjAppIOManager;
	}


	public void setTjAppIOManager(TjAppIOManager tjAppIOManager) {
		this.tjAppIOManager = tjAppIOManager;
	}


	public DataSource getSisdataSource() {
		return sisdataSource;
	}


	public void setSisdataSource(DataSource sisdataSource) {
		this.sisdataSource = sisdataSource;
	}
	
}
