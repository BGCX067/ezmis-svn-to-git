package com.jteap.jhtj.bbzz.manager;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.DateUtils;
import com.jteap.jhtj.bbsj.model.BbSjTs;
import com.jteap.jhtj.bbsjydy.model.BbIO;
import com.jteap.jhtj.bbzz.model.Bbzz;
import com.jteap.jhtj.jkbldy.model.TjInterFace;
import com.jteap.jhtj.sjydy.manager.TjAppIOManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.system.dict.model.DictCatalog;
@SuppressWarnings({ "unchecked", "serial" })
public class BbzzManager extends HibernateEntityDao<Bbzz> {
	private TjAppIOManager tjAppIOManager;
	private DataSource dataSource;
	/**
	 * 
	 *描述：查找sql语句中的接口
	 *时间：2010-4-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public void findInterfaceBySql(Map<String,String> ifMap,String sql){
		boolean bool=true;
		int star=sql.indexOf("[");
		int end=sql.indexOf("]");
		//有接口开始替换
		if(star>0 && end>0){
			while(bool){
				String iface= sql.substring(star+1, end);
				ifMap.put(iface, iface);
				sql=sql.substring(0,star)+iface+sql.substring(end+1,sql.length());
				star=sql.indexOf("[");
				end=sql.indexOf("]");
				if(star<0 && end<0){
					bool=false;
				}
			}
		}
	}
	
	/**
	 * 
	 *描述：把接口进行转换
	 *时间：2010-4-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String InterfaceTransform(Map<String,String> ifMap){
		String result="";
		for(Iterator<String> it=ifMap.keySet().iterator();it.hasNext();){
			String key=it.next();
			if(key.equalsIgnoreCase(TjInterFace.NIAN)){
				result=result+key;
				Date myDate=new Date();
				String starNian=DateUtils.getDate(myDate, "yyyy");
				String endNian=new Integer(new Integer(starNian)-10).toString();
				result=result+","+endNian+","+starNian+","+starNian+"!";
			}
			if(key.equalsIgnoreCase(TjInterFace.SNIAN)){
				result=result+key;
				Date myDate=new Date();
				String starNian=DateUtils.getDate(myDate, "yyyy");
				String endNian=new Integer(new Integer(starNian)-10).toString();
				result=result+","+endNian+","+starNian+","+starNian+"!";
			}
			if(key.equalsIgnoreCase(TjInterFace.ENIAN)){
				result=result+key;
				Date myDate=new Date();
				String starNian=DateUtils.getDate(myDate, "yyyy");
				String endNian=new Integer(new Integer(starNian)-10).toString();
				result=result+","+endNian+","+starNian+","+starNian+"!";
			}
			if(key.equalsIgnoreCase(TjInterFace.YUE)){
				result=result+key;
				Date myDate=new Date();
				String starMonth=DateUtils.getDate(myDate, "MM");
				if(starMonth.charAt(0)=='0'){
					starMonth=starMonth.substring(1);
				}
				result=result+",1,12,"+starMonth+"!";
			}
			if(key.equalsIgnoreCase(TjInterFace.SYUE)){
				result=result+key;
				Date myDate=new Date();
				String starMonth=DateUtils.getDate(myDate, "MM");
				if(starMonth.charAt(0)=='0'){
					starMonth=starMonth.substring(1);
				}
				result=result+",1,12,"+starMonth+"!";
			}
			if(key.equalsIgnoreCase(TjInterFace.EYUE)){
				result=result+key;
				Date myDate=new Date();
				String starMonth=DateUtils.getDate(myDate, "MM");
				if(starMonth.charAt(0)=='0'){
					starMonth=starMonth.substring(1);
				}
				result=result+",1,12,"+starMonth+"!";
			}
			if(key.equalsIgnoreCase(TjInterFace.RI)){
				result=result+key;
				String starDay=DateUtils.getDate(new Date(), "dd");
				if(starDay.charAt(0)=='0'){
					starDay=starDay.substring(1);
				}
				String endDay=DateUtils.getLastDate(new Date());
				result=result+",1,"+endDay.split("-")[2]+","+starDay+"!";
			}
			if(key.equalsIgnoreCase(TjInterFace.SRQ)){
				result=result+key;
				String starDay=DateUtils.getDate(new Date(), "dd");
				if(starDay.charAt(0)=='0'){
					starDay=starDay.substring(1);
				}
				String endDay=DateUtils.getLastDate(new Date());
				result=result+",1,"+endDay+","+starDay+"!";
			}
			if(key.equalsIgnoreCase(TjInterFace.ERQ)){
				result=result+key;
				String starDay=DateUtils.getDate(new Date(), "dd");
				if(starDay.charAt(0)=='0'){
					starDay=starDay.substring(1);
				}
				String endDay=DateUtils.getLastDate(new Date());
				result=result+",1,"+endDay+","+starDay+"!";
			}
			if(key.equalsIgnoreCase(TjInterFace.JZ)){
				result=result+key+","+this.getTjSjzdByiCode()+"!";
			}
		}
		if(result.lastIndexOf("!")==result.length()-1){
			result=result.substring(0,result.length()-1);
		}
		return result;
	}
	
	/**
	 * 
	 *描述：得到机组的起始和终止值
	 *时间：2010-4-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getTjSjzdByiCode(){
		String result="";
		DictCatalog catalog=this.findUniqueBy(DictCatalog.class, "uniqueName", "JZMC");
		if(catalog!=null){
			Set<Dict> dictSet=catalog.getDicts();
			for(Dict dict:dictSet){
				result=result+dict.getValue()+"-";
			}
			if(!result.equals("")){
				result=result.substring(0,result.length()-1);
			}
		}
		return result;
	}
	
	/**
	 * 
	 *描述：得到模板定义的数据源列表
	 *时间：2010-4-28
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<BbIO> getBbioListByXml(String xml) throws JDOMException, IOException{
		List<BbIO> ioList=new ArrayList<BbIO>();
		Map<String,String> ioidMap=new HashMap<String, String>();
		SAXBuilder builder = new SAXBuilder();
		StringReader sin = new StringReader(xml);
		Document doc = builder.build(sin);
		XPath diPath = XPath.newInstance("//root//sheet//di");
		List<Element> elements = diPath.selectNodes(doc);
		//过滤重复的ioid
		for(Element element:elements){
			String bbioid=element.getAttribute("bbioid").getValue();
			ioidMap.put(bbioid, bbioid);
		}
		for(Iterator<String> it=ioidMap.keySet().iterator();it.hasNext();){
			String bbioid=it.next();
			BbIO io=this.findUniqueBy(BbIO.class, "id", bbioid);
			if(null!=io){
				ioList.add(io);
			}
		}
		return ioList;
	}
	
	/**
	 * 
	 *描述：报表制作
	 *时间：2010-4-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws SQLException 
	 */
	public String generateBb(String bbindexid,String ifvalue) throws JDOMException, IOException, SQLException{
		StringBuffer result=new StringBuffer();
		BbSjTs ts=this.findUniqueBy(BbSjTs.class, "bbindexid", bbindexid);
		String xml=ts.getExcelDataItemXml();
		List<BbIO> ioList=this.getBbioListByXml(xml);
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		String sql =null;
		try{
			conn=this.dataSource.getConnection();
			for(BbIO io:ioList){
				//查询sql语句的结果
				sql=io.getSqlstr();
				sql=this.tjAppIOManager.replaceInterfaceSql(sql, ifvalue);
				state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs=state.executeQuery(sql);
				//遍历报表模板中定义的字段
				SAXBuilder builder = new SAXBuilder();
				StringReader sin = new StringReader(xml);
				Document doc = builder.build(sin);
				XPath diPath = XPath.newInstance("//root//sheet//di");
				List<Element> elements = diPath.selectNodes(doc);
				for(Element element:elements){
					String bbioid=element.getAttribute("bbioid").getValue();
					String vname=element.getAttribute("vname").getValue();
					String fname=element.getAttribute("fname").getValue();
					String ce=element.getAttribute("ce").getValue();
					String fx=element.getAttribute("fx").getValue();
					String dnum=element.getAttribute("dnum").getValue();
					String merge=element.getAttribute("merge").getValue();
					//如果是当前数据源就取得该字段的值
					if(io.getId().equals(bbioid)){
						//System.out.println(sql+"-------");
						StringBuffer values=new StringBuffer();
						if(!rs.isBeforeFirst()){
							rs.beforeFirst();
						}
						while(rs.next()){
							Object value=rs.getObject(fname);
							if(value!=null){
								values.append(value.toString()+",");
							}else{
								values.append(value+"0,");
							}
						}
						if(!values.toString().equals("")){
							values.deleteCharAt(values.length()-1);
						}
						result.append("{bbioid:'"+bbioid+"',vname:'"+vname+"',fname:'"+fname+"',ce:'"+ce+"',fx:'"+fx+"',dnum:'"+dnum+"',merge:'"+merge+"',value:'"+values.toString()+"'},");
					}
				}
				
				if(rs!=null){
					rs.close();
				}
				if(state!=null){
					state.close();
				}
			}
			if(!result.toString().equals("")){
				result.deleteCharAt(result.length()-1);
			}
		}catch(Exception e){
			System.err.println(sql);
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
	 * 
	 *描述：报表制作
	 *时间：2010-4-27
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws SQLException 
	 */
	public Map<String,Double> generateBbByZhcx(String bbindexid,String ifvalue) throws JDOMException, IOException, SQLException{
		Map<String,Double> dataMap = new HashMap<String, Double>();
		BbSjTs ts=this.findUniqueBy(BbSjTs.class, "bbindexid", bbindexid);
		String xml=ts.getExcelDataItemXml();
		List<BbIO> ioList=this.getBbioListByXml(xml);
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		String sql =null;
		try{
			conn=this.dataSource.getConnection();
			for(BbIO io:ioList){
				//查询sql语句的结果
				sql=io.getSqlstr();
				sql=this.tjAppIOManager.replaceInterfaceSql(sql, ifvalue);
				state=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs=state.executeQuery(sql);
				//遍历报表模板中定义的字段
				SAXBuilder builder = new SAXBuilder();
				StringReader sin = new StringReader(xml);
				Document doc = builder.build(sin);
				XPath diPath = XPath.newInstance("//root//sheet//di");
				List<Element> elements = diPath.selectNodes(doc);
				for(Element element:elements){
					String bbioid=element.getAttribute("bbioid").getValue();
					String vname=element.getAttribute("vname").getValue();
					String fname=element.getAttribute("fname").getValue();
					//如果是当前数据源就取得该字段的值
					if(io.getId().equals(bbioid)){
						//System.out.println(sql+"-------");
						StringBuffer values=new StringBuffer();
						if(!rs.isBeforeFirst()){
							rs.beforeFirst();
						}
						while(rs.next()){
							Object value=rs.getObject(fname);
							if(value!=null){
								values.append(value.toString()+",");
							}else{
								values.append(value+"0,");
							}
						}
						if(!values.toString().equals("")){
							values.deleteCharAt(values.length()-1);
						}
						dataMap.put(vname+"."+fname, Double.valueOf(values.toString()));
					}
				}
				
				if(rs!=null){
					rs.close();
				}
				if(state!=null){
					state.close();
				}
			}
		}catch(Exception e){
			System.err.println(sql);
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
		
		return dataMap;
	}
	
	/**
	 * 
	 *描述：
	 *时间：2010-4-28
	 *作者：童贝
	 *参数：sqlValue 格式为:"NIAN,2010!YUE,3"
	 *返回值:
	 *抛出异常：
	 */
	public String getInterfaceValue(String sqlValue){
		StringBuffer result=new StringBuffer();
		Map<String, String> map=new HashMap<String, String>();
		if(StringUtils.isNotEmpty(sqlValue)){
			String[] keys=sqlValue.split("!");
			for(String key:keys){
				String[] nameAndValue=key.split(",");
				if(nameAndValue.length==2){
					map.put(nameAndValue[0], nameAndValue[1]);
				}
			}
		}
		//保证接口的顺序
		List<TjInterFace> faces=this.getInterfaceList();
		for(TjInterFace tjif:faces){
			String value=map.get(tjif.getVname());
			if(value!=null){
				result.append(value+"_");
			}
		}
		if(!result.toString().equals("")){
			result.deleteCharAt(result.length()-1);
		}
		return result.toString();
	}
	
	/**
	 * 
	 *描述：得到接口的列表
	 *时间：2010-4-28
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public List<TjInterFace> getInterfaceList(){
		List<TjInterFace> faces=new ArrayList<TjInterFace>();
		String hql="from TjInterFace order by vorder";
		faces=this.find(hql);
		return faces;
	}
	
	/**
	 * 
	 *描述：查看报表内容是否存在
	 *时间：2010-4-28
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public boolean findBbzzIsExistByBbindexidAndKey(String bbindexid,String key){
		boolean res=true;
		String hql="from Bbzz where bbindexid='"+bbindexid+"' and key='"+key+"'";
		Bbzz bbzz=(Bbzz)this.findUniqueByHql(hql);
		if(bbzz!=null){
			res=false;
		}
		return res;
	}
	
	/**
	 * 查看报表是否生成
	 * @param bbindexid
	 * @param key
	 * @return
	 */
	public boolean findBbzzIsStateByBbindexidAndKey(String bbindexid,String key){
		boolean res=true;
		String hql="from Bbzz where bbindexid='"+bbindexid+"' and key='"+key+"' and  status='1'";
		Bbzz bbzz=(Bbzz)this.findUniqueByHql(hql);
		if(bbzz!=null){
			res=false;
		}
		return res;
	}
	

	public TjAppIOManager getTjAppIOManager() {
		return tjAppIOManager;
	}

	public void setTjAppIOManager(TjAppIOManager tjAppIOManager) {
		this.tjAppIOManager = tjAppIOManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * 
	 *描述：取得最新的排序号
	 *时间：2010-4-28
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public Long getSortno(){
		Long res=1l;
		String hql="select max(sortno) from Bbzz";
		Long value=(Long)this.findUniqueByHql(hql);
		if(value!=null){
			res=value+1;
		}
		return res;
	}
}
