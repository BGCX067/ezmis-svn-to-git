package com.jteap.form.cform.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import bsh.EvalError;
import bsh.Interpreter;

import com.jteap.core.Constants;
import com.jteap.core.utils.DateUtils;
import com.jteap.core.utils.NumberUtils;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.utils.UUIDGenerator;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.form.dbdef.manager.PhysicTableManager;
import com.jteap.system.group.model.Group;
import com.jteap.system.jdbc.manager.JdbcManager;
import com.jteap.system.person.manager.P2GManager;

/**
 * 自定义表单，公式计算类
 * new CFormExp(value,sessionAttrs).eval("@Value()");
 * @author tanchang
 *
 */
@SuppressWarnings({"unchecked","unused"})
public class CFormExp {
	private String value;		//数据项本身的value
	private Map sessionAttrs;	//http session属性
	private CFormExpContext context = new CFormExpContext();
	
	public static String[]  fomulas= {
		"@IF","@UUID",
		"@HTTPSESSION","@USERLOGINNAME",
		"@USERLOGINNAME","@USERLOGINID",
		"@USERCNAME","@SQL","@VALUE",
		"@PARSENUMBER","@SYSDT",
		"@GROUPNAME","@GROUPSHORTNAME","@CONTEXT",
		"@INVOKE"
		
	};
	
	public CFormExp(Map sessionAtts){
		this(null,sessionAtts);
	}
	
	public CFormExp(String value,Map sessionAtts) {
		this.value = value;
		this.sessionAttrs = sessionAtts;
	}
	
