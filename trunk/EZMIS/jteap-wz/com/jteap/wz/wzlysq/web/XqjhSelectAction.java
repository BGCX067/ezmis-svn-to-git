package com.jteap.wz.wzlysq.web;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import com.jteap.core.dao.HibernateEntityDao;
import com.jteap.core.dao.support.Page;
import com.jteap.core.support.SystemConfig;
import com.jteap.core.utils.JSONUtil;
import com.jteap.core.utils.StringUtil;
import com.jteap.core.web.AbstractAction;
import com.jteap.core.web.SpringContextUtil;
import com.jteap.wz.wzlysq.manager.XqjhSelectManager;

@SuppressWarnings({"serial", "unused"})
public class XqjhSelectAction extends AbstractAction{

	private XqjhSelectManager xqjhSelectManager;
	
	public XqjhSelectManager getXqjhSelectManager() {
		return xqjhSelectManager;
	}

	public void setXqjhSelectManager(XqjhSelectManager xqjhSelectManager) {
		this.xqjhSelectManager = xqjhSelectManager;
	}

	// 取得Page
	public Page getPage(String sql) throws Exception {
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";

		int iStart = Integer.parseInt(start)+1;
		int iLimit = Integer.parseInt(limit);
		return this.pagedQueryTableData(sql, iStart, iLimit);
	}
	/**
	 * 分页查询指定sql数据
	 * @param sql
	 * @param parseInt
	 * @param parseInt2
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public Page pagedQueryTableData(String sql, int start, int limit) throws Exception {
		DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
		String countSql = " select count (*) from (" + sql+")";
		Connection conn=dataSource.getConnection();
		try{
			
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(countSql);
			int count=0;
			if(rs.next()){
				count=rs.getInt(1);
			}
			rs.close();
			
			if(count<1)
				return new Page();

			sql="SELECT * FROM(SELECT A.*, ROWNUM RN FROM ("+sql+") A WHERE ROWNUM <= "+(start+limit-1)+") WHERE RN >= "+start;
			
			rs=st.executeQuery(sql);
			List list=new ArrayList();
			ResultSetMetaData rsmeta=rs.getMetaData();
			
			while(rs.next()){
				Map map=new HashMap();
				for(int i=1;i<=rsmeta.getColumnCount();i++){
					Object obj=rs.getObject(i);
					//针对oracle timestamp日期单独处理，转换成date
					if(obj instanceof oracle.sql.TIMESTAMP){
						obj=((oracle.sql.TIMESTAMP)obj).dateValue().getTime();
					}
					obj = StringUtil.clobToStringByDB(obj);
					map.put(rsmeta.getColumnName(i), obj);
				}
				list.add(map);
			}
			rs.close();
			return new Page(start, count, limit, list);
		}catch(Exception ex){
			throw ex;
		}finally{
			conn.close();
		}
		
	}
	@SuppressWarnings("unchecked")
	public String showXQJHListAction()throws Exception{
		/** ************************ */
		String limit = request.getParameter("limit");
		if (StringUtils.isEmpty(limit))
			limit = SystemConfig.getProperty("PAGE_DEFAULT_LIMIT");

		// 开始索引
		String start = request.getParameter("start");
		if (StringUtils.isEmpty(start))
			start = "0";
		
