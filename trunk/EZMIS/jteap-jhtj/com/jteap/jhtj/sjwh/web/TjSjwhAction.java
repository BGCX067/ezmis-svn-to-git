package com.jteap.jhtj.sjwh.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.GenericsUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.jkbldy.model.TjInterFace;
import com.jteap.jhtj.sjflsz.manager.TjItemKindManager;
import com.jteap.jhtj.sjflsz.model.TjItemKind;
import com.jteap.jhtj.sjflsz.model.TjItemKindKey;
import com.jteap.jhtj.sjflsz.model.TjKndJZ;
import com.jteap.jhtj.sjwh.manager.TjSjwhManager;
import com.jteap.jhtj.sjwh.model.KeyModel;
import com.jteap.jhtj.sjxxxdy.model.TjInfoItem;
import com.jteap.system.person.manager.PersonManager;
@SuppressWarnings({ "unchecked", "serial" })
public class TjSjwhAction extends AbstractTreeAction<TjItemKind> {
	private TjItemKindManager tjItemKindManager;
	private TjSjwhManager tjSjwhManager;
	private PersonManager personManager;
	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		List<TjItemKind> childList=new ArrayList<TjItemKind>();
		TjItemKind kind=(TjItemKind)bean;
		Set<TjItemKind> set=kind.getChildKind();
		if(set!=null){
			childList.addAll(set);
		}
		//如果不是节点数据那么就到了最底层，再判断该数据类型是否含有机组
		if(kind.getFlflag()!=0&&kind.getFlflag()!=4){
			String kid=kind.getKid();
			boolean isJz=tjItemKindManager.isIncludeJz(kid);
			if(isJz){
				List<TjKndJZ> jzList=tjItemKindManager.findTjKndJZByKid(kid);
				for(TjKndJZ jz:jzList){
					TjItemKind jzKind=new TjItemKind();
					jzKind.setId(jz.getId());
					jzKind.setKid(jz.getKid());
					jzKind.setKname(jz.getJzname());
					jzKind.setFlflag(4l);
					childList.add(jzKind);
				}
			}
		}
		
