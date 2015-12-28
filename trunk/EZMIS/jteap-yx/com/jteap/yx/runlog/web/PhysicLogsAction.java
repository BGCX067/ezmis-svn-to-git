/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.runlog.web;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.yx.runlog.manager.LogsColumnInfoManager;
import com.jteap.yx.runlog.manager.LogsTableInfoManager;
import com.jteap.yx.runlog.manager.PhysicLogsManager;
import com.jteap.yx.runlog.model.LogsColumnInfo;
import com.jteap.yx.runlog.model.LogsTableInfo;

/**
 * 运行日志物理表操作Action
 * @author caihuiwen
 */
@SuppressWarnings({"serial","unchecked"})
public class PhysicLogsAction extends AbstractAction{
	
	private PhysicLogsManager physicLogsManager;
	private LogsTableInfoManager logsTableInfoManager;
	private LogsColumnInfoManager logsColumnInfoManager;
	
	public void setLogsColumnInfoManager(LogsColumnInfoManager logsColumnInfoManager) {
		this.logsColumnInfoManager = logsColumnInfoManager;
	}

	public void setLogsTableInfoManager(LogsTableInfoManager logsTableInfoManager) {
		this.logsTableInfoManager = logsTableInfoManager;
	}

	public void setPhysicLogsManager(PhysicLogsManager physicLogsManager) {
		this.physicLogsManager = physicLogsManager;
	}
	
	private String json;
	private String tableId;
	private String zbbc;
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getZbbc() {
		return zbbc;
	}

