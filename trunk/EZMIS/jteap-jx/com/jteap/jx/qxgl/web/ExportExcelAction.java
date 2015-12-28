package com.jteap.jx.qxgl.web;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

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

import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.ArrayUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.form.dbdef.manager.DefColumnInfoManager;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.form.eform.manager.EFormFjManager;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.form.eform.model.EFormFj;
import com.jteap.form.eform.util.EFormExp;
import com.jteap.form.eform.web.EFormAction;
import com.jteap.jx.qxgl.manager.QxtjManager;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
/**
 * 针对检修统计 重写的导出excel方法
 * @author lvchao
 *
 */
@SuppressWarnings({"unchecked","serial"})
public class ExportExcelAction extends EFormAction{
	
	private EFormManager eformManager;
	private PhysicTableManager physicTableManager;
	private EFormFjManager eformFjManager;
	private DefColumnInfoManager defColumnInfoManager;
	private DictManager dictManager;
	private QxtjManager qxtjManager;
	/**
	 * 根据表单标识和记录编号，导出指定表单的指定记录
	 * @return
	 * @throws Exception 
	 */
	public String eformRecExportEFormRecExcelAction() throws Exception{
		String formSn = request.getParameter("formSn");
		//电厂统计期
		String dctjq = request.getParameter("dctjq");
		//开始时间
		String strdt = request.getParameter("strdt");
		//结束时间
		String enddt = request.getParameter("enddt");
		//统计分类
		String fl = request.getParameter("fl");
		//自定义表单对象
		EForm eform = eformManager.getEFormBySn(formSn);
		
		//查询指定docid的数据记录
		Map rec =  new HashMap();;
		//如果电厂统计期不为空 则按电厂统计期查询数据
		if(StringUtil.isNotEmpty(dctjq)){
			rec = qxtjManager.getJsonByTjIdAndFl(dctjq, fl);;
		}else{
			//如为空 则按选择的时间查询数据
			if(fl.equals("cn")){
				qxtjManager.findQxtj(strdt, enddt,rec);
			}else{
				qxtjManager.findQxtjByDljt(strdt, enddt,rec);
			}
		}
		
		rec.put("STR_DT", strdt);
		rec.put("END_DT", enddt);
		
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
		Row excelRow =sheet.getRow(row);
		Cell excelCell=excelRow.getCell(col);
		
		//excelCell.setEncoding(HSSFCell.ENCODING_UTF_16);
		//取得单元格的值
		String value = _getEditorValueForExportExcel(recMap,diElement);
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
			//String subTablePK = edrParamMap.get("subTablePK");
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
			if(result!=null){
				NativeArray na = (NativeArray) result;
				int j = 0;
				for (Object object : subTableResultList) {
					Map subTableRec = (Map) object;
					for (int i = 0; i < na.jsGet_length(); i++) {
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
						if(j==0)
							sbHeader.append(header+" ");
						Object vl = subTableRec.get(dataIndex);
						if(vl==null) vl=" ";
						sbBody.append(vl+" ");
					}
					sbBody.append("\r\n");//每一行换行
					j++;
				}
				sbHeader.append("\r\n");//标题行换行
			}
			sValue = sbHeader.toString()+sbBody.toString();
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

	public EFormManager getEformManager() {
		return eformManager;
	}

	public void setEformManager(EFormManager eformManager) {
		this.eformManager = eformManager;
	}

	public PhysicTableManager getPhysicTableManager() {
		return physicTableManager;
	}

	public void setPhysicTableManager(PhysicTableManager physicTableManager) {
		this.physicTableManager = physicTableManager;
	}

	public EFormFjManager getEformFjManager() {
		return eformFjManager;
	}

	public void setEformFjManager(EFormFjManager eformFjManager) {
		this.eformFjManager = eformFjManager;
	}

	public DefColumnInfoManager getDefColumnInfoManager() {
		return defColumnInfoManager;
	}

	public void setDefColumnInfoManager(DefColumnInfoManager defColumnInfoManager) {
		this.defColumnInfoManager = defColumnInfoManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
	}

	public QxtjManager getQxtjManager() {
		return qxtjManager;
	}

	public void setQxtjManager(QxtjManager qxtjManager) {
		this.qxtjManager = qxtjManager;
	}
	
	
	
}
