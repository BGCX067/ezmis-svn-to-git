package com.jteap.wz.gdzc.web;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.ArrayUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.form.dbdef.manager.DefColumnInfoManager;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.form.eform.manager.EFormFjManager;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.form.eform.model.EFormFj;
import com.jteap.form.eform.util.EFormExp;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.wz.gdzc.manager.GdzcdjJDBCManager;
import com.jteap.wz.gdzc.manager.GdzcdjManager;

@SuppressWarnings( { "serial", "unchecked", "unused" })
public class GdzcdjAction extends AbstractAction{

	private GdzcdjManager gdzcdjManager;
	private GdzcdjJDBCManager gdzcdjJDBCManager;

	private DictManager dictManager;
	private DataSource dataSource;
	
	private EFormManager eformManager;
	private EFormFjManager eformFjManager;
	private PhysicTableManager physicTableManager;
	private DefColumnInfoManager defColumnInfoManager;

	
	public DefColumnInfoManager getDefColumnInfoManager() {
		return defColumnInfoManager;
	}

	public void setDefColumnInfoManager(DefColumnInfoManager defColumnInfoManager) {
		this.defColumnInfoManager = defColumnInfoManager;
	}

	public PhysicTableManager getPhysicTableManager() {
		return physicTableManager;
	}

	public void setPhysicTableManager(PhysicTableManager physicTableManager) {
		this.physicTableManager = physicTableManager;
	}

	public EFormManager getEformManager() {
		return eformManager;
	}

	public void setEformManager(EFormManager eformManager) {
		this.eformManager = eformManager;
	}

	public GdzcdjManager getGdzcdjManager() {
		return gdzcdjManager;
	}

	public void setGdzcdjManager(GdzcdjManager gdzcdjManager) {
		this.gdzcdjManager = gdzcdjManager;
	}

	public GdzcdjJDBCManager getGdzcdjJDBCManager() {
		return gdzcdjJDBCManager;
	}

