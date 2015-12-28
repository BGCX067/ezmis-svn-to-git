package com.jteap.jhtj.dnb.web;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.chart.MSLineChart;
import com.jteap.jhtj.dnb.manager.DnbManager;
import com.jteap.jhtj.dnb.model.Dnb;
import com.jteap.jhtj.dnb.model.DnbData;
/**
 * 
 * 描述:电能表操作类
 * 时间:2010 11 9
 * 作者:tngbei
 * 参数:
 * 返回值:
 * 抛出异常:
 */
@SuppressWarnings({ "unchecked", "serial" })
public class DnbAction extends AbstractTreeAction<Dnb> {
	private DnbManager dnbManager;
	
	/**
	 * 
	 * 描述:得到数据的本日累计和本月累计
	 * 时间:2010 11 9
	 * 作者:童贝
	 * 参数:
	 * 返回值:String
	 * 抛出异常:
	 */
	public String getDataTableAction() throws Exception{
		String id=request.getParameter("id");
		//当前选中的日期
		String curDate=request.getParameter("curDate");
		String type=request.getParameter("type");
		if(StringUtil.isEmpty(curDate)){
			curDate=DateUtils.getDate("yyyy-MM-dd");
		}
		if(StringUtil.isEmpty(type)){
			type="PZ";
		}
		DnbData dnbData=this.dnbManager.getNewDataByTimeAndID(id, curDate);
		String json=JSONUtil.objectToJson(dnbData, new String[]{"id","pz","pf","qz","qf","brpz","brpf","brqz","brqf","bypz","bypf","byqz","byqf"});
		
		MSLineChart chart=this.dnbManager.generateChart(id, curDate, type);
		outputJson("{success:true,data:["+json+"],chart:'"+chart.toString()+"'}");
		System.out.println(json);
		return NONE;
	}
	
	@Override
	public String showListAction() throws Exception {
		String id=request.getParameter("id");
		//当前选中的日期
		String curDate=request.getParameter("curDate");
		if(StringUtil.isEmpty(curDate)){
			curDate=DateUtils.getDate("yyyy-MM-dd");
		}
		
		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		// 开始分页查询
		Page page = this.dnbManager.getListByCurdate(id,curDate,Integer.parseInt(start),Integer.parseInt(limit));
		List<Map> obj = (List<Map>) page.getResult();
		StringBuffer json=new StringBuffer();//JSONUtil.listToJson(obj,new String[]{"TIMES","PZ","QF","QZ","PF","ELECID"});
		for(Map map:obj){
			String curRow="";
			for (Iterator it = map.keySet().iterator(); it.hasNext();) {
				String paramName = (String) it.next();
				Object paramValue = map.get(paramName);
				curRow=curRow+"'"+paramName+"':"+paramValue+",";
			}
			if(curRow.length()>0){
				curRow=curRow.substring(0,curRow.lastIndexOf(","));
			}
			curRow="{"+curRow+"},";
			json.append(curRow);
		}
		if(json.length()>0){
			json.deleteCharAt(json.lastIndexOf(","));
		}
		System.out.println(json);
		long totalCount=page.getTotalCount();
		
		StringBuffer dataBlock = new StringBuffer();
		dataBlock.append("{totalCount:'" + totalCount + "',list:["+ json + "]}");
		this.outputJson(dataBlock.toString());
		return NONE;
	}
	
	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "name";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		return this.dnbManager.getAll();
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "id";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "name";
	}

	@Override
	public HibernateEntityDao getManager() {
		return dnbManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","name","dv","pz","pf","qz","qf","zyf","zyp","zyg","fyf","fyp","fyg","zwf","zwp","zwg","fwf","fwg","t1","t1n","t2","t2n","t3","t3n","pt","ct"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","name","dv","pz","pf","qz","qf","zyf","zyp","zyg","fyf","fyp","fyg","zwf","zwp","zwg","fwf","fwg","t1","t1n","t2","t2n","t3","t3n","pt","ct"};
	}

	public DnbManager getDnbManager() {
		return dnbManager;
	}

	public void setDnbManager(DnbManager dnbManager) {
		this.dnbManager = dnbManager;
	}
}
