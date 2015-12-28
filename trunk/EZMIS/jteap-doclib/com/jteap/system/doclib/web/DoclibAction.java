package com.jteap.system.doclib.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Attribute;
import org.dom4j.Element;

import com.jteap.core.Constants;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.AbstractAction;
import com.jteap.system.doclib.manager.DoclibAttachManager;
import com.jteap.system.doclib.manager.DoclibCatalogManager;
import com.jteap.system.doclib.manager.DoclibFvManager;
import com.jteap.system.doclib.manager.DoclibLevelManager;
import com.jteap.system.doclib.manager.DoclibLevelRoleManager;
import com.jteap.system.doclib.manager.DoclibManager;
import com.jteap.system.doclib.model.Doclib;
import com.jteap.system.doclib.model.DoclibAttach;
import com.jteap.system.doclib.model.DoclibCatalog;
import com.jteap.system.doclib.model.DoclibCatalogField;
import com.jteap.system.doclib.model.DoclibFv;
import com.jteap.system.doclib.model.DoclibLevel;
import com.jteap.system.doclib.model.DoclibLevelRole;
import com.jteap.system.doclib.util.DOM4JXMLOperator;
import com.jteap.system.doclib.util.HtmlSupport;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
import com.jteap.system.role.model.Role;
import com.opensymphony.xwork2.util.URLUtil;

