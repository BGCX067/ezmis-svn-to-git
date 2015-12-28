package com.jteap.jhtj.ljydy.web;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.BeanUtils;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.dynamicconnect.DynamicConnectionManager;
import com.jteap.jhtj.ljydy.manager.AppSystemConnManager;
import com.jteap.jhtj.ljydy.model.AppSystemConn;
import com.jteap.jhtj.ljydy.model.AppSystemField;
@SuppressWarnings({ "unchecked", "serial" })
public class AppSystemConnAction extends AbstractTreeAction<AppSystemConn> {

	private AppSystemConnManager appSystemConnManager;
	
	/**
	 * 如果是系统节点ID为系统ID，如果是表节点，那么节点ID是表ID
	 */
	@Override
	public String showTreeAction() throws Exception {
		List<AppSystemConn> connList=this.appSystemConnManager.getAll();
		StringBuffer result=new StringBuffer();
		for(AppSystemConn conn:connList){
			StringBuffer children=new StringBuffer();
			Set<AppSystemField> fields=conn.getAppSystemFields();
			if(fields.size()>0){
				Map<String, String> tables=new HashMap<String, String>();
				for(AppSystemField af:fields){
					tables.put(af.getVname(), "{\"id\":\""+af.getId()+"\",\"text\":\""+af.getVname()+"\",\"expanded\":false,\"leaf\":true}");
				}
				for(String key:tables.keySet()){
					children.append(tables.get(key)+",");
				}
				if(children.toString().length()>0){
					children.deleteCharAt(children.length()-1);
				}
				String json="{\"id\":\""+conn.getId()+"\",\"text\":\""+conn.getName()+"\",\"expanded\":true,\"leaf\":false,\"children\":["+children.toString()+"]}";
				result.append(json+",");
			}else{
				String json="{\"id\":\""+conn.getId()+"\",\"text\":\""+conn.getName()+"\",\"expanded\":true,\"leaf\":false,\"children\":[]}";
				result.append(json+",");
			}
		}
		if(result.length()>0){
			result.deleteCharAt(result.length()-1);
		}
		//输出
		this.outputJson("["+result.toString()+"]");
		return NONE;
	}

	/**
	 * 
	 *描述：验证连接名称是否唯一
	 *时间：2010-4-1
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String validateAppSystemConnNameUniqueAction() throws Exception {
		String dbsName = request.getParameter("name");
		dbsName=StringUtil.isoToGb(dbsName);
		String id = request.getParameter("id");
		AppSystemConn repDataSource = appSystemConnManager.findUniqueBy("name",
				dbsName);
		if (repDataSource != null) {
			if (StringUtils.isNotEmpty(id)
					&& id.equals(repDataSource.getId().toString())) {
				outputJson("{unique:true}");
			} else {
				outputJson("{unique:false}");
			}
		} else {
			outputJson("{unique:true}");
		}
		return NONE;
	}

	/**
	 * 
	 *描述：验证数据库连接
	 *时间：2010-4-1
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String validateDataBaseConn() throws Exception {
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		String className = request.getParameter("className");
		String url = request.getParameter("url");
		Connection conn = null;
		try {
			Class.forName(className);
			// 注意测试
			conn = DriverManager.getConnection(url, userId, userPwd);
			outputJson("{success:true}");
		} catch (Exception ex) {
			outputJson("{success:false,msg:'数据库连接失败'}");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return NONE;
	}
	
	/**
	 * 保存更新连接源
	 */
	public String saveUpdateAction() throws BusinessException {
		try{
			String id = request.getParameter("id");
			AppSystemConn appSystem = null;
			AppSystemConn originalObject = null;
			// 有id表示修改，否则表示创建
			if (StringUtils.isNotEmpty(id)) {
				appSystem = this.appSystemConnManager.get(id);
				originalObject = new AppSystemConn();
				BeanUtils.copyProperties(originalObject, appSystem);
			} else {
				appSystem = new AppSystemConn();
			}
	
			boolean bToCharSet = (request.getParameter("charset") != null);
			Map paramMap = request.getParameterMap();
	
			for (Object key : paramMap.keySet()) {
				if (key.equals("id"))
					continue;
	
				String paramName = (String) key;
				String paramValue = request.getParameter(paramName);
				if (bToCharSet)
					paramValue = StringUtil.isoToUTF8(paramValue);
				try {
					BeanUtils.setProperty(appSystem, paramName, paramValue);
				} catch (Exception ex) {
					log.error("为对象【" + appSystem + "】设置属性【" + paramName + "】的时候出错");
					continue;
				}
			}
			
			//在连接池里面加入连接
			DynamicConnectionManager dm=DynamicConnectionManager.getInstance();
			dm.addConnectionManager(appSystem.getClassName(), appSystem.getUrl(), appSystem.getUserId(), appSystem.getUserPwd(), 100);
			
			this.appSystemConnManager.save(appSystem);
			getOut().print("{success:true}");
		
		}catch(Exception ex){
			ex.printStackTrace();
			throw new BusinessException(ex);
		}
		return NONE;
	}
	