		//Person person = personManager.getCurrentPerson(sessionAttrs);
		//if(!this.isCurrentRootUser()){
		///	childList=this.tjItemKindManager.filterKindListByQx(person, childList);
		//}
		return childList;
	}
	
	@Override
	public String showTreeAction() throws Exception {
		final JsonConfig jsonConfig=getTreeJsonConfig();//配置对象、循环策略、过滤字段、bean处理器
		TreeActionJsonBeanProcessor jsonBeanProcessor=new TreeActionJsonBeanProcessor();
		jsonBeanProcessor.setTreeActionJsonBeanHandler(new TreeActionJsonBeanHandler(){
			public void beanHandler(Object obj, Map map) {
				TjItemKind kind=(TjItemKind) obj;
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
	 *描述：动态查询列
	 *时间：2010-4-14
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findDynaColumnsAction() throws Exception{
		StringBuffer result=new StringBuffer();
		String kid=request.getParameter("kid");
		List<TjItemKindKey> keyList=this.tjSjwhManager.findTjItemKeyByKid(kid);
		List<TjInfoItem> itemList=this.tjSjwhManager.findTjInfoItemByKid(kid);
		for(TjItemKindKey key:keyList){
			result.append("{'"+key.getIname()+"':'"+key.getIcode()+"'},");
		}
		for(TjInfoItem item:itemList){
			result.append("{'"+item.getIname()+"':'"+item.getItem()+"'},");
		}
		if(StringUtils.isNotEmpty(result.toString())){
			result.deleteCharAt(result.length()-1);
			this.outputJson("{success:true,list:["+result.toString()+"]}");
		}else{
			this.outputJson("{success:true,list:[]}");
		}
		System.out.println(result.toString());
		return NONE;
	}
	
	/**
	 * 
	 *描述：动态查找数据
	 *时间：2010-4-14
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findDynaDataAction() throws Exception{
		String kid=request.getParameter("kid");
		String flid=request.getParameter("flid");//有可能是机组ID有可能是分类ID
		String keyList=request.getParameter("keyList");
		String jzVal=this.tjSjwhManager.getTjKndJZNameById(flid);
		
		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		// 开始分页查询
		Page page = this.tjSjwhManager.findDynaDataBykid(kid,jzVal,keyList.split("!"),Integer.parseInt(start), Integer.parseInt(limit));
		List<Map> obj = (List<Map>) page.getResult();
		
		String json="";
		long totalCount=0l;
		//主要针对初始化查询显示最晚一条数据
		if(keyList.equals("")){
			if(obj.size()>0){
				List<Map> lastList=new ArrayList<Map>();
				lastList.add(obj.get(0));
				json=JSONUtil.listToJson(lastList);
				totalCount=1l;
			}
		}else{
			// 将集合JSON化
			json = JSONUtil.listToJson(obj);
			totalCount=page.getTotalCount();
		}
		StringBuffer dataBlock = new StringBuffer();
		dataBlock.append("{totalCount:'" + totalCount + "',list:"
				+ json + "}");
		this.outputJson(dataBlock.toString());
		//System.out.println(json);
		return NONE;
	}
	
	/**
	 * 
	 *描述：查找操作状态,看能否使用该模块
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findOperateStateAction() throws Exception{
		String kid=request.getParameter("kid");
		boolean isOper=this.tjItemKindManager.findOperateState(kid);
		outputJson("{success:true,operate:"+isOper+"}");
		return NONE;
	}
	
	/**
	 * 
	 *描述：查看数据表中是否有数据
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String isExistDataAction() throws Exception{
		String kid=request.getParameter("kid");
		String flid=request.getParameter("flid");
		boolean isExist=this.tjSjwhManager.isExistData(kid, flid);
		outputJson("{success:true,operate:"+isExist+"}");
		return NONE;
	}
	
	/**
	 * 
	 *描述：假如是第一次统计数据，那么提供日期界面给用户选择
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showSelectDatePageAction() throws Exception{
		String kid=request.getParameter("kid");
		List<TjItemKindKey> keyList = this.tjSjwhManager.findTjItemKeyByKid(kid);
		List<String> keyCodeList=new ArrayList<String>();
		for(TjItemKindKey key:keyList){
			String code=key.getIcode();
			if(!"JZ".equalsIgnoreCase(code)){
				keyCodeList.add(code);
			}
		}
		String keyString=this.tjSjwhManager.createSelectKey(keyCodeList);
		request.setAttribute("selectKey",keyString);
		return "showSelectDatePageAction";
	}
	
	/**
	 * 
	 *描述：查找初始化日期
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findInitDataAction() throws Exception{
		String kid=request.getParameter("kid");
		String flid=request.getParameter("flid");
		String jzVal=this.tjSjwhManager.getTjKndJZNameById(flid);
		
		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		// 开始分页查询
		Page page = this.tjSjwhManager.findDynaDataBykid(kid,jzVal,"".split(","),Integer.parseInt(start), Integer.parseInt(limit));
		List<Map> data = (List<Map>) page.getResult();
		
		if(data.size()>0){
			//得到关键字列表
			List<TjItemKindKey> keyList=this.tjSjwhManager.findTjItemKeyByKid(kid);
			List<String> keyCodeList=new ArrayList<String>();
			for(TjItemKindKey key:keyList){
				String code=key.getIcode();
				if(!"JZ".equalsIgnoreCase(code)){
					keyCodeList.add(code);
				}
			}
			//得到第一条记录,也就是时间最晚的一条
			Map<String,String> map=data.get(0);
			String initData=this.tjSjwhManager.getDefaultKeyData(map, keyCodeList);
			outputJson("{success:true,initData:'"+initData+"'}");
		}else{
			outputJson("{success:false}");
		}
		return NONE;
	}
	
	/**
	 * 
	 *描述：显示增加或修改界面
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showAddAction() throws Exception{
		String kid=request.getParameter("kid");
		String flid=request.getParameter("flid");//分类ID或机组ID
		String keydata=request.getParameter("initdata");
		StringBuffer keyAll=new StringBuffer();
		boolean isRoot=this.isCurrentRootUser();//是否是root用户
		boolean isExistJz=false;//是否存在机组
		//得到关键字列表
		List<TjItemKindKey> keyList=this.tjSjwhManager.findTjItemKeyByKid(kid);
		for(TjItemKindKey key:keyList){
			List<KeyModel> list=new ArrayList<KeyModel>();
			if(TjInterFace.NIAN.equalsIgnoreCase(key.getIcode())){
				list=this.tjSjwhManager.getYearList();
			}else if(TjInterFace.YUE.equalsIgnoreCase(key.getIcode())){
				list=this.tjSjwhManager.getMonthList();
			}else if(TjInterFace.RI.equalsIgnoreCase(key.getIcode())){
				list=this.tjSjwhManager.getDayList(keydata);
			}else if(TjInterFace.JZ.equalsIgnoreCase(key.getIcode())){
				list=this.tjSjwhManager.getJzList(kid);
				//修改的时候keydata中已经包含机组，所以只有增加的时候才会用到isExistJz
				if(keydata.indexOf(TjInterFace.JZ)==-1){
					isExistJz=true;
				}
			}
			//只有root用户才可以选择关键字条件
			if(isRoot){
				String json=JSONUtil.listToJson(list,new String[]{"displayValue","value"});
				keyAll.append(""+key.getIcode()+":["+json+"],");
			}
		}
		
		if(isExistJz){
			String jz=this.tjSjwhManager.getTjKndJZNameById(flid);
			keydata=keydata+"!"+TjInterFace.JZ+","+jz;
		}
		///所有关键字字段
		keyAll.append("keydata:\""+keydata+"\"");
		//查找所有的普通字段
		String fieldAll=this.tjSjwhManager.initFieldData(kid);
		//普通字段的值
		String fieldValues=this.tjSjwhManager.findDynaDataBykidAndKeys(kid, keydata.split("!"));
		//String valuesjson=JSONUtil.listToJson(fieldValues);
		
		
		request.setAttribute("keyAll","{"+keyAll.toString()+"}");
		request.setAttribute("fieldAll", "["+fieldAll+"]");
		request.setAttribute("fieldValues","{data:["+fieldValues+"]}");
		//System.out.println("{data:["+fieldValues+"]}");
		request.setAttribute("kid", kid);
		request.setAttribute("flid", flid);
		request.setAttribute("isRoot", isRoot);
		return "showAddAction";
	}
	
	/**
	 * 
	 *描述：操作完毕后使该模块可用
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String updateOperateStateAction() throws Exception{
		String kid=request.getParameter("kid");
		boolean isOper=this.tjItemKindManager.updateOperateState(kid);
		outputJson("{success:true,operate:"+isOper+"}");
		return NONE;
	}
	
	/**
	 * 选择日期时改变日的值
	 *描述：
	 *时间：2010-4-15
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findLastDayAction() throws Exception{
		String curDate=request.getParameter("curDate");
		String strDate=DateUtils.getLastDate(curDate);
		Date date=DateUtils.StrToDate(strDate, "");
		String day=DateUtils.getDate(date,"dd");
		outputJson("{success:true,day:'"+day+"'}");
		return NONE;
	}
	
	/**
	 * 
	 *描述：取数
	 *时间：2010-4-16
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getDataAllAction() throws Exception{
		String kid=request.getParameter("kid");
		String keys=request.getParameter("keyList");
		String fields=request.getParameter("fieldList");
		String excelPath=request.getSession().getServletContext().getRealPath("/jteap/jhtj/excel");
		//删除原来的记录
		this.tjSjwhManager.deleteTempTable(kid);
		//增加新的关键字字段记录
		this.tjSjwhManager.addTempTable(kid, keys.split("!"));
		//把页面上面的值保存到数据库中
		this.tjSjwhManager.beforeGetDataSave(kid, keys.split("!"), fields.split("!"),"TEMP_");
		//增加其他系统字段记录
		this.tjSjwhManager.getOtherSystem(kid, keys.split("!"));
		//执行dll取数
		this.tjSjwhManager.executeDllData(kid, excelPath, keys.split("!"));
		//调用存储过程
		this.tjSjwhManager.executeProcData(kid);
		//返回数据
		String json=this.tjSjwhManager.findTempTableData(kid,fields.split("!"));
		//System.out.println(json);
		outputJson(json);
		return NONE;
	}
	
	/**
	 * 
	 *描述：计算
	 *时间：2010-4-17
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String computeDataAction() throws Exception{
		String kid=request.getParameter("kid");
		String keys=request.getParameter("keyList");
		String fields=request.getParameter("fieldList");
		//删除原来的记录
		this.tjSjwhManager.deleteTempTable(kid);
		//增加新的关键字字段记录
		this.tjSjwhManager.addTempTable(kid, keys.split("!"));
		//把页面上面的值保存到数据库中
		this.tjSjwhManager.beforeGetDataSave(kid, keys.split("!"), fields.split("!"),"TEMP_");
		//调用存储过程
		this.tjSjwhManager.executeProcData(kid);
		//返回数据
		String json=this.tjSjwhManager.findTempTableData(kid,fields.split("!"));
		//System.out.println(json);
		outputJson(json);
		return NONE;
	}
	
	/**
	 * 
	 *描述：保存或更新
	 *时间：2010-4-17
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String saveOrUpdateAction() throws Exception{
		String kid=request.getParameter("kid");
		String keys=request.getParameter("keyList");
		String fields=request.getParameter("fieldList");
		boolean isSoU=this.tjSjwhManager.saveOrUpdateData(kid,keys.split("!"),fields.split("!"));
		outputJson("{success:"+isSoU+"}");
		return NONE;
	}
	
	/**
	 * 
	 *描述：动态生成查询面板的条件
	 *时间：2010-4-20
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String dynaAddSearPanelAction() throws Exception{
		String flflag=request.getParameter("flflag");
		StringBuffer findWhere=new StringBuffer();
		StringBuffer defaultValue=new StringBuffer();
		//日数据
		if("1".equals(flflag)){
			List<KeyModel> year=this.tjSjwhManager.getYearList();
			String yearJson=JSONUtil.listToJson(year,new String[]{"displayValue","value"});
			findWhere.append(""+TjInterFace.NIAN+":["+yearJson+"],");

			List<KeyModel> month=this.tjSjwhManager.getMonthList();
			String monthJson=JSONUtil.listToJson(month,new String[]{"displayValue","value"});
			findWhere.append(""+TjInterFace.YUE+":["+monthJson+"]");
			
			String defaultYear=DateUtils.getDate("yyyy");
			defaultValue.append(TjInterFace.NIAN+"value:'"+defaultYear+"',");
			String defaultMonth=DateUtils.getDate("MM");
			defaultValue.append(TjInterFace.YUE+"value:'"+(defaultMonth.charAt(0)=='0'?defaultMonth.substring(1):defaultMonth)+"'");
		}else if("2".equals(flflag)){
			List<KeyModel> year=this.tjSjwhManager.getYearList();
			String yearJson=JSONUtil.listToJson(year,new String[]{"displayValue","value"});
			findWhere.append(""+TjInterFace.NIAN+":["+yearJson+"]");
			
			String defaultYear=DateUtils.getDate("yyyy");
			defaultValue.append(TjInterFace.NIAN+"value:'"+defaultYear+"'");
		}
		String json="{success:true,"+findWhere.toString()+","+defaultValue.toString()+"}";
		System.out.println(json);
		outputJson(json);
		return NONE;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "kname";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		List<TjItemKind> list=tjItemKindManager.getRootList();
		//Person person = personManager.getCurrentPerson(sessionAttrs);
		//if(!this.isCurrentRootUser()){
		//	list=this.tjItemKindManager.filterKindListByQx(person, list);
		//}
		return list;
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
		return tjItemKindManager;
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
}
