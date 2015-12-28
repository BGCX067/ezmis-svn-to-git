package com.jteap.jhtj.bbsjydy.web;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;


import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.exception.BusinessException;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.web.AbstractTreeAction;
import com.jteap.jhtj.bbsjydy.manager.BbIOManager;
import com.jteap.jhtj.bbsjydy.model.BbIO;
import com.jteap.jhtj.bbsjydy.model.BbSjzd;
import com.jteap.jhtj.bbzc.manager.BbIndexManager;
import com.jteap.jhtj.bbzc.manager.BbzcManager;
import com.jteap.jhtj.bbzc.model.Bbzc;
import com.jteap.jhtj.ljydy.model.AppSystemField;
import com.jteap.jhtj.sjydy.manager.TjAppIOManager;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.Person;
@SuppressWarnings({ "unchecked", "serial" })
public class BbIOAction extends AbstractTreeAction {

	private BbzcManager bbzcManager;
	private BbIOManager bbIOManager;
	private BbIndexManager bbIndexManager;
	private JdbcManager jdbcManager;
	private TjAppIOManager tjAppIOManager;
	private PersonManager personManager;
	@Override
	public HibernateEntityDao getManager() {
		return bbIOManager;
	}
	
	/**
	 * 如果是系统节点ID为系统ID，如果是表节点，那么节点ID是表ID
	 */
	@Override
	public String showTreeAction() throws Exception {
		//得到所有父节点的集合
		List<Bbzc> connList=this.bbzcManager.getParentBbzcList();
		Person person = personManager.getCurrentPerson(sessionAttrs);
		//if(!this.isCurrentRootUser()){
		//	connList=this.bbzcManager.filterBbzcListByQx(person, connList);
		//}
		StringBuffer result=new StringBuffer();
		for(Bbzc bbzc:connList){
			result.append(this.bbzcManager.getChildsBbzc(bbzc, this.isCurrentRootUser(), person));
		}
		if(result.length()>0){
			result.deleteCharAt(result.length()-1);
		}
		//System.out.println(result.toString());
		//输出
		this.outputJson("["+result.toString()+"]");
		return NONE;
	}
	
