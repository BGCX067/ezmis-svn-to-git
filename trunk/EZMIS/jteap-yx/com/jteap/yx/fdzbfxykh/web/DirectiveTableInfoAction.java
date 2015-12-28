/*
 * Copyright 2010 [Techhero.JTEAP], Inc. All rights reserved.
 * Website: http://www.techhero.com.cn
 */

package com.jteap.yx.fdzbfxykh.web;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.yx.fdzbfxykh.manager.DirectiveColumnInfoManager;
import com.jteap.yx.fdzbfxykh.manager.DirectivePhysicManager;
import com.jteap.yx.fdzbfxykh.manager.DirectiveTableInfoManager;
import com.jteap.yx.fdzbfxykh.model.DirectiveColumnInfo;
import com.jteap.yx.fdzbfxykh.model.DirectiveTableInfo;
import com.jteap.yx.jjb.manager.JiaoJieBanManager;
import com.jteap.yx.jjb.model.JiaoJieBan;

/**
 * 小指标表定义Action
 * @author caihuiwen
 */
/**
 * @author Mr.W
 *
 */
@SuppressWarnings({"unchecked","serial"})
public class DirectiveTableInfoAction extends AbstractTreeAction<DirectiveTableInfo> {
	
	private DirectiveTableInfoManager directiveTableInfoManager;
	private DirectivePhysicManager directivePhysicManager;
	private DirectiveColumnInfoManager directiveColumnInfoManager;
	private DictManager dictManager;
	private JiaoJieBanManager jiaoJieBanManager;
	
