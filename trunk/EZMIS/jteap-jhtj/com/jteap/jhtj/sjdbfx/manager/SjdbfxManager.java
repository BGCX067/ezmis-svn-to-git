package com.jteap.jhtj.sjdbfx.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;



import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.DateUtils;
import com.jteap.jhtj.chart.MSColumn3DCategories;
import com.jteap.jhtj.chart.MSColumn3DCategory;
import com.jteap.jhtj.chart.MSColumn3DChart;
import com.jteap.jhtj.chart.MSColumn3DDataSet;
import com.jteap.jhtj.chart.MSColumn3DSet;
import com.jteap.jhtj.chart.MSLineApplication;
import com.jteap.jhtj.chart.MSLineApply;
import com.jteap.jhtj.chart.MSLineCategories;
import com.jteap.jhtj.chart.MSLineCategory;
import com.jteap.jhtj.chart.MSLineChart;
import com.jteap.jhtj.chart.MSLineDataSet;
import com.jteap.jhtj.chart.MSLineDefinition;
import com.jteap.jhtj.chart.MSLineSet;
import com.jteap.jhtj.chart.MSLineStyle;
import com.jteap.jhtj.chart.MSLineStyles;
import com.jteap.jhtj.jkbldy.model.TjInterFace;
import com.jteap.jhtj.sjflsz.model.TjItemKind;
import com.jteap.jhtj.sjxxxdy.model.TjInfoItem;
@SuppressWarnings({ "unchecked", "serial" })
public class SjdbfxManager extends HibernateEntityDao<TjItemKind> {
	private DataSource dataSource;
	/**
	 * 
	 *描述：生成折线图
	 *时间：2010-5-5
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws Exception 
	 */
	public MSLineChart generateLineChart(Map<String,String> keyMap,String item,String kid,String flflag) throws Exception{
		//图形
		MSLineChart chart=new MSLineChart();
		chart.setCaption(this.getInameByKidAndItem(kid, item));
		chart.setSubcaption("");
		chart.setLabelStep("1");
		chart.setBaseFontSize("12");
		//标题
		MSLineCategories mSLineCategories=new MSLineCategories();
		
		//数据集
		MSLineDataSet dataSet1=new MSLineDataSet();
		dataSet1.setSeriesName("对比期");
		MSLineDataSet dataSet2=new MSLineDataSet();
		dataSet2.setSeriesName("当期");
		//存放数据集的集合
		List<MSLineDataSet> dataSetList=new ArrayList<MSLineDataSet>();
		dataSetList.add(dataSet1);
		dataSetList.add(dataSet2);
		
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;

		try{
			StringBuffer skeyWhere=new StringBuffer();//存放查询条件
			StringBuffer sfield=new StringBuffer();   //存放查询的字段
			
			StringBuffer ekeyWhere=new StringBuffer();//存放查询条件
			StringBuffer efield=new StringBuffer();   //存放查询的字段
			//对比期
			String startNian=keyMap.get(TjInterFace.SNIAN);
			String startYue=keyMap.get(TjInterFace.SYUE);
			//当期
			String endNian=keyMap.get(TjInterFace.ENIAN);
			String endYue=keyMap.get(TjInterFace.EYUE);
			
			//因为日期之间存在天数的差异，所以定义标示符以日期天数多的为标题
			int categoryFlag=0;

			//日数据
			if("1".equals(flflag)){
				sfield.append(TjInterFace.NIAN+","+TjInterFace.YUE+","+TjInterFace.RI+","+item);
				String endRqs=DateUtils.getLastDate(startNian+"-"+startYue+"-1");
				//取到对比期的最后一天
				String sRis=DateUtils.getDate(DateUtils.StrToDate(endRqs, ""), "dd");
				skeyWhere.append("(NIAN="+startNian+" and YUE="+startYue+" and RI<="+sRis+") ");
				
				efield.append(TjInterFace.NIAN+","+TjInterFace.YUE+","+TjInterFace.RI+","+item);
				String endRqe=DateUtils.getLastDate(endNian+"-"+endYue+"-1");
				//取到当期的最后一天
				String sRie=DateUtils.getDate(DateUtils.StrToDate(endRqe, ""), "dd");
				ekeyWhere.append("(NIAN="+endNian+" and YUE="+endYue+" and RI<="+sRie+") ");
				
				if(Integer.parseInt(sRis)<Integer.parseInt(sRie)){
					categoryFlag=1;
				}
			//月数据
			}else if("2".equals(flflag)){
				sfield.append(TjInterFace.NIAN+","+TjInterFace.YUE+","+item);
				skeyWhere.append("(NIAN="+startNian+") ");
				
				efield.append(TjInterFace.NIAN+","+TjInterFace.YUE+","+item);
				ekeyWhere.append("(NIAN="+endNian+") ");
			//年数据
			}else if("3".equals(flflag)){
				endNian=DateUtils.getDate("yyyy");
				startNian=new Integer(Integer.parseInt(endNian)).toString();
				sfield.append(TjInterFace.NIAN+","+item);
				skeyWhere.append("NIAN>="+startNian+" and  NIAN<="+endNian+"");
				
				efield.append(TjInterFace.NIAN+","+item);
				ekeyWhere.append("NIAN>="+startNian+" and  NIAN<="+endNian+"");
			}
			
			//增加机组条件
			String jz=keyMap.get(TjInterFace.JZ);
			if(StringUtils.isNotEmpty(jz)){
				if(skeyWhere.toString().equals("")){
					skeyWhere.append(" "+TjInterFace.JZ+"='"+jz+"'");
				}else{
					skeyWhere.append(" and "+TjInterFace.JZ+"='"+jz+"'");
				}
				
				if(ekeyWhere.toString().equals("")){
					ekeyWhere.append(" "+TjInterFace.JZ+"='"+jz+"'");
				}else{
					ekeyWhere.append(" and "+TjInterFace.JZ+"='"+jz+"'");
				}
			}
			//查询语句
			String sqls="select "+sfield.toString()+" from DATA_"+kid+" where "+skeyWhere.toString();
			String sqle="select "+efield.toString()+" from DATA_"+kid+" where "+ekeyWhere.toString();
			List<String> sqlList=new ArrayList<String>();
			sqlList.add(sqls);
			sqlList.add(sqle);
			conn=this.dataSource.getConnection();
			
			for(int j=0;j<sqlList.size();j++){
				//当前的sql
				String sql=sqlList.get(j);
				//当前的数据集
				MSLineDataSet dataSet=dataSetList.get(j);
				//存放查询出来的所有结果
				List<Map<String,String>>  rowRecords=new ArrayList<Map<String,String>>();
				state=conn.createStatement();
				rs=state.executeQuery(sql);
				ResultSetMetaData rsmeta=rs.getMetaData();
				while(rs.next()){
					Map<String,String> map=new LinkedHashMap<String,String>();
					for(int i=1;i<=rsmeta.getColumnCount();i++){
						Object obj=rs.getObject(i);
						map.put(rsmeta.getColumnName(i).toString(), (obj==null?"":obj.toString()));
					}
					rowRecords.add(map);
				}
				
				//初始化起始时间到终止时间之间的值
				List<Map<String,String>> allData=this.initQssjToZzsjData(keyMap, flflag, item ,j);
				
				//全部日期的值
				for(Map<String,String> itemMap:allData){
					MSLineSet set=new MSLineSet();
					String nian=itemMap.get(TjInterFace.NIAN);
					String yue=itemMap.get(TjInterFace.YUE);
					String ri=itemMap.get(TjInterFace.RI);
					//查询出来有记录的值
					for(Map<String,String> recordMap:rowRecords){
						String reNian=recordMap.get(TjInterFace.NIAN);
						String reYue=recordMap.get(TjInterFace.YUE);
						String reRi=recordMap.get(TjInterFace.RI);
						if("1".equals(flflag)){
							if(nian.equals(reNian)&&yue.equals(reYue)&&ri.equals(reRi)){
								itemMap.put(item, recordMap.get(item));
							}
						}else if("2".equals(flflag)){
							if(nian.equals(reNian)&&yue.equals(reYue)){
								itemMap.put(item, recordMap.get(item));
							}
						}else if("3".equals(flflag)){
							if(nian.equals(reNian)){
								itemMap.put(item, recordMap.get(item));
							}
						}
					}
					MSLineCategory category=new MSLineCategory();
					if("1".equals(flflag)){
						category.setLabel(new Integer(ri).toString());
					}else if("2".equals(flflag)){
						category.setLabel(new Integer(yue).toString());
					}else if("3".equals(flflag)){
						category.setLabel(new Integer(nian).toString());
					}
					//只加一遍标题
					if(categoryFlag==j){
						mSLineCategories.getCategorys().add(category);
					}
					set.setValue(itemMap.get(item));
					
					
					dataSet.getMSLineSets().add(set);
				}
				
				chart.getMSLineDataSets().add(dataSet);
			}
			
			//下面公共的样式属性
			MSLineStyle style=new MSLineStyle();
			style.setName("captionFont");
			MSLineDefinition defin=new MSLineDefinition();
			defin.setMSLineStyle(style);
			
			MSLineApply apply=new MSLineApply();
			MSLineApplication applition=new MSLineApplication();
			applition.getMSLineApplys().add(apply);
			
			MSLineStyles styles=new MSLineStyles();
			styles.setMSLineApplication(applition);
			styles.setMSLineDefinition(defin);
			
			
			chart.setMSLineCategories(mSLineCategories);
			chart.setMSLineStyles(styles);
			//System.out.println(chart.toString());
		}catch(Exception e){
			throw e;
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
		return chart;
	}
	
	public MSColumn3DChart generateColumnChart(Map<String,String> keyMap,String item,String kid,String flflag) throws Exception{
		//图形
		MSColumn3DChart chart=new MSColumn3DChart();
		chart.setCaption(this.getInameByKidAndItem(kid, item));
		chart.setShowBorder("1");
		
		//标题
		MSColumn3DCategories mSColumn3DCategories=new MSColumn3DCategories();
		mSColumn3DCategories.setFontSize("12");
		
		//数据集
		MSColumn3DDataSet dataSet1=new MSColumn3DDataSet();
		dataSet1.setSeriesName("对比期");
		MSColumn3DDataSet dataSet2=new MSColumn3DDataSet();
		dataSet2.setSeriesName("当期");
		//存放数据集的集合
		List<MSColumn3DDataSet> dataSetList=new ArrayList<MSColumn3DDataSet>();
		dataSetList.add(dataSet1);
		dataSetList.add(dataSet2);
		
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;

		try{
			StringBuffer skeyWhere=new StringBuffer();//存放查询条件
			StringBuffer sfield=new StringBuffer();   //存放查询的字段
			
			StringBuffer ekeyWhere=new StringBuffer();//存放查询条件
			StringBuffer efield=new StringBuffer();   //存放查询的字段
			//对比期
			String startNian=keyMap.get(TjInterFace.SNIAN);
			String startYue=keyMap.get(TjInterFace.SYUE);
			//当期
			String endNian=keyMap.get(TjInterFace.ENIAN);
			String endYue=keyMap.get(TjInterFace.EYUE);
			
			//因为日期之间存在天数的差异，所以定义标示符以日期天数多的为标题
			int categoryFlag=0;

			//日数据
			if("1".equals(flflag)){
				sfield.append(TjInterFace.NIAN+","+TjInterFace.YUE+","+TjInterFace.RI+","+item);
				String endRqs=DateUtils.getLastDate(startNian+"-"+startYue+"-1");
				//取到对比期的最后一天
				String sRis=DateUtils.getDate(DateUtils.StrToDate(endRqs, ""), "dd");
				skeyWhere.append("(NIAN="+startNian+" and YUE="+startYue+" and RI<="+sRis+") ");
				
				efield.append(TjInterFace.NIAN+","+TjInterFace.YUE+","+TjInterFace.RI+","+item);
				String endRqe=DateUtils.getLastDate(endNian+"-"+endYue+"-1");
				//取到当期的最后一天
				String sRie=DateUtils.getDate(DateUtils.StrToDate(endRqe, ""), "dd");
				ekeyWhere.append("(NIAN="+endNian+" and YUE="+endYue+" and RI<="+sRie+") ");
				
				if(Integer.parseInt(sRis)<Integer.parseInt(sRie)){
					categoryFlag=1;
				}
			//月数据
			}else if("2".equals(flflag)){
				sfield.append(TjInterFace.NIAN+","+TjInterFace.YUE+","+item);
				skeyWhere.append("(NIAN="+startNian+") ");
				
				efield.append(TjInterFace.NIAN+","+TjInterFace.YUE+","+item);
				ekeyWhere.append("(NIAN="+endNian+") ");
			//年数据
			}else if("3".equals(flflag)){
				endNian=DateUtils.getDate("yyyy");
				startNian=new Integer(Integer.parseInt(endNian)).toString();
				sfield.append(TjInterFace.NIAN+","+item);
				skeyWhere.append("NIAN>="+startNian+" and  NIAN<="+endNian+"");
				
				efield.append(TjInterFace.NIAN+","+item);
				ekeyWhere.append("NIAN>="+startNian+" and  NIAN<="+endNian+"");
			}
			
			//增加机组条件
			String jz=keyMap.get(TjInterFace.JZ);
			if(StringUtils.isNotEmpty(jz)){
				if(skeyWhere.toString().equals("")){
					skeyWhere.append(" "+TjInterFace.JZ+"='"+jz+"'");
				}else{
					skeyWhere.append(" and "+TjInterFace.JZ+"='"+jz+"'");
				}
				
				if(ekeyWhere.toString().equals("")){
					ekeyWhere.append(" "+TjInterFace.JZ+"='"+jz+"'");
				}else{
					ekeyWhere.append(" and "+TjInterFace.JZ+"='"+jz+"'");
				}
			}
			//查询语句
			String sqls="select "+sfield.toString()+" from DATA_"+kid+" where "+skeyWhere.toString();
			String sqle="select "+efield.toString()+" from DATA_"+kid+" where "+ekeyWhere.toString();
			List<String> sqlList=new ArrayList<String>();
			sqlList.add(sqls);
			sqlList.add(sqle);
			conn=this.dataSource.getConnection();
			
			for(int j=0;j<sqlList.size();j++){
				//当前的sql
				String sql=sqlList.get(j);
				//当前的数据集
				MSColumn3DDataSet dataSet=dataSetList.get(j);
				//存放查询出来的所有结果
				List<Map<String,String>>  rowRecords=new ArrayList<Map<String,String>>();
				state=conn.createStatement();
				rs=state.executeQuery(sql);
				ResultSetMetaData rsmeta=rs.getMetaData();
				while(rs.next()){
					Map<String,String> map=new LinkedHashMap<String,String>();
					for(int i=1;i<=rsmeta.getColumnCount();i++){
						Object obj=rs.getObject(i);
						map.put(rsmeta.getColumnName(i).toString(), (obj==null?"":obj.toString()));
					}
					rowRecords.add(map);
				}
				
				//初始化起始时间到终止时间之间的值
				List<Map<String,String>> allData=this.initQssjToZzsjData(keyMap, flflag, item ,j);
				
				//全部日期的值
				for(Map<String,String> itemMap:allData){
					MSColumn3DSet set=new MSColumn3DSet();
					String nian=itemMap.get(TjInterFace.NIAN);
					String yue=itemMap.get(TjInterFace.YUE);
					String ri=itemMap.get(TjInterFace.RI);
					//查询出来有记录的值
					for(Map<String,String> recordMap:rowRecords){
						String reNian=recordMap.get(TjInterFace.NIAN);
						String reYue=recordMap.get(TjInterFace.YUE);
						String reRi=recordMap.get(TjInterFace.RI);
						if("1".equals(flflag)){
							if(nian.equals(reNian)&&yue.equals(reYue)&&ri.equals(reRi)){
								itemMap.put(item, recordMap.get(item));
							}
						}else if("2".equals(flflag)){
							if(nian.equals(reNian)&&yue.equals(reYue)){
								itemMap.put(item, recordMap.get(item));
							}
						}else if("3".equals(flflag)){
							if(nian.equals(reNian)){
								itemMap.put(item, recordMap.get(item));
							}
						}
					}
					MSColumn3DCategory category=new MSColumn3DCategory();
					if("1".equals(flflag)){
						category.setLabel(new Integer(ri).toString());
					}else if("2".equals(flflag)){
						category.setLabel(new Integer(yue).toString());
					}else if("3".equals(flflag)){
						category.setLabel(new Integer(nian).toString());
					}
					//只加一遍标题
					if(categoryFlag==j){
						mSColumn3DCategories.getMSColumn3DCategorys().add(category);
					}
					set.setValue(itemMap.get(item));
					
					
					dataSet.getMSColumn3DSets().add(set);
				}
				
				chart.getMSColumn3DDataSets().add(dataSet);
			}
			chart.setMsColumn3DCategories(mSColumn3DCategories);
			//System.out.println(chart.toString());
		}catch(Exception e){
			throw e;
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
		return chart;
	}
	
	/**
	 * 
	 *描述：初始化起始时间到终止时间之间的每天的值
	 *时间：2010-5-5
	 *作者：童贝
	 *参数：keyMap 查询条件集合,flflag 分类标识,item 选择的字段,selQr 如果是0就是对比期,1就是当期
	 *返回值:
	 *抛出异常：
	 */
	public List<Map<String,String>> initQssjToZzsjData(Map<String,String> keyMap,String flflag,String item ,int selQr){
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		String nian="";
		String yue="";
		//String ri="";
		if(0==selQr){
			nian=keyMap.get(TjInterFace.SNIAN);
			yue=keyMap.get(TjInterFace.SYUE);
			//ri=keyMap.get(TjInterFace.SRI);
		}else{
			nian=keyMap.get(TjInterFace.ENIAN);
			yue=keyMap.get(TjInterFace.EYUE);
			//ri=keyMap.get(TjInterFace.ERI);
		}		
		if("1".equals(flflag)){
			int start=1;
			//本月的最后一天
			String lastDate=DateUtils.getLastDate(nian+"-"+yue+"-1");
			String endDate=DateUtils.getDate(DateUtils.StrToDate(lastDate,""),"dd");
			endDate=(endDate.charAt(0)=='0'?endDate.substring(1):endDate);
			int end=new Integer(endDate);
			for(int i=start;i<=end;i++){
				Map<String, String> map=new HashMap<String, String>();
				map.put(TjInterFace.NIAN, nian);
				map.put(TjInterFace.YUE, yue);
				map.put(TjInterFace.RI, i+"");
				map.put(item, "0");
				result.add(map);
			}
		}else if("2".equals(flflag)){
			int start=1;
			int end=12;
			for(int i=start;i<=end;i++){
				Map<String, String> map=new HashMap<String, String>();
				map.put(TjInterFace.NIAN, nian);
				map.put(TjInterFace.YUE, i+"");
				map.put(item, "0");
				result.add(map);
			}
		}else if("3".equals(flflag)){
			int end=Integer.parseInt(DateUtils.getDate("yyyy"));
			int start=end-10;
			for(int i=start;i<=end;i++){
				Map<String, String> map=new HashMap<String, String>();
				map.put(TjInterFace.NIAN, i+"");
				map.put(item, "0");
				result.add(map);
			}
		}
		return result;
	}
	
	/**
	 * 
	 *描述：得到图形的标题(名字+单位)
	 *时间：2010-5-6
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getInameByKidAndItem(String kid,String item){
		String hql="from  TjInfoItem where kid='"+kid+"' and item='"+item+"'";
		List<TjInfoItem> list=this.find(hql);
		if(list.size()>0){
			TjInfoItem infoitem=list.get(0);
			String iname=infoitem.getIname();
			String dw=infoitem.getDw();
			if(iname!=null){
				return iname+"("+dw+")";
			}
		}
		return "";
	}
	
	
	/**
	 * 
	 *描述：判断日期是否合法
	 *时间：2010-5-6
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public boolean isDateLawful(String keyList,String flflag){
		boolean result=true;
		Map<String,String> keyMap=new HashMap<String, String>();
		String[] keys=keyList.split("!");
		for(String key:keys){
			String[] nameAndValue=key.split(",");
			if(nameAndValue.length==2){
				String name=nameAndValue[0];
				String value=nameAndValue[1];
				keyMap.put(name, value);
			}
		}
		String startNian=keyMap.get(TjInterFace.SNIAN);
		String startYue=keyMap.get(TjInterFace.SYUE);
		String endNian=keyMap.get(TjInterFace.ENIAN);
		String endYue=keyMap.get(TjInterFace.EYUE);
		if("1".equals(flflag)){
			long res=this.compareStringTime(endNian+"-"+(endYue.length()==1?"0"+endYue:endYue)+"-01",startNian+"-"+(startYue.length()==1?"0"+startYue:startYue)+"-01",  "yyyy-MM-dd");
			if(res<0){
				result=false;
			}
		}else if("2".equals(flflag)){
			Integer sNian=Integer.parseInt(startNian);
			Integer eNian=Integer.parseInt(endNian);
			if(sNian>eNian){
				result=false;
			}
		}
		return result;
	}
	
	/**
	 * 
	 *描述：将条件字段转换成map集合(符合数据对比分析的逻辑封装)
	 *时间：2010-5-7
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public Map<String,String> unPackKeyList(String keyList,String flflag){
		Map<String,String> keyMap=new HashMap<String, String>();
		String[] keys=keyList.split("!");
		for(String key:keys){
			String[] nameAndValue=key.split(",");
			if(nameAndValue.length==2){
				String name=nameAndValue[0];
				String value=nameAndValue[1];
				keyMap.put(name, value);
			}
		}
		return keyMap;
	}
	
	public long compareStringTime(String t1, String t2, String parrten) {
		SimpleDateFormat formatter = new SimpleDateFormat(parrten);
		ParsePosition pos = new ParsePosition(0);
		ParsePosition pos1 = new ParsePosition(0);
		Date dt1 = formatter.parse(t1, pos);
		Date dt2 = formatter.parse(t2, pos1);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt1);
		long day1 = calendar.getTimeInMillis();
		calendar.setTime(dt2);
		long day2 = calendar.getTimeInMillis();
		//System.out.println((day1 - day2)/(1000*60*60*24));
		return (day1 - day2);
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
