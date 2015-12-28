package com.jteap.system.dataperm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

import com.jteap.core.Constants;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.HqlUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UserSession;
import com.jteap.system.dataperm.manager.DatapermManager;
import com.jteap.system.dataperm.manager.TableToClassManager;
import com.jteap.system.dataperm.model.DataPerm;
import com.jteap.system.dataperm.model.TableToClass;
import com.jteap.system.person.manager.PersonManager;
import com.jteap.system.person.model.P2Role;
import com.jteap.system.person.model.Person;
/**
 * 
 * 功能说明:对具体的模块进行数据权限的过滤
 * @author 童贝		
 * @version Dec 16, 2009
 */
@SuppressWarnings({ "unchecked", "serial" })
public class SearchAdvice{
	public TableToClassManager tableToClassManager;
	public PersonManager personManager;
	private DatapermManager datapermManager;
	private Log log = LogFactory.getLog(SearchAdvice.class);
	
	/**
	 * 拦截入口
	 * @param call
	 * @return
	 * @throws Throwable
	 */
	public Object mainAdvice(ProceedingJoinPoint call) throws Throwable{
		Object obj = call.getTarget();
		if(obj instanceof DataPermAdviceInterface){
			String methodName=call.getSignature().getName();
			Method[] ms =DataPermAdviceInterface.class.getDeclaredMethods();
			for (int i = 0; i < ms.length; i++) {
				if(ms[i].getName().equals(methodName)){
					return doAop(call);
				}
			}
		}
        return call.proceed();
	}

	/**
	 * AOP拦截处理
	 * @param call
	 * @return
	 * @throws Throwable
	 */
	private Object doAop(ProceedingJoinPoint call) throws Throwable {
		List args = new ArrayList();
		Object[] argsArray = call.getArgs();
		for (Object object : argsArray) {
			args.add(object);
		}
		String hql = (String) args.get(0);
		StringBuffer hqlSB=new StringBuffer(hql);
		List<String> whereList = new ArrayList<String>();
		_initWhereList(hql,whereList);
		String whereSql = _buildWhereCondition(whereList);
		HqlUtil.addWholeCondition(hqlSB, whereSql, HqlUtil.LOGIC_AND);
		args.set(0, hqlSB.toString());
		log.info("数据权限后： "+ hqlSB);
		Object rev=call.proceed(args.toArray());
		return rev;
	}
	

	/**
	 * 初始化WhereList条件列表,将符合当前用户的数据权限加入到whereList中去
	 * @param hql
	 * @param whereList
	 * @throws Exception
	 */
	private void _initWhereList(String hql,List<String> whereList) throws Exception {
		List<TableToClass> tableList=tableToClassManager.getAll();
		for(TableToClass table:tableList){
			String pathClass=table.getClasspath()+"."+table.getClassname();
			if(hql.indexOf(pathClass)>-1){
				//得到对应的表明
				String tableName=table.getTablename();
				List<DataPerm> dpList=this.getDatapermByPersonAndRole(tableName);
				for(DataPerm dp:dpList){
					//取出条件
					String qualification=dp.getQualification();
					if(StringUtil.isEmpty(qualification))
						qualification = "";
					//计算条件中的表达式
					qualification = _evalWhere(qualification);
					StringBuffer sb=new StringBuffer(qualification.toUpperCase());
					Class cls = Class.forName(pathClass);
					Field[] a=cls.getDeclaredFields();
					for(Field b:a){
						Column column=b.getAnnotation(Column.class);
						if(column!=null){
							//替换条件中出现的字段,把字段替换成类的属性
							if(sb.toString().indexOf(column.name())>-1){
								StringUtil.replaceAll(sb, column.name(), b.getName());
							}
							//System.out.println(column.name()+"++++++");
						}
					}
					whereList.add(sb.toString());
				}
			}
		}
		
	}

	/**
	 * 组装where条件
	 * @param whereList
	 * @return
	 */
	private String _buildWhereCondition(List<String> whereList) {
		String whereSql = "";
		for (String s1 : whereList) {
			if(StringUtil.isNotEmpty(whereSql)){
				whereSql = whereSql + " or (" + s1 + ")";
			}else{
				whereSql = s1;
			}
		}
		//如果有条件就组装到hql上去，如果没有条件，则表示当前用户没有权限查看指定的数据
		if(StringUtil.isNotEmpty(whereSql)){
			whereSql = "("+whereSql+")";
		}else{
			whereSql = "(1=0)";
		}
		return whereSql;
	}

