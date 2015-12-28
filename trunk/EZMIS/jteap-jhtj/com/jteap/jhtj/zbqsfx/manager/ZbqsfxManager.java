package com.jteap.jhtj.zbqsfx.manager;

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
import com.jteap.core.utils.JSONUtil;
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

/**
 * 描述:指标趋势分析
 * 作者:童贝
 */
@SuppressWarnings({ "unchecked", "serial" })
public class ZbqsfxManager extends HibernateEntityDao<TjItemKind> {
	private DataSource dataSource;
	/**
	 * 
	 *描述：生成图形
	 *时间：2010-5-5
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws Exception 
	 */
	public MSLineChart generateChart(String keyList,String item,String kid,String flflag) throws Exception{
		//图形
		MSLineChart chart=new MSLineChart();
		chart.setCaption(this.getInameByKidAndItem(kid, item));
		chart.setSubcaption("");
		chart.setLabelStep("1");
		chart.setBaseFontSize("12");
		//标题
		MSLineCategories mSLineCategories=new MSLineCategories();
		
		//数据集
		MSLineDataSet dataSet=new MSLineDataSet();
		dataSet.setSeriesName("");
		String [] color={"F1683C","2AD62A"};
		dataSet.setColor(color[0]);
		dataSet.setAnchorBgColor(color[0]);
		dataSet.setAnchorBorderColor(color[0]);
		
		Connection conn=null;
		Statement state=null;
		ResultSet rs=null;
		//组装查询条件
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
		try{
			StringBuffer keyWhere=new StringBuffer();
			StringBuffer field=new StringBuffer();
			
			String startNian=keyMap.get(TjInterFace.SNIAN);
			String startYue=keyMap.get(TjInterFace.SYUE);
			String startRi=keyMap.get(TjInterFace.SRI);
			String endNian=keyMap.get(TjInterFace.ENIAN);
			String endYue=keyMap.get(TjInterFace.EYUE);
			String endRi=keyMap.get(TjInterFace.ERI);
			//日数据
			if("1".equals(flflag)){
				field.append(TjInterFace.NIAN+","+TjInterFace.YUE+","+TjInterFace.RI+","+item);
				keyWhere.append("((NIAN="+startNian+" and YUE="+startYue+" and RI>="+startRi+") or (NIAN="+startNian+" and YUE>"+startYue+") or (NIAN="+endNian+" and YUE<"+endYue+") or (NIAN="+endNian+" and YUE="+endYue+" and RI<="+endRi+")) ");
			//月数据
			}else if("2".equals(flflag)){
				field.append(TjInterFace.NIAN+","+TjInterFace.YUE+","+item);
				keyWhere.append("((NIAN="+startNian+" and YUE>="+startYue+") or (NIAN>"+startNian+" and NIAN<"+endNian+") or (NIAN="+endNian+" and YUE<="+endYue+"))");
			//年数据
			}else if("3".equals(flflag)){
				field.append(TjInterFace.NIAN+","+item);
				keyWhere.append("NIAN>="+startNian+" and  NIAN<="+endNian+"");
			}
			
			//增加机组条件
			String jz=keyMap.get(TjInterFace.JZ);
			if(StringUtils.isNotEmpty(jz)){
				keyWhere.append(" and "+TjInterFace.JZ+"='"+jz+"'");
			}
			//查询语句
			String sql="select "+field.toString()+" from DATA_"+kid+" where "+keyWhere.toString();
			
			//存放查询出来的所有结果
			List<Map<String,String>>  rowRecords=new ArrayList<Map<String,String>>();
			conn=this.dataSource.getConnection();
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
			
			//初始化起始时间到终止时间之间的每天的值
			List<Map<String,String>> allData=this.initQssjToZzsjData(keyMap, flflag, item);
			
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
				mSLineCategories.getCategorys().add(category);
				
				set.setValue(itemMap.get(item));
				dataSet.getMSLineSets().add(set);
			}
			
			chart.getMSLineDataSets().add(dataSet);
			
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
	 *参数：keyMap 查询条件集合,flflag 分类标识,item 选择的字段
	 *返回值:
	 *抛出异常：
	 */
	public List<Map<String,String>> initQssjToZzsjData(Map<String,String> keyMap,String flflag,String item){
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		String startNian=keyMap.get(TjInterFace.SNIAN);
		String startYue=keyMap.get(TjInterFace.SYUE);
		String startRi=keyMap.get(TjInterFace.SRI);
		String endNian=keyMap.get(TjInterFace.ENIAN);
		String endYue=keyMap.get(TjInterFace.EYUE);
		String endRi=keyMap.get(TjInterFace.ERI);
		
		if("1".equals(flflag)){
			while(true){
				if(startNian.equals(endNian)&&startYue.equals(endYue)){
					int start=new Integer(startRi);
					int end=new Integer(endRi);
					for(int i=start;i<=end;i++){
						Map<String, String> map=new HashMap<String, String>();
						map.put(TjInterFace.NIAN, startNian);
						map.put(TjInterFace.YUE, startYue);
						map.put(TjInterFace.RI, i+"");
						map.put(item, "0");
						result.add(map);
					}
					break;
				}else{
					int start=new Integer(startRi);
					//本月的最后一天
					String lastDate=DateUtils.getLastDate(startNian+"-"+startYue+"-1");
					String endDate=DateUtils.getDate(DateUtils.StrToDate(lastDate,""),"dd");
					endDate=(endDate.charAt(0)=='0'?endDate.substring(1):endDate);
					int end=new Integer(endDate);
					for(int i=start;i<=end;i++){
						Map<String, String> map=new HashMap<String, String>();
						map.put(TjInterFace.NIAN, startNian);
						map.put(TjInterFace.YUE, startYue);
						map.put(TjInterFace.RI, i+"");
						map.put(item, "0");
						result.add(map);
					}
					//把日期往后推一个月
					Date curDate=DateUtils.StrToDate(startNian+"-"+startYue+"-"+startRi, "yyyy-MM-dd");
					String nextDate=DateUtils.getNextDate(curDate, 1, "yyyy-MM-dd");
					startNian=DateUtils.getDate(DateUtils.StrToDate(nextDate,""), "yyyy");
					startYue=DateUtils.getDate(DateUtils.StrToDate(nextDate,""), "MM");
					startYue=(startYue.charAt(0)=='0'?startYue.substring(1):startYue);
					startRi="1";
				}
			}
		}else if("2".equals(flflag)){
			while(true){
				if(startNian.equals(endNian)){
					int start=new Integer(startYue);
					int end=new Integer(endYue);
					for(int i=start;i<=end;i++){
						Map<String, String> map=new HashMap<String, String>();
						map.put(TjInterFace.NIAN, startNian);
						map.put(TjInterFace.YUE, i+"");
						map.put(item, "0");
						result.add(map);
					}
					break;
				}else{
					int start=new Integer(startYue);
					int end=12;
					for(int i=start;i<=end;i++){
						Map<String, String> map=new HashMap<String, String>();
						map.put(TjInterFace.NIAN, startNian);
						map.put(TjInterFace.YUE, i+"");
						map.put(item, "0");
						result.add(map);
					}
					//直接是下一年
					startNian=new Integer(new Integer(startNian)+1).toString();
					//下一年的第一个月
					startYue="1";
				}
			}
		}else if("3".equals(flflag)){
			int start=new Integer(startNian);
			int end=new Integer(endNian);
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
		String startRi=keyMap.get(TjInterFace.SRI);
		String endNian=keyMap.get(TjInterFace.ENIAN);
		String endYue=keyMap.get(TjInterFace.EYUE);
		String endRi=keyMap.get(TjInterFace.ERI);
		if("1".equals(flflag)){
			long res=this.compareStringTime(endNian+"-"+(endYue.length()==1?"0"+endYue:endYue)+"-"+(endRi.length()==1?"0"+endRi:endRi),startNian+"-"+(startYue.length()==1?"0"+startYue:startYue)+"-"+(startRi.length()==1?"0"+startRi:startRi),  "yyyy-MM-dd");
			if(res<0){
				result=false;
			}else{
				//限定日期在两个月之间(包括2个月)
				String startDate=startNian+"-"+(startYue.length()==1?"0"+startYue:startYue)+"-"+(startRi.length()==1?"0"+startRi:startRi);
				String twoMonth=DateUtils.getNextDate(DateUtils.StrToDate(startDate, "yyyy-MM-dd"), 2, "yyyy-MM-dd");
				long ps=this.compareStringTime(twoMonth,endNian+"-"+(endYue.length()==1?"0"+endYue:endYue)+"-"+(endRi.length()==1?"0"+endRi:endRi), "yyyy-MM-dd");
				if(ps<0){
					result=false;
				}
			}
		}else if("2".equals(flflag)){
			long res=this.compareStringTime(endNian+"-"+endYue+"-1",startNian+"-"+startYue+"-1",  "yyyy-MM-dd");
			if(res<0){
				result=false;
			}
		}else if("3".equals(flflag)){
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
	 * 描述:比较两个数据之间的差
	 * 时间:Oct 30, 2010
	 * 作者:童贝
	 * 参数:
	 * 返回值:long
	 * 抛出异常:
	 */
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
	
	/**
	 * 
	 * 描述:把数据转换成json
	 * 时间:Oct 30, 2010
	 * 作者:童贝
	 * 参数:
	 * 返回值:String
	 * 抛出异常:
	 */
	public String getTableToJson(MSLineChart chart){
		StringBuffer result=new StringBuffer();
		//记录集
		for(MSLineDataSet dataset:chart.getMSLineDataSets()){
			List<Map> sortList=new ArrayList<Map>();
			List<MSLineSet> setList=dataset.getMSLineSets();
			int flag=0;
			for(MSLineCategory category:chart.getMSLineCategories().getCategorys()){
				Map<String,String> map=new HashMap<String, String>();
				String label=category.getLabel();
				MSLineSet set=setList.get(flag);
				String value=set.getValue();
				map.put(label, value);
				sortList.add(map);
				flag++;
			}
			result.append(JSONUtil.listToJson(sortList));
		}
		return result.toString();
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