	/**
	 * 
	 *描述：根据系统ID得到该系统下的所有表
	 *时间：2010-4-2
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getTablbsBySistemAction() throws Exception{
		String sAppSystemId=request.getParameter("sysid");
		AppSystemConn sysConn=null;
		if(StringUtils.isNotEmpty(sAppSystemId)&&StringUtils.isNotBlank(sAppSystemId)){
			sysConn=this.appSystemConnManager.get(sAppSystemId);
		}
		if(sysConn==null){
			throw new Exception("不存在的应用系统连接");
		}
		List<AppSystemField> tables=this.appSystemConnManager.findTablesByConn(sysConn,"");
		request.setAttribute("tables",tables);
		request.getRequestDispatcher("/jteap/jhtj/ljydy/dataDefineList.jsp?currentSysId="+sAppSystemId).forward(request, response);
		return "getTablbsBySistemAction";
	}
	
	/**
	 * 
	 *描述：根据系统ID和表名得到该表下所有的字段
	 *时间：2010-4-2
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getAllFieldInfoInTableAction() throws Exception{
		String sAppSystemId=request.getParameter("sysid");//系统ID
		String sTableName=request.getParameter("sTableName");//表名
		if(StringUtils.isNotEmpty(sAppSystemId)&&StringUtils.isNotEmpty(sTableName)){
			AppSystemConn system=this.appSystemConnManager.get(sAppSystemId);
			List<AppSystemField> fieldList=this.appSystemConnManager.getAllFieldInfoInTable(system, sTableName, "");
			String json=JSONUtil.listToJson(fieldList,new String[]{"fname","cfname","ftype"});
			outputJson("{success:true,list:["+json+"]}");
		}else{
			outputJson("{success:false}");
		}
		return NONE;
	}
	
	
	/**
	 * 保存数据定义
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws BasicException
	 */
	public String saveDataDefineAction() throws Exception {

		String sXml = request.getParameter("xmlFields");
		SAXBuilder builder = new SAXBuilder();
		StringReader sin = new StringReader(sXml);
		try {
			Document doc = builder.build(sin);
			// 得到xml文档中的所有table[@checked='true']
			XPath tablePath = XPath.newInstance("//table[@checked='true']");
			List tables = tablePath.selectNodes(doc);
			/***********************里面的表有可能保存过*******************************/
			for (Iterator iterator = tables.iterator(); iterator.hasNext();) {
				Element oTable = (Element) iterator.next();
				this.appSystemConnManager.processTableInfo(oTable);
			}
			outputJson("{success:true}");
		} catch (Exception e) {
			outputJson("{success:false}");
			log.error("解析xml[" + sXml + "]时出错:" + e);
		}
		return NONE;

	}

	public AppSystemConnManager getAppSystemConnManager() {
		return appSystemConnManager;
	}

	public void setAppSystemConnManager(
			AppSystemConnManager appSystemConnManager) {
		this.appSystemConnManager = appSystemConnManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		AppSystemConn appConn = (AppSystemConn) bean;
		return appConn.getAppSystemFields();
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		return "name";
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		return this.appSystemConnManager.getAll();
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		return "sortno";
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		return "name";
	}

	@Override
	public HibernateEntityDao getManager() {
		return appSystemConnManager;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[] { "id", "name", "dbType", "server", "dbName",
				"userId", "userPwd", "port", "url", "className", "sortno",
				"appSystemFields", "vname" };
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[] { "id", "name", "dbType", "server", "dbName",
				"userId", "userPwd", "port", "url", "className", "sortno",
				"appSystemFields", "vname" };
	}

}
