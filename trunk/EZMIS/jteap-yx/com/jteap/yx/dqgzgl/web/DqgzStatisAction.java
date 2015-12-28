/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.dqgzgl.web;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.yx.dqgzgl.manager.DqgzHandleManager;
import com.jteap.yx.dqgzgl.model.DqgzHandle;

/**
 * 定期工作月统计报表Action
 * @author caihuiwen
 */
@SuppressWarnings("serial")
public class DqgzStatisAction extends AbstractAction{
	
	private DictManager dictManager;
	private DqgzHandleManager dqgzHandleManager;
	
	public void setDqgzHandleManager(DqgzHandleManager dqgzHandleManager) {
		this.dqgzHandleManager = dqgzHandleManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		return null;
	}
	
	/**
	 * 定期工作月统计报表 List
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map> yueTongJiList(){
		
		List<Map> resultList = new ArrayList<Map>();
		
		//发送数汇总
		int setSendSum = 0;
		//已完成数汇总
		int handleYSum = 0;
		//未完成数汇总
		int handleWSum = 0;
		//汇总完成率
		double wanclSum = 0.0;
		
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		//部门
		String fzbm = request.getParameter("fzbm");
		//年月
		String ny = request.getParameter("ny");
		
		//获得所有定期工作规律
		Collection<Dict> dictList = dictManager.findDictByUniqueCatalogName("dqgzgl");
		for (Dict dict : dictList) {
			String gzgl = dict.getValue();
			String gzglKey = dict.getKey();
			
			//发送数
			int setSendNum = 0;
			//已完成数
			int handleYNum = 0;
			//未完成数
			int handleWNum = 0;
			//完成率
			double wancl = 0.0;
			
			//定期工作发送
			String hqlSend = "from DqgzHandle d where d.gzgl='" + gzgl + "'";
			
			if(fzbm != null && !fzbm.equals("")){
				hqlSend += " and d.fzbm='" + fzbm + "'";
			}
			if(ny == null){
				//如果月份为空 则取当前时间的年月
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
				hqlSend += " and to_char(d.dqgzCreateDt,'yyyy-mm')='" + dateFormat.format(new Date()) + "'";
			}else {
				hqlSend += " and to_char(d.dqgzCreateDt,'yyyy-mm')='" + ny +"'";
			}
			
			List<DqgzHandle> sendList = dqgzHandleManager.find(hqlSend);
			/** 发送数 */
			setSendNum = sendList.size();
			setSendSum += setSendNum;
			
			//定期工作处理 已完成
			hqlSend += " and d.status='已完成'";
			List<DqgzHandle> handleYList = dqgzHandleManager.find(hqlSend);
			/** 已完成数 */
			handleYNum = handleYList.size();
			handleYSum += handleYNum;
			
			/** 未完成数 */
			handleWNum = setSendNum - handleYNum;
			handleWSum += handleWNum;
			
			/** 完成率 */
			if(handleYNum > 0){
				wancl = (double)handleYNum / setSendNum * 100;
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("gzglKey", gzglKey);
			map.put("gzgl", gzgl);
			map.put("setSendNum", setSendNum);
			map.put("handleYNum", handleYNum);
			map.put("handleWNum", handleWNum);
			map.put("wancl", decimalFormat.format(wancl) + "%");
			resultList.add(map);
			
		}
		/** 汇总 */
		Map<String, Object> sumMap = new HashMap<String, Object>();
		sumMap.put("gzglKey", "合计");
		sumMap.put("setSendNum", setSendSum);
		sumMap.put("handleYNum", handleYSum);
		sumMap.put("handleWNum", handleWSum);
		if(handleYSum > 0){
			wanclSum = (double)handleYSum / setSendSum * 100;
		}
		sumMap.put("wancl", decimalFormat.format(wanclSum) + "%");
		resultList.add(sumMap);
		
		return resultList;
	}
	
	/**
	 * 定期工作月统计报表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String yueTongJiAction(){
		try {
			// 每页大小
			String limit = request.getParameter("limit");
			if (StringUtils.isEmpty(limit))
				limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");
	
			// 开始索引
			String start = request.getParameter("start");
			if (StringUtils.isEmpty(start))
				start = "0";
			
			List<Map> resultList = (List<Map>)yueTongJiList();
			
			//分页
			int startIndex = Integer.parseInt(start);
			int limitIndex = Integer.parseInt(limit) + startIndex;
			if(limitIndex > resultList.size()){
				limitIndex = resultList.size();
			}
			List<Map> pageList = resultList.subList(startIndex, limitIndex);
			
			String json = JSONUtil.listToJson(pageList);
			json = "{totalCount:'" + resultList.size() + "',list:"+ json + "}";
		
			this.outputJson(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
	
	/**
	 * 导出Excel动作
	 * @return
	 * @throws Exception
	 */
	public String exportExcel() throws Exception {

		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = request.getParameter("paraHeader");
		
		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");

		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");

		List<Map> list = yueTongJiList();

		// 调用导出方法
		export(list, paraHeader, paraDataIndex, paraWidth);

		return NONE;
	}
	
}