	public void setJiaoJieBanManager(JiaoJieBanManager jiaoJieBanManager) {
		this.jiaoJieBanManager = jiaoJieBanManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	public void setDirectiveColumnInfoManager(
			DirectiveColumnInfoManager directiveColumnInfoManager) {
		this.directiveColumnInfoManager = directiveColumnInfoManager;
	}

	public void setDirectivePhysicManager(
			DirectivePhysicManager directivePhysicManager) {
		this.directivePhysicManager = directivePhysicManager;
	}

	public void setDirectiveTableInfoManager(DirectiveTableInfoManager directiveTableInfoManager) {
		this.directiveTableInfoManager = directiveTableInfoManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return null;
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		Collection<DirectiveTableInfo> list = directiveTableInfoManager.findSortnoAll();
		return list;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortno";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "tableName";
	}

	@Override
	public HibernateEntityDao getManager() {
		return directiveTableInfoManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{
			"id","tableCode","tableName","remark","sortno"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{
			"id","tableCode","tableName","remark","sortno"
		};
	}
	
	/**
	 * 删除小指标表定义、删除小指标的字段定义
	 * @return
	 */
	public String deleteTableInfoAction(){
		String id = request.getParameter("id");
		String tableCode = directiveTableInfoManager.deleteDirectiveTableInfo(id);
		try {
			this.outputJson("{success:true,tableCode:'" + tableCode + "'}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 删除小指标对应的物理记录表
	 * @return
	 */
	public String deletePhysicTableAction(){
		String schema = SystemConfig.getProperty("jdbc.schema");
		String tableCode = request.getParameter("tableCode");
		
		try {
			if(tableCode != null){
				tableCode = "TB_YX_DIRECTIVE_" + StringUtil.upperCase(tableCode);
				directivePhysicManager.deletePhysicTable(tableCode, schema);
				this.outputJson("{success:true}");
			}else{
				this.outputJson("{success:false}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 同步物理表结构
	 * @param tableInfo
	 */
	public void rebuildPhysicTable(DirectiveTableInfo tableInfo){
		String schema = SystemConfig.getProperty("jdbc.schema");
		String tableCode = tableInfo.getTableCode();
		
		if(tableInfo.getModifyRec() == null){
			//如果该表定义的 修改记录为空 就重建该表
			List<DirectiveColumnInfo> list = directiveColumnInfoManager.findByTableId(tableInfo.getId());
			directivePhysicManager.rebuildTable(schema, tableCode, list);
		}else {
			//有修改记录 就执行该修改记录
			try {
				directivePhysicManager.executeSqlBatch(tableInfo.getModifyRec());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//将该表定义的修改记录清空
		tableInfo.setModifyRec("");
		//将该表定义的同步状态设为 已同步  1
		tableInfo.setFinalManuscript(true);
		directiveTableInfoManager.save(tableInfo);
	}
	
	/**
	 * 同步物理表结构
	 * @return
	 * @throws Exception
	 */
	public String rebuildPhysicTableAction(){
		String id = request.getParameter("directiveId");
		DirectiveTableInfo tableInfo = directiveTableInfoManager.get(id);
		if(tableInfo != null){
			rebuildPhysicTable(tableInfo);
			try {
				outputJson("{success:true}");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return NONE;
	}
	
	/**
	 * 构建小指标查询页面的 列模型
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("deprecation")
	public String buildColsAction() throws SQLException{
		
		String tableCode = request.getParameter("tableCode");
		String schema = SystemConfig.getProperty("jdbc.schema");
		boolean isRebuildPhysic = false;
		
		SimpleDateFormat  dateFormatYear = new SimpleDateFormat("yyyy年");
		Date nowDate = new Date();
		String nowYear = dateFormatYear.format(nowDate);
		
		List<DirectiveTableInfo> tableInfoList = directiveTableInfoManager.findBy("tableCode", tableCode);
		if(tableInfoList.size() < 1 ){
			try {
				this.outputJson("{success:true,existTable:false}");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return NONE;
		}
		DirectiveTableInfo tableInfo = tableInfoList.get(0);
		
		tableCode = "TB_YX_DIRECTIVE_" + StringUtil.upperCase(tableCode);
		//如果该表不存在,自动同步物理表
		if(!directivePhysicManager.isExist(schema, tableCode)){
			rebuildPhysicTable(tableInfo);
			isRebuildPhysic = true;
		//如果存在,但未同步物理表,自动同步物理表
		}else if(!tableInfo.getFinalManuscript()){
			rebuildPhysicTable(tableInfo);
			isRebuildPhysic = true;
		}
		
		String hql = "from DirectiveColumnInfo t where t.directiveId=? order by t.sortno asc";
		List<DirectiveColumnInfo> columnInfoList = directiveColumnInfoManager.find(hql, tableInfo.getId());
		
		//列模型 map
		Map<String, String> colMap = new LinkedHashMap<String, String>();
		//时间 当前年份
		colMap.put("zbsj", nowYear);
		//值别
		colMap.put("zbzb", "值别");
		//循环添加列模型
		for(DirectiveColumnInfo columnInfo : columnInfoList){
			colMap.put(columnInfo.getDirectiveCode(), columnInfo.getDirectiveName());
		}
		
		String colJson = JSONUtil.mapToJson(colMap);
		try {
			if(isRebuildPhysic){
				//如果自动同步物理表,给出提示
				this.outputJson("{success:true,existTable:true,existPhysic:false,cols:" + colJson + "}");
			}else {
				this.outputJson("{success:true,existTable:true,existPhysic:true,cols:" + colJson + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 构建小指标查询页面的 行数据
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String buildRowsAction(){
		String tableCode = request.getParameter("tableCode");
		String zbsj = request.getParameter("zbsj");
		
		//行数据源
		List<Map> rowsList = getRowsList(tableCode,zbsj);
		
		String rowsJson = JSONUtil.listToJson(rowsList);
		try {
			this.outputJson("{list:" + rowsJson + ",totalCount:" + rowsList.size() + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 获取行数据List      			
	 * @param tableCode
	 * @param columnInfoList	
	 * @return	
	 */
	private List<Map> getRowsList(String tableCode, String zbsj){
		List<Map> rowsList = new ArrayList<Map>();
		
		List<DirectiveTableInfo> tableInfoList = directiveTableInfoManager.findBy("tableCode", tableCode);
		if(tableInfoList.size() < 1 ){
			return rowsList;
		}
		DirectiveTableInfo tableInfo = tableInfoList.get(0);
		
		//指标字段定义list
		String hql = "from DirectiveColumnInfo t where t.directiveId=? order by t.sortno asc";
		List<DirectiveColumnInfo> columnInfoList = directiveColumnInfoManager.find(hql, tableInfo.getId());
		
		rowsList = (List<Map>)directivePhysicManager.buildRowData(zbsj, tableCode, columnInfoList);
		
		return rowsList;
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

		String tableCode = request.getParameter("tableCode");
		String zbsj = request.getParameter("zbsj");
		
		List<Map> list = getRowsList(tableCode,zbsj);
		
		// 调用导出方法
		export(list, paraHeader, paraDataIndex, paraWidth);

		return NONE;
	}
	
	/**
	 * 构建小指标汇总 单表列模型
	 * @return
	 * @throws SQLException 
	 */
	public String buildSumColsAction() throws Exception{
		//机组号
		int jzNum = Integer.parseInt(request.getParameter("jzNum"));
		
		//指标表定义名称
		String tableCode = "JZGZXZB_" + jzNum;
		String schema = SystemConfig.getProperty("jdbc.schema");
		boolean isRebuildPhysic = false;
		
		List<DirectiveTableInfo> tableInfoList = directiveTableInfoManager.findBy("tableCode", tableCode);
		if(tableInfoList.size() < 1 ){
			try {
				this.outputJson("{success:true,existTable:false}");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return NONE;
		}
		DirectiveTableInfo tableInfo = tableInfoList.get(0);
		
		tableCode = "TB_YX_DIRECTIVE_" + tableCode;
		//如果该表不存在,自动同步物理表
		if(!directivePhysicManager.isExist(schema, tableCode)){
			rebuildPhysicTable(tableInfo);
			isRebuildPhysic = true;
		//如果存在,但未同步物理表,自动同步物理表
		}else if(!tableInfo.getFinalManuscript()){
			rebuildPhysicTable(tableInfo);
			isRebuildPhysic = true;
		}
		
		//指标字段定义list
		String hql = "from DirectiveColumnInfo t where t.directiveId=? order by t.sortno asc";
		List<DirectiveColumnInfo> columnInfoList = directiveColumnInfoManager.find(hql, tableInfo.getId());
		
		//列模型 map
		Map<String, String> colMap = new LinkedHashMap<String, String>();
		colMap.put("directiveSum", "值别");
		for(DirectiveColumnInfo columnInfo : columnInfoList){
			String directiveCode = columnInfo.getDirectiveCode();
			
			String directiveName = columnInfo.getDirectiveName();
			if(columnInfo.getSumOrAvg() != null){
				directiveName = columnInfo.getSumOrAvg() + ":" + directiveName;
			}
			colMap.put(directiveCode, directiveName);
		}
		
		String colJson = JSONUtil.mapToJson(colMap);
		try {
			if(isRebuildPhysic){
				//如果自动同步物理表,给出提示
				this.outputJson("{success:true,existTable:true,existPhysic:false,cols:" + colJson + "}");
			}else {
				this.outputJson("{success:true,existTable:true,existPhysic:true,cols:" + colJson + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
	
	/**
	 * 构建指标汇总数据源
	 * @return
	 */
	public String buildSumRowsAction(){
		//机组号
		int jzNum = Integer.parseInt(request.getParameter("jzNum"));
		//时间 (月度)
		String zbsj = request.getParameter("zbsj");
		
		//指标表定义名称
		String tableCode = "JZGZXZB_" + jzNum;
		
		//行数据源
		List<Map> rowsList = getSumRowsList(tableCode,zbsj);
		String rowsJson = JSONUtil.listToJson(rowsList);
		try {
			this.outputJson("{list:" + rowsJson + ",totalCount:" + rowsList.size() + "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 获取指标汇总数据List      			
	 * @param tableCode
	 * @param columnInfoList	
	 * @return	
	 */
	private List<Map> getSumRowsList(String tableCode, String zbsj){
		List<Map> rowsList = new ArrayList<Map>();
		
		DirectiveTableInfo tableInfo = directiveTableInfoManager.findUniqueBy("tableCode", tableCode);
		
		//指标字段定义list
		String hql = "from DirectiveColumnInfo t where t.directiveId=? order by t.sortno asc";
		List<DirectiveColumnInfo> columnInfoList = directiveColumnInfoManager.find(hql, tableInfo.getId());
		
		//rowsList = (List<Map>)directivePhysicManager.buildSumRowData(zbsj, tableCode, columnInfoList);
		rowsList = (List<Map>)directivePhysicManager.buildSumRowData2(zbsj, tableCode, columnInfoList);
		
		return rowsList;
	}
	
	/**
	 * 导出总计Excel动作
	 * @return
	 * @throws Exception
	 */
	public String exportSumExcel() throws Exception {
		
		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = request.getParameter("paraHeader");
		
		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");

		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");
		
		//机组号
		int jzNum = Integer.parseInt(request.getParameter("jzNum"));
		//时间 (月度)
		String zbsj = request.getParameter("zbsj");
		
		//指标表定义名称
		String tableCode = "JZGZXZB_" + jzNum;
		
		List<Map> list = getSumRowsList(tableCode,zbsj);
		
		// 调用导出方法
		export(list, paraHeader, paraDataIndex, paraWidth);

		return NONE;
	}
	
	/**
	 * 小指标取数
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String xzbQsAction(){
		String jizuNum = request.getParameter("jizuNum");
		String qushuYm = request.getParameter("qushuYm");
		String qushuYm2 = request.getParameter("qushuYm2");
		String tableCode = request.getParameter("tableCode");
		
		if("1".equals(jizuNum) || "2".equals(jizuNum)){
			//处理动态取数表的日期后缀
			String qushuY = qushuYm2.substring(0,4);
			String qushuM = qushuYm2.substring(4,6);
			int intM = Integer.parseInt(qushuM);
			
			qushuYm2 = qushuY + intM;
		}
		
		
		DirectiveTableInfo tableInfo = directiveTableInfoManager.findUniqueBy("tableCode", tableCode);
		List<DirectiveColumnInfo> columnList = directiveColumnInfoManager.findBy("directiveId", tableInfo.getId());
		
		Dict dictYeB = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "ye_beginTime");
		Dict dictYeE = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "ye_endTime");
		Dict dictBaiB = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "bai_beginTime");
		Dict dictBaiE = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "bai_endTime");
		Dict dictZhongB = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "zhong_beginTime");
		Dict dictZhongE = dictManager.findDictByCatalogNameWithKey("zbbc_sj_dy", "zhong_endTime");
		
		//当前天
		String nowYmd = DateUtils.getDate(new Date(), "yyyy-MM-dd");
		//取数月的第一天
		String qushuBegin = qushuYm + "-01";
		//取数月的最后一天
		String qushuEnd = DateUtils.getLastDate(qushuBegin);
		//第一天与最后一天之间相差的天数
		int subDate = DateUtils.compareStringTimes(qushuEnd, qushuBegin, "yyyy-MM-dd");
		
		//遍历查询月份小指标数据
		for (int i = 1; i < subDate+2 ; i++) {
			String qushuYmd = qushuYm + "-";
			if(i < 10){
				qushuYmd += "0" + i;
			}else {
				qushuYmd += i;
			}
			
			//根据取数时间Ymd 查询值班记录,获取一天3个值别
			List<JiaoJieBan> jjbList = jiaoJieBanManager.findByYmd(qushuYmd);
			for (int j = 0; j < jjbList.size(); j++) {
				//值班班次
				String zbbc = jjbList.get(j).getJiebanbc();
				String beginTime = qushuYmd + " ";
				String endTime = qushuYmd + " ";
				if("夜班".equals(zbbc)){
					beginTime += dictYeB.getValue();
					endTime += dictYeE.getValue();
				}else if("白班".equals(zbbc)){
					beginTime += dictBaiB.getValue();
					endTime += dictBaiE.getValue();
				}else if("中班".equals(zbbc)){
					beginTime += dictZhongB.getValue();
					endTime += dictZhongE.getValue();
				}
					
				//值班值别
				String zbzb = jjbList.get(j).getJiebanzb();
				//以时间ymd、值班值别 查询是否存在该条小指标记录,判断修改或保存
				String id = directivePhysicManager.findJzXzb_Id(tableCode, qushuYmd, zbzb);
				
				//ezmis小指标表中有该条记录、或者为当前天 不做取数操作
				if(!"".equals(id) || qushuYmd.equals(nowYmd)){
					break;
				}
				
				Map<String, String> rowMap = new HashMap<String, String>();
				
				if("1".equals(jizuNum) || "2".equals(jizuNum)){
					//1、2号机组取sis表数据
					rowMap = directivePhysicManager.findDataBySs(jizuNum, columnList, qushuYm2, beginTime, endTime);
				}else if("3".equals(jizuNum) || "4".equals(jizuNum)){
					//3、4号机组取sis表数据
					rowMap = directivePhysicManager.findDataBySis(jizuNum, columnList, qushuYm2, beginTime, endTime);
				}
				directivePhysicManager.getFdlByJiziNumAndBc(jizuNum,zbbc,qushuYmd,rowMap);
				//保存
				directivePhysicManager.saveJzXzb(tableCode, qushuYmd, zbzb, rowMap);
			}
		}
		
		try {
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
}
