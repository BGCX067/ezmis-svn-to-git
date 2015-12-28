package com.jteap.jhtj.sjydy.web;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.ljydy.manager.AppSystemConnManager;
import com.jteap.jhtj.ljydy.model.AppSystemConn;
import com.jteap.jhtj.ljydy.model.AppSystemField;
import com.jteap.jhtj.sjydy.manager.TjAppIOManager;
import com.jteap.jhtj.sjydy.model.TjAppIO;
import com.jteap.jhtj.sjydy.model.TjAppSjzd;
@SuppressWarnings({ "unchecked", "serial" })
public class TjAppIOAction extends AbstractTreeAction {

	private AppSystemConnManager appSystemConnManager;
	
	private TjAppIOManager tjAppIOManager;
	@Override
	public HibernateEntityDao getManager() {
		return tjAppIOManager;
	}
	
	/**
	 * 如果是系统节点ID为系统ID，如果是表节点，那么节点ID是表ID
	 */
	@Override
	public String showTreeAction() throws Exception {
		List<AppSystemConn> connList=this.appSystemConnManager.getAll();
		StringBuffer result=new StringBuffer();
		for(AppSystemConn conn:connList){
			StringBuffer children=new StringBuffer();
			List<TjAppIO> appios=tjAppIOManager.findTjAppIOBySystemId(conn.getId());
			if(appios.size()>0){
				Map<String, String> tables=new HashMap<String, String>();
				for(TjAppIO io:appios){
					tables.put(io.getVname(), "{\"id\":\""+io.getId()+"\",\"text\":\""+io.getCvname()+"\",\"expanded\":false,\"leaf\":true}");
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
	 *描述：根据系统ID得到本系统下的取的数据定义所选的所有表
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
		List<AppSystemField> tables=this.tjAppIOManager.filterRepeatTablesByConn(sysConn);
		request.setAttribute("tables",tables);
		request.getRequestDispatcher("/jteap/jhtj/sjydy/dataDefineList.jsp?currentSysId="+sAppSystemId).forward(request, response);
		return NONE;
	}
	
	
	/**
	 * 
	 *描述：根据系统ID和表名得到该表下所有的字段
	 *时间：2010-4-6
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
			List<AppSystemField> fieldList=this.tjAppIOManager.getFieldsByConnAndTableName(system, sTableName);
			String json=JSONUtil.listToJson(fieldList,new String[]{"fname","cfname","ftype"});
			outputJson("{success:true,list:["+json+"]}");
		}else{
			outputJson("{success:false}");
		}
		return NONE;
	}
	
	/**
	 * 
	 *描述：显示测试保存页面
	 *时间：2010-4-6
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showLastPageAction() throws Exception{
		ArrayList<String> fieldList = new ArrayList<String>();// 用来存放表_字段 名；
		ArrayList<String> tableList = new ArrayList<String>();// 用来存放表名
		ArrayList<AppSystemField> addFieldList = new ArrayList<AppSystemField>();// 用来存放字段（对象）；
		
		String sysid = request.getParameter("sysid");
		String sXml = request.getParameter("xmlField");
		String isSql = request.getParameter("isSql");
		String isUpdate = request.getParameter("isUpdate");
		String id = request.getParameter("id");
		SAXBuilder builder = new SAXBuilder();
		StringReader sin = new StringReader(sXml);
		try {
			Document doc = builder.build(sin);
			// 得到xml文档中的所有table[@checked='true']
			XPath tablePath = XPath.newInstance("//table[@checked='true']");
			List<Element> tables = tablePath.selectNodes(doc);
			/***********************里面的表有可能保存过*******************************/
			for (Iterator<Element> iterator = tables.iterator(); iterator.hasNext();) {
				Element oTable = (Element) iterator.next();
				this.tjAppIOManager.processTableInfo(oTable, fieldList,
						tableList, addFieldList,sysid);
			}
			
			
			String strSql = "";// 用来保存组装的sql语句；
			// 组装sql语句
			strSql = "select ";
			for (int i = 0; i < fieldList.size(); i++) {
				String tempField = (String) fieldList.get(i);
				strSql += tempField;
				if (i != (fieldList.size() - 1)) {
					strSql += ", ";
				} else {
					strSql += " ";
				}

			}
			strSql += " from ";
			for (int i = 0; i < tableList.size(); i++) {
				String tempTable = (String) tableList.get(i);
				strSql += tempTable;
				if (i != (tableList.size() - 1)) {
					strSql += ", ";
				} else {
					strSql += " ";
				}
			}
			request.setAttribute("strSql", strSql);
			request.setAttribute("sysid", sysid);
			request.setAttribute("isSql", isSql);
			request.setAttribute("isUpdate", isUpdate);
			request.setAttribute("id", id);
			//request.setAttribute("xmlField", sXml.trim());
			//request.getRequestDispatcher("/jteap/jhtj/sjydy/tjAppIOForm.jsp").forward(request, response);
		} catch (Exception e) {
			log.error("解析xml[" + sXml + "]时出错:" + e);
		}

		return "showLastPageAction";
	}
	
	/**
	 * 
	 *描述：查找SQL语句是否正确
	 *时间：2010-5-31
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String isRightSqlAction() throws Exception{
		String sql=request.getParameter("sql");
		String systemId=request.getParameter("systemId");
		boolean bool=this.tjAppIOManager.isSql(sql, systemId);
		outputJson("{success:"+bool+"}");
		return NONE;
	}
	
	/**
	 * 
	 *描述：创建初始化接口状态
	 *时间：2010-5-31
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String createInterfaceBySqlAction() throws Exception{
		String sql=request.getParameter("sql");
		String data=this.tjAppIOManager.createInterfaceBySql(sql);
		outputJson("{success:true,data:'"+data+"'}");
		return NONE;
	}
	
	/**
	 * 
	 *描述：动态查询列
	 *时间：2010-3-19
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findDynaColumnsAction() throws Exception{
		StringBuffer result=new StringBuffer();
		String sql=request.getParameter("sql");
		String sysid=request.getParameter("sysid");
		List<String> columnNameList=this.tjAppIOManager.findDynaColumnsBySql(sql, sysid);
		for(String columnName:columnNameList){
			result.append("{'"+columnName+"':'"+columnName+"'},");
		}
		if(StringUtils.isNotEmpty(result.toString())){
			result.deleteCharAt(result.length()-1);
			System.out.println(result);
			this.outputJson("{success:true,list:["+result.toString()+"]}");
		}else{
			this.outputJson("{success:true,list:[]}");
		}
		return NONE;
	}
	
	/**
	 * 功能说明:动态查找数据
	 * @author 童贝
	 * @version Nov 30, 2009
	 * @return
	 * @throws Exception
	 */
	public String findDynaDataAction() throws Exception{
		String sql=request.getParameter("sql");
		String sysid=request.getParameter("sysid");
		List list=this.tjAppIOManager.querySqlData(sql, sysid);
		String json=JSONUtil.listToJson(list);
		outputJson("{success:true,totalCount:'" + list.size() + "',list:"+ json + "}");
		return NONE;
	}
	
	/**
	 * 
	 *描述：把对应的接口替换成用户选择的值
	 *时间：2010-4-7
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String replaceInterfaceSqlAction() throws Exception{
		String sql=request.getParameter("sql");
		String sqlValue=request.getParameter("sqlValue");
		String resSql=this.tjAppIOManager.replaceInterfaceSql(sql, sqlValue);
		outputJson("{success:true,sql:\""+resSql.replaceAll("\r\n"," ")+"\"}");
		return NONE;
	}
	
	
	@Override
	public String saveUpdateAction() throws BusinessException {
		String id=request.getParameter("id");
		String vname=request.getParameter("vname");
		String cvname=request.getParameter("cvname");
		//String xmlField=request.getParameter("xmlField");
		String sysid=request.getParameter("sysid");
		//带接口的sql
		String sqlstr=request.getParameter("sqlstr");
		//替换了接口的sql
		String testsql=request.getParameter("testsql");
		try{
			List<AppSystemField> addFieldList=this.tjAppIOManager.getAppSystemFieldBySqlAndSystemId(testsql, sysid);
			
			TjAppIO io=new TjAppIO();
			if(StringUtils.isNotEmpty(id)){
				io=this.tjAppIOManager.get(id);
				this.tjAppIOManager.deleteTjAppSjzdByVname(sysid,vname);
			}
			io.setSid(sysid);
			io.setVname(vname);
			io.setCvname(cvname);
			io.setSqlstr(sqlstr);
			this.tjAppIOManager.save(io);
			
			long forder=1l;
			for(AppSystemField af:addFieldList){
				TjAppSjzd sjzd=new TjAppSjzd();
				sjzd.setSid(sysid);
				sjzd.setVname(vname);
				sjzd.setFname(af.getFname());
				sjzd.setFtype(af.getFtype());
				sjzd.setCfname(af.getCfname());
				sjzd.setForder(forder);
				forder++;
				this.tjAppIOManager.save(sjzd);
			}
			outputJson("{success:true}");
		}catch(Exception e){
			throw new BusinessException("保存出现异常", e);
		}
		return NONE;
	}
	
	@Override
	protected String beforeDeleteNode(Object node) throws Exception {
		TjAppIO io=(TjAppIO)node;
		String msg=null;
		boolean isDel=this.tjAppIOManager.findTjInfoItemByVnameAndSysid(io.getVname(), io.getSid());
		if(isDel){
			this.tjAppIOManager.deleteTjAppSjzdByVname(io.getSid(),io.getVname());
		}else{
			msg="该数据源已被使用,请先删除关联数据";
		}
		return msg;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","vname","cvname","sqlstr"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","vname","cvname","sqlstr"};
	}

	public TjAppIOManager getTjAppIOManager() {
		return tjAppIOManager;
	}

	public void setTjAppIOManager(TjAppIOManager tjAppIOManager) {
		this.tjAppIOManager = tjAppIOManager;
	}

	@Override
	protected Collection getChildren(Object bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getParentPropertyName(Class beanClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection getRootObjects() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getSortNoPropertyName(Class beanClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		// TODO Auto-generated method stub
		return null;
	}

	public AppSystemConnManager getAppSystemConnManager() {
		return appSystemConnManager;
	}

	public void setAppSystemConnManager(AppSystemConnManager appSystemConnManager) {
		this.appSystemConnManager = appSystemConnManager;
	}

}