	/**
	 * 计算条件中的表达式
	 * 支持的表达式主要包括
	 * ${curPersonId}
	 * ${curPersonName}
	 * ${curPersonLoginName}
	 * @param qualification
	 * @return
	 */
	private String _evalWhere(String where) {
		if(StringUtil.isEmpty(where))
			return where;
		// 匹配所有的表达式的正则表达式
		String patt = "\\$\\{[a-z]+\\({0,1}"
				+ "([1-9]|[a-z]|[A-Z]|\\#| |-|:|')*" + "\\){0,1}\\}";
		
		Pattern p3 = Pattern.compile(patt);
		Matcher m3 = p3.matcher(where);
		while (m3.find()) {
			String rule = m3.group();
			String value = null;
			value = _getRuleValue(rule);
			if (StringUtil.isNotEmpty(value)) {
				where = StringUtil.replace(where, rule, value);
			}
		}
		return where;
	}

	/**
	 * 计算规则,支持的规则
	 * ${curPersonLoginName} 当前登录用户名
	 * ${curPersonId}	当前登录用户ID
	 * ${curPersonName}	当前用户名称
	 * @param rule
	 * @return
	 */
	private String _getRuleValue(String rule) {
		HttpSession session = (HttpSession)UserSession.get(Constants.THREADLOCAL_CURRENT_SESSION);
		String retVal = "";
		int idx;
		if (rule.equals("${curPersonLoginName}")) {
			retVal = session.getAttribute(Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		} else if (rule.equals("${curPersonId}")) {
			retVal = session.getAttribute(Constants.SESSION_CURRENT_PERSONID).toString();
		} else if (rule.equals("${curPersonName}")) {
			retVal = session.getAttribute(Constants.SESSION_CURRENT_PERSON_NAME).toString();
		} else if ((idx = rule.indexOf("${date(")) >= 0) {
			String fm = rule.substring(idx + 8, rule.indexOf(")"));
			retVal = DateUtils.getDate(fm);
		}
		return retVal;
	}

	/**
	 * 取得当前用户的所有数据权限对象，包括Person+Role
	 * @param tablename
	 * @return
	 */
   public List<DataPerm> getDatapermByPersonAndRole(String tablename){
	   List<DataPerm> result=new ArrayList<DataPerm>();
	   HttpSession session = (HttpSession)UserSession.get(Constants.THREADLOCAL_CURRENT_SESSION);
	   String currentPersonId = (String) session.getAttribute(Constants.SESSION_CURRENT_PERSONID);
	   if(!currentPersonId.equals(Constants.ADMINISTRATOR_ID)){
		   Person person = personManager.get(Person.class, currentPersonId);
		   Map<String,DataPerm> p2pMap=datapermManager.getDatapermByPersonId(currentPersonId);
		   for (String key : p2pMap.keySet()) {
			   DataPerm dp = p2pMap.get(key);
			   if(dp.getTablename().equalsIgnoreCase(tablename)){
				   result.add(dp);
			   }
		   }
		   Set<P2Role> p2roleSet=person.getRoles();
		   for(P2Role p2role:p2roleSet){
			   Map<String,DataPerm> p2rMap=datapermManager.getDatapermByRoleId(p2role.getRole().getId().toString());
			   for(String key:p2rMap.keySet()){
				   DataPerm dp = p2rMap.get(key);
				   if(dp.getTablename().equalsIgnoreCase(tablename)){
					   result.add(dp);
				   }
			   }
		   }
	   }
	   return result;
   }

	public DatapermManager getDatapermManager() {
		return datapermManager;
	}
	
	public void setDatapermManager(DatapermManager datapermManager) {
		this.datapermManager = datapermManager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public TableToClassManager getTableToClassManager() {
		return tableToClassManager;
	}

	public void setTableToClassManager(TableToClassManager tableToClassManager) {
		this.tableToClassManager = tableToClassManager;
	}

}
