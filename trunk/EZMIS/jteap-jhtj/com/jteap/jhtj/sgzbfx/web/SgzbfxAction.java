package com.jteap.jhtj.sgzbfx.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.chart.MSColumn3DChart;
import com.jteap.jhtj.jkbldy.model.TjInterFace;
import com.jteap.jhtj.sgzbfx.manager.SgzbfxManager;
import com.jteap.jhtj.sjflsz.manager.TjItemKindManager;
import com.jteap.jhtj.sjflsz.model.TjItemKind;
import com.jteap.jhtj.sjflsz.model.TjItemKindKey;
import com.jteap.jhtj.sjwh.manager.TjSjwhManager;
import com.jteap.jhtj.sjwh.model.KeyModel;
@SuppressWarnings({ "unchecked", "serial" })
public class SgzbfxAction extends AbstractTreeAction {
	private TjItemKindManager tjItemKindManager;
	private SgzbfxManager sgzbfxManager;
	private TjSjwhManager tjSjwhManager;
	
	
	@Override
	public String showTreeAction() throws Exception {
		JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				TjItemKind kind=(TjItemKind) obj;
				map.put("leaf", kind.getChildKind().size()>0?false:true);
				map.put("expanded", true);
				map.put("flflag", kind.getFlflag());
				map.put("kid", kind.getKid());
			}
		});
		final Class cls=GenericsUtils.getSuperClassGenricType(getClass());
		jsonConfig.registerJsonBeanProcessor(cls,jsonBeanProcessor);
		//开始json化
		Collection roots=getRootObjects();
		JSONArray result=JSONArray.fromObject(roots,jsonConfig);
		//输出
		this.outputJson(result.toString());
		return NONE;
	}
	
	/**
	 * 
	 *描述：显示列表
	 *时间：2010-4-29
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showFindPageAction() throws Exception{
		String id=request.getParameter("id");
		String kid=request.getParameter("kid");//分类编码
		String flflag=request.getParameter("flflag");//分类标示符
		String fields=request.getParameter("fields");//选择的字段列表
		List<KeyModel> jzlist=this.tjSjwhManager.getJzList(kid);
		//是否有机组
		if(jzlist.size()>0){
			request.setAttribute("isJz", "true");
		}else{
			request.setAttribute("isJz", "false");
		}
		//关键字列表
		List<TjItemKindKey> keyList=this.tjSjwhManager.findTjItemKeyByKid(kid);
		StringBuffer keys=new StringBuffer();
		for(TjItemKindKey key:keyList){
			keys.append(key.getIcode()+","+key.getIname()+"!");
		}
		if(!keys.toString().equals("")){
			keys.deleteCharAt(keys.length()-1);
		}
		request.setAttribute("id", id);
		request.setAttribute("kid", kid);
		request.setAttribute("flflag", flflag);
		request.setAttribute("fields", fields);
		request.setAttribute("keys", keys);
		return "showFindPageAction";
	}
	
	
	/**
	 * 
	 *描述：动态生成查询面板的条件
	 *时间：2010-4-29
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String dynaAddSearPanelAction() throws Exception{
		//String flflag=request.getParameter("flflag");
		String kid=request.getParameter("kid");
		StringBuffer findWhere=new StringBuffer();
		StringBuffer defaultValue=new StringBuffer();
		//日数据
		//if("1".equals(flflag)){
		//终止年,起始年列表
		List<KeyModel> year=this.tjSjwhManager.getYearList();
		String yearJson=JSONUtil.listToJson(year,new String[]{"displayValue","value"});
		findWhere.append(""+TjInterFace.ENIAN+":["+yearJson+"],");
		findWhere.append(""+TjInterFace.SNIAN+":["+yearJson+"],");
		//终止月,起始月列表
		List<KeyModel> month=this.tjSjwhManager.getMonthList();
		String monthJson=JSONUtil.listToJson(month,new String[]{"displayValue","value"});
		findWhere.append(""+TjInterFace.EYUE+":["+monthJson+"],");
		findWhere.append(""+TjInterFace.SYUE+":["+monthJson+"],");
		
		String endYear=DateUtils.getDate("yyyy");
		String endMonth=DateUtils.getDate("MM");
		String endDay=DateUtils.getDate("dd");
		//向前推一天
		Date endDate=DateUtils.getPreDate(new Date());
		endYear=DateUtils.getDate(endDate, "yyyy");
		endMonth=DateUtils.getDate(endDate, "MM");
		endDay=DateUtils.getDate(endDate, "dd");
		
		//向前推一个月
		String startDateStr=DateUtils.getNextDate(endDate, -1, "yyyy-MM-dd");
		Date startDate=DateUtils.StrToDate(startDateStr, "yyyy-MM-dd");
		String startYear=DateUtils.getDate(startDate, "yyyy");
		String startMonth=DateUtils.getDate(startDate, "MM");
		String startDay=DateUtils.getDate(startDate, "dd");
		
		String endMonthValue=(endMonth.charAt(0)=='0'?endMonth.substring(1):endMonth);
		//终止日列表
		List<KeyModel> day=this.tjSjwhManager.getDayList(TjInterFace.NIAN+","+endYear+"!"+TjInterFace.YUE+","+endMonthValue);
		String dayJson=JSONUtil.listToJson(day,new String[]{"displayValue","value"});
		findWhere.append(""+TjInterFace.ERI+":["+dayJson+"],");
	
		//机组列表
		List<KeyModel> jzlist=this.tjSjwhManager.getJzList(kid);
		if(jzlist.size()>0){
			String jzJson=JSONUtil.listToJson(jzlist,new String[]{"displayValue","value"});
			findWhere.append(""+TjInterFace.JZ+":["+jzJson+"],");
			defaultValue.append(TjInterFace.JZ+"value:'"+jzlist.get(0).getValue()+"',");
		}
		
		String startMonthValue=(startMonth.charAt(0)=='0'?startMonth.substring(1):startMonth);
		//起始日列表
		List<KeyModel> startdayList=this.tjSjwhManager.getDayList(TjInterFace.NIAN+","+startYear+"!"+TjInterFace.YUE+","+startMonthValue);
		String startdayJson=JSONUtil.listToJson(startdayList,new String[]{"displayValue","value"});
		findWhere.append(""+TjInterFace.SRI+":["+startdayJson+"]");
		
		
		
		
		String endDayValue=(endDay.charAt(0)=='0'?endDay.substring(1):endDay);
		String startDayValue=(startDay.charAt(0)=='0'?startDay.substring(1):startDay);
		//默认值 
		defaultValue.append(TjInterFace.ENIAN+"value:'"+endYear+"',");
		defaultValue.append(TjInterFace.EYUE+"value:'"+endMonthValue+"',");
		defaultValue.append(TjInterFace.ERI+"value:'"+endDayValue+"',");
		defaultValue.append(TjInterFace.SNIAN+"value:'"+startYear+"',");
		defaultValue.append(TjInterFace.SYUE+"value:'"+startMonthValue+"',");
		defaultValue.append(TjInterFace.SRI+"value:'"+startDayValue+"'");

		String json="{success:true,"+findWhere.toString()+","+defaultValue.toString()+"}";
		//System.out.println(json);
		outputJson(json);
		return NONE;
	}
	
	
	/**
	 * 
	 *描述：显示指标的主页面
	 *时间：2010-4-29
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showIndexAction() throws Exception{
		String id=request.getParameter("id");
		String kid=request.getParameter("kid");
		String flflag=request.getParameter("flflag");
		//查找所有的普通字段
		String fieldAll=this.tjSjwhManager.initFieldData(kid);
		request.setAttribute("fieldAll", "["+fieldAll+"]");
		request.setAttribute("kid", kid);
		request.setAttribute("id", id);
		request.setAttribute("flflag", flflag);
		
		return "showIndexAction";
	}
	
	/**
	 * 
	 *描述：显示图形
	 *时间：2010-5-5
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showChartAction() throws Exception{
		String kid=request.getParameter("kid");
		String fields=request.getParameter("fields");//选择的字段列表
		String flflag=request.getParameter("flflag");//分类标示符
		List<KeyModel> jzlist=this.tjSjwhManager.getJzList(kid);
		//是否有机组
		if(jzlist.size()>0){
			request.setAttribute("isJz", "true");
		}else{
			request.setAttribute("isJz", "false");
		}
		request.setAttribute("kid", kid);
		request.setAttribute("fields", fields);
		request.setAttribute("flflag", flflag);
		return "showChartAction";
	}
	
	
	public String generateChartAction() throws Exception{
		String kid=request.getParameter("kid");
		String fields=request.getParameter("fields");
		String flflag=request.getParameter("flflag");
		String keyList=request.getParameter("keyList");
		//判断选择的日期是否有效
		boolean result=this.sgzbfxManager.isDateLawful(keyList, flflag);
		if(result){
			MSColumn3DChart chart=this.sgzbfxManager.generateChart(keyList, fields, kid, flflag);
			outputJson("{success:true,chartData:'"+chart.toString()+"',tableData:'"+chart.getTableToXml()+"',fields:'"+fields+"'}");
			//System.out.println("{success:true,chartData:'"+chart.toString()+"',tableData:'"+chart.getTableToXml()+"'}");
		}else{
			outputJson("{success:false,msg:'日期选择不正确!'}");
		}
		
		return NONE;
	}
	
	
	@Override
	protected Collection getChildren(Object bean) {
		TjItemKind kind=(TjItemKind)bean;
		Set<TjItemKind> set=kind.getChildKind();
		List<TjItemKind> childList=new ArrayList<TjItemKind>();
		childList.addAll(set);
		return childList;
	}
	
	

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "kname";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		return tjItemKindManager.getRootList();
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortno";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "kname";
	}

	@Override
	public HibernateEntityDao getManager() {
		return this.sgzbfxManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","kid","kname","flflag","sortno"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","kid","kname","flflag","sortno"};
	}

	public TjItemKindManager getTjItemKindManager() {
		return tjItemKindManager;
	}

	public void setTjItemKindManager(TjItemKindManager tjItemKindManager) {
		this.tjItemKindManager = tjItemKindManager;
	}

	public TjSjwhManager getTjSjwhManager() {
		return tjSjwhManager;
	}

	public void setTjSjwhManager(TjSjwhManager tjSjwhManager) {
		this.tjSjwhManager = tjSjwhManager;
	}

	public SgzbfxManager getSgzbfxManager() {
		return sgzbfxManager;
	}

	public void setSgzbfxManager(SgzbfxManager sgzbfxManager) {
		this.sgzbfxManager = sgzbfxManager;
	}

}