	public void setZbbc(String zbbc) {
		this.zbbc = zbbc;
	}

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
	 * 保存或更新日志
	 * @return
	 */
	public String saveOrUpdateLogsAction(){
		
		String json = request.getParameter("json");
		String tableId = request.getParameter("tableId");
		String zbbc = request.getParameter("zbbc");
		String zbsj = request.getParameter("zbsj");
		
		List<Map<String, String>> listMap = JSONUtil.parseList(json);
		LogsTableInfo tableInfo = logsTableInfoManager.get(tableId);
		
		//当前班次的采样点
		Map<String, Object> caiMap = physicLogsManager.findCaiYangMap(tableInfo.getCaiyangdian(),zbbc);
		List<String> caiyangList = (List<String>) caiMap.get("caiyangList");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat hmsFormat = new SimpleDateFormat("HH:mm:ss");
		
		DecimalFormat decimalFormat = new DecimalFormat("0.00"); 
		//保存时 取系统当前时间
		String nowTime = dateFormat.format(new Date());
		
		for(Map<String, String> jsonMap : listMap){
			String columnId = jsonMap.get("columnId");
			String id = jsonMap.get("id_" + columnId);
			//当前登录人Id
			String curPersonId=(String)request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSONID);
			
			Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
			if(id != null && !id.trim().equals("")){
				rowMap.put("ID", id);
			}else {
				rowMap.put("ID", UUIDGenerator.hibernateUUID());
			}
			
			rowMap.put("ROW_ID", columnId);
			rowMap.put("PERSON_ID", curPersonId);
			rowMap.put("FILL_DATE", nowTime);
			rowMap.put("ZBSJ", zbsj + " " + hmsFormat.format(new Date()));
			
			double sumBan = 0;
			double avgBan = 0;
			//循环当前班次采样点获取字段名和字段值 VALUE_1,VALUE_2 ...
			for (int i = 0; i < caiyangList.size(); i++) {
				String caidian = caiyangList.get(i);
				String tdName = "value" + caidian + "_" + columnId;
				String tdValue = jsonMap.get(tdName);
				String columnCode = "VALUE_" + caidian;
				
				if(!tdValue.equals("")){
					try {
						//当前班次 累计值
						sumBan += Double.parseDouble(tdValue);
					} catch (Exception e) {
						sumBan = 0;
						e.printStackTrace();
					}
				}
				rowMap.put(columnCode, tdValue);
			}
			
			//当前班次 平均值
			avgBan = sumBan/caiyangList.size();
			
			if(zbbc.equals("夜班")){
				rowMap.put("SUM_BAN_0", decimalFormat.format(sumBan));
				rowMap.put("AVG_BAN_0", decimalFormat.format(avgBan));
			}else if(zbbc.equals("白班")){
				rowMap.put("SUM_BAN_1", decimalFormat.format(sumBan));
				rowMap.put("AVG_BAN_1", decimalFormat.format(avgBan));
			}else if(zbbc.equals("中班")){
				rowMap.put("SUM_BAN_2", decimalFormat.format(sumBan));
				rowMap.put("AVG_BAN_2", decimalFormat.format(avgBan));
			}
			
			try {
				if(id != null && !id.trim().equals("")){
					//修改
					physicLogsManager.updateLogs(tableInfo.getTableCode(), rowMap);
				}else {
					//保存
					physicLogsManager.saveLogs(tableInfo.getTableCode(),rowMap);
				}
				
				//获取当天记录累计值, 并更新到该条记录中去
				physicLogsManager.updateCountDay(tableInfo, columnId, zbsj);
				
				//获取当月记录累计值, 并更新到最后一条记录中去
				physicLogsManager.updateCountMonth(tableInfo, columnId, zbsj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			this.outputJson("{success:true}");
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
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

		//Excel表的头信息(逗号表达式),由于前台是通过window.open(url)来传输此中文,则需要通过转码
		StringBuffer paraHeader = new StringBuffer("记录项目,额定值,");
		//表索引信息(逗号表达式)
		StringBuffer paraDataIndex = new StringBuffer("columnName,edingzhi,");
		//宽度(逗号表达式)
		StringBuffer paraWidth = new StringBuffer("200,100,");
		
		List<Map<String, String>> listMap = JSONUtil.parseList(json);
		LogsTableInfo tableInfo = logsTableInfoManager.get(tableId);
		
		//当前班次的采样点
		Map<String, Object> caiMap = physicLogsManager.findCaiYangMap(tableInfo.getCaiyangdian(),zbbc);
		List<String> caiyangList = (List<String>) caiMap.get("caiyangList");
		
		for(int i=0; i<caiyangList.size(); i++){
			//追加表的头信息
			paraHeader.append(caiyangList.get(i) + ":00,");
			//追加表索引信息
			paraDataIndex.append("value" + caiyangList.get(i) + ",");
			//追加宽度
			paraWidth.append("100,");
		}
		paraHeader.append("班累计,班平均,全天累计,全天平均,月累计,月平均");
		paraDataIndex.append("sumBan,avgBan,sumDay,avgDay,sumMonth,avgMonth");
		paraWidth.append("100,100,100,100,100,100");
		
		/** 构建表身 */
		for(Map<String, String> jsonMap : listMap){
			String columnId = jsonMap.get("columnId");
			LogsColumnInfo columnInfo = logsColumnInfoManager.get(columnId);

			Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
			rowMap.put("columnName", columnInfo.getColumnName());
			rowMap.put("edingzhi", columnInfo.getEdingzhi());
			for (int i = 0; i < caiyangList.size(); i++) {
				String tdValue = jsonMap.get("value" + caiyangList.get(i) + "_" + columnId);
				rowMap.put("value" + caiyangList.get(i), tdValue);
			}
			rowMap.put("sumBan", jsonMap.get("sumBan_" + columnId));
			rowMap.put("avgBan", jsonMap.get("avgBan_" + columnId));
			rowMap.put("sumDay", jsonMap.get("sumDay_" + columnId));
			rowMap.put("avgDay", jsonMap.get("avgDay_" + columnId));
			rowMap.put("sumMonth", jsonMap.get("sumMonth_" + columnId));
			rowMap.put("avgMonth", jsonMap.get("avgMonth_" + columnId));
			
			list.add(rowMap);
		}
		
		// 调用导出方法
		export(list, paraHeader.toString(), paraDataIndex.toString(), paraWidth.toString());
		return NONE;
	}
	
	/**
	 * 运行日志取数
	 * @return
	 * @throws Exception 
	 */
	public String logsQsAction() throws Exception{
		String tableId = request.getParameter("tableId");
		String zbsj = request.getParameter("zbsj");
		String zbbc = request.getParameter("zbbc");
		//当前登录人Id
		String curPersonId = (String)request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSONID);
		
		//如果运行日志表中没有该时间、班次的记录,则从sis或ss表中取得数据
		int logState = physicLogsManager.logsQs(tableId, zbsj, zbbc, curPersonId);
		switch (logState) {
			case 1:
				this.outputJson("{success:true}");
				break;
			case 2:
				this.outputJson("{success:true,errMsg:'该日志配置表定义被删,请联系管理员...'}");
				break;
			case 3:
				this.outputJson("{success:true,errMsg:'该日志没有配置指标,请联系管理员...'}");
				break;
			case 4:
				this.outputJson("{success:true,errMsg:'取数成功,但该日志某些指标配置信息不全...'}");
				break;	
			default:
				break;
		}
		
		return NONE;
	}
	
}
