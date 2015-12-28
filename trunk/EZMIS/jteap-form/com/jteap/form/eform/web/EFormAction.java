package com.jteap.form.eform.web;

import groovy.util.ScriptException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jbpm.graph.exe.ProcessInstance;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.support.LogMethod;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.ArrayUtils;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.FileUtil;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.utils.WebUtils;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.dbdef.manager.DefColumnInfoManager;
import com.jteap.form.dbdef.manager.DefTableInfoManager;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.form.dbdef.model.DefColumnInfo;
import com.jteap.form.dbdef.model.DefColumnType;
import com.jteap.form.dbdef.model.DefTableInfo;
import com.jteap.form.eform.manager.EFormCatalogManager;
import com.jteap.form.eform.manager.EFormFjManager;
import com.jteap.form.eform.manager.EFormManager;
import com.jteap.form.eform.model.EForm;
import com.jteap.form.eform.model.EFormCatalog;
import com.jteap.form.eform.model.EFormFj;
import com.jteap.form.eform.util.EFormExp;
import com.jteap.system.dict.manager.DictManager;
import com.jteap.system.dict.model.Dict;
import com.jteap.wfengine.wfi.web.WorkFlowInstanceAction;
import com.jteap.wfengine.workflow.manager.FlowConfigManager;
import com.jteap.wfengine.workflow.model.FlowConfig;
import com.jteap.wfengine.workflow.web.WorkflowAction;
import com.jteap.wz.xqjhsq.manager.XqjhsqLogManager;