	public void setGdzcdjJDBCManager(GdzcdjJDBCManager gdzcdjJDBCManager) {
		this.gdzcdjJDBCManager = gdzcdjJDBCManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	/**
	 * 物资领用申请流程-待处理
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showDclAction()throws Exception{
//		String LYDQF = request.getParameter("LYDQF");
		/** ************************ */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		// 角色（专业）过滤
		// String userRole =
		// sessionAttrs.get(Constants.SESSION_CURRENT_PERSONROLES).toString();
		// String[] userRoles = userRole.split(",");
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.FLOW_TOPIC, ");
			sbSql.append("a.ID as TASKTODOID, ");
			sbSql.append("a.FLOW_NAME, ");
			sbSql.append("a.FLOW_INSTANCE_ID, ");
			sbSql.append("a.CURRENT_TASKNAME, ");
			sbSql.append("a.POST_PERSON, ");
			sbSql.append("to_char(a.POST_TIME,'yyyy-MM-dd HH:mi:ss') as POST_TIME, ");
			sbSql.append("a.TOKEN, ");
			sbSql.append("a.CURSIGNIN, ");
			sbSql.append("b.*, ");
			sbSql.append("d.personname as curSignInName ");
			sbSql.append("from tb_wf_todo a, ");
			sbSql.append("TB_WZ_GDZCDJ b, ");
			sbSql.append("jbpm_variableinstance c, ");
			sbSql.append("tb_sys_person d ");
			sbSql.append("where a.current_process_person like '%");
			sbSql.append(userLoginName);
			sbSql.append("%' ");
			sbSql.append("and a.flag = '1'");
			sbSql.append("and a.flow_instance_id = c.processinstance_ ");
			sbSql.append("and b.id = c.stringvalue_ ");
			sbSql.append("and c.name_='docid' ");
			sbSql.append("and b.flow_status != '已作废' ");
			sbSql.append("and d.id(+) = a.cursignin ");
			String sql = sbSql.toString();
			Page page = gdzcdjJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ************************ */

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "FLOW_TOPIC", "TASKTODOID", "FLOW_NAME",
							"FLOW_INSTANCE_ID", "CURRENT_TASKNAME",
							"POST_PERSON", "POST_TIME", "TOKEN", "CURSIGNIN","CURSIGNINNAME",
							"ID",
							"ZCBM",
							"SQSJ",
							"time",
							"ZT",
							"CZYXM",
							"CZY",
							"FLOW_STATUS" });
			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 物资领用申请流程-已处理
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showYclAction()throws Exception{
//		String LYDQF = request.getParameter("LYDQF");
		/** ************************** */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			//sbSql.append("from TB_WZ_GDZCDJ a,");
			sbSql.append("from (select * from TB_WZ_GDZCDJ t " +
					"where t.id not in (select t.id from TB_WZ_GDZCDJ t " +
					"where t.flow_status is null and t.flow_status = '已作废')) a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			//sbSql.append("and a.flow_status is not null and a.flow_status != '已作废' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id) ");
			sbSql.append("and a.id not in ");
			sbSql.append("(select docid from tb_wf_todo f  where f.flow_instance_id = c.id_ and f.flag = '1' and f.CURRENT_PROCESS_PERSON like '%"
							+ userLoginName + "%')");
			String sql = sbSql.toString();
			Page page = gdzcdjJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ************************** */

			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 物资领用申请流程-草稿箱
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showCgxAction()throws Exception{
		/** ********************* */
//		String LYDQF = request.getParameter("LYDQF");
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from TB_WZ_GDZCDJ a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and e.task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' ");
			sbSql.append("and a.flow_status is null ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id) ");
			String sql = sbSql.toString();
			Page page = gdzcdjJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ********************* */

			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	@SuppressWarnings("unchecked")
	public String showZfAction()throws Exception{
		/*****************************/
//		String LYDQF = request.getParameter("LYDQF");
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		String userLoginName = sessionAttrs.get(
				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from TB_WZ_GDZCDJ a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.flow_status ='已作废' ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log where ");
			sbSql.append("task_login_name ='");
			sbSql.append(userLoginName);
			sbSql.append("' group by pi_id)");
			String sql = sbSql.toString();
			Page page = gdzcdjJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
		/*****************************/

		String json = JSONUtil.listToJson((List) page.getResult(),
				listJsonProperties());

		json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
				+ "}";
		this.outputJson(json);
	} catch (Exception ex) {
		this.outputJson("{success:false}");
		ex.printStackTrace();
	}
	return NONE;
	}
	
	@SuppressWarnings("unchecked")
	public String showAllListAction()throws Exception{
		/** ************************** */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";

		int iStart = Integer.parseInt(start);
		int iLimit = Integer.parseInt(limit);
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("select a.*,");
			sbSql.append("b.processinstance_,");
			sbSql.append("c.id_,");
			sbSql.append("c.version_,");
			sbSql.append("c.start_,");
			sbSql.append("c.end_,");
			sbSql.append("d.flow_name,");
			sbSql.append("d.id as FLOW_CONFIG_ID,");
			sbSql.append("d.flow_form_id ");
			sbSql.append("from TB_WZ_GDZCDJ a,");
			sbSql.append("jbpm_variableinstance b,");
			sbSql.append("jbpm_processinstance c,");
			sbSql.append("tb_wf_flowconfig d,");
			sbSql.append("tb_wf_log e ");
			sbSql.append("where b.name_ = 'docid' ");
			sbSql.append("and b.stringvalue_ = a.id ");
			sbSql.append("and b.processinstance_ = c.id_ ");
			sbSql.append("and c.processdefinition_ = d.pd_id ");
			sbSql.append("and e.pi_id = c.id_ ");
			sbSql.append("and a.flow_status is not null ");
			sbSql.append("and e.id in (select max(id) from tb_wf_log group by pi_id)");
			String sql = sbSql.toString();
			Page page = gdzcdjJDBCManager.pagedQueryTableData(sql, iStart, iLimit);
			/** ************************** */

			String json = JSONUtil.listToJson((List) page.getResult(),
					listJsonProperties());

			json = "{totalCount:'" + page.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 物资领用申请流程-作废处理
	 * @return
	 * @throws Exception
	 */
	public String cancelWzlyAction() throws Exception {
		String id = request.getParameter("id");
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			String sql = "update TB_WZ_GDZCDJ t set t.flow_status = '已作废' where t.id ='"
					+ id + "'";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.close();
			this.outputJson("{success:true}");
		} catch (Exception e) {
			this.outputJson("{success:false}");
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return NONE;
	}
	
	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return new String[]{
			"ID_", "VERSION_", "START_", "END_",
			"PROCESSINSTANCE_", "FLOW_NAME", "FLOW_CONFIG_ID",
			"FLOW_FORM_ID",
			"ID",
			"ZCBM",
			"SQSJ",
			"time",
			"ZT",
			"CZYXM",
			"CZY",
			"FLOW_STATUS"
		};
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return new String[]{
				"ID_", "VERSION_", "START_", "END_",
				"PROCESSINSTANCE_", "FLOW_NAME", "FLOW_CONFIG_ID",
				"FLOW_FORM_ID",
				"ID",
				"ZCBM",
				"SQSJ",
				"time",
				"ZT",
				"CZYXM",
				"CZY",
				"FLOW_STATUS"
			};
	}

	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 根据表单标识和记录编号，导出指定表单的指定记录
	 * @return
	 * @throws Exception 
	 */
	public String eformRecExportEFormRecExcelAction() throws Exception{
		String formSn = request.getParameter("formSn");
		String docid = request.getParameter("docid");
		//自定义表单对象
		EForm eform = eformManager.getEFormBySn(formSn);
		//查询指定docid的数据记录
		String schema = eform.getDefTable().getSchema();
		String tableName = eform.getDefTable().getTableCode();
		Map rec = physicTableManager.getRecById(docid,schema,tableName);
		//数据项映射规则
		String diXml = eform.getExcelDataItemXml();
		Document diXmlDocument = DocumentHelper.parseText(diXml);
		
		//excel文件
		byte[] excelTemplate = eform.getExcelTemplate();
		ByteArrayInputStream bais = new ByteArrayInputStream(excelTemplate);
		
		//为了兼容office 2007，采用本方法
		Workbook excelWorkBook=null;
		String excelExtendName = "";//excel扩展名根据2007和2003决定是xlsx还是xls
		try{
			//如果打开office2003的excel，该方法会抛出异常，然后采用HSS相关的类进行读取
			excelWorkBook = new XSSFWorkbook(bais);
			excelExtendName = "xlsx";
		}catch(Exception ex){
			bais.close();
			bais = new ByteArrayInputStream(excelTemplate);
			excelWorkBook = new HSSFWorkbook(bais);
			excelExtendName = "xls";
		}finally{
			if(bais!=null){
				bais.close();
			}
		}
		//查询所有的数据项节点并遍历所有数据项
		List<Element> diList = diXmlDocument.selectNodes("/root/di");
		for (Element diElement : diList) {
			_bianliDiForExportExcel(excelWorkBook,rec,diElement);
		}
		//输出excel文件
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment; filename=excel.xls");
		ServletOutputStream out = response.getOutputStream();
		excelWorkBook.write(out);
		return NONE;
	}
	
	/**
	 * 遍历数据映射，并根据每一个数据项的配置，定位excel单元格，设置对应的值
	 * @param excel
	 * @param ce
	 * @param tp
	 * @param edr
	 * @throws Exception 
	 */
	private void _bianliDiForExportExcel(Workbook excel,Map recMap,Element diElement) throws Exception{
		String ce = diElement.attribute("ce").getStringValue();
		//取得行列号,poi以0开始记录单元格的索引，所以取得的行列号需要减一
		int idx1 = ce.indexOf(":");
		int row = Integer.parseInt(ce.substring(0,idx1));row--;
		short col = Short.parseShort(ce.substring(idx1+1));col--;
		//定位单元格对象
		Sheet sheet = excel.getSheetAt(0);
		//设置Excel横向打印
		sheet.getPrintSetup().setLandscape(true);
//		sheet.autoSizeColumn(( short ) 0 ); // 调整第1列宽度 
//		sheet.autoSizeColumn(( short ) 1 ); // 调整第2列宽度 
//		sheet.autoSizeColumn(( short ) 2 ); // 调整第3列宽度 
//		sheet.autoSizeColumn(( short ) 3 ); // 调整第4列宽度 
		Row excelRow =sheet.getRow(row);
		Cell excelCell=excelRow.getCell(col);
		
		//excelCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		//取得单元格的值
		String value = "";
		String fd = diElement.attribute("fd").getStringValue();
		if(("ZT").equals(fd) || ("CZY").equals(fd) || ("CZYXM").equals(fd) || ("SQSJ").equals(fd) || ("SQBM").equals(fd)){
			value = "";
		}else{
			value = _getEditorValueForExportExcel(recMap,diElement);
		}
		excelCell.setCellValue(value);
	}
	
	/**
	 * 根据编辑器的不同，返回对应不同的现实方式的值,供Excel导出方法调用
	 * @param value
	 * @param edr
	 * @param tp
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	private String _getEditorValueForExportExcel(Map recMap,Element diElement) throws Exception{
		String tp = diElement.attribute("tp").getStringValue();
		String edr = diElement.attribute("edr").getStringValue();
		String fd = diElement.attribute("fd").getStringValue();
		String docid = request.getParameter("docid");
		String cf_sh = diElement.attributeValue("cf_sh");//计算公式，显示时计算
		//控件参数
		String edrParam = diElement.attribute("edr_pa").getStringValue();
		edrParam = StringUtil.decodeChars(edrParam, "',\",&");//反向解码
		Map<String,String> edrParamMap = JSONUtil.parseObject(edrParam);
		//字段值
		Object value = recMap.get(fd);
		String sValue = (value==null?"":value.toString());
		
		if(edr.equals("BTN")){ //不显示按钮
			value = "";
		}else if("TXT、TXTE、TXTA".indexOf(edr)>=0){//文本框，事件文本框和文本域
			
		}else if("CAL".indexOf(edr)>=0){//日期控件，需要根据定义的格式格式化数据值
			if(tp.equals("TIMESTAMP")){
				if(value instanceof Date){
					String fm = edrParamMap.get("fm");
					if(StringUtil.isEmpty(fm)) fm = "yyyy-MM-dd";
					sValue = DateUtils.formatDate((Date)value,fm);
				}
			}
		}else if("UPL".equals(edr) || "IMG".equals(edr)){
			String fjId = sValue;
			if(StringUtil.isNotEmpty(fjId)){
				EFormFj fj = eformFjManager.get(fjId);
				if(fj!=null){
					sValue = fj.getName();
				}
			}
		}else if("ATLT".equals(edr)){	//附件列表
			
			if(StringUtil.isNotEmpty(docid)){
				StringBuffer sb = new StringBuffer();
				List<EFormFj> fjList = eformFjManager.findBy("docid", docid);
				for (EFormFj formFj : fjList) {
					String name = formFj.getName();
					sb.append(name+"\r\n");
				}
				sValue = sb.toString();
			}
		}else if("SUB".equals(edr)){//子表
			String subTableName = edrParamMap.get("subTableName");
			String subTableFK = edrParamMap.get("subTableFK");
			String subTableWhere = edrParamMap.get("subTableWhere");
			String subTableOrder = edrParamMap.get("subTableOrder");
			String subTablePK = edrParamMap.get("subTablePK");
			String subTableCM = edrParamMap.get("subTableCM");
			String sql = _buildSubGridQuerySql(subTableName, subTableFK,subTableWhere, subTableOrder, docid);
			log.info("子表查询SQL:"+sql);
			List subTableResultList = physicTableManager.querySqlData(sql);
			subTableCM = subTableCM.replaceAll("\r\n", "");
			//因为列模型中的json字符串不规则，有函数定义，jsonutil解析不出来，所以采用javascript解析组件解析当前的列模型
			Context cx = Context.enter();
			Object result = null;
			try {
				Scriptable scope = cx.initStandardObjects(null);
				result = cx.evaluateString(scope, subTableCM, null, 1, null);
			} finally {
				Context.exit();
			}
			//遍历解析出来的对象集合 NativeArray[NativeObject,NativeObject]
			StringBuffer sbHeader = new StringBuffer();
			StringBuffer sbBody = new StringBuffer();
			//定义每列的 字节长度
			int[] byteleng = {26,20,12,8,14,14,14,14,10};
			if(result!=null){
				NativeArray na = (NativeArray) result;
				int j = 0;
				for (Object object : subTableResultList) {
					int len = 0;
					Map subTableRec = (Map) object;
					for (int i = 0; i < na.jsGet_length(); i++) {
						String headerWidth = "";
						String dataIndexWidth = "";
						NativeObject obj = (NativeObject) na.get(i, null);
						
						//根据列模型中指定该列是否隐藏决定该列的数据是否输出
						Object hidden = NativeObject.getProperty(obj, "hidden");
						if(hidden instanceof Boolean && (Boolean)hidden==true){
							continue;
						}
						if(hidden instanceof String && hidden.equals("true")){
							continue;
						}
						
						//获取标题和字段名称
						String header = (String) NativeObject.getProperty(obj, "header");
						String dataIndex = (String) NativeObject.getProperty(obj, "dataIndex");
						//只有第一次循环的时候，获取标题
						if(j==0){
							if(("固定资产名称").equals(header)){
								sbHeader.append(this.forMatStrByByte(header,byteleng[len]));
							}else if(("型号规格").equals(header)){
								sbHeader.append(this.forMatStrByByte(header,byteleng[len]));
							}else if(("计量单位").equals(header)){
								sbHeader.append(this.forMatStrByByte(header,byteleng[len]));
							}else if(("数量").equals(header)){
								sbHeader.append(this.forMatStrByByte(header,byteleng[len]));
							}else if(("预计价格").equals(header)){
								sbHeader.append(this.forMatStrByByte(header,byteleng[len]));
							}else if(("金额").equals(header)){
								sbHeader.append(this.forMatStrByByte(header,byteleng[len]));
							}else if(("税额").equals(header)){
								sbHeader.append(this.forMatStrByByte(header,byteleng[len]));
							}else if(("杂费").equals(header)){
								sbHeader.append(this.forMatStrByByte(header,byteleng[len]));
							}else if(("发票号").equals(header)){
								sbHeader.append(this.forMatStrByByte(header,byteleng[len]));
							}
						}
						Object vl = subTableRec.get(dataIndex);
						if(vl==null){
							 vl="    ";
						}else{
//							System.out.println(vl.toString()+":"+vl.toString().getBytes().length);
//							System.out.println(byteleng[len]);
							sbBody.append(this.forMatStrByByte(vl.toString(), byteleng[len]));
							len++;
						}
					}
					sbBody.append("\r\n");//每一行换行
					j++;
				}
				sbHeader.append("\r\n");//标题行换行
			}
			sValue = sbHeader.toString()+sbBody.toString();
			System.out.println(sValue);
		}else if("RAD、CHK、COMB".indexOf(edr)>=0){// 下拉框、单选框、复选框
			if(StringUtil.isNotEmpty(sValue)){
				String dataList = edrParamMap.get("list");
				String dictName = edrParamMap.get("dict");
				String sql = edrParamMap.get("sql");
				String tmpArray[] = sValue.split(",");
				StringBuffer sb = new StringBuffer();
				//静态配置的数据列表
				if(StringUtil.isNotEmpty(dataList)){
					String dataArray[] = dataList.split(",");
					for (String data : dataArray) {
						int idx =data.indexOf("|"); 
						if(idx>=0){
							String displayValue = data.substring(0,idx);
							String hiddenValue = data.substring(idx+1);
							if(ArrayUtils.isExist(tmpArray, hiddenValue)>=0){
								sb.append(displayValue+",");
							}
						}else{
							if(ArrayUtils.isExist(tmpArray, data)>=0){
								sb.append(data+",");
							}
						}
					}
				}else if(StringUtil.isNotEmpty(dictName)){//数据字典方式
					Collection<Dict> dicts = dictManager.findDictByUniqueCatalogName(dictName);
					for (Dict dict : dicts) {
						if(ArrayUtils.isExist(tmpArray, dict.getValue())>=0){
							sb.append(dict.getKey()+",");
						}
					}
				}else if(StringUtil.isNotEmpty(sql)){//sql语句方式
					List data = physicTableManager.querySqlData(sql);
					String sqlKeyField = edrParamMap.get("sqlKeyField");
					String sqlValueField = edrParamMap.get("sqlValueField");
					for (Object object : data) {
						Map rec = (Map) object;
						String hiddenValue = (String) rec.get(sqlValueField);
						if(ArrayUtils.isExist(tmpArray, hiddenValue)>=0){
							String displayValue = (String) rec.get(sqlKeyField);
							sb.append(displayValue+",");
						}
					}
				}
				//去除之后的逗号
				if(sb.length()>0){
					sb.deleteCharAt(sb.length()-1);
				}
				sValue = sb.toString();
			}
		}
		//是否定义了显示时计算的公式，如果有则计算
		if(StringUtil.isNotEmpty(cf_sh)){
			EFormExp exp = new EFormExp(sessionAttrs);
			exp.initExpContext(request);
			exp.setValue(sValue);
			sValue = exp.eval(cf_sh);
		}
		return sValue;
	}
	
	/**
	 * 子表控件 查询数据sql语句生成逻辑
	 * @param subTableName
	 * @param fkFieldName
	 * @param subTableWhere
	 * @param subTableOrder
	 * @param docid
	 * @return
	 */
	private String _buildSubGridQuerySql(String subTableName,
			String fkFieldName, String subTableWhere, String subTableOrder,
			String docid) {
		String schema = SystemConfig.getProperty("jdbc.schema");
		List<DefColumnInfo> cols = defColumnInfoManager.findColumnInfoByTableName(schema, subTableName);
		StringBuffer fieldsSB = new StringBuffer();
		for (DefColumnInfo col : cols) {
			String field = col.getColumncode();
			if(col.getColumntype().indexOf("TIMESTAMP")>=0 || col.getColumntype().indexOf("DATE")>=0){
				String fm = col.getFormat();
				if(fm==null)
					fm = (col.getColumntype().indexOf("TIMESTAMP")>=0?"YYYY-MM-DD HH24:MI:SS":"YYYY-MM-DD");
				else{
					//EXT JS的日期格式为Y-m-d H:i:s 需要转换为ORACLE日期格式
					fm = _extDtFormatToOracleDtFormat(fm);
				}
				field="TO_CHAR("+field+",'"+fm+"') AS "+field;
			}
			fieldsSB.append(field+",");
		}
		fieldsSB.deleteCharAt(fieldsSB.length()-1);
		
		String sql = "SELECT "+fieldsSB+" FROM "+subTableName+" WHERE "+fkFieldName+"='"+docid+"'";
		if(StringUtil.isNotEmpty(subTableWhere)){
			sql = sql.concat(" AND ("+subTableWhere+")");
		}
		String sort = request.getParameter("sort");
		if(StringUtil.isNotEmpty(sort)){
			String dir = request.getParameter("dir");
			if(StringUtil.isEmpty(dir)) dir = "ASC";
			sql = HqlUtil.addOrder(sql, sort, dir);
		}else{
			if(StringUtil.isNotEmpty(subTableOrder)){
				String field[] = subTableOrder.split(" ");
				String dir = "ASC";
				for(int i=1;i<field.length;i++){
					if(StringUtil.isNotEmpty(field[i])){
						dir = field[i];
						break;
					}
				}
				sql = HqlUtil.addOrder(sql, field[0] , dir);
			}
		}
		return sql;
	}
	
	/**
	 * 
	 *描述：EXT 的日期格式 转换为 ORACLE可识别的日期格式
	 *	EXT: 'Y-m-d H:i:s'
	 *  ORACLE 'YYYY-MM-DD HH24:MI:SS'
	 *时间：2010-6-8
	 *作者：谭畅
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	private String _extDtFormatToOracleDtFormat(String extDtFormat){
		if(StringUtil.isEmpty(extDtFormat))
			return extDtFormat;
		extDtFormat = extDtFormat.replaceAll("Y", "YYYY");
		extDtFormat = extDtFormat.replaceAll("m", "MM");
		extDtFormat = extDtFormat.replaceAll("d", "DD");
		extDtFormat = extDtFormat.replaceAll("H", "HH24");
		extDtFormat = extDtFormat.replaceAll("i", "MI");
		extDtFormat = extDtFormat.replaceAll("s", "SS");
		return extDtFormat;
	}
	/**
	 * 将字符串 转化成byte数组对比 长度不够的补 空
	 * @param str
	 * @param byteLength
	 * @return
	 */
	public String forMatStrByByte(String str,int byteLength){
		int len = byteLength-str.getBytes().length;
		boolean is_mode = false;
		if(len>0){
			if(len%2==0){
				is_mode = true;
			}
		}
		for(int i=0;i<len/2;i++){
			str = " "+str+" ";
		}
		if(!is_mode){
			str = str+" ";
		}
		return str;
	}
	public EFormFjManager getEformFjManager() {
		return eformFjManager;
	}

	public void setEformFjManager(EFormFjManager eformFjManager) {
		this.eformFjManager = eformFjManager;
	} 

}