/**
 * 文档中心操作对象
 * 
 * @author caofei
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DoclibAction extends AbstractAction {

	private DoclibManager doclibManager;

	private DoclibCatalogManager doclibCatalogManager;

	private DoclibFvManager doclibFvManager;
	private DoclibAttachManager doclibAttachManager;

	private DoclibLevelManager doclibLevelManager;
	private DoclibLevelRoleManager doclibLevelRoleManager;
	private PersonManager personManager;
	private File[] attachFile;

	public DoclibCatalogManager getDoclibCatalogManager() {
		return doclibCatalogManager;
	}

	public void setDoclibCatalogManager(
			DoclibCatalogManager doclibCatalogManager) {
		this.doclibCatalogManager = doclibCatalogManager;
	}

	public DoclibManager getDoclibManager() {
		return doclibManager;
	}

	public void setDoclibManager(DoclibManager doclibManager) {
		this.doclibManager = doclibManager;
	}

	@Override
	public HibernateEntityDao getManager() {
		return doclibManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "title", "creator","createdt", "time",
				"doclibLevel", "levelName", "doclibCatalog" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "title", "creator", "content", "createdt", "time",
				"doclibLevel", "levelName" };
	}

	/**
	 * 列表显示之前针对条件和排序进行处理
	 */
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {

		// 添加查询分类条件
		String catalogId = request.getParameter("catalogId");
		if (StringUtils.isNotEmpty(catalogId)) {
			HqlUtil.addCondition(hql, "doclibCatalog.id", catalogId);
		}
		if (!isCurrentRootUser()) {
			Person person = personManager.getCurrentPerson(this.sessionAttrs);
			java.util.Set<P2Role> p2rSet = person.getRoles();
			//System.out.println(p2rSet);
			Collection<DoclibLevelRole> docLevelList = doclibLevelRoleManager
					.findDoclibLevelByRoleList(p2rSet);
			String levelList = getDoclibLevelStringFormList(docLevelList);
			if (levelList == null || levelList.equals("")) {
				levelList = "''";
			}
			HqlUtil.addCondition(hql, "doclibLevel.id", levelList,
					HqlUtil.LOGIC_AND, HqlUtil.TYPE_IN);
		}
		// 默认按照创建时间倒序排序
		if (!this.isHaveSortField()) {
			HqlUtil.addOrder(hql, "createdt", "desc");
		}
	}
	
	

	/**
	 * 
	 * @param levelSet
	 * @return
	 */
	private String getDoclibLevelStringFormList(
			Collection<DoclibLevelRole> levelSet) {
		int intCount = 1;
		StringBuffer inWhere = new StringBuffer();
		for (DoclibLevelRole doclibLevelRole : levelSet) {
			String id = doclibLevelRole.getDoclibLevel().getId();
			inWhere.append("'");
			inWhere.append(id);
			inWhere.append("'");
			if (intCount < levelSet.size()) {
				inWhere.append(",");
			}
			intCount++;
		}
		//System.out.println(inWhere.toString());
		return inWhere.toString();
	}

	/**
	 * 显示文档信息
	 */
	public String showDoclibInfoAction() throws Exception {
		request.setCharacterEncoding("UTF-8");
		Doclib doclib = null;
		DoclibCatalog doclibCatalog = null;
		DoclibCatalogField doclibCatalogField = null;
		DoclibFv doclibFv = null;
		DoclibAttach doclibAttach = null;
		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			doclib = doclibManager.findDoclibById(id);
			doclibCatalog = doclibManager.findDoclibCatalogById(id);
			doclibCatalogField = doclibManager.findDoclibCatalogFieldById(id);
			doclibFv = doclibManager.findDoclibFvById(id);
			doclibAttach = doclibManager.findDoclibAttachById(id);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		String createdt = dateFormat.format(doclib.getCreatedt());
		request.setAttribute("createdt", createdt);
		request.setAttribute("doclib", doclib);
		request.setAttribute("doclibCatalog", doclibCatalog);
		request.setAttribute("doclibCatalogField", doclibCatalogField);
		request.setAttribute("doclibFv", doclibFv);
		request.setAttribute("doclibAttach", doclibAttach);
		return "showDoclibInfo";
	}

	/**
	 * 修改文档信息
	 */
	public String modifyDoclibInfoAction() throws Exception {
		request.setCharacterEncoding("UTF-8");
		Doclib doclib = null;
		DoclibCatalog doclibCatalog = null;
		DoclibCatalogField doclibCatalogField = null;
		DoclibFv doclibFv = null;
		DoclibAttach doclibAttach = null;
		String id = request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			doclib = doclibManager.findDoclibById(id);
		}
		String json = JSONUtil.objectToJson(doclib, updateJsonProperties());
		outputJson("{success:true,fdlist:" + json + "}");
		return NONE;
	}

	/**
	 * 显示需要更新的文档附件集合
	 */
	public String showUpdataAttachFileAction() throws Exception {
 		String id = request.getParameter("id");
 		//System.out.println(id);
		Doclib doblib = (Doclib) doclibManager.get(id);
		List<DoclibAttach> list = doclibAttachManager.getAttachByDoclib(id);
		String[] fields = new String[] { "id", "name", "type", "doclibSize" };
		String json = JSONUtil.listToJson(list, fields);
		outputJson("{success:true,fdlist:" + json + "}");
		return NONE;
	}

	/**
	 * 
	 *描述：显示要修改的文件扩展字段值集合
	 *时间：2010-6-24(修改ID)
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showDoclibFieldValueAction() throws Exception {
		String docId = request.getParameter("id");
		//字段关联的值
		Collection<DoclibFv> doclibFvs = doclibFvManager.findDoclibFvByDocId(docId);
		//String[] fields = new String[] { "id", "name", "value" };
		//String json = JSONUtil.listToJson(doclibFvs, fields);
		StringBuffer json=new StringBuffer();
		//这边的ID应为字段的ID，不应为具体值的ID
		for(DoclibFv fv:doclibFvs){
			json.append("{'id':'"+fv.getFieldId()+"','name':'"+fv.getName()+"','value':'"+(fv.getValue()==null?"":fv.getValue())+"'},");
		}
		if(!json.toString().equals("")){
			json.deleteCharAt(json.length()-1);
		}
		outputJson("{success:true,fdlist:[" + json.toString() + "]}");
		return NONE;
	}

	/**
	 * 通过分类ID获得所有扩展字段名称
	 */
	public String getDoclibFieldPropertiesAction() throws Exception {
		String id = request.getParameter("id");
		Doclib doc = doclibManager.findDoclibById(id);
		DoclibCatalog doclibCatalog = null;
		if(doc.getId()!=null){
			DoclibCatalog catalogId = doclibManager.findDoclibCatalogById(id);
			doclibCatalog = doclibCatalogManager.findUniqueBy("id",
					catalogId.getId());
		}else{
			doclibCatalog = doclibCatalogManager.findUniqueBy("id",
					id);
		}
		List list = new ArrayList();
		getCatalogField(list, doclibCatalog);
		String json = JSONUtil.listToJson(list, new String[] { "id", "name",
				"type", "emunValue", "format" });
		outputJson("{success:true,fdlist:" + json + "}");
		//System.out.println("===========>getCatalogFieldJson<============"
		//		+ json);
		return NONE;
	}

	/**
	 * 删除文档，级联删除文档扩展字段值和文档附件
	 */
	public String removeAction() throws Exception {
		Doclib doclib = new Doclib();
		String id = request.getParameter("ids");
		doclibManager.remove(doclib, id.split(","));
		outputJson("{success:true}");
		return NONE;
	}

	/**
	 * 递归获得所有扩展字段对象
	 */
	public void getCatalogField(List list, DoclibCatalog doclibCatalog) {
		Collection fields = doclibCatalog.getFields();
		list.addAll(fields);
		if (doclibCatalog.getParent() != null) {
			getCatalogField(list, doclibCatalog.getParent());
		}
	}

	/**
	 * 添加文档信息 2009-08-10
	 */
	public String saveUpdataAction() throws Exception {

		String docTitle = request.getParameter("title");
		String strCreator = request.getParameter("creator");
		String strCreatedt = request.getParameter("createdt");
		String content = request.getParameter("content");
		String strLevelID = request.getParameter("levelID");
		String id = request.getParameter("id");
		String ops = request.getParameter("ops");
		String fdListJosn = request.getParameter("fdListJson");
		try {
			Doclib doclib = null;
			DoclibCatalog doclibCatalog = null;
			DoclibCatalogField doclibCatalogField = null;
			DoclibFv doclibFv = null;

			doclib = (Doclib) this.creatBlankObject();

			doclibCatalog = doclibCatalogManager.get(id);
			
			//DoclibLevel doclibLevel = doclibLevelManager.get(strLevelID);		//旧的文档级别
			DoclibLevel doclibLevel = doclibCatalog.getCatalogPerm();
			doclib.setDoclibLevel(doclibLevel);
			doclib.setDoclibCatalog(doclibCatalog);
			doclib.setTitle(docTitle);
			doclib.setCreator(strCreator);
			doclib.setContent(content);				//保存HTML内容
			//doclib.setWypx(docTitle.split("]")[1]);
			Date createDate = DateUtils.StrToDate(strCreatedt, "yyyy-MM-dd HH:mm:ss");
			doclib.setCreatedt(createDate);
			java.util.Set<DoclibAttach> fields = new java.util.HashSet<DoclibAttach>();
			//System.out.println(this.attachFile.length);
			if (StringUtils.isNotEmpty(ops)) {
				//System.out.println(ops);
				List<Map<String, String>> fieldList = JSONUtil.parseList(ops);
				int mark = 0; // 附件文件的下标
				for (Map fieldMap : fieldList) {
					DoclibAttach doclibAttach = new DoclibAttach();
					// 得到上传文件列表的文件索引。当索引值为null 时 设置为-1
					String strIndex = fieldMap.get("fileIndex") == null ? "-1"
							: fieldMap.get("fileIndex").toString();
					mark = Integer.parseInt(strIndex); // 得到上传的附件文件的索引
					if (mark >= 0) { // 附件文件索引>=0
						byte[] fileContent = FileUtils
								.readFileToByteArray(attachFile[mark]);
						doclibAttach.setContent(fileContent);
					}
					
					doclibManager.save(doclibAttach);
					doclibManager.flush();
					doclibManager.clear();
					setFieldProperties(doclibAttach, fieldMap);
					fields.add(doclibAttach);
					doclibAttach.setDoclib(doclib);
				}
				doclib.setAttachs(fields);
			}
			
			//${basePath}
			
			//生成html
			//DOM4JXMLOperator dom4j=new DOM4JXMLOperator();
			//String prxUrl=request.getSession().getServletContext().getRealPath("/");
			//prxUrl=prxUrl+"jteap/system/doclib/";
			//dom4j.open(prxUrl+"template/"+doclibCatalog.getTemplateFile());
			//List list=dom4j.find("/html/body/div");
			//Iterator it=list.iterator();
//			while(it.hasNext()){
//				Element element=(Element)it.next();
//				Attribute divAtt=element.attribute("id");
//				String divid=divAtt.getValue();
//				if("title".equals(divid)){
//					element.addText(""+docTitle);
//				}else if("creator".equals(divid)){
//					element.addText(""+strCreator);
//				}else if("createdt".equals(divid)){
//					element.addText(""+strCreatedt);
//				}else if("content".equals(divid)){
//					element.addText(""+content);
//				}else if("attachs".equals(divid)){
//					String downPath=request.getContextPath()+"/jteap/doclib/DoclibAction!downAttachAction.do";
//					String attachContent=this.doclibAttachManager.getAttachTableBySet(downPath, fields);
//					element.addText(""+attachContent);
//				}
//			}
//			dom4j.setUrl(prxUrl+"generate/"+doclibCatalog.getTemplateFile().substring(0,doclibCatalog.getTemplateFile().indexOf(".")+1)+"html");
//			dom4j.saveToHtml();
			doclib.setPathurl("/jteap/system/doclib/generate/"+UUIDGenerator.javaId()+".html");
			this.toHtml(doclib,doclibCatalog.getTemplateFile());
			
			doclibManager.save(doclib);
			
			// 文档扩展字段
			List<Map<String, String>> fdList = JSONUtil.parseList(fdListJosn);
			for (Map fdMap : fdList) {
				doclibFv = new DoclibFv();
				doclibFv.setDoclib(doclib);
				String name = fdMap.get("name") != null ? fdMap.get("name")
						.toString() : "";
				doclibFv.setName(name);
				doclibFv.setFieldId(fdMap.get("id")!=null ?fdMap.get("id").toString() : "");
				doclibFv.setValue(fdMap.get("value") != null ? fdMap.get("value").toString() : "");
				doclibManager.save(doclibFv);
			}
			
			
			outputJson("{success:true,id:'" + doclib.getId() + "'}");
		} catch (Exception ex) {
			ex.printStackTrace();
			outputJson("{success:false,msg:'" + ex.getMessage() + "'}");
		}
		return NONE;
	}

	/**
	 * 修改文档及相关信息 2009-08-20
	 */
	public String updateDoclibAction() throws Exception {

		String docTitle = request.getParameter("title");
		String strCreator = request.getParameter("creator");
		String strCreatedt = request.getParameter("createdt");
		String content = request.getParameter("content");
		String id = request.getParameter("id");
		String ops = request.getParameter("ops");
		//String strLevelID = request.getParameter("levelID");
		String fdListJosn = request.getParameter("fdListJson");
		try {
			Doclib doclib = null;
			DoclibCatalog doclibCatalog = null;
			DoclibCatalogField doclibCatalogField = null;
			DoclibFv doclibFv = null;
			if (StringUtil.isNotEmpty(id)) {
				doclib = (Doclib) doclibManager.get(id);
				doclibCatalog=doclib.getDoclibCatalog();
				//删除原来生成的静态文件
				File file=new File(ServletActionContext.getServletContext().getRealPath("")+doclib.getPathurl());
				if(file.isFile()){
					file.delete();
				}
			}
			doclib.setTitle(docTitle);
			doclib.setCreator(strCreator);
			doclib.setContent(content);
			//doclib.setWypx(docTitle.split("]")[1]);
			Date createDate = DateUtils.StrToDate(strCreatedt, "yyyy-MM-dd HH:mm:ss");
			doclib.setCreatedt(createDate);
			DoclibLevel doclibLevel = doclib.getDoclibLevel();
			// 当文档级别ID 和数据库表的文档级别ID不同时说明文档级别已经被修改
//			if (!doclibLevel.getId().equals(strLevelID)) { // 判断文档级别ID 是否相等
//				DoclibLevel newDoclibLevle = doclibLevelManager.get(strLevelID);
//				doclib.setDoclibLevel(newDoclibLevle);
//			}
			doclib.setPathurl("/jteap/system/doclib/generate/"+UUIDGenerator.javaId()+".html");
			doclibManager.save(doclib);
			//doclibManager.flush();
			this.toHtml(doclib,doclibCatalog.getTemplateFile());
			
			
			// 文档扩展字段
			List<Map<String, String>> fdList = JSONUtil.parseList(fdListJosn);
			//System.out.println("fdList=======>" + fdListJosn);
			
			for (Map fdMap : fdList) {
				// doclibFv = new DoclibFv();
				String fieldid = fdMap.get("id") != null ? fdMap.get("id")
						.toString() : "";
				doclibFv = this.doclibFvManager.findUniqueDoclibFvByDocidAndFieldid(doclib.getId(), fieldid);
				doclibFv.setDoclib(doclib);
				String name = fdMap.get("name") != null ? fdMap.get("name")
						.toString() : "";
				doclibFv.setName(name);
				doclibFv.setFieldId(fdMap.get("id")!=null ?fdMap.get("id").toString() : "");
				doclibFv.setValue(fdMap.get("value") != null ? fdMap.get(
						"value").toString() : "");
				doclibManager.save(doclibFv);
			}
			
			outputJson("{success:true,id:'" + doclib.getId() + "'}");
		} catch (Exception ex) {
			ex.printStackTrace();
			outputJson("{success:false,msg:'" + ex.getMessage() + "'}");
		}
		return NONE;
	}
	
	
	public String upLoadDoclibAction() throws Exception {

		String docTitle = request.getParameter("title");
		String strCreator = request.getParameter("creator");
		String strCreatedt = request.getParameter("createdt");
		String content = request.getParameter("content");
		String id = request.getParameter("id");
		String ops = request.getParameter("ops");
		String fdListJosn = request.getParameter("fdListJson");
		String attcahid=null;
		try {
			Doclib doclib = null;
			if (StringUtil.isNotEmpty(id)) {
				doclib = (Doclib) doclibManager.get(id);
			}
			//java.util.Set<DoclibAttach> fields = new java.util.HashSet<DoclibAttach>();
			if (StringUtils.isNotEmpty(ops)) {
				//System.out.println(ops+"---------------------------");
				List<Map<String, String>> fieldList = JSONUtil.parseList(ops);
				int mark = 0; // 附件文件的下标

				for (Map fieldMap : fieldList) {
					String strIndex = fieldMap.get("fileIndex") == null ? "-1"
							: fieldMap.get("fileIndex").toString();
					DoclibAttach doclibAttach  = new DoclibAttach();
					// 得到上传文件列表的文件索引。当索引值为null 时 设置为-1
					mark = Integer.parseInt(strIndex); // 得到上传的附件文件的索引
					if (mark >= 0) { // 附件文件索引>=0
						byte[] fileContent = FileUtils.readFileToByteArray(attachFile[mark]);
						//System.out.println("2--------------------"+this.attachFile[mark].length());
						doclibAttach.setContent(fileContent);
						setFieldProperties(doclibAttach, fieldMap);
						doclib.getAttachs().add(doclibAttach);
						doclibAttach.setDoclib(doclib);
						doclibManager.save(doclibAttach);
						attcahid=doclibAttach.getId();
					}
				}
				doclibManager.save(doclib);
			}
			outputJson("{success:true,id:'" + attcahid + "'}");
		} catch (Exception ex) {
			ex.printStackTrace();
			outputJson("{success:false,msg:'" + ex.getMessage() + "'}");
		}
		return NONE;
	}
	
	
	/**
	 * 生成静态HTML
	 */
	public String toHtml(Doclib entity,String templateName) {
		// jsp路径
		String path = request.getContextPath();
		String basePath = path;
		String title = entity.getTitle();// 内容标题
		String creator = entity.getCreator();// 发布人姓名
		String createdt = DateUtils.getTime(entity.getCreatedt(),"yyyy-MM-dd");// 发布时间
		String content = entity.getContent();    // 内容
		String parent = entity.getDoclibCatalog().getTitle();//父文档名
		Collection<DoclibAttach> attachs = entity.getAttachs();// 附件列表
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("basePath", basePath);
		data.put("title", title);
		data.put("creator", creator);
		data.put("createdt", createdt);
		data.put("content", content);
		data.put("list", attachs);
		data.put("parent",parent);
		
		// 调用静态页面方法
		HtmlSupport.toHTML(data,templateName, entity.getPathurl());
		return NONE;
	}

	/**
	 * 设置文档附件对象属性
	 * 
	 * @param field
	 * @param opMap
	 */
	private void setFieldProperties(DoclibAttach doclibAttach, Map opMap) {

		doclibAttach.setName(opMap.get("name").toString());
		doclibAttach.setType(opMap.get("type").toString());
		doclibAttach.setDoclibSize(opMap.get("doclibSize").toString());

	}
	
	/**
	 * 
	 *描述：动态查询列
	 *时间：2010-6-21
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findDynaColumnsAction() throws Exception{
		String catalogId = request.getParameter("catalogId");
		String catalogCode = request.getParameter("catalogCode");
		DoclibCatalog doclibCatalog ;
		if(StringUtils.isEmpty(catalogId)){
			doclibCatalog = doclibCatalogManager.findUniqueBy("catalogCode",
					catalogCode);
		}else{
			 doclibCatalog = doclibCatalogManager.findUniqueBy("id",
						catalogId);
		}
		
		List list = new ArrayList();
		
		
		DoclibCatalogField  id =new DoclibCatalogField();
		id.setName("id");
		id.setCode("id");
		id.setLen(100);
		list.add(id);
		
		DoclibCatalogField  title =new DoclibCatalogField();
		title.setName("标题");
		title.setCode("title");
		title.setLen(200);
		list.add(title);
		
		DoclibCatalogField  creator =new DoclibCatalogField();
		creator.setName("创建人");
		creator.setLen(80);
		creator.setCode("creator");
		list.add(creator);
		
		DoclibCatalogField  createdt =new DoclibCatalogField();
		createdt.setName("创建时间");
		createdt.setLen(150);
		createdt.setCode("createdt");
		list.add(createdt);
		
		if(doclibCatalog != null){
			getCatalogField(list, doclibCatalog);
		}
//		StringBuffer data  = new StringBuffer();
//		data.append("{'id':'id','标题':'title'},{'创建人':'creator'},{'创建时间':'createdt'},");
//		if(list.size()>0){
//			for(int i=0;i<list.size();i++){
//				DoclibCatalogField  field = (DoclibCatalogField)list.get(i);
//				data.append("{'"+field.getName()+"':'"+field.getName()+"'");
//				if(i==list.size()-1){
//					data.append("}");
//				}else{
//					data.append("},");
//				}
//			}
//		}else{
//			data.deleteCharAt(data.length()-1);
//		}
		
		outputJson("{success:true,data:["+JSONUtil.listToJson(list,new String[]{"id","name","len","code"})+"]}");
		//System.out.println("{success:true,data:["+JSONUtil.listToJson(list,new String[]{"name","len"})+"]}");
		return NONE;
	}
	
	
	/**
	 * 
	 *描述：动态查询数据
	 *时间：2010-6-21
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findDynaDataAction() throws Exception{
		String catalogId = request.getParameter("catalogId");
		String catalogCode = request.getParameter("catalogCode");
		StringBuffer hql=this.getPageBaseHql();
		//根据传过来的分类名称去查询当前分类名称下的所有文档分类
		if(StringUtils.isEmpty(catalogId)&&StringUtils.isNotEmpty(catalogCode)){
			DoclibCatalog doclibCatalog = doclibCatalogManager.findUniqueBy("catalogCode",
					catalogCode);
			//取得所有对象的父节点id
			StringBuffer parids = new StringBuffer();
			List<DoclibCatalog> list =doclibCatalogManager.find("from DoclibCatalog");
			for(DoclibCatalog doc:list){
				if(doc.getParent()!=null){
					parids.append(doc.getParent().getId()+",");
				}
			}
			//存放所有子分类id
			StringBuffer childids = new StringBuffer();
			childids.append("'"+doclibCatalog.getId()+"',");
			//取得当前分类下所有子分类的id
			doclibCatalogManager.getChlids(childids,parids,doclibCatalog.getId());
			if(childids.toString().split(",").length>0){
				HqlUtil.addCondition(hql, "doclibCatalog.id", childids.toString().substring(0, childids.toString().length()-1), HqlUtil.LOGIC_AND, HqlUtil.TYPE_IN);
			}else{
				HqlUtil.addCondition(hql, "doclibCatalog.id",catalogId);
			}
		}else{
			HqlUtil.addCondition(hql, "doclibCatalog.id",catalogId);
		}
		StringBuffer data  = new StringBuffer();
		
		// 每页大小
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		
		String title=request.getParameter("title");
		String creator=request.getParameter("creator");
		String createdt=request.getParameter("createdt");
		String orderBy = request.getParameter("sort");
		Map<String, String> param=new HashMap<String, String>();
		
		if(StringUtil.isEmpty(orderBy)){
			HqlUtil.addOrder(hql, "createdt", "desc");
		}
		if(StringUtil.isNotEmpty(title)){
			HqlUtil.addCondition(hql, "title", title ,HqlUtil.LOGIC_AND,HqlUtil.TYPE_STRING_LIKE);
		}
		if(StringUtil.isNotEmpty(creator)){
			HqlUtil.addCondition(hql, "creator", creator ,HqlUtil.LOGIC_AND,HqlUtil.TYPE_STRING_LIKE);
		}
		if(StringUtil.isNotEmpty(createdt)){
			hql.append(" and createdt=to_date('"+createdt+"','yyyy-MM-dd')");
		}
		
		// 开始分页查询
		Page page=this.doclibManager.pagedQuery(Integer.parseInt(start), Integer.parseInt(limit), hql.toString(), this.showListHqlValues.toArray());
		//查询分类下面的文档
		List<Doclib> libList = (List<Doclib>) page.getResult();
		//String json=JSONUtil.listToJson(libList,new String[]{"title","creator","createdt","time","doclibFvs","name","value"});
		for(Doclib lib:libList){
			Set<DoclibFv> fvSet=lib.getDoclibFvs();
			StringBuffer fvSb  = new StringBuffer();
			for(DoclibFv fv:fvSet){
				fvSb.append("'"+fv.getName()+"':'"+(fv.getValue()==null?"":fv.getValue())+"',");
			}
			if(!fvSb.toString().equals("")){
				fvSb.deleteCharAt(fvSb.length()-1);
				data.append("{'id':'"+lib.getId()+"','title':'"+lib.getTitle()+"','creator':'"+lib.getCreator()+"','createdt':'"+DateUtils.getDate(lib.getCreatedt(), "yyyy-MM-dd")+"',"+fvSb.toString()+"},");
			}else{
				data.append("{'id':'"+lib.getId()+"','title':'"+lib.getTitle()+"','creator':'"+lib.getCreator()+"','createdt':'"+DateUtils.getDate(lib.getCreatedt(), "yyyy-MM-dd")+"'},");
			}
		}
		if(!data.toString().equals("")){
			data.deleteCharAt(data.length()-1);
		}
		outputJson("{success:true,totalCount:"+page.getTotalCount()+",list:["+data.toString()+"]}");
		//System.out.println("------------------{success:true,totalCount:"+libList.size()+",list:["+data.toString()+"]}");
		return NONE;
	}
	
	/**
	 * 
	 *描述：获取静态页面的地址
	 *时间：2010-6-22
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getUrlAction() throws Exception{
		String docid=request.getParameter("docid");
		if(StringUtils.isNotEmpty(docid)){
			Doclib lib=this.doclibManager.get(docid);
			String url=lib.getPathurl();
			url=url.substring(url.lastIndexOf("/")+1).trim();
			File file=new File(ServletActionContext.getServletContext().getRealPath("")+lib.getPathurl());
			//如果文件不存在 则新建文件
			if(!file.isFile()){
				DoclibCatalog doclibCatalog=lib.getDoclibCatalog();
				this.toHtml(lib, doclibCatalog.getTemplateFile());
			}
			outputJson("{success:true,url:'"+url+"'}");
		}else{
			outputJson("{success:false,msg:'文档ID为空'}");
		}
		
		return NONE;
	}
	/**
	 * 返回公会简介的url
	 * @return
	 * @throws Exception 
	 */
	public String getGhjjUrlAction() throws Exception{
		//查询所有公会简介文档
		List list = doclibManager.findDoclibListByCatalogId("ghjj",2);
		//返回第一个最新的文档
		Doclib doc = (Doclib) list.get(0);
		if(doc!=null){
			String url=doc.getPathurl();
			url=url.substring(url.lastIndexOf("/")+1).trim();
			File file=new File(ServletActionContext.getServletContext().getRealPath("")+doc.getPathurl());
			//如果文件不存在 则新建文件
			if(!file.isFile()){
				DoclibCatalog doclibCatalog=doc.getDoclibCatalog();
				this.toHtml(doc, doclibCatalog.getTemplateFile());
			}
			outputJson("{success:true,url:'"+url+"'}");
		}else{
			outputJson("{success:false,msg:''}");
		}
		return NONE;
	}
	
	
	/**
	 * 废弃方法
	 * @return
	 * @throws Exception
	 */
	public String getColumnInfoButLobAction() throws Exception{
		String catalogId = request.getParameter("catalogId");
		String json = "[]";
		String[] columnArray = new String[]{"id","columncode","columnname"};
		DoclibCatalog doclibCatalog = doclibCatalogManager.findUniqueBy("id",
				catalogId);
		List list = new ArrayList();
		if(doclibCatalog != null){
			getCatalogField(list, doclibCatalog);
		}
		String[] fields = new String[] { "id", "name", "value" };
		StringBuffer data  = new StringBuffer();
		List columList = new ArrayList();
		HashMap column = new HashMap();
		column.put("id", "TITLE");
		column.put("columncode", "TITLE");
		column.put("columnname", "标题");
		columList.add(column);
		HashMap column1 = new HashMap();
		column1.put("id", "CREATOR");
		column1.put("columncode", "CREATOR");
		column1.put("columnname", "创建人");
		columList.add(column1);
		HashMap column2 = new HashMap();
		column2.put("id", "CREATEDT");
		column2.put("columncode", "CREATEDT");
		column2.put("columnname", "创建时间");
		columList.add(column2); 

		if(list!=null &&  list.size()>0){
			
			String fieldCode = "" ;
			String fieldName = "" ;
			for(int i=0;i<list.size();i++){
				DoclibCatalogField  field = (DoclibCatalogField)list.get(i);
				fieldCode = field.getName() ;
				fieldName = field.getName() ;
				if(!fieldName.equals("")){
					HashMap col = new HashMap();
					col.put("id", fieldCode);
					col.put("columncode", fieldCode);
					col.put("columnname", fieldName);
					columList.add(col);
				}
			}
		}
		json = JSONUtil.listToJson(columList,columnArray);
		outputJson("{success:true,data:"+json+"}");
		return NONE;
		
	}
	
	/**
	 * 
	 *描述：下载功能
	 *时间：2010-6-23
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String downAttachAction() throws Exception{
		//附件ID
		String attachid=request.getParameter("attachid");
		if(StringUtils.isNotEmpty(attachid)){
			DoclibAttach attach=this.doclibAttachManager.get(attachid);
			//Accessories acc=this.personManager.getAccessoriesByID(accid);
			byte[] content=attach.getContent();
			if(content!=null){
				//开始读取写入客户端
				response.reset();
				response.setHeader("Content-Length", content.length + "");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Expires", "0");
				response.setContentType("application/octet-stream; CHARSET=gb2312");
				//文件名
				String filename= new String(attach.getName().getBytes("gb2312"),"iso8859-1") ;
				response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");
				response.setHeader("Content-Type","application/octet-stream; charset=gb2312");
				//开始输出
				java.io.BufferedOutputStream outs = new java.io.BufferedOutputStream(response.getOutputStream());
				outs.write(content);
				outs.flush();
				outs.close();
			}
		}
		return NONE;
	}
	
	public File[] getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(File[] attachFile) {
		this.attachFile = attachFile;
	}

	public DoclibFvManager getDoclibFvManager() {
		return doclibFvManager;
	}

	public void setDoclibFvManager(DoclibFvManager doclibFvManager) {
		this.doclibFvManager = doclibFvManager;
	}

	public DoclibLevelManager getDoclibLevelManager() {
		return doclibLevelManager;
	}

	public void setDoclibLevelManager(DoclibLevelManager doclibLevelManager) {
		this.doclibLevelManager = doclibLevelManager;
	}

	public DoclibLevelRoleManager getDoclibLevelRoleManager() {
		return doclibLevelRoleManager;
	}

	public void setDoclibLevelRoleManager(
			DoclibLevelRoleManager doclibLevelRoleManager) {
		this.doclibLevelRoleManager = doclibLevelRoleManager;
	}

	public DoclibAttachManager getDoclibAttachManager() {
		return doclibAttachManager;
	}

	public void setDoclibAttachManager(DoclibAttachManager doclibAttachManager) {
		this.doclibAttachManager = doclibAttachManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}
	
}