	/**
	 * 通过json字符串初始化环境变量
	 * 格式：
	 * {var:value,var:value,.....}
	 * @param json
	 */
	public void initContextVarFromJson(String json){
		JSONObject jsonObject = JSONObject.fromObject(json);
		for(Iterator iterator=jsonObject.keys();iterator.hasNext();){
			String key = (String) iterator.next();
			Object obj = jsonObject.get(key);
			if(obj!=null && obj instanceof JSONObject){
				JSONObject jobj = (JSONObject)obj;
				if(jobj.isNullObject()){
					obj = null;
				}else{
					//日期型数据还原
					if(jobj.get("time")!=null){
						obj = new Date(jobj.getLong("time"));
					}
				}
			}
			this.context.setVar(key, obj);
		}
	}
	/**
	 * @IF((Asd>100 && Bsdf<=100)?@DATE(\"yyyy-MM月dd日\"):@VALUE())
	 * 验证公式变量是否有效，是否在context中有声明
	 * @param cf
	 * @return
	 */
	public boolean validateCalcFormulaVar(String cf){
		//(?<=[^\"@])表示不以" @ 开头的单词
		String regex = "(?<=[^\"@])\\b[a-zA-Z]\\w*\\b";
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		StringBuffer sb = new StringBuffer();
		while(mt.find()){
			boolean bIn = false;
			String x = mt.group();
			for(Iterator iterator = this.context.getVars().iterator();iterator.hasNext();){
				if(x.equals(iterator.next())){
					bIn = true;
					break;
				}
			}
			if(bIn == false){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 验证计算公式是否有效，是否有公式名称拼写错误
	 * @param cf
	 * @return
	 */
	public static boolean validateCalcFormula(String cf){
		boolean bResult =true;
		String regex = "@[a-zA-Z]*\\(";
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while(mt.find()){
			String group = mt.group();
			group =group.substring(0,mt.group().length()-1);
			boolean bIn = false;
			for (int i = 0; i < fomulas.length; i++) {
				if(fomulas[i].equals(group)){
					bIn = true;
					break;
				}
			}
			if(!bIn){
				bResult = false;
				break;
			}
			cf = mt.replaceFirst("");
			mt = p.matcher(cf);
		}
		return bResult;
	} 
	
	
	/**
	 * 计算公式
	 * @param cf
	 * @return
	 * @throws Exception 
	 */
	public String eval(String cf) {
		if (StringUtil.isNotEmpty(cf)) {
			cf = StringUtil.decodeChars(cf, "@,>,<,+,-,?,(,), ,.,:,\",'");
		}
		
		cf = eval_VALUE(cf);			//1.匹配@VALUE公式
		cf = eval_CONTEXT(cf);			//1.匹配@CONTEXT公式
		cf = eval_PARSENUMBER(cf);		//2.匹配@PARSENUMBER();公式
		cf = eval_SYSDT(cf);			//3.匹配@SYSDT公式
		cf = eval_GROUPNAME(cf);			//4.匹配@GROUPNAME公式
		cf = eval_GROUPSHORTNAME(cf);		//5.匹配@GROUPSHORTNAME公式
		cf = eval_HTTPSESSION(cf);			//6.匹配@HTTPSESSION公式
		cf = eval_USERLOGINNAME(cf);		//7.匹配@USERLOGINNAME公式
		cf = eval_USERLOGINID(cf);			//8.匹配@USERLOGINID公式
		cf = eval_USERCNAME(cf);			//9.匹配@USERCNAME公式
		cf = eval_SQL(cf);			//10.匹配@SQL公式
		cf = eval_UUID(cf);			//11.匹配@UUID公式
		cf = eval_INVOKE(cf);			//11.匹配@UUID公式
		cf = eval_IF(cf);			//12.匹配@IF公式
		
		Interpreter i = new Interpreter();
		try {
			cf = i.eval(cf)+"";
		} catch (EvalError e) {
			e.printStackTrace();
			String msg = e.getMessage();
			msg = StringUtil.formatHTML(msg);
			return "%ERROR%";
		}
		return cf;
	}
	
	
	/**
	 * 计算value公式
	 * @param cf
	 * @return
	 */
	public String eval_VALUE(String cf){
		String regex = "@VALUE\\(\\)";	//匹配@VALUE公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while(mt.find()){
			cf = mt.replaceFirst("\""+value+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	/**
	 * 计算@PARSENUMBER("333.33");公式
	 * @param cf
	 * @return
	 */
	public String eval_PARSENUMBER(String cf){
		String regex = "(@PARSENUMBER\\([0-9|.|\"]*\\))";	//匹配@VALUE公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while(mt.find()){
			String group = mt.group();
			String number = group.substring(group.indexOf("\"")+1,group.lastIndexOf("\""));
			if(NumberUtils.isNumeric(number)){
				cf =mt.replaceFirst(number);
			}else{
				cf = mt.replaceFirst("0");
			}
			mt = p.matcher(cf);
		}
		return cf;
	}
	/**
	 * CONTEXT公式
	 * @param cf
	 * @return
	 */
	public String eval_CONTEXT(String cf){
		String regex = "(@CONTEXT\\([a-zA-Z0-9\"-\\/_ 年月日:,]+\\))";	//匹配@VALUE公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while(mt.find()){
			String group = mt.group();
			String tmpArray[] = group.split("\"");
			String varName = tmpArray[1].toUpperCase();
			Object value = this.context.getVar(varName);
			if(value==null){
				value = "";
			}else{
				if(value instanceof Date){
					String fm = "yyyy-MM-dd";
					if(tmpArray.length>3){
						fm = tmpArray[3];
					}
					value = DateUtils.formatDate((Date)value, fm);
				}
			}
			cf = mt.replaceFirst("\""+value+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	/**
	 * 计算@SYSDT公式
	 * @param cf
	 * @return
	 */
	public String eval_SYSDT(String cf){
		String regex = "@SYSDT\\(\"[yMdhmsiH年月日时分秒: -]*\"\\)";	//匹配@VALUE公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while(mt.find()){
			String group = mt.group();
			String fm = group.substring(group.indexOf("\"")+1,group.lastIndexOf("\""));
			String dt = DateUtils.getDate(fm);
			cf = mt.replaceFirst("\""+dt+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	
	/**
	 * 当前Session中用户所属的组织的名称
	 * 组织名称,当前登录用户所属的组织名称,是否带路径 @GROUPNAME(true|false);	
	 * @param cf
	 * @return
	 */
	public String eval_GROUPNAME(String cf){
		String regex = "@GROUPNAME\\(((false)|(true))\\)";	//匹配@GROUPNAME公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		P2GManager p2gManager = (P2GManager) SpringContextUtil.getBean("p2gManager");
		while(mt.find()){
			String gp = mt.group();
			String fm = gp.substring(gp.indexOf("(") + 1, gp.indexOf(")"));
			Boolean flag = Boolean.parseBoolean(fm);
			String personId = (String)this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSONID);

			ArrayList<Group> entities = (ArrayList<Group>) p2gManager.findGroupsOfThePerson(personId);

			// 获得第一个组织名
			Group entity = entities.get(0);
			
			String groupName = null;
			if (!flag) {
				groupName = entity.getGroupName();
				// 返回第一个组织名
			} else {
				// 需要获得完整路径
				// 当前组织ID
				groupName = entity.getPathWithText();
			}
			
			cf = mt.replaceFirst("\""+groupName+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	
	public String eval_GROUPSHORTNAME(String cf){
		
		return cf;
	}
	/**
	 * 取得指定的session值
	 * @param cf
	 * @return
	 */
	public String eval_HTTPSESSION(String cf){
		String regex = "@HTTPSESSION\\(\"[^\\)\\(\"']*\"\\)";	//匹配@HTTPSESSION公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while(mt.find()){
			String group = mt.group();
			String varName = group.substring(group.indexOf("\"")+1,group.lastIndexOf("\""));
			Object value = this.sessionAttrs.get(varName);
			if(value == null){
				value = "";
			}
			cf = mt.replaceFirst("\""+value+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	/**
	 * 取得当前登录的用户名称
	 * @param cf
	 * @return
	 */
	public String eval_USERLOGINNAME(String cf){
		String regex = "@USERLOGINNAME\\(\\)";	//匹配@USERLOGINNAME公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		String value = (String) this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_LOGINNAME); 
		while(mt.find()){
			cf = mt.replaceFirst("\""+value+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	
	/**
	 * 当前登录用户的编号
	 * @param cf
	 * @return
	 */
	public String eval_USERLOGINID(String cf){
		String regex = "@USERLOGINID\\(\\)";	//匹配@USERLOGINNAME公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		String value = (String) this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSONID); 
		while(mt.find()){
			cf = mt.replaceFirst("\""+value+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	
	/**
	 * 当前登录用户的中文名称
	 * @param cf
	 * @return
	 */
	public String eval_USERCNAME(String cf){
		String regex = "@USERCNAME\\(\\)";	//匹配@USERLOGINNAME公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		String value = (String) this.sessionAttrs.get(Constants.SESSION_CURRENT_PERSON_NAME); 
		while(mt.find()){
			cf = mt.replaceFirst("\""+value+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	
	/**
	 * 
	 *描述：@SQL公式,返回SQL语句查询结果第一条记录的第一个字段
	 *时间：2010-5-27
	 *作者：谭畅
	 *参数：cf @SQL("SELECT * FROM XXX")
	 *返回值: SQL语句的查询结果
	 *抛出异常：
	 */
	public String eval_SQL(String cf){
		String regex = "@SQL\\(\"[^\\)\\(]*\"\\)";	//匹配@HTTPSESSION公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		JdbcManager jdbcManager = (JdbcManager) SpringContextUtil.getBean("jdbcManager");
		while(mt.find()){
			String group = mt.group();
			String sql = group.substring(group.indexOf("\"")+1,group.lastIndexOf("\""));
			Object value = jdbcManager.queryUniqueBySql(sql);
			cf = mt.replaceFirst("\""+value+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	
	
	
	
	/**
	 * 条件判断
	 * @IF(表单变量?"":"");  aaa && bbb || ccc
	 * @param cf
	 * @return
	 */
	public String eval_IF(String cf){
		//"123"+@IF((A>100 && B<100)?@UUID():@VALUE());
		String regex = "@IF\\(.*\\)";	//匹配@IF公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while(mt.find()){
			String group = mt.group();
			String condition = group.substring(group.indexOf("(")+1,group.indexOf("?"));
			String v1 = group.substring(group.indexOf("?")+1,group.indexOf(":"));
			String v2 = group.substring(group.indexOf(":")+1,group.lastIndexOf(")"));
			Interpreter i = new Interpreter();
			String v = "\"\"";
			
			try{
				initEvalContext(i);
				Boolean ret = (Boolean) i.eval(condition);
				if(ret == true){
					v = v1;
				}else{
					v = v2;
				}
				mt = p.matcher(cf);
			}catch(Exception ex){
				//ex.printStackTrace();
			}
			cf = mt.replaceFirst(v);
			
		}
		return cf;
	}
	/**
	 * 初始化变量环境
	 * @param i
	 * @throws EvalError 
	 */
	private void initEvalContext(Interpreter i) throws EvalError{
		Set vars = this.context.getVars();
		Iterator iterator = vars.iterator();
		while(iterator.hasNext()){
			String var = iterator.next()+"";
			Object obj = context.getVar(var);
			try {
				i.set(var, obj);
			} catch (EvalError e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * @UUID公式 动态生成一个32位的uuid字符串
	 * 形如：40288aa02331d080012331d080710000
	 * @param cf
	 * @return
	 */
	public String eval_UUID(String cf){

		String regex = "@UUID\\(\\)";	//匹配@VALUE公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		while(mt.find()){
			cf = mt.replaceFirst("\""+UUIDGenerator.hibernateUUID()+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	
	
	/**
	 * invoke class 公式
	 * @param cf
	 * @return
	 */
	public String eval_INVOKE(String cf){
		String regex = "@INVOKE\\(\"[^\\)\\(\"']*\"\\)";	//匹配@INVOKE公式
		Pattern p = Pattern.compile(regex);
		Matcher mt = p.matcher(cf);
		String result = "";
		while(mt.find()){
			String group = mt.group();
			String className = group.substring(group.indexOf("\"")+1,group.lastIndexOf("\""));
			try {
				Class invokeClass = Class.forName(className);
				CalculateFormula cfObject = (CalculateFormula) invokeClass.newInstance();
				result = cfObject.calculate(context);
			} catch (Exception e) {
				e.printStackTrace();
			}
			cf = mt.replaceFirst("\""+result+"\"");
			mt = p.matcher(cf);
		}
		return cf;
	}
	
	public static void main(String[] args) throws EvalError {
		String cf = "\"sdfsdfsdf【\"+@UUID()+\"】SDFSDFSDF\"";
		CFormExp exp =  new CFormExp("3",new HashMap());
		String ret = exp.eval_UUID(cf);
		System.out.println(ret);
		Interpreter i = new Interpreter();
		try {
			cf = i.eval(ret)+"";
		} catch (EvalError e) {
			e.printStackTrace();
		}
		
		System.out.println("计算结果："+cf);
//		
//		
		
//		
//		Interpreter i = new Interpreter();
//		i.set("DEPTNAME", new String("SSS"));
//		Object x = i.eval("DEPTNAME.eq(\"SSS\")");
//		System.out.println(x);
		
//		Interpreter i = new Interpreter();
//		i.set("A", 100);
//		i.set("B", 200);
//		i.set("C", 150);
//		System.out.println(i.eval("(A<B) && (A>C)"));
//		String v = exp.eval_SYSDT("@SYSDT(\"\")+@SYSDT(\"\")");
//		System.out.println(v);
		
		
		
		
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CFormExpContext getContext() {
		return context;
	}

	public void setContext(CFormExpContext context) {
		this.context = context;
	}


	/**
	 * 将request中存在的param以及attribute全部存入context中
	 * @param request
	 */
	public void initExpContext(HttpServletRequest request) {
		
		Enumeration<String> enumParams = request.getParameterNames();
		while(enumParams.hasMoreElements()){
			String param = enumParams.nextElement();
			context.setVar(param,request.getParameter(param));
		}
		
		Enumeration<String> enumAttrs =  request.getAttributeNames();
		while(enumAttrs.hasMoreElements()){
			String attr = enumAttrs.nextElement();
			context.setVar(attr, request.getAttribute(attr));
		}
		
		String recordJson = (String) context.getVar("recordJson");
		if(StringUtil.isNotEmpty(recordJson)){
			initContextVarFromJson(recordJson);
		}
		
	}
	


}