		String bm = request.getParameter("bm")==null?"":request.getParameter("bm");
		String gclb = request.getParameter("gclb")==null?"":request.getParameter("gclb");
		String gcxm = request.getParameter("gcxm")==null?"":request.getParameter("gcxm");
		
//		int iStart = Integer.parseInt(start);
//		int iLimit = Integer.parseInt(limit);

//		String sqbm = request.getParameter("sqbm")!=null?request.getParameter("sqbm"):"";
//		String gcxm = request.getParameter("gcxm")!=null?request.getParameter("gcxm"):"";
//		String userLoginName = sessionAttrs.get(
//				Constants.SESSION_CURRENT_PERSON_LOGINNAME).toString();
		// 角色（专业）过滤
		// String userRole =
		// sessionAttrs.get(Constants.SESSION_CURRENT_PERSONROLES).toString();
		// String[] userRoles = userRole.split(",");
		//System.out.println(gclb);
		//System.out.println(gcxm);
		try {
//			StringBuffer initSql = new StringBuffer();
//			initSql.append("SELECT ")
//			initSql.append("SELECT T.ID FROM TB_WZ_XQJH T WHERE T.XQJHSQBH IN(SELECT ID FROM TB_WZ_XQJHSQ WHERE XQJHQF = '1') AND ");
//			initSql.append("XQJH.SQBM = '"+bm+"'");
//			//如果有工程项目工程类别
//			if(StringUtils.isNotEmpty(gcxm)){
//				initSql.append(" AND XQJH.GCXM = '"+gcxm+"' AND XQJH.GCLB = '"+gclb+"'");	
//			}
//			Page initpage = this.getPage(initSql.toString());
//			List list =(List)initpage.getResult();
//			for(int i=0;i<list.size();i++){
//				
//			}
			
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT DISTINCT xqjh.XQJHBH ,xqjh.ID as XQJHID,");
			sbSql.append("xqjh.XQJHSQBH, ");
			sbSql.append("to_char(xqjh.SXSJ,'yyyy-MM-dd') as SXSJ, ");
			sbSql.append("xqjh.GCLB, ");
			sbSql.append("xqjh.SQBM, ");
			sbSql.append("xqjh.GCXM, ");
			sbSql.append("xqjh.OPERATOR, ");
			sbSql.append("xqjhmx.STATUS ");
			sbSql.append("FROM TB_WZ_XQJH xqjh,TB_WZ_XQJH_DETAIL  xqjhmx, TB_WZ_XQJHSQ xqjhsq ");
			sbSql.append("WHERE ");
			sbSql.append("( xqjh.ID = xqjhmx.XQJHBH ) and");
			sbSql.append("( xqjh.XQJHSQBH = xqjhsq.id ) and");
			sbSql.append("( xqjhsq.XQJHQF = '1') and");        //领用单只需要显示需求计划申请，借料申请不需要显示
			sbSql.append("( xqjhmx.PZSL > xqjhmx.SLSL )  and ");// and
			sbSql.append("(xqjhmx.dhsl>0 or (xqjhmx.dyszt = '1' or xqjhmx.dyszt = '2'))  and ");// and
			sbSql.append("xqjh.SQBM='"+bm+"' ");
			if(StringUtils.isNotEmpty(gcxm)){
				sbSql.append(" AND XQJH.GCXM = '"+gcxm.replace("!", "#")+"' AND XQJH.GCLB = '"+gclb+"'");	
			}
			sbSql.append(" order by xqjh.GCXM asc,xqjh.OPERATOR asc");
			
//			sbSql.append("(xqjh.GCLB='"+gclb+"' and xqjh.GCXM='"+gcxm+"' and xqjh.SQBM='"+bm+"' )");
			//sbSql.append("(sqbm = '"+sqbm+"') and (gcxm = '"+gcxm+"')");

			String sql = sbSql.toString();
			//System.out.println("主单："+sql);
			Page page = this.getPage(sql);
			/** ************************ */

			String json = JSONUtil.listToJson((List) page.getResult(),
					new String[] { "XQJHBH","XQJHID", "XQJHSQBH", "SXSJ",
							"GCLB", "SQBM",
							"GCXM", "STATUS","OPERATOR"});
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
	public String showXQJHMXListAction()throws Exception{

		String xqjhbh = request.getParameter("xqjhbh")!=null?request.getParameter("xqjhbh"):"";
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT xqjhmx.ID as XQJHMXID,xqjhmx.XQJHBH,");
			sbSql.append("xqjhmx.WZBM, ");
			sbSql.append("xqjhmx.XH, ");
			sbSql.append("xqjhmx.PZSL, ");
			sbSql.append("xqjhmx.JLDW, ");
			//修改计划单价为物资档案的计划单价 修改日期：2011-5-31
			sbSql.append("wzda.JHDJ, ");
			sbSql.append("xqjhmx.FREE, ");
			sbSql.append("xqjhmx.CGSL, ");
			sbSql.append("xqjhmx.DHSL, ");
			sbSql.append("xqjhmx.LYSL, ");
			sbSql.append("xqjhmx.SLSL, ");
			sbSql.append("xqjh.GCLB, ");
			sbSql.append("xqjh.GCXM, ");
			sbSql.append("wzda.PJJ,wzda.WZMC,ck.CKMC,wzda.XHGG, ");
			sbSql.append("wzda.KWBM,wzda.DQKC,wzda.YFPSL ");
			sbSql.append("FROM TB_WZ_XQJH_DETAIL xqjhmx , TB_WZ_XQJH xqjh, TB_WZ_SWZDA wzda ,TB_WZ_SKWGL kw,TB_WZ_CKGL ck ");
			sbSql.append("WHERE (xqjhmx.XQJHBH = xqjh.ID) and ");
			sbSql.append("( xqjhmx.WZBM = wzda.ID ) and (wzda.KWBM=kw.ID) and (kw.CKID=ck.ID) and ");
			sbSql.append("( xqjhmx.XQJHBH = '"+xqjhbh+"' ) and ");// and 
			sbSql.append("( xqjhmx.PZSL > xqjhmx.SLSL ) and (xqjhmx.dhsl>0 or (xqjhmx.dyszt = '1' or xqjhmx.dyszt = '2'))");// and 

			String sql = sbSql.toString();
			
			Page result = xqjhSelectManager.pagedQueryTableData(sql, 0, 1000000);
			/** ************************ */
			String json = JSONUtil.listToJson((List)result.getResult(),
					new String[] { "XQJHMXID","XQJHBH", "WZBM", "XH",
							"PZSL", "JLDW", "GCLB", "GCXM",
							"JHDJ", "FREE","CGSL","DHSL","LYSL","SLSL","PJJ","KWBM","WZMC","CKMC","DQKC","YFPSL","XHGG"});
			json = "{totalCount:'" + result.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	@SuppressWarnings("unchecked")
	public String showXQJHMXFPListAction()throws Exception{

		String wzmc = request.getParameter("wzmc")!=null?request.getParameter("wzmc"):"";
		String xhgg = request.getParameter("xhgg")!=null?request.getParameter("xhgg"):"";
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT xqjhmx.ID as XQJHMXID,xqjhmx.XQJHBH,")
				 .append("xqjhmx.WZBM, ")
				 .append("xqjhmx.XH, ")
				 .append("xqjhmx.PZSL, ")
				 .append("xqjhmx.JLDW, ")
				 //修改计划单价为物资档案的计划单价 修改日期：2011-5-31
				 .append("wzda.JHDJ, ")
				 .append("xqjhmx.FREE, ")
				 .append("xqjhmx.CGSL, ")
				 .append("xqjhmx.DHSL, ")
				 .append("xqjhmx.LYSL, ")
				 .append("xqjhmx.SLSL, ")
				 .append("xqjh.XQJHBH as BH, ")
				 .append("wzda.DQKC,wzda.YFPSL ")
				 .append("FROM TB_WZ_XQJH_DETAIL  xqjhmx ,TB_WZ_SWZDA wzda ,TB_WZ_XQJH xqjh ")
				 .append("WHERE ")
				 .append("( xqjhmx.WZBM = wzda.ID ) AND ")
				 .append("( xqjhmx.XQJHBH = xqjh.ID ) AND ")
//				 .append("( wzda.WZMC like '%"+wzmc+"%' ) and ")
				 .append("( xqjhmx.FREE +xqjhmx.DHSL > xqjhmx.LYSL )")
				 ;
			if(StringUtils.isNotEmpty(wzmc)){
				sbSql.append(" AND (wzda.WZMC like '%"+wzmc+"%')");
			}
			if(StringUtils.isNotEmpty(xhgg)){
				sbSql.append(" AND (wzda.XHGG like '%"+xhgg+"%')");
			}
			String sql = sbSql.toString();
			Page result = this.getPage(sql);
			/** ************************ */
			String json = JSONUtil.listToJson((List)result.getResult(),
					new String[] { 	"XQJHMXID",
									"XQJHBH", 
									"BH",
									"WZBM", 
									"XH",
									"PZSL", 
									"JLDW",
									"JHDJ", 
									"FREE",
									"CGSL",
									"DHSL",
									"LYSL",
									"SLSL",
									"DQKC",
									"YFPSL"});
			json = "{totalCount:'" + result.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	@SuppressWarnings("unchecked")
	public String showXQJHMXAllListAction()throws Exception{

		String wzmc = request.getParameter("wzmc")!=null?request.getParameter("wzmc"):"";
		String xhgg = request.getParameter("xhgg")!=null?request.getParameter("xhgg"):"";
		String start = request.getParameter("dtstart");
		String end = request.getParameter("dtend");
		StringBuffer whereDt = new StringBuffer();
		if(StringUtils.isNotEmpty(wzmc)){
			whereDt.append(" AND wzda.WZMC like '%"+wzmc+"%'");
		}
		if(StringUtils.isNotEmpty(xhgg)){
			whereDt.append(" AND wzda.XHGG like '%"+xhgg+"%'");
		}
		if(StringUtils.isNotEmpty(start)){
			whereDt.append(" AND to_char(xqjh.SXSJ,'yyyy-mm-dd')>='")
				   .append(start)
				   .append("'");
		}
		if(StringUtils.isNotEmpty(end)){
			whereDt.append(" AND to_char(xqjh.SXSJ,'yyyy-mm-dd')<='")
				   .append(end)
				   .append("'");
		}
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT xqjhmx.ID as XQJHMXID,xqjhmx.XQJHBH,")
				 .append("xqjhmx.WZBM, ")
				 .append("xqjhmx.XH, ")
				 .append("xqjhmx.XYSJ, ")
				 .append("xqjhmx.PZSL, ")
				 .append("xqjhmx.JLDW, ")
				 //修改计划单价为物资档案的计划单价 修改日期：2011-5-31
				 .append("wzda.JHDJ, ")
				 .append("xqjhmx.FREE, ")
				 .append("xqjhmx.CGSL, ")
				 .append("xqjhmx.DHSL, ")
				 .append("xqjhmx.LYSL, ")
				 .append("xqjhmx.SLSL, ")
				 .append("xqjh.XQJHBH as BH, ")
				 .append("xqjh.GCLB , ")
				 .append("xqjh.GCXM , ")
				 .append("xqjh.SQBM , ")
				 .append("xqjhsq.ID as XQJHSQID, ")
				 .append("xqjhsq.XQJHSQBH , ")
				 .append("wzda.DQKC,wzda.YFPSL  ")
				 .append("FROM TB_WZ_XQJH_DETAIL  xqjhmx ,TB_WZ_SWZDA wzda ,TB_WZ_XQJH xqjh, TB_WZ_XQJHSQ xqjhsq ")
				 .append("WHERE ")
				 .append("( xqjhmx.WZBM = wzda.ID ) AND ")
				 .append("( xqjhmx.XQJHBH = xqjh.ID ) AND ")
				 .append("( xqjh.XQJHSQBH = xqjhsq.ID )")
//				 .append("( wzda.WZMC like '%"+wzmc+"%' ) ")
				 .append(whereDt.toString());//AND
			//这里主单状态怎么修改还没有确定
//				 .append("( xqjh.STATUS <> '0' ) ");

			String sql = sbSql.toString();
			Page result = this.getPage(sql);
			/** ************************ */
			String json = JSONUtil.listToJson((List)result.getResult(),
					new String[] { 	"XQJHMXID",
									"XQJHBH", 
									"BH",
									"GCLB",
									"GCXM",
									"SQBM",
									"XQJHSQBH",
									"XQJHSQID",
									"WZBM", 
									"XH",
									"XYSJ",
									"PZSL", 
									"JLDW",
									"JHDJ", 
									"FREE",
									"CGSL",
									"DHSL",
									"LYSL",
									"SLSL",
									"DQKC",
									"YFPSL",
									"time"});
			json = "{totalCount:'" + result.getTotalCount() + "',list:" + json
					+ "}";
			this.outputJson(json);
		} catch (Exception ex) {
			this.outputJson("{success:false}");
			ex.printStackTrace();
		}
		return NONE;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public HibernateEntityDao getManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] listJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] updateJsonProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
