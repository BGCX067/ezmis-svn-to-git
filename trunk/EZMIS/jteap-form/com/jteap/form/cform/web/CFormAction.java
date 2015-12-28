package com.jteap.form.cform.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import bsh.EvalError;
import bsh.Interpreter;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.LogMethod;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.form.cform.manager.CFormManager;
import com.jteap.form.cform.model.CForm;
import com.jteap.form.cform.util.CFormExp;
import com.jteap.form.dbdef.manager.DefTableInfoManager;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.form.dbdef.model.DefTableInfo;

/**
 * 自定义表单动作对象
 * 
 * @author tanchang
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class CFormAction extends AbstractAction {
	private CFormManager cformManager;
	private DefTableInfoManager defTableInfoManager;
	private PhysicTableManager physicTableManager;
	private File excelFile;
	
	public PhysicTableManager getPhysicTableManager() {
		return physicTableManager;
	}
	public void setPhysicTableManager(PhysicTableManager physicTableManager) {
		this.physicTableManager = physicTableManager;
	}

	public DefTableInfoManager getDefTableInfoManager() {
		return defTableInfoManager;
	}

	public void setDefTableInfoManager(DefTableInfoManager defTableInfoManager) {
		this.defTableInfoManager = defTableInfoManager;
	}

	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	/**
	 * 列表显示之前针对条件和排序进行处理
	 */
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {

		HqlUtil.addCondition(hql, "newVer", '1');// 取最新的版本的表单

		// 添加查询分类条件
		String catalogId = request.getParameter("catalogId");
		if (StringUtils.isNotEmpty(catalogId)) {
			HqlUtil.addCondition(hql, "catalogId", catalogId);
		}

		// 默认按照创建时间倒序排序
		if (!this.isHaveSortField()) {
			HqlUtil.addOrder(hql, "creatDt", "desc");
		}
		
		//查询条件
		String hqlWhere = StringUtil.isoToUTF8(request.getParameter("queryParamsSql"));
		if(StringUtils.isNotEmpty(hqlWhere)){
			String hqlWhereTemp = hqlWhere.replace("$", "%");
			HqlUtil.addWholeCondition(hql, hqlWhereTemp);
		}
	}

	/**
	 * 返回JSON字符串数组
	 * 
	 * @return
	 */
	public String[] listJsonProperties() {
		return new String[] { "id", "title", "sn", "version", "catalogId",
				"creator", "creatDt", "type", "eformUrl", "time","exHtmlUrl" };
	}

	public CFormManager getCformManager() {
		return cformManager;
	}

	public void setCformManager(CFormManager cformManager) {
		this.cformManager = cformManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return cformManager;
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 删除所选表单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		//EForm方法
//		cformManager.deleteEFormByCFormIds(keys.split(","));

		if (keys != null) {
			this.getManager().removeBatch(keys.split(","));
			outputJson("{success:true}");
		}
		return NONE;
	}

	/**
	 * 保存Excel表单
	 * 
	 * @return
	 * @throws Exception
	 */
	@LogMethod(name="保存Excel表单")
	public String saveOrUpdateExcelCFormAction() throws BusinessException {
		try{
			String id = request.getParameter("id"); // 表单标识，作为是新建还是修改的判断依据
			String catalogId = request.getParameter("txtCatalogId"); // 分类编号
			String title = request.getParameter("txtTitle"); // 标题
			String formType = request.getParameter("selDimFormType"); // 标题
			String defTableId = request.getParameter("defTableId"); // 表单关联表定义对象的编号
			String excelDataItemXml = request.getParameter("excelDataItemXml"); // 数据项配置xml
			String saveNewVer = request.getParameter("txtSaveNewVer"); // 是否创建新的版本 0-不创建 1-创建
			String editableInputs = request.getParameter("editableInputs"); // 可编辑域JSON集合[{"fd":"QianFa_time","cp":"签发时间","tp":"02"}]
			String excelHtmlFileName = request.getParameter("excelHtmlFileName");
			String dictId = request.getParameter("txtDictId"); 		// 关联字典编号
			String rowzbCell = request.getParameter("txtRowZbCell"); // 行指标开始单元格信息
			
			CForm cform = null;
			CForm oldCForm = null;
			String excelHtmlFileUrl = excelHtmlFileName;

			if (StringUtil.isNotEmpty(id)) {
				cform = cformManager.get(id);
				oldCForm = cform;
				//if(StringUtil.isEmpty(cform.getExHtmlUrl())){
					cform.setExHtmlUrl(excelHtmlFileUrl);
				//}
			} else {
				cform = new CForm();
				cform.setNewVer(true);
				cform.setNm(UUIDGenerator.hibernateUUID());// 产生新建表单的内码
				cform.setExHtmlUrl(excelHtmlFileUrl);
			}
	
			int ver = cform.getVersion();
			// 是否创建新的版本
			if (StringUtil.isNotEmpty(saveNewVer) && saveNewVer.equals("1")) {
				CForm cform2 = new CForm();
				BeanUtils.copyProperties(cform2, cform);
				cform2.setId(null);
				// 设置为旧版本并保存
				cform2.setNewVer(true);
				cform.setNewVer(false);
				cformManager.save(cform);
	
				cform = cform2;
				ver = cformManager.getMaxVerNo(cform.getNm());
				ver++;
			}
			cform.setEditableInputs(editableInputs);
			cform.setVersion(ver);
	
			cform.setType(formType); // 表单类型
			cform.setCatalogId(catalogId); // 所属分类
			cform.setCreatDt(new Date()); // 创建日期
			cform.setTitle(title); // 标题
			cform.setDictId(dictId);				// 字典编号
			cform.setRowzbCell(rowzbCell) ; // 行单元格
			// 创建人
			cform.setCreator(this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString());
			cform.setExcelDataItemXml(excelDataItemXml);
			
			// Excel文件
			cform.setExcelTemplate(FileUtils.readFileToByteArray(excelFile));
	
			if (StringUtil.isNotEmpty(defTableId)) {
				DefTableInfo table = defTableInfoManager.get(defTableId);
				cform.setDefTable(table);
			}
	
			cformManager.save(cform);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException("保存Excel表单时出现异常:SDS");
		}
		return NONE;
	}
	
	

	/**
	 * 显示修改Excel表单动作
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@LogMethod(name="显示修改Excel表单动作")
	public String showExcelUpdateAction() throws UnsupportedEncodingException {

		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			CForm cform = cformManager.get(id);
			request.setAttribute("cform", cform);
		}
		return "showExcelFormUpdate";
	}

	/**
	 * 读取Excel大字段
	 * 
	 * @return
	 * @throws IOException
	 */
	@LogMethod(name="读取自定义表单Excel")
	public String readExcelBlobAction() throws IOException {
		String id = request.getParameter("id");
		CForm cform = cformManager.get(id);
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment; filename=excel.xls");
		OutputStream outStream = response.getOutputStream();
		ByteArrayInputStream ais = new ByteArrayInputStream(cform.getExcelTemplate());
		
		byte[] buf = new byte[1024];
		int bytes = 0;
		while ((bytes = ais.read(buf)) != -1)
			outStream.write(buf, 0, bytes);
		ais.close();
		outStream.close();
		return NONE;
	}

	/**
	 * 在表单定义中数据项的默认值dv可以是不同的规则组合的占位符 将dv中的占位符解析出来用真实的值代替并返回 dv规则： ${gh} 当前登录名称
	 * 可使用工号 ${sysdt(yyyy-MM-dd HH:mm:ss)} 系统当前时间 ${group('all|1|2|3')} 组织
	 * ${no(####)} 流水号 ${uid} 32位UUID ${ref(gzp-1)}
	 * 引用config.properties中配置的编码生成规则
	 * 
	 * @return
	 * @throws Exception
	 */
	public String defaultValueAction() throws Exception {
		String dv = request.getParameter("dv"); // default value schema

		// 匹配所有的dv规则的正则表达式
		String patt = "\\$\\{[a-z]+\\({0,1}"
				+ "([1-9]|[a-z]|[A-Z]|\\#| |-|:|')*" + "\\){0,1}\\}";
		Pattern p3 = Pattern.compile(patt);
		Matcher m3 = p3.matcher(dv);
		String sNoRule = ""; // ${no}规则
		while (m3.find()) {
			String rule = m3.group();
			if (rule.indexOf("${no(") >= 0) {
				sNoRule = rule;
			} else {
				String value = getRuleValue(rule);
				if (StringUtil.isNotEmpty(value)) {
					dv = StringUtil.replace(dv, rule, value);
				}
			}
		}
		// 单独针对流水号进行处理
		int idx = -1;
		if ((idx = dv.indexOf("${no(")) >= 0) {
			String tableName = request.getParameter("tn");
			String fd = request.getParameter("fd"); // 字段名称

			String prefix = dv.substring(0, idx);
			String numFm = dv.substring(dv.indexOf("(#") + 1,
					dv.indexOf("#)") + 1);
			String sNo = cformManager.getMaxNo(tableName, fd, prefix, numFm);
			dv = StringUtil.replace(dv, sNoRule, sNo);
		}

		outputJson("{success:true,value:'" + dv + "'}");
		return NONE;
	}

	/**
	 * dv规则： ${gh} 当前登录名称 可使用工号 ${sysdt(yyyy-MM-dd HH:mm:ss)} 系统当前时间
	 * ${group('all|1|2|3')} 组织 ${no(####)} 流水号 ${uuid} 32位UUID ${ref(gzp-1)}
	 * 引用config.properties中配置的编码生成规则
	 * 
	 * @param rule
	 * @return
	 */
	private String getRuleValue(String rule) {
		String retVal = "";
		int idx;
		if (rule.equals("${gh}")) {
			retVal = this.sessionAttrs.get(
					Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		} else if (rule.equals("${uuid}")) {
			retVal = UUIDGenerator.hibernateUUID();
		} else if ((idx = rule.indexOf("${no(")) >= 0) {
			retVal = "";// 待所有配置完成之后最后处理流水号
		} else if ((idx = rule.indexOf("${ref(")) >= 0) {
			retVal = "应用编码配置";
		} else if ((idx = rule.indexOf("${sysdt(")) >= 0) {
			String fm = rule.substring(idx + 8, rule.indexOf(")"));
			retVal = DateUtils.getDate(fm);
		}
		return retVal;
	}

	/**
	 * 保存Excel表单记录动作 获取参数： recordJson 当前提交业务数据记录json串 defTableId 表定义对象的编号 docid
	 * 文档编号
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String saveOrUpdateExcelCFormRecAction() throws Exception {
		String recordJson = request.getParameter("recordJson");
		String defTableId = request.getParameter("defTableId");
		String docid = request.getParameter("docid");
		String diXml = request.getParameter("excelDataItemXml");
		CFormExp exp = new CFormExp(sessionAttrs);
		exp.initExpContext(request);
		DefTableInfo defTable = defTableInfoManager.get(defTableId);
		getPhysicTableManager().saveOrUpdateRec(defTable.getSchema(), defTable
				.getTableCode(), docid, recordJson,diXml,exp);
		this.writeOpSuccessScript("数据保存成功");
		return NONE;
	}

	/**
	 * 保存Excel表单记录动作 获取参数： recordJson 当前提交业务数据记录json串 defTableId 表定义对象的编号 docid
	 * 文档编号
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@LogMethod(name="采取Ajax方式保存Excel表单记录动作")
	public String saveOrUpdateExcelCFormRecInAjaxAction() throws Exception {
		String recordJson = request.getParameter("recordJson");
		String defTableId = request.getParameter("defTableId");
		String docid = request.getParameter("docid");
		String formType = request.getParameter("formType");
		String diXml = request.getParameter("excelDataItemXml");
		
		CFormExp exp = new CFormExp(sessionAttrs);
		try {
			DefTableInfo defTable = defTableInfoManager.get(defTableId);
			Collection coll = null;
			Map result = new HashMap();
			String json ="{}";
			String schema = defTable.getSchema();
			String tableName = defTable.getTableCode();
			exp.initExpContext(request);
			docid = getPhysicTableManager().saveOrUpdateRec(schema, tableName, docid, recordJson,diXml,exp);
			result = getPhysicTableManager().getRecById(docid, schema, tableName);
			json = JSONUtil.mapToJson(result);
			outputJson("{success:true,docid:'" + docid + "',data:"+json+"}");
		} catch (Exception ex) {
			ex.printStackTrace();
			outputJson("success:false,msg:'" + ex.getMessage() + "'");
		}
		return NONE;
	}

	/**
	 * 查询可编辑域
	 * 
	 * @return
	 * @throws Exception
	 */
	public String findEditableInputsAction() throws Exception {
		String id = request.getParameter("id");
		CForm cform = cformManager.get(id);
		String editableInputs = "[]";
		if (cform != null) {
			editableInputs = cform.getEditableInputs();
		}

		outputJson("{success:true,editableInputs:" + editableInputs + "}");
		return NONE;
	}

	/**
	 * 数据项计算公式有效性验证器 将所有@开头的匹配的公式替换成字符串， 并使用beanshell进行计算，如果不抛出异常，表明格式正确
	 * 验证成功输出：success:true 验证失败输出: success:false
	 * 
	 * @return
	 * @throws Exception
	 */
	public String calcFormulaValidatorAction() throws Exception {
		String cf = request.getParameter("cf");
		if (StringUtil.isNotEmpty(cf)) {
			cf = StringUtil.decodeChars(cf, "@,>,<,+,-,?,(,), ,.,:,\",'");
		}

		boolean bValidateRet = CFormExp.validateCalcFormula(cf);
		String msg = "";
		if (!bValidateRet) {
			msg = "哎呀！公式中包含无效公式，仔细点啦!!!";
		} else {
			String regex = "@[a-zA-Z]+\\(.*\\)";
			Pattern p = Pattern.compile(regex);
			Matcher mt = p.matcher(cf);
			while (mt.find()) {
				cf = mt.replaceAll("\"\"");
				mt = p.matcher(cf);
			}
			try {
				Interpreter i = new Interpreter();
				Object ret = i.eval(cf);
				if (ret == null)
					throw new Exception(cf);

			} catch (Exception ex) {
				bValidateRet = false;
				msg = ex.getMessage();
				msg = StringUtil.formatHTML(msg);
			}
		}
		if (!bValidateRet) {
			outputJson("{success:false,msg:'公式格式不正确:<br/>" + msg + "'}");
		} else {
			outputJson("{success:true}");
		}
		return NONE;
	}

	/**
	 * 显示时计算 通过ajax请求，传输计算公式，将计算结果输出
	 * 
	 * @cf 计算公式
	 * @value 字段值
	 * @return
	 * @throws Exception
	 */
	public String calcFormulaShowAction() throws Exception {
		String cf = request.getParameter("cf"); // 公式
		String value = request.getParameter("value"); //原本值

		if (StringUtil.isNotEmpty(cf)) {
			cf = StringUtil.decodeChars(cf, "@,>,<,+,-,?,(,), ,.,:,\",'");
		}
		try {
			CFormExp exp = new CFormExp(value, this.sessionAttrs);
			exp.initExpContext(request);
			String ret = exp.eval(cf);
			outputJson("{success:true,ret:'" + ret + "'}");
		} catch (Exception ex) {
			String msg = ex.getMessage();
			msg = StringUtil.formatHTML(msg);
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}
	public static void main(String[] args) throws EvalError {
		String f = "3+\"tanchang\"+@IF(A>100?@SYSDT('YYYY-mm-DD'):@USERLOGINID())+@UUID()+@INVOKE('com.jteap.cform.util.CalculateFormula')+\"SSSSSSSSS\"+@SQL('SELECT VALUE FROM XXXX WHERE A>\'100\' AND B<100')";
		String regex = "@[a-zA-Z]+\\([^\\)\\(@]*\\)";
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(f);
		while (mt.find()) {
			f = mt.replaceAll("\"\"");
			mt = p.matcher(f);
		}
		Interpreter i = new Interpreter();
		System.out.println(i.eval(f));
	}
}