	@Override
	protected void beforeShowList(HttpServletRequest request,
			HttpServletResponse response, StringBuffer hql) {
		String bbindexid=request.getParameter("bbindexid");
		HqlUtil.addCondition(hql, "bbindexid",bbindexid);
		HqlUtil.addOrder(hql, "sortno", "asc");
	}
	
	
	/**
	 * 
	 *描述：根据条件查询本系统的表集合
	 *时间：2010-4-22
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String getTablbsAction() throws Exception{
		String bbindexid=request.getParameter("bbindexid");
		List list=this.jdbcManager.findTableList("DATA_");
		request.setAttribute("tables",list);
		request.getRequestDispatcher("/jteap/jhtj/bbsjydy/dataDefineList.jsp?bbindexid="+bbindexid).forward(request, response);
		return NONE;
	}
	
	/**
	 * 
	 *描述：根据表名查询所有字段
	 *时间：2010-4-22
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String findDefColumnInfoListByTableAction() throws Exception{
		String sTableName=request.getParameter("sTableName");
		//String schema=this.bbIOManager.getSchema();
		List list=this.jdbcManager.findDefColumnInfoListByTable(sTableName);
		outputJson("{success:true,list:["+JSONUtil.listToJson(list)+"]}");
		return NONE;
	}
	
	
	/**
	 * 
	 *描述：显示测试保存页面
	 *时间：2010-4-22
	 *作者：童贝
	 *参数：
	 *返回值:
	 *抛出异常：
	 */
	public String showLastPageAction() throws Exception{
		ArrayList<String> fieldList = new ArrayList<String>();// 用来存放表_字段 名；
		ArrayList<String> tableList = new ArrayList<String>();// 用来存放表名
		ArrayList<AppSystemField> addFieldList = new ArrayList<AppSystemField>();// 用来存放字段（对象）；
		
		String bbindexid = request.getParameter("bbindexid");
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
						tableList, addFieldList,bbindexid);
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
			request.setAttribute("bbindexid", bbindexid);
			request.setAttribute("isSql", isSql);
			request.setAttribute("isUpdate", isUpdate);
			request.setAttribute("id", id);
		} catch (Exception e) {
			log.error("解析xml[" + sXml + "]时出错:" + e);
		}

		return "showLastPageAction";
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
		List<String> columnNameList=this.tjAppIOManager.findDynaColumnsBySql(sql, null);
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
		List list=this.tjAppIOManager.querySqlData(sql, null);
		String json=JSONUtil.listToJson(list);
		outputJson("{success:true,totalCount:'" + list.size() + "',list:"+ json + "}");
		return NONE;
	}
	
	
	public String saveUpdateAction() throws BusinessException {
		String id=request.getParameter("id");
		String vname=request.getParameter("vname");
		String cvname=request.getParameter("cvname");
		String bbindexid=request.getParameter("bbindexid");
		//带接口的sql
		String sqlstr=request.getParameter("sqlstr");
		//替换了接口的sql
		String testsql=request.getParameter("testsql");
		try{
			List<AppSystemField> addFieldList=this.tjAppIOManager.getAppSystemFieldBySqlAndSystemId(testsql, null);
			
			BbIO io=new BbIO();
			if(StringUtils.isNotEmpty(id)){
				io=this.bbIOManager.get(id);
				this.bbIOManager.deleteBbSjzdByBbioid(id);
			}else{
				io.setBbindexid(bbindexid);
				io.setVname(vname);
				io.setSortno(this.bbIOManager.getSortNo());
			}
			io.setCvname(cvname);
			io.setSqlstr(sqlstr);
			this.bbIOManager.save(io);
			this.bbIOManager.flush();
			long forder=1l;
			for(AppSystemField af:addFieldList){
				BbSjzd sjzd=new BbSjzd();
				sjzd.setBbioid(io.getId());
				sjzd.setBbindexid(io.getBbindexid());
				sjzd.setFname(af.getFname());
				sjzd.setFtype(af.getFtype());
				sjzd.setCfname(af.getCfname());
				sjzd.setForder(forder);
				forder++;
				this.bbIOManager.save(sjzd);
			}
			outputJson("{success:true}");
		}catch(Exception e){
			throw new BusinessException("保存出现异常", e);
		}
		return NONE;
	}
	
	public String removeAction() throws Exception {
		String keys = request.getParameter("ids");
		try {
			if (keys != null) {
				boolean isDel=true;
				if(isDel){
					BbIO bbio=this.bbIOManager.get(keys.split(",")[0]);
					this.bbIOManager.deleteBbSjzdByBbioid(bbio.getId());
					this.bbIOManager.removeBatch(keys.split(","));
				}
				outputJson("{success:true}");
			}
		} catch (Exception ex) {
			String msg = ex.getMessage();
			outputJson("{success:false,msg:'" + msg + "'}");
		}
		return NONE;
	}

	@Override
	public String[] listJsonProperties() {
		return new String[]{"id","vname","cvname","sqlstr","sortno"};
	}

	@Override
	public String[] updateJsonProperties() {
		return new String[]{"id","vname","cvname","sqlstr","sortno"};
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

	public BbzcManager getBbzcManager() {
		return bbzcManager;
	}

	public void setBbzcManager(BbzcManager bbzcManager) {
		this.bbzcManager = bbzcManager;
	}

	public BbIOManager getBbIOManager() {
		return bbIOManager;
	}

	public void setBbIOManager(BbIOManager bbIOManager) {
		this.bbIOManager = bbIOManager;
	}

	public BbIndexManager getBbIndexManager() {
		return bbIndexManager;
	}

	public void setBbIndexManager(BbIndexManager bbIndexManager) {
		this.bbIndexManager = bbIndexManager;
	}

	@Override
	protected String getTextPropertyName(Class beanClass) {
		// TODO Auto-generated method stub
		return null;
	}

	public JdbcManager getJdbcManager() {
		return jdbcManager;
	}

	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}

	public TjAppIOManager getTjAppIOManager() {
		return tjAppIOManager;
	}

	public void setTjAppIOManager(TjAppIOManager tjAppIOManager) {
		this.tjAppIOManager = tjAppIOManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

}