/**
 * 自定义表单动作对象
 * 
 * @author tanchang
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class EFormAction extends AbstractAction {
	
	private EFormManager eformManager;
	private EFormCatalogManager eformCatalogManager;
	private DefTableInfoManager defTableInfoManager;
	private EFormFjManager eformFjManager;
	private File excelFile;
	private DefColumnInfoManager defColumnInfoManager;
	private PhysicTableManager physicTableManager;
	private DictManager dictManager;
	private XqjhsqLogManager xqjhsqLogManager;
	private FlowConfigManager flowConfigManager;
	
	public XqjhsqLogManager getXqjhsqLogManager() {
		return xqjhsqLogManager;
	}

	public void setXqjhsqLogManager(XqjhsqLogManager xqjhsqLogManager) {
		this.xqjhsqLogManager = xqjhsqLogManager;
	}

	public FlowConfigManager getFlowConfigManager() {
		return flowConfigManager;
	}

	public void setFlowConfigManager(FlowConfigManager flowConfigManager) {
		this.flowConfigManager = flowConfigManager;
	}

	public DictManager getDictManager() {
		return dictManager;
	}

	public void setDictManager(DictManager dictManager) {
		this.dictManager = dictManager;
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

	public void setDefColumnInfoManager(DefColumnInfoManager defColumnInfoManager) {
		this.defColumnInfoManager = defColumnInfoManager;
	}

	public void setDefTableInfoManager(DefTableInfoManager defTableInfoManager) {
		this.defTableInfoManager = defTableInfoManager;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	public void setEformCatalogManager(EFormCatalogManager eformCatalogManager) {
		this.eformCatalogManager = eformCatalogManager;
	}

	public void setEformManager(EFormManager eformManager) {
		this.eformManager = eformManager;
	}

	/**
	 * 列表显示之前针对条件和排序进行处理
	 */
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		this.isUseQueryCache = false;
		HqlUtil.addCondition(hql, "newVer", '1');// 取最新的版本的表单

		// 添加查询分类条件
		String catalogId = request.getParameter("catalogId");
		if (StringUtils.isNotEmpty(catalogId)) {
			HqlUtil.addCondition(hql, "catalog.id", catalogId);
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
		return new String[] { "id", "title", "sn", "version", "catalog",
				"creator", "creatDt", "type", "eformUrl", "time","exHtmlUrl","width","height","finalManuscript"};
	}
	
	
	@Override
	public HibernateEntityDao getManager() {
		return eformManager;
	}


	@Override
	public String[] updateJsonProperties() {
		return null;
	}
	
	/**
	 * 
	 *描述：根据数据项映射，同步指定表的所有字段，如果表中不存在对应的字段，则新建，如果存在，则不做处理
	 *时间：2010-6-23
	 *作者：谭畅
	 *参数：	@param request excelDataItemXml
	 *		@param request defTableId
	 *返回值:
	 *抛出异常：
	 * @throws Exception 
	 */
	public String synchronizeDefTableAction() throws Exception {
		String diXml = request.getParameter("excelDataItemXml"); // 数据项配置xml
		String defTableId = request.getParameter("defTableId"); // 表单关联表定义对象的编号
		String formSn = request.getParameter("formSn");
		EForm eform = eformManager.getEFormBySn(formSn);
		
		DefTableInfo table = defTableInfoManager.get(defTableId);
		Document document = null;
		boolean isSetupFinalStatu = false;
		try {
			document = DocumentHelper.parseText(diXml);
			Map<String,Element> diMap = new HashMap<String,Element>();
			Map dataMap = new HashMap();
			List<Element> diList = document.selectNodes("/root/di");
			for (Element diNode : diList) {
				String fd = diNode.attribute("fd").getValue(); //以字段名大写作为map key
				String cp = diNode.attribute("cp").getValue(); //以字段名大写作为map key
				String tp = diNode.attribute("tp").getValue(); //以字段名大写作为map key
				String wid = diNode.attribute("wid").getValue(); //以字段名大写作为map key
				
				if(tp.equals("NONE"))//非映射数据项不同步到数据库字段
					continue;
				
				if(tp.equals("NUMBER")){
					wid= "10";
				}
				String hql = "select count(*) from DefColumnInfo obj where obj.table.id='"+defTableId+"' and obj.columncode='"+fd+"'";
				Long count = (Long) this.getManager().findUniqueByHql(hql);
				if(count==0){
					log.info(table.getTableCode()+"中不存在字段"+fd+",创建中。。。");
					DefColumnInfo defColumn = new DefColumnInfo();
					defColumn.setColumncode(fd);
					defColumn.setColumnname(cp);
					defColumn.setColumntype(tp);
					
					defColumn.setColumnlength(Integer.parseInt(wid));
					
					defColumn.setTable(table);
					table.getColumns().add(defColumn);
					defColumnInfoManager.save(defColumn);
					defTableInfoManager.modifyRecByDefColumnInfoChange(defColumn, "add", null, null, false);
 
					//设置未定稿状态
					if(eform!=null && eform.isFinalManuscript()){
						eform.setFinalManuscript(false);
						isSetupFinalStatu = true;
					}
				}else{
					log.info(table.getTableCode()+"中已经存在字段"+fd+"");
				}
				//初始化数据项的数据
			}
			//是否修改了定稿标记
			if(isSetupFinalStatu){
				eformManager.save(eform);
			}
			
			this.outputJson("{success:true}");
		} catch (Exception ex){
			this.outputJson("{success:false,msg:'同步数据定义失败'}");
			ex.printStackTrace();
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
	public String saveOrUpdateEFormAction() throws BusinessException {
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
			String sn = request.getParameter("txtSn");	//表单标识
			String width = request.getParameter("txtWidth");
			String height = request.getParameter("txtHeight");
			String onloadScript = request.getParameter("onloadScript");
			
			EForm eform = null;
			EForm oldEForm = null;
			String excelHtmlFileUrl = sn+".html";

			if (StringUtil.isNotEmpty(id)) {
				eform = eformManager.get(id);
				oldEForm = eform;
			} else {
				eform = new EForm();
				eform.setNewVer(true);
				eform.setNm(UUIDGenerator.hibernateUUID());// 产生新建表单的内码
				if(StringUtil.isNotEmpty(catalogId)){
					EFormCatalog catalog = eformCatalogManager.get(catalogId);
					eform.setCatalog(catalog);
				}
			}
			eform.setExHtmlUrl(excelHtmlFileUrl);
			eform.setSn(sn);
			eform.setWidth(Integer.parseInt(width));
			eform.setHeight(Integer.parseInt(height));
	
			int ver = eform.getVersion();
			// 是否创建新的版本
			if (StringUtil.isNotEmpty(saveNewVer) && saveNewVer.equals("1")) {
				EForm eform2 = new EForm();
				BeanUtils.copyProperties(eform2, eform);
				eform2.setId(null);
				// 设置为旧版本并保存
				eform2.setNewVer(true);
				eform.setNewVer(false);
				eformManager.save(eform);
				eform = eform2;
				ver = eformManager.getMaxVerNo(eform.getNm());
				ver++;
			}
			eform.setEditableInputs(editableInputs);
			eform.setVersion(ver);
			
			eform.setCreatDt(new Date()); // 创建日期
			eform.setTitle(title); // 标题
			eform.setOnloadScript(onloadScript);
			// 创建人
			eform.setCreator(this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString());
			eform.setExcelDataItemXml(excelDataItemXml);
			
			// Excel文件
			eform.setExcelTemplate(FileUtils.readFileToByteArray(excelFile));
	
			if (StringUtil.isNotEmpty(defTableId)) {
				DefTableInfo table = defTableInfoManager.get(defTableId);
				eform.setDefTable(table);
			}else{
				
			}
			eformManager.save(eform);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException("保存Excel表单时出现异常:SDS");
		}
		return NONE;
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
		EForm eform = eformManager.get(id);
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment; filename=excel.xls");
		OutputStream outStream = response.getOutputStream();
		ByteArrayInputStream ais = new ByteArrayInputStream(eform.getExcelTemplate());
		
		byte[] buf = new byte[1024];
		int bytes = 0;
		while ((bytes = ais.read(buf)) != -1)
			outStream.write(buf, 0, bytes);
		ais.close();
		outStream.close();
		return NONE;
	}


	/**
	 *描述：自定义表单提交数据并保存相关数据
	 *1.解析映射元素据并以字段名称作为KEY值加入到MAP中方便存取
	 *2.遍历所有提交的字段，取到每个字段的值并存入MAP中
	 *3.创建或取得对应表的记录集，并针对每个字段进行修改动作
	 *  每个字段根据类型进行有针对性的处理,如果是附件？ 如果是子表？
	 *时间：2010-5-13
	 *作者：谭畅
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws Exception 
	 */
	public String eformRecSaveOrUpdateAction() throws Exception{
		String diXml = (request.getParameter("excelDataItemXml"));	//数据项配置
		String editableInputs = (request.getParameter("editableInputs"));	//可编辑域
		List<Map<String,String>> editableList = null;
		if(StringUtil.isNotEmpty(editableInputs)){
			editableList = JSONUtil.parseList(editableInputs);
		}
		
		String eformId = request.getParameter("eformId");			//表单编号
		String docid = request.getParameter("docid");				//记录主键
		EForm eform = eformManager.get(eformId);					//表单对象
		//关联表对象
		String tableCode = eform.getDefTable().getTableCode();
		String tableSchema = eform.getDefTable().getSchema();
		
		//解析数据项xml   <root><di></di><di></di>....</root>
		Document document;
		try {
			document = DocumentHelper.parseText(diXml);
			Map<String,Element> diMap = new HashMap<String,Element>();
			Map dataMap = new HashMap();
			List<Element> diList = document.selectNodes("/root/di");
			Map<String,String> atltMap = new HashMap<String, String>();//附件列表数据
			Map<String,String> subMap = new HashMap<String, String>();//子表数据
			for (Element diNode : diList) {
				String fd = diNode.attribute("fd").getValue(); //以字段名大写作为map key
				String tp = diNode.attribute("tp").getValue(); //以字段名大写作为map key
				
				String cf_sa = null;
				String cf_cr = null;
				if(diNode!=null){
					cf_sa = diNode.attributeValue("cf_sa");
					cf_cr = diNode.attributeValue("cf_cr");
				}
//				去掉此过滤判断，所有的数据项全部通过，主要是因为类似SUB这样的控件，
				//需要存储数据，但是没有具体映射到哪个字段上，这个时候tp为NONE
//				if(tp.equals("NONE")){
//					continue;
//				}
				
				boolean isEditable = _isInEditableList(editableList, fd);
				if(!isEditable){
					continue;
				}
				
				//初始化数据项配置
				diMap.put(fd.toUpperCase(), diNode);
				if(cf_sa!=null || cf_cr!=null){
					//此处需要将可编辑域制空，主要是针对需要计算型的数据项来说,计算域是空值，
					//通过计算公式进行计算最终的值，如果此处不将数据项添加到集合中，计算公式不会执行
					dataMap.put(fd.toUpperCase(), null);
				}
				
				//初始化数据项的数据
				_initDiData(dataMap,diNode,atltMap,subMap);
			}
			
			//保存数据
			docid = _saveOrUpdateRec(tableSchema, tableCode, docid, diMap,dataMap);
			//如果存在附件列表数据，那么进行处理
			if(atltMap.size()>0){
				_processAtltData(docid,atltMap);
			}
			onAferSave();
			outputJson("{success:true,msg:'保存成功',docid:'"+docid+"'}");
		} catch (Exception e) {
			String msg = e.getMessage();
			outputJson("{success:false,msg:'"+msg+"'}");
			e.printStackTrace();
		}
		return NONE;
	}
	/**
	 * 在保存之后进行操作处理
	 */
	protected void onAferSave()throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 处理附件列表数据，遍历所有加入到附件列表的字段，并分析每个字段中的附件列表,
	 * 并去除附件列表中属性isNew为true的附件进行保存，其他已经存在的附件不动
	 * @param docid 
	 * @param atltMap 
	 */
	private void _processAtltData(String docid, Map<String, String> atltMap) {
		//遍历所有附件列表字段
		for (String fd : atltMap.keySet()) {
			String value = atltMap.get(fd);
			List atlist = JSONUtil.parseList(value);
			//遍历附件列表
			for (Object obj : atlist) {
				Map map = (Map) obj;
				Boolean isNew = (Boolean) map.get("isNew");					//是否最新上传的文件
				Boolean bEncodeFileName = (Boolean) map.get("encodeFileName");//是否加密文件名
				
				String st = (String) map.get("st");
				if(isNew!=null && isNew){
					EFormFj fj = new EFormFj();
					fj.setDocid(docid);
					fj.setCreator(sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString());
					fj.setDt(new Date());
					fj.setName(map.get("name").toString());
					fj.setType(map.get("type").toString());
					fj.setSize(Long.parseLong(map.get("size").toString()));
					fj.setSt(st);
					//根据附件存储方式，FS方式还是DB方式，来分别执行不同的附件保存过程
					String repository = SystemConfig.getProperty("EFORM_FJ_REPOSITORY",request.getSession().getServletContext().getRealPath("/"));
					String pathRule = _getEFormFjRepositoryPath(request);//路径规则
					
					//附件临时文件的文件名
					String fileId = map.get("id").toString();
					
					//临时文件
					String tmpFilePath = repository +"/tmp/"+fileId;//临时文件路径，绝对地址
					File tmpFile = new File(tmpFilePath);
					
					if(st.equals("FS")){
						String dirPath = repository+pathRule;
						File dirFile = new File(dirPath);
						
						if(!dirFile.exists()) dirFile.mkdirs();
						
						//确定是否加密文件名，文件名默认是按照uuid方式存储，如果不用加密文件名，则将文件名重命名
						String newFileName =(bEncodeFileName?fileId:fj.getName());
						String newFileRelativePath = pathRule+"/"+newFileName;	//新文件相对地址,用于存储数据库path字段中
						//最终存储文件
						File newFile = new File(dirFile,newFileName);
						
						//如果文件存在，则以覆盖的方式进行，先删除，再创建
						if(newFile.exists())
							newFile.delete();
						
						tmpFile.renameTo(newFile);
						
						//去除绝对附件库地址
						fj.setPath(newFileRelativePath);
					}else{
						//如果文件存储方式为DB方式，则存入到EFormFj的content字段中
						if(tmpFile.exists()){
							byte bytes[] = FileUtil.getBytesFromFile(tmpFile);
							fj.setContent(bytes);
							tmpFile.delete();
						}
					}
					eformFjManager.save(fj);
				}
			}
			
		}
	}

	/**
	 * 获取根据配置文件中的EFORM_FJ_REPOSITORY和EFORM_FJ_PATH_RULE指定的规则生成动态的存放附件的路径并返回
	 * @param request
	 * @return
	 */
	private String _getEFormFjRepositoryPath(HttpServletRequest request) {
		String pathRule = SystemConfig.getProperty("EFORM_FJ_PATH_RULE","");
		String userLoginName = (String) request.getSession().getAttribute(Constants.SESSION_CURRENT_PERSON_LOGINNAME);
		Map<String,Object> eviMap = new HashMap<String,Object>();
		eviMap.put("userLoginName", userLoginName);
		eviMap.put("date",new Date());
		String path = StringUtil.evalExp(pathRule, eviMap);
		return path;
	}
	/**
	 * 判断指定的字段是否在可编辑域中存在，存在返回true,不存在返回false
	 * @param editableList	可编辑域列表
	 * @param fd	当前字段名称
	 * @return
	 */
	private boolean _isInEditableList(List<Map<String, String>> editableList,
			String fd) {
		boolean isEditable = false;
		if(editableList!=null){
			for (Map eaitableRec : editableList) {
				String fdInMap = (String) eaitableRec.get("fd");
				if(fdInMap!=null && fdInMap.equals(fd)){
					isEditable = true;
					break;
				}
				
			}
		}
		return isEditable;
	}

	/**
	 * 
	 * 描述 : 查询可编辑域
	 * 作者 : wangyun
	 * 时间 : Jun 28, 2010
	 */
	public String findEditableInputsAction() throws Exception {
		String id = request.getParameter("id");
		EForm eform = eformManager.get(id);
		String editableInputs = "[]";
		if (eform != null) {
			editableInputs = eform.getEditableInputs();
		}

		outputJson("{success:true,editableInputs:" + editableInputs + "}");
		return NONE;
	}

	/**
	 * 
	 *描述：初始化数据项的数据
	 *时间：2010-5-14
	 *作者：谭畅
	 *参数：
	 *    dataMap:需要初始化的map集合对象，Map中数据为<数据项名，提交的数据对象>，
	 *            其中数据对象会根据控件的不同而不同，如果为上传空间，这数据对象为EFormFj对象,
	 *            如果是日历控件的话，数据对象将会是Date对象
	 *    diNode:数据项XML节点配置对象<di fd="" tp=""..../>
	 *返回值:
	 *    dataMap:作为输出参数
	 *抛出异常：
	 * @param subMap 
	 * @param atltMap 附件列表数据
	 */
	private void _initDiData(Map dataMap, Element diNode, Map<String, String> atltMap, Map<String, String> subMap) {
		//数据类型可以为VARCHAR、NUMBER、BLOB、CLOB、TIMESTAMP
		String tp = diNode.attribute("tp").getValue();	//数据类型
		String edr = diNode.attribute("edr").getValue();//控件类型
		String edr_pa = diNode.attribute("edr_pa").getValue();//控件类型
		edr_pa = StringUtil.decodeChars(edr_pa, "@,>,<,+,-,?,(,), ,.,:,\",'");
		String fd = diNode.attribute("fd").getValue();	//字段名称
		Map edrParamMap = JSONUtil.parseObject(edr_pa);
		
		//是不是附件
		if("UPL".equals(edr) || "IMG".equals(edr)){
			if(request instanceof MultiPartRequestWrapper){
				MultiPartRequestWrapper multiRequest = (MultiPartRequestWrapper) request;
				File[] f = multiRequest.getFiles(fd);
				String[] filename = multiRequest.getFileNames(fd);
				if(f!=null && f.length>0){
					//创建附件对象并保存
					String type = FilenameUtils.getExtension(filename[0]);
					byte[] content = FileUtil.getBytesFromFile(f[0]);
					EFormFj fj = new EFormFj();
					fj.setName(filename[0]);fj.setType(type);
					fj.setCreator(sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME).toString());
					fj.setSt("DB");
					fj.setDt(new Date());fj.setContent(content);
					eformFjManager.save(fj);
					String fjId = fj.getId();
					if("IMG".equals(edr)){
						fjId = "id:"+fjId;
					}
					dataMap.put(fd.toUpperCase(),fjId);
				}
			}
		}else if("SUB".equals(edr)){//如果是子表控件
			String value = request.getParameter(fd);
			if(value!=null){
				List<Map<String,String>> subList = JSONUtil.parseList(value);
				dataMap.put(fd.toUpperCase(), subList);
			}
		}else if("ATLT".equals(edr)){//如果是附件列表控件
			String value = request.getParameter(fd);
			if(value!=null){
				atltMap.put(fd, value);
//				atltMap.put(fd+"_edr_pa", edr_pa);
			}
		}else if("TXTE".equals(edr)){//事件文本框
			String showValue = request.getParameter(fd+"_show");
			String hiddenValue = request.getParameter(fd+"_hidden");
			if(showValue!=null && hiddenValue!=null){
				dataMap.put(fd.toUpperCase(), hiddenValue);
			}
			
		}else if("CHK".equals(edr)){//如果是复选框控件
			String[] values = request.getParameterValues(fd);
			if(values!=null){
				dataMap.put(fd.toUpperCase(),StringUtils.join(values,','));
			}else{
				dataMap.put(fd.toUpperCase(),"");
			}
		}else{
			//一般类型直接存
			if("VARCHAR、VARCHAR2、NUMBER".indexOf(tp)>=0){
				String value = request.getParameter(fd);
				if(value!=null){
					dataMap.put(fd.toUpperCase(), value);
				}
			}
			if("CLOB".indexOf(tp)>=0){
				String value = request.getParameter(fd);
				if(value!=null){
					if(edr.equals("HTML")){
						//特殊处理单引号，否则在JSON化时会出现问题，该问题只会在富文本中出现
						value = value.replace("&quot;", "\"");
					}
					dataMap.put(fd.toUpperCase(), value);
				}
			}
			
			//日期型，需要解析为日期对象
			if("TIMESTAMP".equals(tp)){
				String value = request.getParameter(fd);
				String fm = (String) edrParamMap.get("fm");
				if(value!=null){
					Timestamp ts = null;
					if(value.length()>0){
						Date dt = DateUtils.parseDate(value,fm);
						long time = dt.getTime();
//						if(time>0){
							ts =  new Timestamp(time);
//						}
					}
					dataMap.put(fd.toUpperCase(),ts);
				}
			}
			//如果是二进制类型
			if("BLOB".equals(tp)){
				if(request instanceof MultiPartRequestWrapper){
					MultiPartRequestWrapper multiRequest = (MultiPartRequestWrapper) request;
					File[] f = multiRequest.getFiles(fd);
					if(f.length>0){
						byte[] fileContent = FileUtil.getBytesFromFile(f[0]);
						dataMap.put(fd.toUpperCase(),fileContent);
					}
				}
			}
		}
	}

	/**
	 * 显示时计算 通过ajax请求，传输计算公式，将计算结果输出
	 * 
	 * @cf 计算公式
	 * @value 字段值
	 * @return
	 * @throws Exception
	 */
	public String evalCalcFormulaAction() throws Exception {
		String cf = request.getParameter("cf"); // 公式
		String value = request.getParameter("value"); //原本值
		if (StringUtil.isNotEmpty(cf)) {
			cf = StringUtil.decodeChars(cf, "@,>,<,+,-,?,(,), ,.,:,\",'");
		}
		try {
			EFormExp exp = new EFormExp(value, this.sessionAttrs);
			exp.initExpContext(request);
			String ret = exp.eval(cf);
			log.info("公式"+cf+"计算结果:"+ret);
			outputJson("{success:true,ret:'" + ret + "'}");
		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = ex.getMessage();
			msg = StringUtil.formatHTML(msg);
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}
	
	
	
	/**
	 * 定稿功能
	 * @return
	 */
	public String finalManuscriptAction(){
		try {
			String id = request.getParameter("id");
			EForm eForm = eformManager.get(id);
			String schema = SystemConfig.getProperty("jdbc.schema");
			DefTableInfo defTableInfo = eForm.getDefTable();
			
			if(null != defTableInfo){
				if(!physicTableManager.isExist(schema,defTableInfo.getTableCode())){
					//不存在该物理表, 重建物理表
					String tableName = defTableInfo.getTableCode();
					List list = defColumnInfoManager.findColumnInfoByTableName(schema, tableName);
					physicTableManager.rebuildTable(schema, tableName, list);
				}
				
				if(StringUtil.isNotEmpty(defTableInfo.getModifyRec())){
					//执行修改记录(SQL)
					physicTableManager.executeSqlBatch(defTableInfo.getModifyRec());
					//清空表定义中的修改记录
					defTableInfo.setModifyRec("");
					defTableInfoManager.save(defTableInfo);
				}
				//设置为 已定稿
				eForm.setFinalManuscript(true);
				eformManager.save(eForm);
				this.outputJson("{success:true}");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return NONE;
	}
	
	
	/**
	 *描述：执行自定义表单查询功能，最终返回唯一符合条件的记录并返回其docid
	 *时间：2010-5-31
	 *作者：谭畅
	 *参数：
	 *   @request formSn 自定义表单标识，用以定位该表单关联的表定义对象，并据此构建对应的SQL语句的SELECT、FROM部分
	 *   @request condition 条件 将会组装到SQL语句的WHERE部分的SQL代码片段
	 *返回值:
	 *    json:结果集中第一行记录进行JSON化并输出
	 *抛出异常：
	 * @throws Exception 
	 *
	 */
	public String eformRecSearchAction() throws Exception{
		String condition = request.getParameter("condition");
		String formSn = request.getParameter("formSn");
		
		EForm eform = eformManager.findUniqueBy("sn", formSn);
		String tableName = eform.getDefTable().getTableCode();
		String schema = eform.getDefTable().getSchema();
		String pk = physicTableManager.findUniquePrimaryKeyName(schema, tableName);
		String sql = "SELECT "+pk+" FROM "+schema+"."+tableName+" WHERE "+condition;
		log.info("API Search方法调用: "+sql);
		Object id = physicTableManager.queryUniqueBySql(sql);
		if(id!=null){
			this.outputJson("{success:true,docid:'"+id+"'}");
		}else{
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	
	/**
	 * 
	 *描述：用于子表控件获取数据列表
	 *时间：2010-6-2
	 *作者：谭畅
	 *参数：
	 * @throws Exception 
	 *	@request 
	 *       subTableName:子表表明,可包含SCHEMA名称
	         fkFieldName:和主表关联的外键字段名
	         where:额外的子表过滤条件
	        docid:this.docid
	 *返回值:
	 *抛出异常：
	 */
	public String eformRecGetSubDataAction() throws Exception{
		String subTableName = request.getParameter("subTableName");
		String fkFieldName = request.getParameter("fkFieldName");
		String subTableWhere = request.getParameter("where");
		String subTableOrder = request.getParameter("subTableOrder");
		String docid = request.getParameter("docid");
		
		String msg = "";
		if(StringUtil.isEmpty(subTableName)||
				StringUtil.isEmpty(fkFieldName)||
				StringUtil.isEmpty(docid)){
			msg = "参数调用不正确";
		}
		try {
			String sql = _buildSubGridQuerySql(subTableName, fkFieldName,
					subTableWhere, subTableOrder, docid);
			log.info("子表查询SQL:"+sql);
			List list = physicTableManager.querySqlData(sql);
			String json = JSONUtil.listToJson(list);
			this.outputJson("{success:true,totalCount:"+list.size()+",list:"+json+"}");
			return NONE;
		} catch (Exception e) {
			msg = e.toString();
			e.printStackTrace();
		}
		this.outputJson("{success:false,msg:'"+msg+"'}");
		return NONE;
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
	 * 唯一性数据项有效性验证
	 * @return
	 * @throws Exception 
	 */
	public String uniqueDiValidateAction() throws Exception{
		String formSn = WebUtils.getRequestParam(request, "formSn");
		String fd = WebUtils.getRequestParam(request, "fd");
		String fdValue = WebUtils.getRequestParam(request, "fdValue");
		String docid = WebUtils.getRequestParam(request, "docid");
		try{
			EForm eform = eformManager.getEFormBySn(formSn);
			DefTableInfo table = eform.getDefTable();
			String tableCode = table.getTableCode();
			String sql = "SELECT COUNT(*) FROM "+tableCode+" WHERE ";
			DefColumnInfo col = defColumnInfoManager.findColumnByCode(table.getId(), fd);
			if(col.getColumntype().equals("")){
				sql += (fd+" = '"+fdValue+"'");
			}else{
				sql += (fd+" = '"+fdValue+"'");
			}
			
			if(StringUtil.isNotEmpty(docid)){
				sql += " AND ID <> '"+docid+"'";
			}
			log.info("验证表单?的字段?是否唯一:"+sql);
			BigDecimal count = (BigDecimal) physicTableManager.queryUniqueBySql(sql);
			if(count.intValue()>0){
				this.outputJson("{success:true,unique:false}");
			}else{
				this.outputJson("{success:true,unique:true}");
			}
		}catch(Exception ex){
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	/**
	 * 
	 *描述：单独保存子表数据
	 *时间：2010-6-3
	 *作者：谭畅
	 *参数：
	 * @throws Exception 
	 *   @request docid 主表关键字编号 
	 *   @request idFieldName 从表关键字字段名称
	 *   @request json 从表被修改的数据
	 *   @request subTableName 从表表名 
	 *返回值:
	 *抛出异常：
	 */
	public String eformRecSaveSubAction() throws Exception{
		try{
			String json = request.getParameter("json");
			String docid = request.getParameter("docid");
			String subTableName = request.getParameter("subTableName");
			String pkFieldName = request.getParameter("pkFieldName");
			String fkFieldName = request.getParameter("fkFieldName");
			
			List records = JSONUtil.parseList(json);
			StringBuffer sqlList = new StringBuffer();
			for (Object obj : records) {
				Map recMap = (Map) obj;
				String sql = _buildSubGridSaveOrUpdateSql(recMap,subTableName,pkFieldName,fkFieldName,docid);
				sqlList.append(sql+";");
			}
			String sqls = sqlList.toString();
			if(StringUtil.isNotEmpty(sqls)){
				physicTableManager.executeSqlBatch(sqlList.toString());
			}
			this.outputJson("{success:true}");
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	
	/**
	 * 
	 *描述：删除从表数据动作
	 *时间：2010-6-3
	 *作者：谭畅
	 *参数：@request ids 删除编号列表,以逗号分隔多个id
	 *     @request subTableName	子表表名
	 *     @request pkFieldName	子表主键字段名
	 *返回值:
	 *抛出异常：
	 */
	public String eformRecDeleteSubAction() throws Exception{
		try{
			String ids = request.getParameter("ids");
			String subTableName = request.getParameter("subTableName");
			String pkFieldName = request.getParameter("pkFieldName");
			StringBuffer idsSB = new StringBuffer();
			if(StringUtil.isNotEmpty(ids)){
				String[] idsArray = ids.split(",");
				for (String id : idsArray) {
					if(StringUtil.isNotEmpty(id)){
						idsSB.append("'"+id+"',");
					}
				}
				idsSB.append("''");
			}
			String sql = "DELETE "+subTableName+" WHERE "+pkFieldName+" in ("+idsSB+")";
			physicTableManager.executeSqlBatch(sql);
			this.outputJson("{success:true}");
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		
		return NONE;
	}
	
	
	

	/**
	 * 
	 *描述：根据相关数据，构建操作子表记录的INSERT OR UPDATE SQL语句
	 *如果主表的主键有值，表示属于修改,否则属于创建
	 *根据表名，取得该表的所有字段定义列表，并遍历字段列表，构建出不同的SQL语句
	 *时间：2010-6-3
	 *作者：谭畅
	 *参数：
	 *		@param recMap 记录数据
	 *		@param subTableName 子表表名
	 *		@param pkFieldName 子表PK字段名
	 *		@param fkFieldName 子表FK字段名
	 *		@param docid 主表ID值
	 *返回值:
	 *		构建成功的INSERT OR UPDATE SQL语句
	 *抛出异常：
	 */
	private String _buildSubGridSaveOrUpdateSql(Map<String, Object> recMap,
			String subTableName, String pkFieldName,String fkFieldName,String docid) {
		String idValue = (String) recMap.get(pkFieldName);
		String schema = SystemConfig.getProperty("jdbc.schema");
		DefColumnInfoManager defColumnInfoManager = (DefColumnInfoManager) SpringContextUtil.getBean("defColumnInfoManager");
		List<DefColumnInfo> cols = defColumnInfoManager.findColumnInfoByTableName(schema, subTableName);
		boolean isNew = false; 
		if(StringUtil.isEmpty(idValue)){
			isNew = true;
		}
		String sql = null;
		if(isNew){
			sql = _buildSubGridInsertSql(recMap, subTableName, pkFieldName, fkFieldName, docid,cols);
		}else{
			sql = _buildSubGridUpdateSql(recMap, subTableName, pkFieldName,idValue, cols);
		}
		return sql;
	}

	/**
	 * 
	 *描述：构建子表的更新SQL语句
	 *时间：2010-6-8
	 *作者：谭畅
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	private String _buildSubGridUpdateSql(Map<String, Object> recMap,
			String subTableName, String pkFieldName, String idValue,
			List<DefColumnInfo> cols) {
		String sql;
		sql ="UPDATE "+subTableName+" SET";
		StringBuffer fdList = new StringBuffer();
		for (DefColumnInfo col : cols) {
			String fd = col.getColumncode();
			Object value = recMap.get(fd);
			if(value!=null){
				if(col.getColumntype().indexOf("TIMESTAMP")>=0 || col.getColumntype().indexOf("DATE")>=0){
					String fm = col.getFormat();
					if(fm==null){
						fm = (col.getColumntype().indexOf("TIMESTAMP")>=0?"YYYY-MM-DD HH24:MI:SS":"YYYY-MM-DD");
					}else{
						fm = _extDtFormatToOracleDtFormat(fm);
					}
					fdList.append(fd+ "=TO_DATE('"+value+"','"+fm+"'),");
				}else{
					fdList.append(fd +"='"+value+"',");
				}
			}
		}
		if(fdList.length()>0)
			fdList = fdList.deleteCharAt(fdList.length()-1);
		sql = sql + " "+ fdList + " WHERE "+pkFieldName+"='"+idValue+"'";
		return sql;
	}

	/**
	 * 
	 *描述：构建子表插入SQL语句
	 *时间：2010-6-8
	 *作者：谭畅
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	private String _buildSubGridInsertSql(Map<String, Object> recMap, String subTableName,
			String pkFieldName, String fkFieldName, String docid,
			List<DefColumnInfo> cols) {
		String sql;
		sql = "INSERT INTO "+subTableName;
		StringBuffer fdList = new StringBuffer();
		StringBuffer vlList = new StringBuffer();
		for (DefColumnInfo col : cols) {
			String fd = col.getColumncode();
			Object value = null;
			if(fd.equals(pkFieldName)){
				value = UUIDGenerator.hibernateUUID();
			}else if(fd.equals(fkFieldName)){
				value = docid;
			}else{
				value = recMap.get(fd);
			}
			//如果值为空，则不处理该字段
			if(value!=null){
				fdList.append(fd+",");
				if(col.getColumntype().indexOf("TIMESTAMP")>=0 || col.getColumntype().indexOf("DATE")>=0){
					String fm = col.getFormat();
					if(fm==null){
						fm = (col.getColumntype().indexOf("TIMESTAMP")>=0?"YYYY-MM-DD HH24:MI:SS":"YYYY-MM-DD");
					}else{
						fm = _extDtFormatToOracleDtFormat(fm);
					}
					vlList.append("TO_DATE('"+value+"','"+fm+"'),");
				}else{
					vlList.append("'"+value+"',");
					
				}
			}
		}
		fdList.deleteCharAt(fdList.length()-1);
		vlList.deleteCharAt(vlList.length()-1);
		sql = sql + " ("+fdList+") "+" VALUES ("+vlList+")";
		return sql;
	}
	


	/**
	 * 
	 *描述：构建处理子表的SQL语句集，以分号分隔
	 *时间：2010-6-3
	 *作者：谭畅
	 *参数：
	 *    @param subList	所有子表的所有记录的集合
	 *    @param diMap
	 *返回值:
	 *抛出异常：
	 */
	@SuppressWarnings("unchecked")
	private String _buildSubGridSql(Map<String,List<Map>> subListMap, Map<String, Element> diMap,String docid) {
		StringBuffer sqlSB = new StringBuffer();;
		for (String fd : subListMap.keySet()) {
			List<Map> subList = subListMap.get(fd);
			Element di = diMap.get(fd);
			
			String edrParam = di.attributeValue("edr_pa");
			edrParam = StringUtil.decodeChars(edrParam, "',\",&");
			Map<String,String> edrParamMap = JSONUtil.parseObject(edrParam);
			String subTableName = edrParamMap.get("subTableName");
			String pkFieldName = edrParamMap.get("subTablePK");
			String fkFieldName = edrParamMap.get("subTableFK");
			
			for (Map<String, Object> recMap : subList) {
				String sql = _buildSubGridSaveOrUpdateSql(recMap, subTableName, pkFieldName, fkFieldName, docid);
				sqlSB.append(sql + ";\r\n");
			}
		}
		return sqlSB.toString();
	}
	
	

	
	/**
	 * 
	 *描述： 根据指定的数据项定义和数据信息新建或者修改指定表中的一行记录
	 *时间：2010-5-14
	 *作者：谭畅
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws Exception 
	 */
	private String _saveOrUpdateRec(String schema, String tableName,
			String docid, Map<String,Element> diMap, Map<String,Object> dataMap) throws Exception {
		EFormExp exp = new EFormExp(sessionAttrs);
		exp.initExpContext(request);
//		String sfyrz = (String) dataMap.get("SFYRZ");         //是否有操作日志(针对需求计划申请每一步记录具体操作日志)
//		if (StringUtil.isNotEmpty(sfyrz) && "true".equalsIgnoreCase(sfyrz)) {
//			List<Map<String, String>> delList = JSONUtil.parseList((String)dataMap.get("SCSJSZ"));
//			if(delList.size()>0){
//				for(Map<String, String> delMap : delList){
//					System.out.println(delMap.get("wzmc")+","+delMap.get("xhgg")+","+delMap.get("sqsl"));
//				}
//			}
//		}
		boolean isNew = StringUtil.isEmpty(docid);	//如果文档编号为空，表示新创建记录，否则修改记录
		if(isNew)
			docid = UUIDGenerator.hibernateUUID();
		dataMap.put("ID", docid);
		String sql = _buildUpdatePreparedSql(schema,tableName,dataMap,diMap,isNew);
		log.info("保存SQL:"+sql);
		Connection conn = physicTableManager.getConnection();
		boolean autoCommit = conn.getAutoCommit();
		conn.setAutoCommit(false);
		try{
			PreparedStatement pst = conn.prepareStatement(sql);
			Map<String,List<Map>> subList = new HashMap<String,List<Map>>();
			int i=0;
			//设置每个字段值
			for(String fd:dataMap.keySet()){
				Object obj = dataMap.get(fd);
				if(obj instanceof List){
					subList.put(fd, (List<Map>) obj);
					continue;	//过滤掉子表数据
				} 
				i++;
				Object value = dataMap.get(fd);
				Element diNode = (Element) diMap.get(fd);
				String cf_sa = null;
				String cf_cr = null;
				if(diNode!=null){
					cf_sa = diNode.attributeValue("cf_sa");
					cf_cr = diNode.attributeValue("cf_cr");
				}
				/**
				 * 创建时计算
				 */
				if(StringUtil.isNotEmpty(cf_cr) && isNew){
					System.out.println("创建时计算公式："+cf_cr);
					exp.setValue((String)value);
					value = exp.eval(cf_cr);
				}
				
				/**
				 * 保存时计算
				 */
				if(StringUtil.isNotEmpty(cf_sa)){
					System.out.println("保存时计算公式："+cf_sa);
					exp.setValue((String)value);
					value = exp.eval(cf_sa);
				}
				
				pst.setObject(i, value);
			}
			if(!isNew)
				pst.setObject(i+1,docid);
			pst.execute();
			pst.close();
			
			//子表数据保存
			if(subList.size()>0){
				Statement statement = conn.createStatement();
				String sqlList = _buildSubGridSql(subList,diMap,docid);
				String[] sqls = sqlList.split(";");
				for (int j = 0; j < sqls.length; j++) {
					if(StringUtil.isNotEmpty(sqls[j].trim())){
						statement.addBatch(sqls[j]);
					}
				}
				statement.executeBatch();
				statement.close();
			}
			
			eformBeforeSave(conn,docid,diMap,dataMap,isNew);
			
		}catch(Exception ex){
			ex.printStackTrace();
			conn.rollback();
			throw ex;
		}finally{
			conn.commit();
			conn.setAutoCommit(autoCommit);
			if(conn!=null){
				conn.close();
			}
		}
		return docid;
	}

	
	/**
	 * 
	 *描述：根据指定的数据项构建插入和更新SQL语句
	 *时间：2010-5-28
	 *作者：谭畅
	 *参数：
	 *返回值:
	 *抛出异常：
	 * @throws SQLException 
	 */
	private String _buildUpdatePreparedSql(String schema, String tableName,
			Map<String, Object> dataMap, Map<String, Element> diMap,boolean isNew) throws SQLException {
		String sql = null;
		if(isNew){ 
			StringBuffer colListSB = new StringBuffer();
			StringBuffer wh = new StringBuffer();
			for(String col : dataMap.keySet()){
				Object obj = dataMap.get(col);
				if(obj instanceof List) continue;	//过滤掉子表数据
				colListSB.append(col+",");
				wh.append("?,");
			}
//			colListSB.append("ID");
//			wh.append("?");
			if(colListSB.length()>0){
				colListSB.deleteCharAt(colListSB.length()-1);
				wh.deleteCharAt(wh.length()-1);
			}
			sql = "INSERT INTO "+schema+"."+tableName+" ("+colListSB+") VALUES ("+wh+")";
		}else{
			StringBuffer colListSB = new StringBuffer();
			String key = physicTableManager.findUniquePrimaryKeyName(schema,tableName);
			for(String col : dataMap.keySet()){
				Object obj = dataMap.get(col);
				if(obj instanceof List) continue;	//过滤掉子表数据
				colListSB.append(col+"=?,");
			}
			if(colListSB.length()>0){
				colListSB.deleteCharAt(colListSB.length()-1);
			}
			sql = "UPDATE "+schema+"."+tableName+" SET "+colListSB+" WHERE "+key+"=?";
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
	 *描述：下载EFORM记录的EXCEL展现方式
	 *		
	 *时间：2010-6-24
	 *作者：谭畅
	 *参数：
	 *	formSn
	 *  docid
	 *返回值:
	 *抛出异常：
	 * @throws IOException 
	 */
	public String downloadEFormRecInExcelAction() throws IOException{
		String formSn = request.getParameter("formSn");
		String docid = request.getParameter("docid");
		EForm eform = eformManager.getEFormBySn(formSn);
		byte[] excelBytes = eform.getExcelTemplate();
		String excelDataItemXml = eform.getExcelDataItemXml();
		_downloadBytes("application/vnd.ms-excel;charset=utf-8","导出数据_"+UUIDGenerator.javaId(),excelBytes);
		return NONE;
	}
	
	
	private void _downloadBytes(String contentType,String fileName,byte[] bytes) throws IOException{
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(("导出数据_" + System.currentTimeMillis() + ".xlsx")
						.getBytes(), "iso-8859-1"));
		try {
			bis = new BufferedInputStream(new ByteArrayInputStream(bytes));
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			// Simple read/write loop.
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}

		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}

	/**
	 * 导出自定义表单数据动作
	 * leichi
	 * @return
	 * @throws Exception
	 */
	public String eformRecExportExcelAction() throws Exception {

		// Excel表的头信息（逗号表达式）,由于前台是通过window.open(url)来传输此中文,则需要通过转码
		String paraHeader = WebUtils.getRequestParam(request, "paraHeader");

		// 表索引信息（逗号表达式）
		String paraDataIndex = request.getParameter("paraDataIndex");

		// 宽度(逗号表达式)
		String paraWidth = request.getParameter("paraWidth");
		String formSn = request.getParameter("formSn");

		List list = (List)getEFormRecListPage().getResult();
		
		// 调用导出方法
		export(list, paraHeader, paraDataIndex, paraWidth);

		return NONE;
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
	 * 根据指定的表单，找到对应的物理表，并将该物理表中的数据列出来
	 * @return
	 * @throws Exception 
	 */
	public String showEFormRecListAction() throws Exception{		
		try{
			Page page = getEFormRecListPage();
			String json=JSONUtil.listToJson((List)page.getResult());
			json="{totalCount:'" + page.getTotalCount() + "',list:"+ json + "}";
			this.outputJson(json);
		
		}catch(Exception ex){
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 获取EFORMREC的翻页数据
	 * @return
	 */
	public Page getEFormRecListPage(){
		String formSn = request.getParameter("formSn");
		EForm eform = eformManager.getEFormBySn(formSn);
		
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT", "20");
		
		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "1";
		
		int iStart=Integer.parseInt(start);
		int iLimit=Integer.parseInt(limit);
		try{
		
			DefTableInfo tableInfo=eform.getDefTable();
			Collection<DefColumnInfo> cols=tableInfo.getColumns();
			StringBuffer selectColSb=new StringBuffer();
			for (DefColumnInfo defColumnInfo : cols) {
				if(defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_BLOB) ||defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_CLOB)){
					continue;
				}
				if(defColumnInfo.getColumntype().equals(DefColumnType.CTYPE_DATE)){
					selectColSb.append("to_char("+defColumnInfo.getColumncode()+",'YYYY-MM-DD') as "+defColumnInfo.getColumncode()+",");
				}else if(defColumnInfo.getColumntype().indexOf(DefColumnType.CTYPE_TIMESTAMP)>=0){
					selectColSb.append("to_char("+defColumnInfo.getColumncode()+",'YYYY-MM-DD hh24:mi:ss') as "+defColumnInfo.getColumncode()+",");
				}else{
					selectColSb.append(defColumnInfo.getColumncode()+",");
				}
			}
			if(selectColSb.length()>0)
				selectColSb.deleteCharAt(selectColSb.length()-1);
			
			
			String orderBy = request.getParameter("sort");
			String tmpOrder="";
			if (StringUtils.isNotEmpty(orderBy)) {
				String dir = request.getParameter("dir");
				if (StringUtils.isEmpty(dir))
					dir = "asc";
				tmpOrder="ORDER BY "+orderBy+" "+dir+"";
			}
			
			String sql="SELECT "+selectColSb.toString()+" FROM "+tableInfo.getSchema()+"."+tableInfo.getTableCode()+" "+ tmpOrder;
			return physicTableManager.pagedQueryTableData(sql, iStart, iLimit);		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	
	
	/**
	 * 根据自定义表单formSn和记录ID删除自定义表单记录
	 * @return
	 * @throws Exception 
	 */
	public String delEFormRecAction() throws Exception{
		String formSn = request.getParameter("formSn");
		String ids = request.getParameter("ids");
		try{
			EForm eform = eformManager.getEFormBySn(formSn);
			DefTableInfo tableInfo=eform.getDefTable();
			StringBuffer idsSB = new StringBuffer();
			String[] tmpIds = ids.split(",");
			for (String id : tmpIds) {
				idsSB.append("'"+id+"',");
			}
			if(idsSB.length()>0){
				idsSB = idsSB.deleteCharAt(idsSB.length()-1);
			}
			
			String schema = SystemConfig.getProperty("jdbc.schema");
			String pk = physicTableManager.findPrimaryKeyColumnNameList(schema, tableInfo.getTableCode()).get(0);
			String sql = "DELETE "+schema+"."+tableInfo.getTableCode()+" WHERE "+pk+" IN ("+idsSB+")";
			log.info(sql);
			physicTableManager.executeSqlBatch(sql);
			this.outputJson("{success:true}");
		}catch(Exception ex){
			ex.printStackTrace();
			this.outputJson("{success:false}");
		}
		return NONE;
	}
	
	
	
	

	/**
	 * 保存字段信息,并保存表的修改记录.
	 * @author caihuiwen.
	 * @return
	 * @throws Exception
	 */
	public String saveColumnInfoAction()throws Exception{
		try {
			DefColumnInfo defColumnInfo = null;
			String modifyType = null;
			String beforColumnCode = request.getParameter("beforcolumncode");
			String id = request.getParameter("id");
			String tableid = request.getParameter("tableid");
			String columnlength = request.getParameter("columnlength");
			String columnorder = request.getParameter("columnorder");
			String formSn = request.getParameter("formSn");
			
			if(StringUtil.isNotEmpty(id)){
				defColumnInfo = defColumnInfoManager.get(id);
				modifyType = "alter";
			}else {
				defColumnInfo = new DefColumnInfo();
				modifyType = "add";
			}
			
			if (StringUtil.isNotEmpty(tableid)) {
				defColumnInfo.setTable(defTableInfoManager.get(tableid));
			}
			if (StringUtil.isNotEmpty(columnlength)) {
				defColumnInfo.setColumnlength(Integer.parseInt(columnlength));
			}
			if (StringUtil.isNotEmpty(columnorder)) {
				defColumnInfo.setColumnorder(Long.parseLong(columnorder));
			}
			
			String allownull = request.getParameter("allownull");
			if(StringUtil.isNotEmpty(allownull)){
				if("on".equals(allownull)){
					defColumnInfo.setAllownull(true);
				}else {
					defColumnInfo.setAllownull(Boolean.parseBoolean(allownull));
				}
			}else{
				defColumnInfo.setAllownull(false);
			}
			String pk = request.getParameter("pk");
			if(StringUtil.isNotEmpty(pk)){
				if("on".equals(pk)){
					defColumnInfo.setPk(true);
				}else{
					defColumnInfo.setPk(Boolean.parseBoolean(pk));
				}
			}else{
				defColumnInfo.setPk(false);
			}
			
			defColumnInfo.setColumntype(request.getParameter("columntype"));
			defColumnInfo.setColumncode(request.getParameter("columncode"));
			defColumnInfo.setColumnname(request.getParameter("columnname"));
			defColumnInfo.setDefaultvalue(request.getParameter("defaultvalue"));
			defColumnInfo.setFormat(request.getParameter("format"));
			defColumnInfo.setComm(request.getParameter("comm"));
			String columnprec = request.getParameter("columnprec");
			if (StringUtil.isNotEmpty(columnprec)) {
				defColumnInfo.setColumnprec(Integer.parseInt(columnprec));
			}
			
			//唯一键
			boolean unique = false;
			if(StringUtil.isNotEmpty(request.getParameter("chkUnique"))){
				unique = Boolean.parseBoolean(request.getParameter("chkUnique"));
			}
			
			//保存 对应表的修改记录
			defTableInfoManager.modifyRecByDefColumnInfoChange(defColumnInfo, modifyType, beforColumnCode, null, unique);
			//保存字段信息
			defColumnInfoManager.save(defColumnInfo);
			
			//将关联到此数据定义的 表单定稿设为[未定稿]
			EForm eForm = eformManager.getEFormBySn(formSn);
			if(null != eForm){
				eForm.setFinalManuscript(false);
				eformManager.save(eForm);
			}
			
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 移动表单至一个分类
	 * @return
	 */
	public String moveFormToCatalog(){
		String formIds = request.getParameter("formIds");
		String oCurrentNodeId = request.getParameter("oCurrentNodeId");
		
		EFormCatalog catalog = eformCatalogManager.get(oCurrentNodeId);
		String[] arrayId = formIds.split(",");
		for (int i = 0; i < arrayId.length; i++) {
			EForm eForm = eformManager.get(arrayId[i]);
			eForm.setCatalog(catalog);
			eformCatalogManager.save(eForm);
		}
		
		try {
			this.outputJson("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	/**
	 * 根据请求参数验证表单标识是否唯一
	 * @return
	 * @throws Exception
	 */
	public String validateEFormSnUniqueAction() throws Exception{
		String formSn = request.getParameter("formSn");
		try{
			String hql = "select count(*) from EForm e where e.sn = ?";
			Long count = (Long) eformManager.findUniqueByHql(hql, formSn);
			
			if(count>0){
				this.outputJson("{success:true,unique:false}");
			}else{
				this.outputJson("{success:true,unique:true}");
			}
		}catch(Exception ex){
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
		
	}
	
	
	/**
	 * 执行指定的SQL语句并以JSON字符串的方式返回结果集
	 * @return
	 * @throws Exception 
	 */
	public String doQueryBySqlAction() throws Exception{
		try{
			String sql= request.getParameter("sql");
			EFormExp exp = new EFormExp("", this.sessionAttrs);
			exp.initExpContext(request);
			sql = exp.eval(sql);
			List list = physicTableManager.querySqlData(sql);
			
			String json=JSONUtil.listToJson(list);
			json="{success:true,list:"+ json + "}";
			this.outputJson(json);
		}catch(Exception ex){
			this.outputJson("{success:false}");
//			ex.printStackTrace();
		}
		return NONE;
	}

	public static void main(String[] args) throws ScriptException, JavaScriptException {
//		ScriptEngineManager factory = new ScriptEngineManager();
//		ScriptEngine engine = factory.getEngineByName("JavaScript");
//		engine.eval("var xx = [{a:'ccc',b:'cc'},{a:'ccc',b:'cc'}];");
//		NativeArray obj = (NativeArray) engine.get("xx");
//		System.out.println(obj.getLength());
		  {    
		        Context cx = Context.enter();
		        try   
		        {    
		            Scriptable scope = cx.initStandardObjects(null);
		            String str = "function(){getEditor('xxx').setValue();}";    
		            Object result = cx.evaluateString(scope, str, null, 1, null);    
//		            double res = Context.toNumber(result);    
//		            System.out.println(res);
		            NativeArray na = (NativeArray) result;
		            NativeObject obj = (NativeObject) na.get(0, null);
		            Object obj2 = NativeObject.getProperty(obj, "header");
		            Boolean hidden = (Boolean) NativeObject.getProperty(obj, "bbb");
		            System.out.println(obj2);
		        }    
		        finally   
		        {    
		            Context.exit();    
		        }    
		    } 
	}
	
	
	protected void eformBeforeSave(Connection conn, String docid, Map<String, Element> diMap, Map<String, Object> dataMap, boolean isNew)throws Exception{
		
	}
	
}
